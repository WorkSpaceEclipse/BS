package com.bdj.main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPage extends ViewPager {

	private boolean scrollble = true;

	public MyViewPage(Context context) {
		super(context);
	}

	public MyViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (scrollble) {
			return super.onTouchEvent(arg0);
		} else {
			return false;

		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (scrollble) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return false;

		}
	}
	public void setScrollble(boolean scrollble){
		this.scrollble=scrollble;
	}
}
