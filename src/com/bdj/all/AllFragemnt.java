package com.bdj.all;

import java.util.ArrayList;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.bdj.R;
import com.bdj.main.BaseFragment;
import com.bdj.main.HomeFragment;
import com.bdj.main.MyApplication;
import com.bdj.obj.BDJObj;
import com.bdj.tools.Const;
import com.bdj.tools.JsonDownLoad;
import com.bdj.tools.JsonDownLoad.JsonCallBack;
import com.bdj.tools.JsonTools;
import com.bdj.tools.LogUtil;
import com.bdj.tools.Tools;
import com.umeng.analytics.MobclickAgent;

public class AllFragemnt extends BaseFragment {
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private static boolean mHasLoadedOnce = false;
	private View view;
	public static ListView lv;
	private int page = 0;
	private static AlLvAdapter adapter;
	private boolean turnPage;
	private boolean isUpdata = false;
	private int mCurIndex = -1;

	public static AllFragemnt newInstance(int index) {
		// 静态工厂方法需要一个int型的值来初始化fragment的参数， 然后返回新的fragment到调用者
		// 方便调用，构时不易出错
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		AllFragemnt fragment = new AllFragemnt();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.all_fragment, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		initView();
		initListener();
		isPrepared = true;
		lazyLoad();
		return view;
	}

	private void initListener() {
		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (!isUpdata && turnPage && view.getLastVisiblePosition() == view.getCount() - 1) {
						isUpdata = true;
						page++;
						// initData();
						// lv.setEnabled(false);
						addData();

					} else if (isUpdata) {
						MyApplication.getHandler().sendEmptyMessage(Const.ISUPDATA);
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				turnPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

	}

	protected void addData() {
		// 翻页
		JsonDownLoad.getJson(Const.ALL_URL + page, false, new JsonCallBack() {
			// 获取json
			@Override
			public void jsonBack(String jsonStr) {
				ArrayList<BDJObj> data = new ArrayList<BDJObj>();
				data = JsonTools.getBds(jsonStr);
				LogUtil.i("------>AllFragment翻到第" + page + "页");
				isUpdata = false;
				// 更新LV在adapter里更新IMG
				if (MyApplication.currentFragment == Const.ALL_FRAGMENT_INDEX) {

					Message msg = new Message();
					msg.obj = data;
					msg.what = Const.ALL_FRAGMENT_UPDATEVIEW;
					MyApplication.getHandler().sendMessage(msg);
				}
			}
		});
	}

	private void initView() {
		lv = (ListView) view.findViewById(R.id.listView_allfragment);
		/*
		 * ArrayList<BDJObj> bds = new ArrayList<BDJObj>(); for (int i = 0; i <
		 * 10; i++) { BDJObj obj = new BDJObj(); obj.setId(i + "");
		 * bds.add(obj); } AlLvAdapter adapter = new AlLvAdapter(bds);
		 * lv.setAdapter(adapter);
		 */

	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			// 初始化UI
			return;
		} else {
			// 加载数据
			LogUtil.i("ALL_Initialize");
			initData();
		}

	}

	private void initData() {
		JsonDownLoad.getJson(Const.ALL_URL + page, true, new JsonCallBack() {
			// 获取json
			@Override
			public void jsonBack(String jsonStr) {
				mHasLoadedOnce = true;
				ArrayList<BDJObj> data = new ArrayList<BDJObj>();
				data = JsonTools.getBds(jsonStr);
				// 更新LV在adapter里更新IMG
				if (MyApplication.currentFragment == Const.ALL_FRAGMENT_INDEX) {

					Message msg = new Message();
					msg.obj = data;
					msg.what = Const.ALL_FRAGMENT_UPDATEVIEW;
					MyApplication.getHandler().sendMessage(msg);
				}
			}
		});
	}

	public static void updateView(ArrayList<BDJObj> obj) {
		LogUtil.i("DataSize:" + obj.size());
		HomeFragment.setCurrentFragment(Const.ALL_FRAGMENT_INDEX);
		if (adapter == null) {
			adapter = new AlLvAdapter(obj);
			lv.setAdapter(adapter);
		} else {
			adapter.upData(obj);
		}
	}

	public static void updateItemImg(String imgUrl) {
		ImageView imageView = (ImageView) lv.findViewWithTag(imgUrl);
		if (imageView != null) {
			imageView.setImageBitmap(BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl)));
			imageView.setTag("");
		}
		adapter.notifyDataSetChanged();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}
}
