package com.thomas.path.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.google.gson.JsonObject;
import com.thoams.path.BaseActivity;
import com.thoams.path.BaseApplication;
import com.thomas.path.R;
import com.thomas.path.adapter.ListItemClickHelp;
import com.thomas.path.adapter.ListPanoAdapter;
import com.thomas.path.bean.Post;
import com.thomas.path.manager.DialogManager;
import com.thomas.path.manager.LogUtil;
import com.thomas.path.manager.TitleManager;
import com.thomas.path.manager.ToastManager;

public class HomeActivity extends BaseActivity implements OnClickListener,
		ListItemClickHelp {
	private DrawerLayout mDrawerLayout;
	private Button btnAddPerson;
	private Dialog dialog, loderDialog;
	private ListPanoAdapter mAdapter;
	private List<Post> mData = new ArrayList<Post>();
	private ListView lstViewContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_old);
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
		TitleManager.showTitle(this, new int[] { TitleManager.CHANGE_PHONE },
				R.string.app_name, this);
		// 侧滑
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		setEnableDrawerLayout(mDrawerLayout, this);
		lstViewContent = (ListView) this.findViewById(R.id.lstViewContent);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		btnAddPerson = (Button) this.findViewById(R.id.btn_add_person);
		dialog = DialogManager.getCommWarnDialog(this, this);
		loderDialog = DialogManager.getLoadingDialog(this, "正在努力加载ing....");
		mAdapter = new ListPanoAdapter(this, mData, this);
		lstViewContent.setAdapter(mAdapter);
		BmobQuery<Post> query = new BmobQuery<Post>();
		// 按照时间降序
		query.order("-createdAt");
		query.include("author");
		loderDialog.show();
		// 执行查询，第一个参数为上下文，第二个参数为查找的回调
		query.findObjects(this, new FindListener<Post>() {
			@Override
			public void onSuccess(List<Post> posts) {
				mData.addAll(posts);
				LogUtil.e("HomeActivity", "查询成功：" + posts.toString());
				mAdapter.notifyDataSetChanged();
				loderDialog.dismiss();
			}

			@Override
			public void onError(int code, String msg) {
				ToastManager.show(HomeActivity.this, "查询数据失败：" + msg);
				loderDialog.dismiss();
			}
		});

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btnAddPerson.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title_change:
			mDrawerLayout.openDrawer(Gravity.START);
			break;
		case R.id.iv_user_head:
			if (isLogined()) {
				Intent intent = new Intent(this, PersonActivity.class);
				intent.putExtra("user",
						BaseApplication.mInstance.getUserInfo(this));
				startActivity(intent);
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}

			break;
		case R.id.btn_logout:
			BmobUser.logOut(this);
			ToastManager.show(this, "登出成功");
			BaseApplication.mInstance.saveUserInfo(this, null);
			startActivity(new Intent(this, WelcomeActivity.class));
			finish();
			break;
		case R.id.btn_add_person:
			if (isLogined()) {
				startActivity(new Intent(this, AddPostActivity.class));
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;

		case R.id.warn_sure_bt:
			BaseApplication.mInstance.exit();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawers();
		} else {
			dialog.show();
		}
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case R.id.iv_user_img:
			if (isLogined()) {
				Intent intent = new Intent(this, PersonActivity.class);
				intent.putExtra("user", mData.get(position).getAuthor());
				startActivity(intent);
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}

			break;
		case R.id.iv_pano_img:
			if (isLogined()) {
				startActivity(new Intent(this, PostDetailActivity.class));
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		default:
			break;
		}
	}
}
