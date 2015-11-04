package com.bdj.tools;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImgDownload {

	private static ImgDownload imgDownload;
	final int winHeight = Tools.getWinHeight();// 屏幕高
	final int winWidth = Tools.getWinWidth();// 屏幕宽
	private HashMap<String, ImgAsyncTask> map = new HashMap<String, ImgAsyncTask>();
	private Map<String, SoftReference<Bitmap>> imgCaches = new HashMap<String, SoftReference<Bitmap>>();

	public void downLoadImg(String imgUrl, RelativeLayout relContent, ImageView ivImg, Context context,
			ImgCallBack imgCallBack) {
		if (relContent != null && (imgUrl + Const.REL_TAG).equals(relContent.getTag())) {
			LayoutParams params = relContent.getLayoutParams();
			params.height = 0;
			params.width = 0;
			relContent.setLayoutParams(params);
		}
		SoftReference<Bitmap> currentBm = imgCaches.get(imgUrl);
		Bitmap softBm = null;
		if (currentBm != null) {
			softBm = currentBm.get();
		}
		String imgName = Tools.getImgName(imgUrl);
		// 从软引用中取BM
		if (softBm != null && ivImg != null && imgUrl.equals(ivImg.getTag()) && !imgUrl.contains(".gif")) {
			if (softBm.getHeight() < 4000) {
				imgCaches.put(imgUrl, new SoftReference<Bitmap>(softBm));
				if (relContent == null) {
					ivImg.setImageBitmap(softBm);
					ivImg.setTag("");
				} else if (relContent.getTag().equals(imgUrl + Const.REL_TAG)) {
					// imgCallBack.imgCallBack(softBm, imgUrl, ivImg);
					LayoutParams params = getParams(relContent, softBm);
					relContent.setLayoutParams(params);
					relContent.setTag("");
					GifDrawable drawable;
					try {
						drawable = new GifDrawable(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl));
						ivImg.setBackgroundDrawable(drawable);
						ivImg.setTag("");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				LogUtil.i("----->软引用传回adapter:" + imgName);
				// 超长图片暂不做处理
			} else {
				LogUtil.i("----->高度>4096");

			}
		} else if (ivImg != null && imgUrl.equals(ivImg.getTag())) {
			// 文件中取

			File imgFile = new File(Const.IMG_Down_LOCALURL + imgName);
			if (imgFile.exists() && imgFile.isFile()) {
				Bitmap bitmap = BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + imgName);
				if (bitmap.getHeight() < 4000) {
					LogUtil.i("----->文件取图片传回adapter" + imgName);
					// 超长图片暂不做处理
					if (relContent != null && relContent.getTag().equals(imgUrl + Const.REL_TAG)) {
						// imgCallBack.imgCallBack(softBm, imgUrl, ivImg);
						LayoutParams params = getParams(relContent, bitmap);
						relContent.setLayoutParams(params);
						relContent.setTag("");
						GifDrawable drawable;
						if (imgUrl.contains(".gif")) {
							try {
								drawable = new GifDrawable(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl));
								ivImg.setBackgroundDrawable(drawable);
								ivImg.setTag("");
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							imgCaches.put(imgUrl, new SoftReference<Bitmap>(bitmap));
							ivImg.setImageBitmap(bitmap);
							ivImg.setTag("");
						}

					} else {
						imgCaches.put(imgUrl, new SoftReference<Bitmap>(bitmap));
						ivImg.setImageBitmap(bitmap);
						ivImg.setTag("");
					}
				}
			} else if (imgUrl != null && checkImgDownloading(ivImg)) {
				// 下载
				LogUtil.i("------>下载：" + imgUrl);
				ImgAsyncTask asyncTask = new ImgAsyncTask(imgUrl, relContent, ivImg, context, imgCallBack);
				if (ivImg != null) {
					asyncTask.execute();
					map.put(imgUrl, asyncTask);
				}

			}
		}
	}

	private LayoutParams getParams(RelativeLayout relContent, Bitmap bitmap) {
		// 根据图片大小调整 REL的大小，然后使IV match REL 来调整大小，因为GIF不能直接用params
		LayoutParams params = relContent.getLayoutParams();

		// 宽度调整
		if (bitmap.getWidth() < (int) winWidth) {
			// bm宽度<屏幕宽度x0.8，调节rel宽度
			params.width = winWidth;
		} else {
			// rel宽度=BM宽度
			params.width = bitmap.getWidth();
		}
		// 高度调整
		if (bitmap.getHeight() < winHeight) {
			// 根据BM宽高比，调整rel高度
			params.height = params.width * bitmap.getHeight() / bitmap.getWidth();
		} else {
			// bm高度>屏幕高度，对rel宽度进行调整，rel高度=bm高度
			params.height = bitmap.getHeight();
		}

		return params;
	}

	private boolean checkImgDownloading(ImageView ivImg) {
		boolean tag = true;
		String url = (String) ivImg.getTag();
		if (map != null && map.get(url) != null) {
			tag = false;
		}
		return tag;
	}

	private class ImgAsyncTask extends AsyncTask<String, Void, Bitmap> {

		private String imgUrl;
		private ImageView ivImg;
		private Context context;
		private ImgCallBack imgCallBack;
		private RelativeLayout relContent;

		public ImgAsyncTask(String imgUrl, RelativeLayout relContent, ImageView ivImg, Context context,
				ImgCallBack imgCallBack) {
			this.imgUrl = imgUrl;
			this.ivImg = ivImg;
			this.context = context;
			this.imgCallBack = imgCallBack;
			this.relContent = relContent;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			if (imgUrl != null) {
				try {
					URL url = new URL(imgUrl);
					bitmap = BitmapFactory.decodeStream(url.openStream());
					if (bitmap.getHeight() < 4000) {
						// gif不放入缓存
						if (!imgUrl.contains("gif")) {
							imgCaches.put(imgUrl, new SoftReference<Bitmap>(bitmap));
						}
						Tools.writeFileToSD(url.openStream(),
								new File(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl)));
					} else {
						LogUtil.i("-------->图片过长，不下载:" + imgUrl);
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
					LogUtil.i("下载失败========>" + imgUrl);
				} catch (IOException e) {
					e.printStackTrace();
					LogUtil.i("下载失败========>" + imgUrl);
				}
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result.getHeight() < 4000) {
				if (relContent != null && relContent.getTag().equals(imgUrl + Const.REL_TAG)) {
					LayoutParams params = getParams(relContent, result);
					relContent.setLayoutParams(params);
					relContent.setTag("");
					GifDrawable drawable;
					if (imgUrl.contains(".gif")) {
						try {
							drawable = new GifDrawable(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl));
							ivImg.setBackgroundDrawable(drawable);
							ivImg.setTag("");
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (imgUrl.equals(ivImg.getTag())) {
						imgCaches.put(imgUrl, new SoftReference<Bitmap>(result));
						ivImg.setImageBitmap(result);
						ivImg.setTag("");
					}

				} else if (imgUrl.equals(ivImg.getTag())) {
					imgCaches.put(imgUrl, new SoftReference<Bitmap>(result));
					ivImg.setImageBitmap(result);
					ivImg.setTag("");
				}
				if (imgUrl != null && map != null && map.get(imgUrl) != null) {
					map.remove(imgUrl);
				}
				LogUtil.i("-------->下载完毕：" + Tools.getImgName(imgUrl));
			}
			super.onPostExecute(result);
		}
	}

	public interface ImgCallBack {
		public void imgCallBack(Bitmap bitmap, String imgUrl, ImageView ivImg);
	}
}
