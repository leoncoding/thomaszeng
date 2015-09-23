package com.thomas.path.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thomas.path.R;
import com.thomas.path.bean.User;
import com.thomas.path.manager.FontType;
import com.thomas.path.manager.ImageManager;
import com.thomas.path.manager.StringUtils;
import com.thomas.path.view.CircleImageView;

public class ListPersonAdapter extends BaseAdapter {
	private List<User> mData;
	private LayoutInflater inflater;
	private Context context;

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public ListPersonAdapter(Context context, List<User> mData) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			/* 绑定相应的视图 */
			convertView = inflater.inflate(R.layout.item_person_list, null);
			holder.imgViewHead = (ImageView) convertView
					.findViewById(R.id.iv_person_img);
			holder.txtViewNickName = (TextView) convertView
					.findViewById(R.id.tv_personname);
			holder.txtViewSex = (TextView) convertView
					.findViewById(R.id.tv_sex);
			holder.txtViewCity = (TextView) convertView
					.findViewById(R.id.tv_city);
			holder.tv_panos = (TextView) convertView
					.findViewById(R.id.tv_panos);
			holder.tv_follows = (TextView) convertView
					.findViewById(R.id.tv_follows);
			holder.tv_following = (TextView) convertView
					.findViewById(R.id.tv_following);
			holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.txtViewNickName);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.txtViewSex);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.txtViewCity);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.tv_panos);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.tv_follows);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.tv_following);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.tv_sign);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// TODO 异步加载图片
		holder.tv_panos.setText("0\n全景图");
		holder.tv_follows.setText("0\n粉丝");
		holder.tv_following.setText("0\n关注");

		holder.txtViewNickName.setText(mData.get(position).getNickName());
		if (mData.get(position).getSex().equals("male")) {
			holder.txtViewSex.setText("男");
		} else {
			holder.txtViewSex.setText("女");
		}
		holder.txtViewCity.setText(mData.get(position).getCity());
		holder.tv_sign.setText(mData.get(position).getSign());

		ImageLoader.getInstance().displayImage(
				"http://file.bmob.cn/"
						+ mData.get(position).getAvatar().getUrl(),
				holder.imgViewHead, ImageManager.getUserHeadOptions());
		return convertView;
	}

	/**
	 * 自定义类 绑定视图
	 * 
	 * @author zwt
	 */
	final class ViewHolder {
		public ImageView imgViewHead;
		public TextView txtViewNickName, txtViewSex, txtViewCity, tv_panos,
				tv_follows, tv_following, tv_sign;
	}

}
