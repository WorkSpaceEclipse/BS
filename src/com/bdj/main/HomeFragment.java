package com.bdj.main;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bdj.R;
import com.bdj.all.AllFragemnt;
import com.bdj.pic.PicFragemnt;
import com.bdj.tools.Const;
import com.bdj.tools.LogUtil;
import com.bdj.txt.TxtFragemnt;
import com.umeng.analytics.MobclickAgent;

public class HomeFragment extends FragmentActivity implements OnTouchListener {
	private TextView tvAll;
	private TextView tvPic;
	private TextView tvTxt;
	private static ViewPager vPager;
	private MyApplication application;
	private long mExitTime;
	private static ProgressBar bar;

	// private TextView tvVideo;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_fragmet);
		application = (MyApplication) getApplication();
		initView();
		initListener();
	}

	@Override
	protected void onRestart() {
		switch (MyApplication.currentFragment) {
		case Const.ALL_FRAGMENT_INDEX:
			turnToPressOn(tvAll);
			break;
		case Const.PIC_FRAGMENT_INDEX:
			turnToPressOn(tvPic);
			break;
		case Const.TXT_FRAGMENT_INDEX:
			turnToPressOn(tvTxt);
			break;

		}
		super.onRestart();
	}

	private void initListener() {
		tvAll.setOnTouchListener(this);
		tvPic.setOnTouchListener(this);
		tvTxt.setOnTouchListener(this);
		// tvVideo.setOnTouchListener(this);
		vPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				MyApplication.currentFragment = arg0;
				bar.setVisibility(View.GONE);
				switch (arg0) {
				case 0:
					turnToPressOn(tvAll);
					MyApplication.currentFragment = Const.ALL_FRAGMENT_INDEX;
					vPager.setCurrentItem(Const.ALL_FRAGMENT_INDEX);
					break;
				case 1:
					turnToPressOn(tvPic);
					MyApplication.currentFragment = Const.PIC_FRAGMENT_INDEX;
					vPager.setCurrentItem(Const.PIC_FRAGMENT_INDEX);
					break;
				case 2:
					turnToPressOn(tvTxt);
					MyApplication.currentFragment = Const.TXT_FRAGMENT_INDEX;
					vPager.setCurrentItem(Const.TXT_FRAGMENT_INDEX);
					break;
				case 3:
					// turnToPressOn(tvVideo);
					break;
				}

				LogUtil.i("currentFragment:" + MyApplication.currentFragment);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initView() {
		vPager = (ViewPager) findViewById(R.id.viewpager_homefragment);
		bar = (ProgressBar) findViewById(R.id.progressBar1);
		/*
		 * PagerTabStrip tabStrip = (PagerTabStrip)
		 * findViewById(R.id.pagertab_homefragment); // 下划线颜色
		 * tabStrip.setTabIndicatorColor(getResources().getColor(R.color.red));
		 * tabStrip.setBackgroundColor(getResources().getColor(R.color.white));
		 */
		tvAll = (TextView) findViewById(R.id.tv_all_homefragment);
		tvPic = (TextView) findViewById(R.id.tv_pic_homefragment);
		tvTxt = (TextView) findViewById(R.id.tv_txt_homefragment);
		// tvVideo = (TextView) findViewById(R.id.tv_video_homefragment);
		AllFragemnt allFragemnt = AllFragemnt.newInstance(Const.ALL_FRAGMENT_INDEX);
		PicFragemnt picFragemnt = PicFragemnt.newInstance(Const.PIC_FRAGMENT_INDEX);
		TxtFragemnt txtFragemnt = TxtFragemnt.newInstance(Const.TXT_FRAGMENT_INDEX);
		// VideoFragemnt videoFragemnt =
		// VideoFragemnt.newInstance(Const.VIDEO_FRAGMENT_INDEX);
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(allFragemnt);
		fragments.add(picFragemnt);
		fragments.add(txtFragemnt);
		// fragments.add(videoFragemnt);
		MyViewPageAdapter pageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), fragments);
		vPager.setAdapter(pageAdapter);
		vPager.setOffscreenPageLimit(fragments.size());
		vPager.setCurrentItem(Const.ALL_FRAGMENT_INDEX);
		MyApplication.currentFragment = Const.ALL_FRAGMENT_INDEX;
		turnToPressOn(tvAll);
	}

	private void turnToPressOn(TextView tvOn) {
		ArrayList<TextView> tvList = null;
		if (tvList == null) {
			tvList = new ArrayList<TextView>();
			tvList.add(tvAll);
			tvList.add(tvPic);
			tvList.add(tvTxt);
			// tvList.add(tvVideo);
		}
		for (TextView tv : tvList) {
			if (tv == tvOn) {
				LogUtil.i("TvOn:" + tv.getId());
				tv.setPressed(true);
			} else {
				tv.setPressed(false);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bar.setVisibility(View.GONE);
			switch (v.getId()) {
			case R.id.tv_all_homefragment:
				turnToPressOn(tvAll);
				MyApplication.currentFragment = Const.ALL_FRAGMENT_INDEX;
				vPager.setCurrentItem(Const.ALL_FRAGMENT_INDEX);
				break;
			case R.id.tv_pic_homefragment:
				turnToPressOn(tvPic);
				MyApplication.currentFragment = Const.PIC_FRAGMENT_INDEX;
				vPager.setCurrentItem(Const.PIC_FRAGMENT_INDEX);
				break;
			case R.id.tv_txt_homefragment:
				turnToPressOn(tvTxt);
				MyApplication.currentFragment = Const.TXT_FRAGMENT_INDEX;
				vPager.setCurrentItem(Const.TXT_FRAGMENT_INDEX);
				break;
			/*
			 * case R.id.tv_video_homefragment: turnToPressOn(tvVideo);
			 * vPager.setCurrentItem(3); break;
			 */
			}

		}
		return false;
	}

	public static void setCurrentFragment(int index) {
		vPager.setCurrentItem(index);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("onkeyDown=bakc");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public static void barVisibility() {
		bar.setVisibility(View.VISIBLE);
	}

	public static void barGone() {
		bar.setVisibility(View.GONE);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
