package com.bdj.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bdj.tools.LogUtil;

public class MyBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals("android.intent.action.BOOT_COMPLETED")
				|| action.equals("android.intent.action.USER_PRESENT")) {

		}
		if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			LogUtil.i("CONNECTIVITY_CHANGE");
		}
	}
}
