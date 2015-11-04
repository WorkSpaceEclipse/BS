package com.bdj.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bdj.R;
import com.bdj.main.BaseFragment;
import com.bdj.tools.Const;

public class VideoFragemnt extends BaseFragment {
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private static boolean mHasLoadedOnce = false;

	public static VideoFragemnt newInstance(int index) {
		// 静态工厂方法需要一个int型的值来初始化fragment的参数， 然后返回新的fragment到调用者
		// 方便调用，构时不易出错
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		VideoFragemnt fragment = new VideoFragemnt();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		if (view == null) {
			view = inflater.inflate(R.layout.video_fragment, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				// mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		isPrepared = true;
		lazyLoad();
		return view;
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		} else {
			// 加载数据
		}

	}

}
