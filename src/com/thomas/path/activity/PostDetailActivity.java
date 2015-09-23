package com.thomas.path.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.JsonObject;
import com.thoams.path.BaseActivity;
import com.thomas.path.R;
import com.thomas.path.adapter.ListItemClickHelp;
import com.thomas.path.manager.TitleManager;

public class PostDetailActivity extends BaseActivity implements
		OnClickListener, ListItemClickHelp {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_detail);
		init();
		findViews();
		setListener();
	}

	@Override
	public void onRecvData(JsonObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		TitleManager.showTitle(this, new int[] { TitleManager.BACK, }, "全景图",
				this);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back_iv:
			finish();
			break;

		default:
			break;
		}
	}

}
