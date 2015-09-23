package com.thomas.path.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.google.gson.JsonObject;
import com.thoams.path.BaseActivity;
import com.thoams.path.BaseApplication;
import com.thoams.path.proxy.UserProxy;
import com.thoams.path.proxy.UserProxy.ILoginListener;
import com.thomas.path.R;
import com.thomas.path.bean.User;
import com.thomas.path.manager.DialogManager;
import com.thomas.path.manager.FontType;
import com.thomas.path.manager.LogUtil;
import com.thomas.path.manager.StringUtils;
import com.thomas.path.manager.TitleManager;
import com.thomas.path.manager.ToastManager;

public class LoginActivity extends BaseActivity implements OnClickListener,
		ILoginListener {
	private Button btn_login;
	private EditText edt_user, edt_pwd;
	private TextView tv_forget_pwd, tv_third, tv_qq, tv_wechat, tv_sina;
	private UserProxy userProxy;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViews();
		setListener();
		init();
	}

	@Override
	public void onRecvData(JsonObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		TitleManager.showTitle(this, new int[] { TitleManager.BACK },
				R.string.login_title, 0, R.string.register_title, this);

		btn_login = (Button) this.findViewById(R.id.btn_login);
		StringUtils.setButtonTypeface(FontType.XIYUAN, this, btn_login);

		edt_user = (EditText) this.findViewById(R.id.edt_user);
		StringUtils.setEditTextTypeface(FontType.XIYUAN, this, edt_user);

		edt_pwd = (EditText) this.findViewById(R.id.edt_password);
		StringUtils.setEditTextTypeface(FontType.XIYUAN, this, edt_pwd);

		tv_forget_pwd = (TextView) this.findViewById(R.id.tv_forget_pwd);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_forget_pwd);

		tv_third = (TextView) this.findViewById(R.id.tv_third);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_third);

		tv_qq = (TextView) this.findViewById(R.id.tv_qq);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_qq);

		tv_wechat = (TextView) this.findViewById(R.id.tv_wechat);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_wechat);

		tv_sina = (TextView) this.findViewById(R.id.tv_sina);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_sina);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		userProxy = new UserProxy(this);
		userProxy.setOnLoginListener(this);
		dialog = DialogManager.getLoadingDialog(this);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btn_login.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title_back_iv:
			finish();
			break;
		case R.id.title_next_tv:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.btn_login:
			if (TextUtils.isEmpty(edt_user.getText())) {
				ToastManager.show(this, "请输入手机号码", 1 * 1000);
				return;
			}
			if (TextUtils.isEmpty(edt_pwd.getText())) {
				ToastManager.show(this, "请输入密码", 1 * 1000);
				return;
			}
			userProxy.login(edt_user.getText().toString().trim(), edt_pwd
					.getText().toString().trim());
			dialog.show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		DialogManager.toggleDialog(dialog);
		// 报错用户信息
		User user = BmobUser.getCurrentUser(this, User.class);
		BaseApplication.mInstance.saveUserInfo(this, user);
		LogUtil.e("LoginActivity", user.toString());
		startActivity(new Intent(this, HomeActivity.class));
		finish();
	}

	@Override
	public void onLoginFailure(String msg) {
		// TODO Auto-generated method stub
		DialogManager.toggleDialog(dialog);
		ToastManager.show(this, "login failed!" + msg, 1 * 1000);
		LogUtil.e("LoginActivity", "login failed!" + msg);
	}

}
