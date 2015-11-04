package com.bdj.tools;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bdj.obj.BDJObj;

public class JsonTools {

	public static ArrayList<BDJObj> getBds(String jsonstr) {
		ArrayList<BDJObj> objs = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			objs = new ArrayList<BDJObj>();
			LogUtil.i("����OBS");
			JSONArray jsonArray = json.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Iterator iterator = jsonObject.keys();
				BDJObj obj = new BDJObj();
				/*
				 * id; title; ���� matter; ���� pic; ����ͼƬ��ַ zan; �޵ĸ��� cai; �ȵĸ���
				 * zhuan; ת���� titlepic; ͷ�� name; �ǳ� time; ����ʱ�� genre;
				 * 1��Ƶ��2��jif/jpg 3��txt
				 */
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (key.equals("id")) {
						obj.setId(jsonObject.getString(key));
					} else if (key.equals("pic")) {
						obj.setPic(jsonObject.getString(key));
						obj.setPicName(jsonObject.getString(key).substring(
								jsonObject.getString(key).lastIndexOf("/") + 1, jsonObject.getString(key).length()));
					} else if (key.equals("title")) {
						obj.setTitle(jsonObject.getString(key));
					} else if (key.equals("matter")) {
						obj.setMatter(jsonObject.getString(key));
					} else if (key.equals("zan")) {
						obj.setZan(jsonObject.getString(key));
					} else if (key.equals("cai")) {
						obj.setCai(jsonObject.getString(key));
					} else if (key.equals("zhuan")) {
						obj.setZhuan(jsonObject.getString(key));
					} else if (key.equals("titlepic")) {
						obj.setTitlepic(jsonObject.getString(key));
						obj.setTitlepicName(jsonObject.getString(key).substring(
								jsonObject.getString(key).lastIndexOf("/") + 1, jsonObject.getString(key).length()));
					} else if (key.equals("name")) {
						obj.setName(jsonObject.getString(key));
					} else if (key.equals("time")) {
						obj.setTime(jsonObject.getString(key));
					} else if (key.equals("genre")) {
						obj.setGenre(jsonObject.getString(key));
					}
				}
				objs.add(obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("info", "��������");
		}
		return objs;
	}

}
