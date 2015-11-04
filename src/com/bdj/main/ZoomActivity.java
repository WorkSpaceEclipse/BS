package com.bdj.main;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.android.bdj.R;
import com.bdj.tools.Const;
import com.bdj.tools.LogUtil;
import com.bdj.tools.Tools;
import com.umeng.analytics.MobclickAgent;

public class ZoomActivity extends Activity {

	private GifImageView gifImg;
	private ImageView iv;
	private ImageView ivback;
	private String imgUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zoom);
		try {
			imgUrl = getIntent().getExtras().getString("imgUrl");
			LogUtil.i("ZOOM--------------->" + imgUrl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		initView();
		initListener();
		showImg(imgUrl);
	}

	private void showImg(String imgUrl) {
		if (imgUrl.contains(".gif")) {
			try {
				gifImg.setVisibility(View.VISIBLE);
				iv.setVisibility(View.GONE);
				GifDrawable drawable = new GifDrawable(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl));
				gifImg.setBackgroundDrawable(drawable);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			gifImg.setVisibility(View.GONE);
			iv.setVisibility(View.VISIBLE);
			iv.setImageBitmap(BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl)));
		}
	}

	private void initListener() {
		// ivback.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
	}

	private void initView() {
		gifImg = (GifImageView) findViewById(R.id.gifiv_zoom);
		iv = (ImageView) findViewById(R.id.iv_zoom);
		// ivback = (ImageView) findViewById(R.id.iv_back_zoom);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // ͳ��ҳ��(����Activity��Ӧ����SDK�Զ����ã�����Ҫ����д)
		MobclickAgent.onResume(this); // ͳ��ʱ��
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // ������Activity��Ӧ����SDK�Զ����ã�����Ҫ����д����֤
													// onPageEnd ��onPause
													// ֮ǰ����,��Ϊ onPause �лᱣ����Ϣ
		MobclickAgent.onPause(this);
	}
}
