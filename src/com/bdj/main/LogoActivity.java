package com.bdj.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;

import com.android.bdj.R;
import com.bdj.tools.Tools;
import com.umeng.analytics.MobclickAgent;

public class LogoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logo);
		MobclickAgent.updateOnlineConfig(this);
		if (Tools.checkNet(this)) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					startActivity(new Intent(LogoActivity.this, HomeFragment.class));
					finish();
				}
			}, 1500);

		} else {
			setContentView(R.layout.net_error);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
