package com.bdj.obj;

public class ApkDownObject {
	/**
	 * APK下载泛型，需要下载的APK需要先转换对象泛型，再用APKDWONLOAD下载
	 */
	private String apkUrl;// 下载地址
	private String apkName;// 下载后文件名称
	private String id;
	private String title;// 应用名

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String desc) {
		this.title = desc;
	}

}
