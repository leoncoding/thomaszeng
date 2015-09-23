package com.thoams.path;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thomas.path.R;
import com.thomas.path.bean.User;
import com.thomas.path.manager.Constant;
import com.thomas.path.manager.FontType;
import com.thomas.path.manager.ImageManager;
import com.thomas.path.manager.LogUtil;
import com.thomas.path.manager.StringUtils;
import com.thomas.path.view.CircleImageView;
import com.thomas.path.view.CustomAlertDialog;
import com.thomas.path.view.CustomProgressDialog;
import com.thomas.path.volley.GsonRequest;
import com.thomas.path.volley.RequestManager;

public abstract class BaseActivity extends FragmentActivity {
	protected CustomAlertDialog alertDialog;
	// 加载loading框
	private CustomProgressDialog progressdialog;
	// volley消息队列
	protected RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initVolley(this);
		queue = RequestManager.getRequestQueue();
		// 将所有Activity加入任务栈中
		BaseApplication.allActivity.add(this);
	}

	public void initVolley(Context context) {
		RequestManager.init(context);
	}

	/**
	 * 网络请求返回处理方法<json>
	 * 
	 * @param response
	 */
	public abstract void onRecvData(JsonObject response);

	/**
	 * 初始化控件
	 */
	public abstract void findViews();

	/**
	 * 初始化数据
	 */
	public abstract void init();

	/**
	 * 设置控件监听事件
	 */
	public abstract void setListener();

	// 默认不显示加载进度框，错误提示，不能取消请求
	public static final int FLAG_DEFAULT = 0;
	// 是否显示加载进度框
	public static final int FLAG_SHOW_PROGRESS = 1;
	// 是否显示错误提示
	public static final int FLAG_SHOW_ERROR = 2;
	// 是否当关闭进度框时取消相应请求
	public static final int FLAG_CANCEL = 4;
	// 是否加密请求
	public static final int FLAG_ENCRYPT = 8;

	/**
	 * 请求JSON数据
	 * 
	 * @param url
	 *            请求接口路径
	 * @param params
	 *            请求参数
	 */
	protected <T> void request(String url, HashMap<String, String> params) {
		request(url, params, FLAG_DEFAULT);
	}

	/**
	 * 请求JSON数据
	 * 
	 * @param url
	 *            请求接口路径
	 * @param params
	 *            请求参数
	 * @param flags
	 *            FLAG_DEFAULT,FLAG_SHOW_PROGRESS,FLAG_SHOW_ERROR,FLAG_CANCEL
	 */
	public <T> void request(String url, HashMap<String, String> params,
			int flags) {
		boolean isShowProgress = (flags & FLAG_SHOW_PROGRESS) != 0;
		boolean isShowError = (flags & FLAG_SHOW_ERROR) != 0;
		boolean cancelable = (flags & FLAG_CANCEL) != 0;
		boolean isEncrypt = (flags & FLAG_ENCRYPT) != 0;

		request(url, params, isShowProgress, isShowError, cancelable, isEncrypt);
	}

	/**
	 * 请求JSON数据
	 * 
	 * @param url
	 *            请求接口路径
	 * @param params
	 *            请求参数
	 * @param isShowProgress
	 *            是否显示加载进度框
	 * @param isShowError
	 *            是否显示错误提示
	 * @param cancelable
	 *            是否当关闭进度框时取消相应请求
	 * @param isEncrypt
	 *            是否加密请求
	 */
	private <T> void request(String url, final HashMap<String, String> params,
			boolean isShowProgress, final boolean isShowError,
			boolean cancelable, final boolean isEncrypt) {
		final String sParams = params != null ? new Gson().toJson(params) : "";
		final String finalUrl = Constant.API_URL_PREFIX + url;
		LogUtil.i("volley-request", finalUrl + "[" + sParams + "]");
		final GsonRequest<JsonObject> request = new GsonRequest<JsonObject>(
				Request.Method.POST, finalUrl, JsonObject.class,
				new Response.Listener<JsonObject>() {
					@Override
					public void onResponse(JsonObject response) {
						LogUtil.e("volley-response", "[" + response.toString()
								+ "]");
						if (progressdialog != null
								&& progressdialog.isShowing())
							progressdialog.dismiss();
						try {
							if (response.get("result").getAsInt() == 0) {
								onRecvData(response);
							} else if (response.get("result").getAsInt() == 2) {
								Toast.makeText(BaseActivity.this,
										"身份认证已过期，请重新登录", Toast.LENGTH_LONG)
										.show();
							} else if (response.get("result").getAsInt() == 3) {
								Toast.makeText(BaseActivity.this,
										"身份认证不存在，请重新登录", Toast.LENGTH_LONG)
										.show();
							} else if (isShowError) {
								String code = response.get("code")
										.getAsString();
								String message = response.get("message")
										.getAsString();
								showAlertDialog("提示", code + message,
										new View.OnClickListener() {
											@Override
											public void onClick(View arg0) {
												alertDialog.cancel();
											}
										}, "确定", null, null);
							}
						} catch (Exception e) {
							if (isShowError) {
								showAlertDialog("提示", "数据解析失败",
										new View.OnClickListener() {
											@Override
											public void onClick(View arg0) {
												alertDialog.cancel();
											}
										}, "确定", null, null);
							}
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						LogUtil.i("info", volleyError.toString());
						if (progressdialog != null
								&& progressdialog.isShowing())
							progressdialog.dismiss();
						if (!isFinishing()) {
							if (isShowError) {
								showAlertDialog("提示", "数据请求失败",
										new View.OnClickListener() {
											@Override
											public void onClick(View arg0) {
												alertDialog.cancel();
											}
										}, "确定", null, null);
							}

						}
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = super.getHeaders();
				if (isEncrypt) {
					// 不指定为json，防止appserver误解析
					headers.put("Content-Type", "text/html; charset=UTF-8");
				}
				return headers;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				return params;
			}
		};

		if (isShowProgress) {
			this.progressdialog = new CustomProgressDialog(this,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							request.cancel();
						}
					}, cancelable);
			this.progressdialog.show();
		}

		RequestManager.addRequest(request);
	}

	public void showAlertDialog(String title, String msg,
			OnClickListener onPositiveListener,
			OnClickListener onNegativeListener) {
		if (alertDialog != null) {
			alertDialog.dismiss();
		}
		alertDialog = new CustomAlertDialog(this);
		alertDialog.show();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		if (onPositiveListener != null) {
			alertDialog.setOnPositiveListener(onPositiveListener);
		} else {
			alertDialog.getBtn_positive().setVisibility(View.GONE);
		}
		if (onNegativeListener != null) {
			alertDialog.setOnNegativeListener(onNegativeListener);
		} else {
			alertDialog.getBtn_negative().setVisibility(View.GONE);
		}
	}

	public void showAlertDialog(String title, String msg,
			OnClickListener onPositiveListener, String positiveStr,
			String negativeStr, OnClickListener onNegativeListener) {
		if (alertDialog != null) {
			alertDialog.dismiss();
		}
		alertDialog = new CustomAlertDialog(this);
		alertDialog.show();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		if (onPositiveListener != null) {
			alertDialog.setOnPositiveListener(onPositiveListener);
			if (positiveStr != null)
				alertDialog.getBtn_positive().setText(positiveStr);
		} else {
			alertDialog.getBtn_positive().setVisibility(View.GONE);
		}
		if (onNegativeListener != null) {
			alertDialog.setOnNegativeListener(onNegativeListener);
			if (negativeStr != null)
				alertDialog.getBtn_negative().setText(negativeStr);
		} else {
			alertDialog.getBtn_negative().setVisibility(View.GONE);
		}
	}

	public void showAlertDialogNormal(String title, String msg) {
		if (alertDialog != null) {
			alertDialog.dismiss();
		}
		alertDialog = new CustomAlertDialog(this);
		alertDialog.show();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setOnPositiveListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				alertDialog.dismiss();
			}
		});
		alertDialog.getBtn_negative().setVisibility(View.GONE);

	}

	public void showAlertDialogNormal(String title, String msg, boolean isNew) {
		if (alertDialog == null
				|| (alertDialog != null && !alertDialog.isShowing())) {
			if (isNew) {
				final CustomAlertDialog alertDialog = new CustomAlertDialog(
						this);
				alertDialog.show();
				alertDialog.setTitle(title);
				alertDialog.setMessage(msg);
				alertDialog.setOnPositiveListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						alertDialog.dismiss();
					}
				});
				alertDialog.getBtn_negative().setVisibility(View.GONE);
			}
		}
	}

	public void setEnableDrawerLayout(DrawerLayout mDrawerLayout,
			OnClickListener l) {
		LayoutInflater LayoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = LayoutInflater
				.inflate(R.layout.view_drawer_left_menu, null);
		view.setEnabled(false);
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(
				width * 3 / 4, DrawerLayout.LayoutParams.MATCH_PARENT);//
		lp.gravity = Gravity.START;
		view.setLayoutParams(lp);
		mDrawerLayout.addView(view);
		CircleImageView iv_user_img = (CircleImageView) view
				.findViewById(R.id.iv_user_head);
		iv_user_img.setOnClickListener(l);

		Button btnLogout = (Button) view.findViewById(R.id.btn_logout);
		StringUtils.setButtonTypeface(FontType.XIYUAN, this, btnLogout);
		btnLogout.setOnClickListener(l);

		TextView tv_login_reg = (TextView) view.findViewById(R.id.tv_login_reg);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_login_reg);

		TextView tv_login_slogon = (TextView) view
				.findViewById(R.id.tv_login_slogon);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_login_slogon);

		if (isLogined()) {
			ImageLoader.getInstance().displayImage(
					"http://file.bmob.cn/"
							+ BaseApplication.mInstance.getUserInfo(this)
									.getAvatar().getUrl(), iv_user_img,
					ImageManager.getUserHeadOptions());
			tv_login_reg
					.setText("欢迎回来，"
							+ BaseApplication.mInstance.getUserInfo(this)
									.getNickName());
			btnLogout.setVisibility(View.VISIBLE);
		} else {
			tv_login_reg.setText("注册或登录");
			iv_user_img.setImageResource(R.drawable.login_btn_default);
			btnLogout.setVisibility(View.GONE);
		}

		// 设置和意见反馈
		TextView tv_feedback = (TextView) view.findViewById(R.id.tv_feedback);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_feedback);

		TextView tv_settings = (TextView) view.findViewById(R.id.tv_settings);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_settings);

	}

	/**
	 * 判断用户是否登录
	 * 
	 * @return
	 */
	public boolean isLogined() {
		BmobUser user = BmobUser.getCurrentUser(this, User.class);
		if (user != null) {
			return true;
		}
		return false;
	}

}
