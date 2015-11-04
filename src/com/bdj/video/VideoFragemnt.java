package com.bdj.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bdj.R;
import com.bdj.main.BaseFragment;
import com.bdj.tools.Const;

public class VideoFragemnt extends BaseFragment {
	/** ��־λ����־�Ѿ���ʼ����� */
	private boolean isPrepared;
	/** �Ƿ��ѱ����ع�һ�Σ��ڶ��ξͲ���ȥ���������� */
	private static boolean mHasLoadedOnce = false;

	public static VideoFragemnt newInstance(int index) {
		// ��̬����������Ҫһ��int�͵�ֵ����ʼ��fragment�Ĳ����� Ȼ�󷵻��µ�fragment��������
		// ������ã���ʱ���׳���
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
			// ��������
		}

	}

}
