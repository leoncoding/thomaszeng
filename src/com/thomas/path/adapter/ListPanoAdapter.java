package com.thomas.path.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.thomas.path.R;
import com.thomas.path.bean.Post;
import com.thomas.path.manager.FontType;
import com.thomas.path.manager.ImageManager;
import com.thomas.path.manager.LogUtil;
import com.thomas.path.manager.StringUtils;
import com.thomas.path.view.CircleImageView;

public class ListPanoAdapter extends BaseAdapter {
	private List<Post> mData;
	private LayoutInflater inflater;
	private Context context;
	private ListItemClickHelp callback;

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public ListPanoAdapter(Context context, List<Post> mData,
			ListItemClickHelp callback) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mData = mData;
		this.callback = callback;
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
			convertView = inflater
					.inflate(R.layout.item_panoprefect_list, null);
			holder.imgViewHead = (CircleImageView) convertView
					.findViewById(R.id.iv_user_img);
			holder.imgContent = (ImageView) convertView
					.findViewById(R.id.iv_pano_img);
			holder.txtViewNickName = (TextView) convertView
					.findViewById(R.id.tv_username);
			holder.txtViewLikes = (TextView) convertView
					.findViewById(R.id.tv_like);
			holder.dialog_pb = (ProgressBar) convertView
					.findViewById(R.id.dialog_pb);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.txtViewNickName);
			StringUtils.setTextTypeface(FontType.XIYUAN, context,
					holder.txtViewLikes);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mData.get(position).getAuthor() != null) {
			holder.txtViewNickName.setText(mData.get(position).getAuthor()
					.getNickName());
		}

		if (mData.get(position).getLikes_number() != null) {
			holder.txtViewLikes.setText(mData.get(position).getLikes_number());
		}

		// TODO 异步加载图片
		final View view = convertView;
		final int p = position;
		final int one = holder.imgViewHead.getId();
		final int two = holder.imgContent.getId();
		if (mData.get(position).getAuthor() != null) {
			ImageLoader.getInstance().displayImage(
					"http://file.bmob.cn/"
							+ mData.get(position).getAuthor().getAvatar()
									.getUrl(), holder.imgViewHead,
					ImageManager.getUserHeadOptions());
			LogUtil.e("HomeActivity", "头像图片地址=" + "http://file.bmob.cn/"
					+ mData.get(position).getAuthor().getAvatar().getUrl());
		}

		if (mData.get(position).getContent() != null) {
			// add tag for image, to compare it when image loaded finish
			String imageUrl = "http://file.bmob.cn/"
					+ mData.get(position).getContent().getUrl();
			ImageAware imageAware = new ImageViewAware(holder.imgContent, false);
			ImageLoader.getInstance()
					.displayImage(imageUrl, imageAware,
							ImageManager.getPanoOptions(),
							new MyImageLoadingListener());
			LogUtil.e("HomeActivity", "全景图片地址=" + imageUrl);
		}

		holder.imgViewHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(view, parent, p, one);
			}
		});
		holder.imgContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(view, parent, p, two);
			}
		});
		return convertView;
	}

	/**
	 * 自定义类 绑定视图
	 * 
	 * @author zwt
	 */
	final class ViewHolder {
		public CircleImageView imgViewHead;
		public ImageView imgContent;
		public TextView txtViewNickName, txtViewLikes;
		public ProgressBar dialog_pb;
	}

	private class MyImageLoadingListener implements ImageLoadingListener {
		@Override
		public void onLoadingCancelled(String msg, View view) {
			// TODO Auto-generated method stub
			// 加载取消的时候执行
			LogUtil.e("HomeActivity", "加载取消的时候执行");
		}

		@Override
		public void onLoadingComplete(String msg, View view, Bitmap loadedImage) {
			// TODO Auto-generated method stub
			// 加载成功的时候执行
			LogUtil.e("HomeActivity", "加载成功的时候执行");
		}

		@Override
		public void onLoadingFailed(String msg, View view, FailReason failReason) {
			// TODO Auto-generated method stub
			// 加载失败的时候执行
			LogUtil.e("HomeActivity", "加载失败的时候执行");
		}

		@Override
		public void onLoadingStarted(String msg, View view) {
			// TODO Auto-generated method stub
			// 开始加载的时候执行
			LogUtil.e("HomeActivity", "开始加载的时候执行");
		}
	}

}
