package com.bdj.tools;

import android.os.Environment;

public class Const {
	public static String baseUrl = "http://122.195.244.87:8511/";
	public static final String ALL_URL = baseUrl + "all.php?p=";
	public static final String PIC_URL = baseUrl + "pic.php?p=";
	public static final String TXT_URL = baseUrl + "txt.php?p=";
	public static final String VIDEO_URL = baseUrl + "video.php?p=";

	public static final String REL_TAG = "aabbcc";

	public static final int ALL_FRAGMENT_INDEX = 0;
	public static final int PIC_FRAGMENT_INDEX = 1;
	public static final int TXT_FRAGMENT_INDEX = 2;
	public static final int VIDEO_FRAGMENT_INDEX = 3;

	public static final int ALL_FRAGMENT_UPDATEVIEW = 1000;
	public static final int PIC_FRAGMENT_UPDATEVIEW = 1001;
	public static final int TXT_FRAGMENT_UPDATEVIEW = 1002;
	public static final int VIDEO_FRAGMENT_UPDATEVIEW = 1003;

	public static final int ISUPDATA = 2000;
	public static final int PROGRESS_VISIABLITY = 2001;
	public static final int PROGRESS_GONE = 2002;
	public static final int GET_ZOOM = 2003;

	public static final int ALL_FRAGMENT_ITEMIMG_UPDATEVIEW = 1004;

	public static final String IMG_Down_LOCALURL = Environment.getExternalStorageDirectory() + "/bdj/img/";
	public static final String VIDEO_Down_LOCALURL = Environment.getExternalStorageDirectory() + "/bdj/video/";
	public static String INDEX = "index";

}
