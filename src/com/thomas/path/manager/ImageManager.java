package com.thomas.path.manager;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thomas.path.R;

/**
 * 图片异步缓存配置
 * 
 * @author thomas
 * 
 */
public class ImageManager {

	public static ImageLoader getInstance() {
		return ImageLoader.getInstance();
	}

	private static DisplayImageOptions userHeadOptions;
	private static DisplayImageOptions panoOptions;

	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getUserHeadOptions() {
		if (userHeadOptions == null) {
			userHeadOptions = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.login_btn_default)
					.showImageOnFail(R.drawable.login_btn_default)
					.showImageOnLoading(R.drawable.login_btn_default)
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置为RGB565比起默认的ARGB_8888要节省大量的内存
					.delayBeforeLoading(100)// 载入图片前稍做延时可以提高整体滑动的流畅度
					.cacheInMemory(true).cacheOnDisc(true).build();
		}
		return userHeadOptions;
	}

	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getPanoOptions() {
		if (panoOptions == null) {
			panoOptions = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.panoprefect01)
					.showImageOnFail(R.drawable.panoprefect01) 
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置为RGB565比起默认的ARGB_8888要节省大量的内存
					.delayBeforeLoading(100)// 载入图片前稍做延时可以提高整体滑动的流畅度
					.cacheInMemory(true).cacheOnDisc(true).build();
		}
		return panoOptions;
	}

}
