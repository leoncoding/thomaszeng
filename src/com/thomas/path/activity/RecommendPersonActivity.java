package com.thomas.path.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.google.gson.JsonObject;
import com.thoams.path.BaseActivity;
import com.thomas.path.R;
import com.thomas.path.adapter.ListPersonAdapter;
import com.thomas.path.bean.User;
import com.thomas.path.manager.DialogManager;
import com.thomas.path.manager.LogUtil;
import com.thomas.path.manager.TitleManager;
import com.thomas.path.manager.ToastManager;

public class RecommendPersonActivity extends BaseActivity implements
		OnClickListener {
	private ListView lstViewRecommendContent;
	private ListPersonAdapter mAdapter;
	private List<User> mData;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_person);
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
		TitleManager.showTitle(this, new int[] { TitleManager.BACK }, "推荐的用户",
				this);
		lstViewRecommendContent = (ListView) this
				.findViewById(R.id.lstViewRecommendContent);
		mData = new ArrayList<User>(); 
		mAdapter = new ListPersonAdapter(this, mData);
		lstViewRecommendContent.setAdapter(mAdapter);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		dialog = DialogManager.getLoadingDialog(this);
		BmobQuery<User> query = new BmobQuery<User>();
		// 按照时间降序
		query.order("-createdAt");
		dialog.show();
		// 执行查询，第一个参数为上下文，第二个参数为查找的回调
		query.findObjects(this, new FindListener<User>() {
			@Override
			public void onSuccess(List<User> users) {
				mData.addAll(users);
				LogUtil.e("HomeActivity", "查询成功：" + users.toString());
				mAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}

			@Override
			public void onError(int code, String msg) {
				ToastManager
						.show(RecommendPersonActivity.this, "查询数据失败：" + msg);
				dialog.dismiss();
			}
		});
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title_back_iv:
			finish();
			break;

		default:
			break;
		}
	}

}
