package com.bdj.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bdj.main.MyApplication;
import com.bdj.obj.BDJObj;

public class Tools {
	private static int totalSize;

	// д�ļ���SD��
	public static boolean writeFileToSD(InputStream fis, File file) {
		// sLogUtil.i("д�ļ�-------��" + file.getName());
		try {
			File temp = new File(file.getAbsolutePath() + ".temp");
			if (temp.exists()) {
				temp.delete();
			}
			FileOutputStream fos = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			fis.close();
			if (!temp.renameTo(file.getAbsoluteFile())) {
				temp.delete();
				file.delete();
			} else {
				return true;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public static void writeFileToSDwithNotify(InputStream fis, long apkLength, File apkFile, BDJObj ntObj) {
		try {
			File temp = new File(Const.IMG_Down_LOCALURL + System.currentTimeMillis());

			FileOutputStream fos = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int len = 0;
			int downloadCount = 0;
			while ((len = fis.read(buffer)) != -1) {
				totalSize += len;

				// Ϊ�˷�ֹƵ����֪ͨ����Ӧ�óԽ����ٷֱ�����10��֪ͨһ��
				if ((downloadCount == 0) || (int) (totalSize * 100 / apkLength) - 1 > downloadCount) {
					downloadCount += 1;
				}
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();
			temp.renameTo(apkFile.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Drawable getApkIcon(Context context, String apkPath) {
		// ��ȡapkͼ��
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			try {
				return appInfo.loadIcon(pm);
			} catch (OutOfMemoryError e) {
				LogUtil.i("ApkIconLoader" + e.toString());
			}
		}
		return null;
	}

	public static String getApkName(Context context, String apkPath) {
		// ��ȡapkͼ��
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			try {
				return appInfo.loadLabel(pm).toString();
			} catch (OutOfMemoryError e) {
				LogUtil.i("ApkIconLoader" + e.toString());
			}
		}
		return null;
	}

	public static int getNFid(ViewGroup gp) {

		final int count = gp.getChildCount();

		for (int i = 0; i < count; ++i) {
			if (gp.getChildAt(i) instanceof ImageView) {
				return ((ImageView) gp.getChildAt(i)).getId();
			} else if (gp.getChildAt(i) instanceof ViewGroup)
				return getNFid((ViewGroup) gp.getChildAt(i));
		}
		return 0;
	}

	public static void openFile(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static boolean checkCurrentFragment(int Index) {
		/*
		 * Ҫ���µ�UI�Ƿ��ǵ�ǰ��ʾ��UI
		 */
		/*
		 * if (MyApplication.currentFragment == Index) { return true; } else { }
		 */
		return false;
	}

	public static boolean slientInstall(File file) {
		// �Զ���װ
		boolean result = false;
		Process process = null;
		OutputStream out = null;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			dataOutputStream.writeBytes("chmod 777 " + file.getPath() + "\n");
			dataOutputStream.writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r " + file.getPath());
			// �ύ����
			dataOutputStream.flush();
			// �ر�������
			dataOutputStream.close();
			out.close();
			int value = process.waitFor();

			// ����ɹ�
			if (value == 0) {
				result = true;
			} else if (value == 1) { // ʧ��
				result = false;
			} else { // δ֪���
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean checkNet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					LogUtil.i("�������");
					return true;
				}
			}
		}
		LogUtil.i("���粻����");
		return false;
	}

	public static void createFiles() {
		File fileImg = new File(Const.IMG_Down_LOCALURL);
		File fileVideo = new File(Const.VIDEO_Down_LOCALURL);

		if (!fileImg.exists()) {
			fileImg.mkdirs();
		} else {
			// RecursionDeleteFile(fileImg);
			// fileImg.mkdirs();
		}
		if (!fileVideo.exists()) {
			fileVideo.mkdirs();
		} else {
			// RecursionDeleteFile(fileVideo);
			// fileImg.mkdirs();
		}
	}

	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	public static String getImgName(String imgUrl) {
		// LogUtil.i("=====================================");
		// LogUtil.i("---------->" + imgUrl);
		String imgName = "";
		if (imgUrl.contains("qzapp")) {
			// qzapp
			imgName = imgUrl.replace("http://qzapp.qlogo.cn/qzapp/", "").replace("/", "");
		} else if (imgUrl.contains("sinaimg") && !imgUrl.contains(".jpg") && !imgUrl.contains(".gif")
				&& !imgUrl.contains(".png")) {
			imgName = imgUrl.replace("http://tp1.sinaimg.cn/", "").replace("/", "");
		} else {
			imgName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
		}
		// LogUtil.i("---------->" + imgName);
		// LogUtil.i("=====================================");
		return imgName;
	}

	public static int getWinHeight() {

		return ((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getHeight();
	}

	public static int getWinWidth() {

		return ((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
	}
}
