package com.thoams.path;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cn.bmob.v3.Bmob;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.thomas.path.bean.User;
import com.thomas.path.manager.CustomCrashHandler;
import com.thomas.path.manager.LogUtil;

public class BaseApplication extends Application {
	public static BaseApplication mInstance;
	public static List<Activity> allActivity = new LinkedList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initImageLoader(getApplicationContext());
		initCrashHandler();
		initBmobSDK();
	}

	// 初始化全局异常处理类
	public void initCrashHandler() {
		CustomCrashHandler mCustomCrashHandler = CustomCrashHandler
				.getInstance();
		mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
		CrashReport.initCrashReport(this, "900008905", false);
	}

	// 初始化全局图片异步加载类
	public void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "Imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().diskCacheFileCount(100)
				.diskCache(new UnlimitedDiscCache(cacheDir)).build();
		ImageLoader.getInstance().init(config);
	}

	// 销毁所有Activity，退出应用
	public void exit() {
		for (Activity activity : allActivity) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	// 储存用户对象
	public void saveUserInfo(Context context, User user) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().putString("user_info", new Gson().toJson(user)).commit();
		LogUtil.e("####", "用户信息" + new Gson().toJson(user).toString());
	}

	// 获取用户对象
	public User getUserInfo(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String userInfo = sp.getString("user_info", null);
		if (userInfo != null) {
			return new Gson().fromJson(userInfo, User.class);
		}
		return null;
	}

	/**
	 * 清除用户信息
	 */
	public void clearUserInfo(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().putString("user_info", null).commit();
	}

	// 初始化 Bmob SDK
	public void initBmobSDK() {
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, "dae35dc78b3eded535a69d303f052d8c");
	}
}
