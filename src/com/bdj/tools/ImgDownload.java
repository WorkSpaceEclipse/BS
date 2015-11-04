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
	final int winHeight = Tools.getWinHeight();// ��Ļ��
	final int winWidth = Tools.getWinWidth();// ��Ļ��
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
		// ����������ȡBM
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
				LogUtil.i("----->�����ô���adapter:" + imgName);
				// ����ͼƬ�ݲ�������
			} else {
				LogUtil.i("----->�߶�>4096");

			}
		} else if (ivImg != null && imgUrl.equals(ivImg.getTag())) {
			// �ļ���ȡ

			File imgFile = new File(Const.IMG_Down_LOCALURL + imgName);
			if (imgFile.exists() && imgFile.isFile()) {
				Bitmap bitmap = BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + imgName);
				if (bitmap.getHeight() < 4000) {
					LogUtil.i("----->�ļ�ȡͼƬ����adapter" + imgName);
					// ����ͼƬ�ݲ�������
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
				// ����
				LogUtil.i("------>���أ�" + imgUrl);
				ImgAsyncTask asyncTask = new ImgAsyncTask(imgUrl, relContent, ivImg, context, imgCallBack);
				if (ivImg != null) {
					asyncTask.execute();
					map.put(imgUrl, asyncTask);
				}

			}
		}
	}

	private LayoutParams getParams(RelativeLayout relContent, Bitmap bitmap) {
		// ����ͼƬ��С���� REL�Ĵ�С��Ȼ��ʹIV match REL ��������С����ΪGIF����ֱ����params
		LayoutParams params = relContent.getLayoutParams();

		// ��ȵ���
		if (bitmap.getWidth() < (int) winWidth) {
			// bm���<��Ļ���x0.8������rel���
			params.width = winWidth;
		} else {
			// rel���=BM���
			params.width = bitmap.getWidth();
		}
		// �߶ȵ���
		if (bitmap.getHeight() < winHeight) {
			// ����BM��߱ȣ�����rel�߶�
			params.height = params.width * bitmap.getHeight() / bitmap.getWidth();
		} else {
			// bm�߶�>��Ļ�߶ȣ���rel��Ƚ��е�����rel�߶�=bm�߶�
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
						// gif�����뻺��
						if (!imgUrl.contains("gif")) {
							imgCaches.put(imgUrl, new SoftReference<Bitmap>(bitmap));
						}
						Tools.writeFileToSD(url.openStream(),
								new File(Const.IMG_Down_LOCALURL + Tools.getImgName(imgUrl)));
					} else {
						LogUtil.i("-------->ͼƬ������������:" + imgUrl);
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
					LogUtil.i("����ʧ��========>" + imgUrl);
				} catch (IOException e) {
					e.printStackTrace();
					LogUtil.i("����ʧ��========>" + imgUrl);
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
				LogUtil.i("-------->������ϣ�" + Tools.getImgName(imgUrl));
			}
			super.onPostExecute(result);
		}
	}

	public interface ImgCallBack {
		public void imgCallBack(Bitmap bitmap, String imgUrl, ImageView ivImg);
	}
}
