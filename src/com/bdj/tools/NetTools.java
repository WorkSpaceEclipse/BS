package com.bdj.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class NetTools {
	private static DefaultHttpClient client;
	private static ExecutorService executorService;

	public static synchronized HttpClient getSaveHttpClient() {
		if (client == null) {
			HttpParams params = new BasicHttpParams();
			// ���û�������
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// ��ʱ����
			/* �����ӳ���ȡ���ӵĳ�ʱʱ�� */
			ConnManagerParams.setTimeout(params, 1000);
			/* ���ӳ�ʱ */
			HttpConnectionParams.setConnectionTimeout(params, 2000);
			/* ����ʱ */
			HttpConnectionParams.setSoTimeout(params, 4000);
			// ����HttpClient֧��HTTp��HTTPS����ģʽ
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			// ʹ���̰߳�ȫ�����ӹ���������HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			client = new DefaultHttpClient(conMgr, params);
		}
		return client;
	}

	public static ExecutorService getExeService() {
		if (executorService == null) {
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}

}
