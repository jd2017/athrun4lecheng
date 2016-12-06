package org.athrun.ios.instrumentdriver.config;
import java.util.regex.Pattern;
public class StrUtils {
	public static String XML = "xml";
	public static String JSON = "json";
	public static String STRING = "string";
	public static String NUMBER = "number";

	/**
	 * 判断 字符串是否是空的
	 * 
	 * @param string
	 * @return true 空字符串，false 不为空
	 */
	public static boolean StringIsEmpty(String string) {
		if (string == null || ("").equals(string.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到字符串的非空指针
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return
	 */
	public static String getString(String str) {
		if (str == null || str.equals("null") || str.equals("")) {
			return "";
		} else {
			return str.trim();
		}
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str字符流
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		} else {
			Pattern pattern = Pattern.compile("[0-9]*");
			return pattern.matcher(str.trim()).matches();
		}
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

//	/**
//	 * 判断是否是json结构
//	 */
//	public static boolean isJson(String value) {
//		try {
//			new JSONObject(value);
//		} catch (JSONException e) {
//			return false;
//		}
//		return true;
//	}
}
