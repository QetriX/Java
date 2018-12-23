package com.qetrix.libs;

import java.io.UnsupportedEncodingException;

/* Copyright (c) QetriX.com Licensed under MIT License, see /LICENSE.txt file.
 * 18.12.23 | QetriX Utils Class
 */

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	//final public static String NL = "\n";
	public static String D = "";
	public static String SD = "/QetriX/res/common/";
	public static String XD = "/QetriX/res/";
	public static String APP = "";
	public static String APP_NAME = "";

	/*public enum Stage {
		dev, // A lot of messages appears, like DS info and stuff, also performance hints and such
		debug, // Like Test, but with dev conosle
		test, // Like Prod, but emails are for testers only
		prod // Final functionality. Only here errors/warnings/exceptions are only logged and won't show to user/visitor
	}*/

	public Object convert(Object data) throws Exception {
		return convert(data, null, null, null);
	}

	public static Object convert(Object data, String fromFormat, String toFormat, String toType) throws Exception {
		if (toType == null)
			toType = "";

		if (fromFormat == null || toFormat == null) {
			if (toFormat == null)
				toFormat = fromFormat;
			fromFormat = data.getClass().getName().substring(data.getClass().getName().lastIndexOf(".") + 1);
		}

		if (toType != "")
			toType = "_" + toType;
		if (fromFormat == toFormat + toType) {
			if (fromFormat == null)
				throw new Exception("Undefined formats for converting");
			else
				return data; // Converting to the same format makes no sense
		}

		//Util.log("Convert " + fromFormat + " to " + toFormat + toType);

		Class<?> cls = null;
		try {
			try {
				cls = Class.forName("com.qetrix.apps." + QPage.getInstance().name() + ".converters." + fromFormat + "_"
						+ toFormat + toType);
			} catch (ClassNotFoundException e) {
				cls = Class.forName("com.qetrix.apps.shared.converters." + fromFormat + "_" + toFormat + toType);
			}
			return cls.getMethod("convert", Object.class).invoke(cls.newInstance(), data);

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Converter error in " + ex.getMessage() + " for com.qetrix.apps.shared.converters." + fromFormat
					+ "_" + toFormat + toType;
		}
	}

	public static Boolean methodExists(Method[] ma, String mn) {
		for (int i = 0; i < ma.length; i++)
			if (ma[i].getName() == mn)
				return true;
		return false;
	}

	public static Boolean methodExists(Class<?> c, String mn) {
		return Util.methodExists(c.getClass().getDeclaredMethods(), mn);
	}

	public static Dict setNavLink(String pagelink, String link, String text, Map<String, String> items) {
		Dict item = new Dict();
		if (text != null)
			item.set("text", text);
		if (pagelink != link && link != null) {
			item.set("link", link.indexOf("://") > 0 ? QPage.getInstance().path() : "" + link);
			if (pagelink.substring(0, link.length()) == link)
				item.set("selected", "true");
		}
		if (items != null) {
			item.set("items", "array");
			//foreach ($items as $i) $item["items"][] = setNavLink($pagelink, $i["link"], $i["text"], isset($items) ? $i["items"] : null);
		}
		//if (link == null && text == null) return item.get("items");
		return item;
	}

	public static QElem getSettings(String str) {
		Pattern p = Pattern.compile("(\\w+)(:[^\\	]*)?\\	");
		Matcher m = p.matcher(str.trim().replace("	", "	") + "	");

		QElem out = new QElem();
		while (m.find()) {
			//out.set(m.group(1), (m.group(2) != null ? m.group(2).substring(1) : ""));
			//console.log(m.group(1) + (m.group(2) != null ? ":" + m.group(2).substring(1) : ""));
		}
		return out;
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	/*	public static String arrayShift(List<String> arr)
		{
			String str = (String) arr.get(0);
			arr.remove(0);
			return str;
		}*/

	public static void log(Object data) {
		System.out.println(data);
	}

	public static void log(Object data, String desc) {
		if (data == null)
			System.out.println(desc + ": null");
		System.out.println(desc + ": " + data.toString());
	}

	public static String implode(String separator, List<String> list) {
		String str = "";
		for (String s : list)
			str += s + "\n";
		return str.trim().replace("\n", separator);
	}

	/**
	 * processVars
	 *
	 * @param string $str String with %vars%
	 * @param array $data Data for vars, keys must match with vars
	 *
	 * @return string
	 */
	public static String processVars(String str, Map<String, String> data) {
		// If no variables or no data, return the original string (no point of parsing it)
		if (!str.contains("%") || data.isEmpty() || data.size() == 0)
			return str;

		String[] xx = str.split("%");
		String strx = "";
		for (String x : xx)
			strx += (data.containsKey(x) ? data.get(x) : x);
		return strx;
	}

	public static LinkedHashMap<String, String> getSettings(String str, String delimiter, Map<String, String> data) {
		return (LinkedHashMap<String, String>) data; // TODO!!!!
	}

	public static String formatDateTime(String str, String format) {
		/*
		* 201100000000 = year 2011 (2011)
		* 201100100000 = 1st half of 2011
		* 201100010000 = 1st quarter of 2011 (I/2011)
		* 201110000000 = October 2011 (=> 201110) (10/2010)
		* 201101000000 = January 2011 (01/2010)
		* 201101100000 = January 10, 2011 (10.1.2011)
		 */
		if (str == "")
			return "";
		str = (str + "00000000000000").substring(0, 14);
		int s = Integer.parseInt(str.substring(str.length() - 2));
		str = str.substring(0, str.length() - 2);
		int m = Integer.parseInt(str.substring(str.length() - 2));
		str = str.substring(0, str.length() - 2);
		int h = Integer.parseInt(str.substring(str.length() - 2));
		str = str.substring(0, str.length() - 2);
		int d = Integer.parseInt(str.substring(str.length() - 2));
		str = str.substring(0, str.length() - 2);
		int M = Integer.parseInt(str.substring(str.length() - 2));
		str = str.substring(0, str.length() - 2);
		int y = Integer.parseInt(str);

		if (M + 0 == 0 && d + 0 == 0)
			return String.valueOf(y);
		else if (M + 0 > 0 && d + 0 == 0)
			return M + "/" + y;
		else if (M + 0 == 0 && d + 0 > 0) {
			String[] arr = new String[] { null, "I", "II", "III", "IV" };
			return arr[d + 0] + "/" + y;
		}

		return String.format(format, y, M, d, h, m, s);
	}

	public static String base64encode(String value) {
		return com.qetrix.libs.Base64.encodeToString(value.getBytes(), 0);
	}

	public static String base64decode(String value) {
		return com.qetrix.libs.Base64.decode(value, 0).toString();
	}

	public static String urlEncode(String value) {
		try {
			return java.net.URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return value;
		}
	}

	public static String urlDecode(String value) {
		try {
			return java.net.URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return value;
		}
	}
	
	public static String uuid()
	{
		return uuid(false);
	}
	
	public static String uuid(boolean compressed)
	{
		String uuid = java.util.UUID.randomUUID().toString();
		return compressed ? uuid.replace("-", "") : uuid;
	}
}
