package com.bdj.main;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bdj.all.AllFragemnt;
import com.bdj.obj.BDJObj;
import com.bdj.pic.PicFragemnt;
import com.bdj.tools.Const;
import com.bdj.tools.Tools;
import com.bdj.txt.TxtFragemnt;

public class MyApplication extends Application {

	private static Context context;
	private static Handler handler;
	public static int currentFragment = -1;

	public static Handler getHandler() {
		if (handler == null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case Const.ALL_FRAGMENT_UPDATEVIEW:
						AllFragemnt.updateView((ArrayList<BDJObj>) msg.obj);
						break;
					case Const.PIC_FRAGMENT_UPDATEVIEW:
						PicFragemnt.updateView((ArrayList<BDJObj>) msg.obj);
						break;
					case Const.TXT_FRAGMENT_UPDATEVIEW:
						TxtFragemnt.updateView((ArrayList<BDJObj>) msg.obj);
						break;
					case Const.PROGRESS_VISIABLITY:
						HomeFragment.barVisibility();
						break;
					case Const.PROGRESS_GONE:
						HomeFragment.barGone();
						break;
					case Const.GET_ZOOM:
						if (msg.obj != null) {
							Intent intent = new Intent(MyApplication.getContext().getApplicationContext(),
									ZoomActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							intent.putExtra("imgUrl", (String) msg.obj);
							MyApplication.getContext().startActivity(intent);
						}
						break;
					/*
					 * case Const.ALL_FRAGMENT_ITEMIMG_UPDATEVIEW:
					 * AllFragemnt.updateItemImg((String) msg.obj); break;
					 */
					}
				}
			};
		}

		return handler;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Tools.createFiles();
		context = this;

		getHandler();
	}

	public static Context getContext() {

		return context;
	}
}
