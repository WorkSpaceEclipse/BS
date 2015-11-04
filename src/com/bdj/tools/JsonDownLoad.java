package com.bdj.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.bdj.main.MyApplication;

public class JsonDownLoad {

	public static void getJson(final String josnStr, final boolean barSwitch, final JsonCallBack jsonCallBack) {
		LogUtil.i(josnStr);
		if (barSwitch) {
			MyApplication.getHandler().sendEmptyMessage(Const.PROGRESS_VISIABLITY);
		}
		ExecutorService executorService = NetTools.getExeService();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// HttpClient client = new DefaultHttpClient();
				HttpClient client = NetTools.getSaveHttpClient();
				HttpGet get = new HttpGet(josnStr);
				HttpResponse response;
				try {
					response = client.execute(get);

					BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String line = "";
					StringBuilder sb = new StringBuilder();
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					get.abort();
					jsonCallBack.jsonBack(sb.toString());
					if (barSwitch) {
						MyApplication.getHandler().sendEmptyMessage(Const.PROGRESS_GONE);
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					MyApplication.getHandler().sendEmptyMessage(Const.PROGRESS_GONE);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					MyApplication.getHandler().sendEmptyMessage(Const.PROGRESS_GONE);
					e.printStackTrace();
				}

			}
		};
		executorService.execute(runnable);
	}

	public interface JsonCallBack {
		public void jsonBack(String jsonstr);
	}

	public static void getJson(final String josnStr, final JsonCallBack jsonCallBack) {
		LogUtil.i(josnStr);
		ExecutorService executorService = NetTools.getExeService();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// HttpClient client = new DefaultHttpClient();
				HttpClient client = NetTools.getSaveHttpClient();
				HttpGet get = new HttpGet(josnStr);
				HttpResponse response;
				try {
					response = client.execute(get);

					BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String line = "";
					StringBuilder sb = new StringBuilder();
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					get.abort();
					jsonCallBack.jsonBack(sb.toString());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		executorService.execute(runnable);
	}

}
