package com.thomas.path.manager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thomas.path.R;

public class TitleManager {
	/**
	 * app title工具,下面的常量用于setTiele方法中的flag，flag代表的是title中我要显示的iv
	 */
	public static final int TITLE_NAME = 2;// 标题
	public static final int BACK = 3; // me 界面的set
	public static final int SET = 4; // me 界面的set
	public static final int BACK_ARROW_H = 5; // 返回
	public static final int NEXT_ARROW_H = 6; // 下一步
	public static final int CHANGE_PHONE = 7;// 更换号码
	public static final int GROUP = 8;// 群聊成员
	public static final int CAMERA = 9;// 当前聊天用户的信息

	/**
	 * 
	 * @param from
	 *            当前的界面
	 * @param flags
	 *            title中需要显示的view
	 * @param title
	 *            title正中需要显示的文本
	 * @param left
	 *            title左边要显示的文本资源
	 * @param right
	 *            title右边需要显示的文本资源
	 * @param to
	 *            需要调整到那一个界面
	 */
	public static void showTitle(final Activity from, int[] flags,
			Object title, int left, int right, OnClickListener l) {

		if (title instanceof Integer) {
			int title2 = (Integer) title;
			if (title2 > 0) {
				TextView tv = (TextView) from.findViewById(R.id.title_title_tv);
				tv.setText(title2);
				StringUtils.setTextTypeface(FontType.XIYUAN, from, tv);
			}
		}
		if (title instanceof String) {
			TextView tv = (TextView) from.findViewById(R.id.title_title_tv);
			tv.setText((String) title);
			StringUtils.setTextTypeface(FontType.XIYUAN, from, tv);
		}

		if (left > 0) {// 如果设置了文本，该Linearlayout一定要显示
			LinearLayout ll = (LinearLayout) from
					.findViewById(R.id.title_back_ll);
			ll.setVisibility(View.VISIBLE);
			ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					from.finish();
				}
			});
			TextView tv = (TextView) from.findViewById(R.id.title_back_tv);
			tv.setText(left);
			StringUtils.setTextTypeface(FontType.XIYUAN, from, tv);
		}
		if (right > 0) {
			LinearLayout ll = (LinearLayout) from
					.findViewById(R.id.title_next_ll);
			ll.setVisibility(View.VISIBLE);
			TextView tv = (TextView) from.findViewById(R.id.title_next_tv);
			tv.setText(right);
			tv.setOnClickListener(l);
			StringUtils.setTextTypeface(FontType.XIYUAN, from, tv);
		}

		if (flags != null) {// 为null 是没有title bar
			for (int flag : flags) {
				switch (flag) {
				case BACK: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_back_iv);
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(l);
					break;
				}

				case GROUP: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_group_iv);
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(l);
					break;
				}
				case CAMERA: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_camera_iv);
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(l);
					break;
				}
				case CHANGE_PHONE: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_change);
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(l);
					break;
				}

				case SET: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_set_iv);
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(l);
					break;

				}

				}
			}
		} else {
			ImageView iv = (ImageView) from.findViewById(R.id.title_set_iv);
			if (iv.isShown()) {
				iv.setVisibility(View.GONE);
			}
		}

	}

	public static void showTitle(final Activity activity, int[] flags,
			Object title, OnClickListener l) {
		showTitle(activity, flags, title, 0, 0, l);
	}

}
