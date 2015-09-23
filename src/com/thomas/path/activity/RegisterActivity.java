package com.thomas.path.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.google.gson.JsonObject;
import com.thoams.path.BaseActivity;
import com.thoams.path.proxy.UserProxy;
import com.thoams.path.proxy.UserProxy.ISignUpListener;
import com.thomas.path.R;
import com.thomas.path.bean.User;
import com.thomas.path.manager.DialogManager;
import com.thomas.path.manager.FontType;
import com.thomas.path.manager.StringUtils;
import com.thomas.path.manager.TitleManager;
import com.thomas.path.manager.ToastManager;

public class RegisterActivity extends BaseActivity implements OnClickListener,
		ISignUpListener {
	private Button btn_register, btn_yanzhengma;
	private EditText edt_user, edt_pwd, edt_yanzhengma;
	private CheckBox ckb_follow;
	private TextView tv_zhengce;
	private UserProxy userProxy;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
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
				R.string.register_title, this);
		btn_yanzhengma = (Button) this.findViewById(R.id.btn_yanzhengma);
		StringUtils.setButtonTypeface(FontType.XIYUAN, this, btn_yanzhengma);

		btn_register = (Button) this.findViewById(R.id.btn_register);
		StringUtils.setButtonTypeface(FontType.XIYUAN, this, btn_register);

		edt_user = (EditText) this.findViewById(R.id.edt_user);
		StringUtils.setEditTextTypeface(FontType.XIYUAN, this, edt_user);

		edt_pwd = (EditText) this.findViewById(R.id.edt_password);
		StringUtils.setEditTextTypeface(FontType.XIYUAN, this, edt_pwd);

		edt_yanzhengma = (EditText) this.findViewById(R.id.edt_yanzhengma);
		StringUtils.setEditTextTypeface(FontType.XIYUAN, this, edt_yanzhengma);

		ckb_follow = (CheckBox) this.findViewById(R.id.ckb_follow);
		StringUtils.setCheckBoxTypeface(FontType.XIYUAN, this, ckb_follow);

		tv_zhengce = (TextView) this.findViewById(R.id.tv_zhengce);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_zhengce);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		userProxy = new UserProxy(this);
		userProxy.setOnSignUpListener(this);
		dialog = DialogManager.getLoadingDialog(this);

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btn_register.setOnClickListener(this);
		btn_yanzhengma.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title_back_iv:
			finish();
			break;
		case R.id.btn_register:
			if (TextUtils.isEmpty(edt_user.getText())) {
				ToastManager.show(this, "请输入手机号码", 1 * 1000);
				return;
			}
			if (TextUtils.isEmpty(edt_pwd.getText())) {
				ToastManager.show(this, "请输入密码", 1 * 1000);
				return;
			}
			if (TextUtils.isEmpty(edt_yanzhengma.getText())) {
				ToastManager.show(this, "请输入验证码", 1 * 1000);
				return;
			}
			verifySmsCode(edt_user.getText().toString().trim(), edt_yanzhengma
					.getText().toString().trim());
			dialog.show();
			break;
		case R.id.btn_yanzhengma:
			if (TextUtils.isEmpty(edt_user.getText())) {
				ToastManager.show(this, "请输入手机号码", 1 * 1000);
				return;
			}
			requestSMSCode(edt_user.getText().toString());
			break;

		default:
			break;
		}
	}

	@Override
	public void onSignUpSuccess() {
		// TODO Auto-generated method stub
		dialog.dismiss();
		ToastManager.show(this, "注册成功", 1 * 1000);
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	@Override
	public void onSignUpFailure(String msg) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		ToastManager.show(this, "注册失败" + msg, 1 * 1000);
	}

	/**
	 * 获取验证码
	 * 
	 * @param phoneNum
	 */
	public void requestSMSCode(String phoneNum) {
		BmobSMS.requestSMSCode(this, phoneNum, "path验证码",
				new RequestSMSCodeListener() {
					@Override
					public void done(Integer smsId, BmobException ex) {
						// TODO Auto-generated method stub
						if (ex == null) {// 验证码发送成功
							ToastManager.show(RegisterActivity.this, "短信id："
									+ smsId);// 用于查询本次短信发送详情
						} else {
							ToastManager.show(RegisterActivity.this, "获取验证码失败"
									+ ex.getMessage());
						}
					}
				});
	}

	/**
	 * 验证手机用户是否被注册
	 * 
	 * @param phoneNum
	 */
	public void verifyUser(String phoneNum) {
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereContains(phoneNum, "username");

	}

	/**
	 * 验证验证码
	 * 
	 * @param phoneNum
	 * @param code
	 */
	public void verifySmsCode(String phoneNum, String code) {
		BmobSMS.verifySmsCode(this, phoneNum, code,
				new VerifySMSCodeListener() {
					@Override
					public void done(BmobException ex) {
						// TODO Auto-generated method stub
						if (ex == null) {// 短信验证码已验证成功
							ToastManager.show(RegisterActivity.this, "验证通过");
							userProxy.signUp(edt_user.getText().toString()
									.trim(), edt_pwd.getText().toString()
									.trim());
						} else {
							ToastManager.show(
									RegisterActivity.this,
									"验证失败：code =" + ex.getErrorCode()
											+ ",msg = "
											+ ex.getLocalizedMessage());
							dialog.dismiss();
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
