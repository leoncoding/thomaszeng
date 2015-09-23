package com.thomas.path.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thoams.path.BaseActivity;
import com.thomas.path.R;
import com.thomas.path.adapter.ListItemClickHelp;
import com.thomas.path.adapter.ListPanoAdapter;
import com.thomas.path.bean.Post;
import com.thomas.path.bean.User;
import com.thomas.path.manager.DialogManager;
import com.thomas.path.manager.FontType;
import com.thomas.path.manager.ImageManager;
import com.thomas.path.manager.LogUtil;
import com.thomas.path.manager.StringUtils;
import com.thomas.path.manager.TitleManager;
import com.thomas.path.view.CircleImageView;

public class PersonActivity extends BaseActivity implements OnClickListener,
		ListItemClickHelp {
	private String userId;
	private User user;
	private Dialog loderDialog;
	private ListView lstViewPersonContent;
	private ListPanoAdapter mAdapter;
	private List<Post> mData = new ArrayList<Post>();
	private View headerView;
	private TextView tv_username, tv_panos, tv_follows, tv_following;
	private Button btnFollow;
	private CircleImageView head_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
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
		lstViewPersonContent = (ListView) this
				.findViewById(R.id.lstViewPersonContent);
		mAdapter = new ListPanoAdapter(this, mData, this);
		lstViewPersonContent.setAdapter(mAdapter);
		headerView = getLayoutInflater().inflate(R.layout.layout_person_head,
				null);
		lstViewPersonContent.addHeaderView(headerView);
		head_iv = (CircleImageView) headerView.findViewById(R.id.iv_user_head);
		ImageLoader.getInstance().displayImage(
				"http://file.bmob.cn/" + user.getAvatar().getUrl(), head_iv,
				ImageManager.getUserHeadOptions());

		tv_username = (TextView) headerView.findViewById(R.id.tv_username);
		tv_username.setText(user.getNickName());
		tv_panos = (TextView) headerView.findViewById(R.id.tv_panos);
		tv_panos.setText("0\n全景图");
		tv_follows = (TextView) headerView.findViewById(R.id.tv_follows);
		tv_follows.setText("0\n粉丝");
		tv_following = (TextView) headerView.findViewById(R.id.tv_following);
		tv_following.setText("0\n关注");
		btnFollow = (Button) headerView.findViewById(R.id.btn_follow);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_username);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_panos);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_following);
		StringUtils.setTextTypeface(FontType.XIYUAN, this, tv_follows);
		StringUtils.setButtonTypeface(FontType.XIYUAN, this, btnFollow);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		loderDialog = DialogManager.getLoadingDialog(this, "正在努力加载ing....");
		user = (User) getIntent().getExtras().get("user");
		// 判断是否是自己
		if (isCurrentUser(user)) {
			TitleManager.showTitle(PersonActivity.this, new int[] {
					TitleManager.BACK, TitleManager.CAMERA },
					user.getNickName(), PersonActivity.this);
		} else {
			TitleManager.showTitle(PersonActivity.this,
					new int[] { TitleManager.BACK }, user.getNickName(),
					PersonActivity.this);
		}

		getPersonInfo(userId);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
	}

	/**
	 * 获取用户的信息
	 * 
	 * @param objectId
	 */
	private void getPersonInfo(String objectId) {
		loderDialog.show();
		BmobQuery<Post> query = new BmobQuery<Post>();
		query.order("-createdAt");
		query.include("author,favorite");
		query.addWhereEqualTo("author", user);
		query.findObjects(this, new FindListener<Post>() {
			@Override
			public void onSuccess(List<Post> data) {
				// TODO Auto-generated method stub
				loderDialog.dismiss();
				mData.addAll(data);
				mAdapter.notifyDataSetChanged();
				tv_panos.setText(data.size() + "\n全景图");
				LogUtil.e("PersonActivity",
						"###查询用户的全景图集合###" + data.toString());
			}

			@Override
			public void onError(int arg0, String msg) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				LogUtil.e("PersonActivity", "###查询用户全景图失败###" + msg);
				loderDialog.dismiss();
			}
		});

	}

	/**
	 * 判断点击条目的用户是否是当前登录用户
	 * 
	 * @return
	 */
	private boolean isCurrentUser(User user) {
		if (null != user) {
			User cUser = BmobUser.getCurrentUser(this, User.class);
			if (cUser != null && cUser.getObjectId().equals(user.getObjectId())) {
				return true;
			}
		}
		return false;
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

	@Override
	public void onClick(View item, View widget, int position, int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case R.id.iv_pano_img:
			LogUtil.e("######", "####点击条目####" + mData.get(position).toString());
			startActivity(new Intent(this, PostDetailActivity.class));
			break;
		default:
			break;
		}
	}

}
