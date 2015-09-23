package com.thoams.path.proxy;

import java.io.File;

import android.content.Context;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thomas.path.bean.User;
import com.thomas.path.manager.Constant;
import com.thomas.path.manager.LogUtil;

public class UserProxy {

	public static final String TAG = "UserProxy";

	private Context mContext;

	public UserProxy(Context context) {
		this.mContext = context;
	}

	public void login(String userName, String password) {
		final BmobUser user = new BmobUser();
		user.setUsername(userName);
		user.setPassword(password);
		user.login(mContext, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if (loginListener != null) {
					loginListener.onLoginSuccess();
				} else {
					LogUtil.e(TAG, "login listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if (loginListener != null) {
					loginListener.onLoginFailure(msg);
				} else {
					LogUtil.e(TAG, "login listener is null,you must set one!");
				}
			}
		});
	}

	public interface ILoginListener {
		void onLoginSuccess();

		void onLoginFailure(String msg);
	}

	private ILoginListener loginListener;

	public void setOnLoginListener(ILoginListener loginListener) {
		this.loginListener = loginListener;
	}

	
	public interface ISignUpListener{
		void onSignUpSuccess();
		void onSignUpFailure(String msg);
	}
	private ISignUpListener signUpLister;
	public void setOnSignUpListener(ISignUpListener signUpLister){
		this.signUpLister = signUpLister;
	}
	
	public void signUp(String userName,String password){
		User user = new User();
		user.setUsername(userName);
		user.setPassword(password); 
		user.setSex(Constant.SEX_FEMALE); 
		user.setSignature("这个家伙很懒，什么也不说。。。");
		user.signUp(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(signUpLister != null){
					signUpLister.onSignUpSuccess();
				}else{
					LogUtil.i(TAG,"signup listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(signUpLister != null){
					signUpLister.onSignUpFailure(msg);
				}else{
					LogUtil.i(TAG,"signup listener is null,you must set one!");
				}
			}
		});
	}
}
