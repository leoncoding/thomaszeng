package com.thomas.path.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thoams.path.BaseActivity;
import com.thoams.path.BaseApplication;
import com.thomas.path.R;
import com.thomas.path.bean.User;
import com.thomas.path.manager.DialogManager;
import com.thomas.path.manager.FontType;
import com.thomas.path.manager.ImageManager;
import com.thomas.path.manager.StringUtils;
import com.thomas.path.manager.TitleManager;
import com.thomas.path.view.CircleImageView;

public class EditUserInfoActivity extends BaseActivity implements
		OnClickListener {
	private TextView txt_userhead, txt_nickname, tv_nickname, txt_sex, tv_sex,
			txt_city, tv_city, txt_sign, tv_sign;
	private User user;
	private CircleImageView head_iv;
	private Dialog chooseSexDialog, choosePhotoDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_userinfo);
		findViews();
		init();
		setListener();
	}

	@Override
	public void onRecvData(JsonObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		TitleManager.showTitle(this, new int[] { TitleManager.BACK, }, "个人资料",
				this);
		head_iv = (CircleImageView) this.findViewById(R.id.iv_user_head);
		head_iv.setOnClickListener(this);
		txt_userhead = (TextView) this.findViewById(R.id.txt_userhead);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, txt_userhead);
		txt_nickname = (TextView) this.findViewById(R.id.txt_nickname);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, txt_nickname);
		tv_nickname = (TextView) this.findViewById(R.id.tv_nickname);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_nickname);
		txt_sex = (TextView) this.findViewById(R.id.txt_sex);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, txt_sex);
		tv_sex = (TextView) this.findViewById(R.id.tv_sex);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_sex);
		tv_sex.setOnClickListener(this);
		txt_city = (TextView) this.findViewById(R.id.txt_city);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, txt_city);
		tv_city = (TextView) this.findViewById(R.id.tv_city);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_city);
		txt_sign = (TextView) this.findViewById(R.id.txt_sign);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, txt_sign);
		tv_sign = (TextView) this.findViewById(R.id.tv_sign);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_sign);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		user = BaseApplication.mInstance.getUserInfo(this);
		ImageLoader.getInstance().displayImage(
				"http://file.bmob.cn/" + user.getAvatar().getUrl(), head_iv,
				ImageManager.getUserHeadOptions());
		tv_nickname.setText(user.getNickName());
		if (user.getSex().equals("male")) {
			tv_sex.setText("男");
		} else {
			tv_sex.setText("女");
		}
		tv_city.setText(user.getCity());
		tv_sign.setText(user.getSign());
		View view = View.inflate(this, R.layout.layout_actionsheet_up_head,
				null);
		choosePhotoDialog = (Dialog) DialogManager.getActionSheet(this, view,
				this);
		View sexview = View.inflate(this,
				R.layout.layout_actionsheet_choose_sex, null);
		chooseSexDialog = (Dialog) DialogManager.getActionSheet(this, sexview,
				this);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back_iv:
			finish();
			break;
		case R.id.iv_user_head:
			choosePhotoDialog.show();
			break;
		case R.id.tv_sex:
			chooseSexDialog.show();
			break; 
		default:
			break;
		}
	}

}
