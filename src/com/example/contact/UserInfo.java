package com.example.contact;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户基本信息业务
 * 
 * @author Administrator
 *
 */
public class UserInfo {
	public static UserInfo userInstance = new UserInfo();// 单例设计模式的饿汉式
	private String token;

	public static UserInfo getInstance() {
		return userInstance;
	}
//这是测试
	public static String getToken(Context context) {
		return context
				.getSharedPreferences("project_xml", Context.MODE_PRIVATE)
				.getString("token", "");
	}

	public static void setToken(Context context, String token) {
		context.getSharedPreferences("project_xml", Context.MODE_PRIVATE)
				.edit().putString("token", token).commit();
	}
}
