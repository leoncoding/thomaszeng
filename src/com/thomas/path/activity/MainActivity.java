package com.thomas.path.activity;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.JsonObject;
import com.thoams.path.BaseActivity;
import com.thomas.path.R;
import com.thomas.path.adapter.MainTabPager;
import com.thomas.path.fragment.HomeFragment;
import com.thomas.path.fragment.UserCenterFragment;
import com.thomas.path.manager.TitleManager;
import com.thomas.path.view.ViewPagerUnTouch;

public class MainActivity extends BaseActivity implements
		OnCheckedChangeListener, OnPageChangeListener, OnClickListener {
	public static final String BOTTOM_TAB_INDEX = "bottom_tab_index";
	public static final String TOP_TAB_INDEX = "top_tab_index";
	public static final String ACTION_CHANGE_TOP_TAB_NEARBY = "action_change_top_tab";
	public static final String ACTION_CHANGE_TOP_TAB_CONTACT = "action_change_contact_top_tab";
	public static final String ACTION_CHANGE_BOTTOM_TAB = "action_change_bottom_tab";
	private Fragment homeFragment;
	private Fragment userFragment;
	private ViewPager mViewPager;
	// 底部tab导航
	private RadioGroup mTabRG;
	private ChangeTabAndItemReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		init();
		setListener();
		
	}

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		TitleManager.showTitle(this, new int[] { TitleManager.SET },
				R.string.app_name, this);
		mViewPager = (ViewPagerUnTouch) findViewById(R.id.main_vp);
		mTabRG = (((RadioGroup) findViewById(R.id.main_tab_group)));
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		homeFragment = new HomeFragment();
		list.add(homeFragment);
		userFragment = new UserCenterFragment();
		list.add(userFragment);
		MainTabPager myAdapter = new MainTabPager(getSupportFragmentManager(),
				list);
		mViewPager.setAdapter(myAdapter);
		mViewPager.setOffscreenPageLimit(2);
		// ----注册改变条目和tab的广播--------------
		receiver = new ChangeTabAndItemReceiver();
		IntentFilter filter = new IntentFilter(ACTION_CHANGE_BOTTOM_TAB);
		registerReceiver(receiver, filter);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		mViewPager.setOnPageChangeListener(this);
		mTabRG.setOnCheckedChangeListener(this);
	}

	@Override
	public void onRecvData(JsonObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			TitleManager.showTitle(this, new int[] { TitleManager.SET },
					R.string.app_name, this);
			break;
		case 1:
			TitleManager.showTitle(this, null, R.string.title_home, this);

			break;
		case 2:
			TitleManager.showTitle(this, null, R.string.title_user, this);
			break;

		}
		RadioButton rb = (RadioButton) mTabRG.getChildAt(arg0);
		rb.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int index = 0;
		switch (checkedId) {
		case R.id.main_tab_home:
			TitleManager.showTitle(this, new int[] { TitleManager.SET },
					R.string.app_name, this);
			index = 0;
			break;
		case R.id.main_tab_user:
			TitleManager.showTitle(this, null, R.string.app_name, this);
			index = 1;
			break;

		}
		mViewPager.setCurrentItem(index);
	}

	class ChangeTabAndItemReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int bottomTab = intent.getIntExtra(BOTTOM_TAB_INDEX, -1);
			int topTab = intent.getIntExtra(TOP_TAB_INDEX, -1);
			if (bottomTab != -1) {
				mViewPager.setCurrentItem(bottomTab);
				if (topTab != -1) {// 如果上面的导航有改变
					Intent i = null;
					if (topTab == 1) {
						i = new Intent(ACTION_CHANGE_TOP_TAB_NEARBY);
					} else {
						i = new Intent(ACTION_CHANGE_TOP_TAB_CONTACT);
					}
					i.putExtra(TOP_TAB_INDEX, topTab);
					sendBroadcast(i);
				}
			}
		}

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
