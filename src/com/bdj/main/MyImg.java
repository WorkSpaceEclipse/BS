package com.bdj.main;

import android.content.Context;
import android.view.WindowManager;
import android.widget.ImageView;

public class MyImg extends ImageView {

	private long width = (long) ((((WindowManager) (MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE))))
			.getDefaultDisplay().getWidth() * 0.8);

	public MyImg(Context context) {
		super(context);
	}

}
