package com.zoho.booktickets.jsonutil;

import com.google.gson.GsonBuilder;
import com.zoho.booktickets.constant.Constant;

public class JsonUtil {

	public static Object stringToObject(String jsonString, Class<?> type) {
        Object obj = null;
		try {
			obj = new GsonBuilder().setDateFormat(Constant.DATE_FORMATE).create().fromJson(jsonString, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static String objectToString(Object objectClass) {
        String string = null;
		try {
			string =  new GsonBuilder().setDateFormat(Constant.DATE_FORMATE).create().toJson(objectClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
}