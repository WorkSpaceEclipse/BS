package com.bdj.txt;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bdj.R;
import com.bdj.main.MyApplication;
import com.bdj.obj.BDJObj;
import com.bdj.tools.ImgDownload;
import com.bdj.tools.ImgDownload.ImgCallBack;
import com.bdj.tools.LogUtil;

public class TxtLvAdapter extends BaseAdapter {

	private ArrayList<BDJObj> bdList;
	private LayoutInflater inflater;
	private ArrayList<String> task;
	private ImgDownload imgDownload;
	private View mconvertView;
	private ViewHolder mholder;

	public TxtLvAdapter(ArrayList<BDJObj> bds) {
		if (bds != null) {
			this.bdList = bds;
		}
		if (task == null) {
			task = new ArrayList<String>();
		}
		inflater = LayoutInflater.from(MyApplication.getContext());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bdList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bdList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			mholder = holder;
			convertView = inflater.inflate(R.layout.lv_item_txt, null);
			this.mconvertView = convertView;
			// holder.ivPic = (ImageView)
			// convertView.findViewById(R.id.iv_content_lvitem);
			holder.ivTitlePic = (ImageView) convertView.findViewById(R.id.iv_titlepic_lvitem);
			// holder.tvContent = (TextView)
			// convertView.findViewById(R.id.tv_content_lvitem);
			// holder.relContent = (RelativeLayout)
			// convertView.findViewById(R.id.relativeLayout_content_lvitem);
			holder.tvCai = (TextView) convertView.findViewById(R.id.tv_cai_lvitem);
			holder.tvzan = (TextView) convertView.findViewById(R.id.tv_zan_lvitem);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name_lvitme);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time_lvitem);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_lvitem);
			holder.tvZhuan = (TextView) convertView.findViewById(R.id.tv_share_lvitem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String genre = bdList.get(position).getGenre();
		holder.tvCai.setText(bdList.get(position).getCai());
		holder.tvzan.setText(bdList.get(position).getZan());
		holder.tvZhuan.setText(bdList.get(position).getZhuan());
		holder.tvName.setText(bdList.get(position).getName());
		holder.tvTime.setText(bdList.get(position).getTime());
		// holder.tvTitle.setText(Html.fromHtml(bdList.get(position).getTitle()));
		holder.tvTitle.setText(Html.fromHtml(bdList.get(position).getTitle()));
		// holder.tvTitle.setText("激发了大家撒快乐附近的离开房间大沙发里看打随机发的卡死了烦觉得辣爽口付款了大世界；快乐大师傅老大时间发");
		holder.ivTitlePic.setImageBitmap(null);
		holder.ivTitlePic.setImageResource(R.drawable.head_portrait_male);
		// holder.relContent.setVisibility(View.VISIBLE);
		// holder.ivPic.setBackgroundDrawable(null);
		// holder.ivPic.setImageBitmap(null);
		// holder.tvContent.setText("");
		// 异步更新
		imgDownload = new ImgDownload();
		// 头像
		holder.ivTitlePic.setTag(bdList.get(position).getTitlepic());
		imgDownload.downLoadImg(bdList.get(position).getTitlepic(), null, holder.ivTitlePic,
				MyApplication.getContext(), new ImgCallBack() {
					@Override
					public void imgCallBack(Bitmap bitmap, String imgUrl, ImageView ivImg) {
						// ivImg.setImageBitmap(bitmap);
						// ivImg.setTag("");
						ImageView imageView = (ImageView) mconvertView.findViewWithTag(imgUrl);
						if (imageView != null && bitmap != null) {
							LogUtil.i("----------->下载完毕，更新头像");
							imageView.setImageBitmap(bitmap);
							imageView.setTag("");
						}
					}
				});
		if (genre.equals("1")) {
			// 视频
		} else if (genre.equals("2")) {
			// 图片
		} else if (genre.equals("3")) {
			// txt
			// holder.tvContent.setText(Html.fromHtml(bdList.get(position).getTitle()));

		}
		return convertView;
	}

	class ViewHolder {
		// TextView tvContent;
		// ImageView ivPic;
		// RelativeLayout relContent;
		ImageView ivTitlePic;
		TextView tvName;
		TextView tvTime;
		TextView tvTitle;
		TextView tvMatter;
		TextView tvCai;
		TextView tvzan;
		TextView tvZhuan;
	}

	public void upData(ArrayList<BDJObj> obj) {
		if (bdList == null && obj != null) {
			bdList = new ArrayList<BDJObj>();
			bdList = (ArrayList<BDJObj>) obj.clone();
			;
		} else {
			bdList.addAll(obj);
		}
		notifyDataSetChanged();
	}
}
