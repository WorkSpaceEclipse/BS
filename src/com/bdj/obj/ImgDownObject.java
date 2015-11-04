package com.bdj.obj;

public class ImgDownObject {
	/**
	 * IMG下载泛型，需要下载的IMG需要先转换对象泛型，再用IMGDWONLOAD下载
	 */
	private String DLUrl;// 下载地址
	private String DLName;// 下载后文件名称

	public String getDLUrl() {
		return DLUrl;
	}

	public void setDLUrl(String dLUrl) {
		DLUrl = dLUrl;
	}

	public String getDLName() {
		return DLName;
	}

	public void setDLName(String dLName) {
		DLName = dLName;
	}

}
