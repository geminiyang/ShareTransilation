package com.idear.move.util;

import android.text.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这是一个检验数据格式是否正确的工具类
 * @author yangqiqi
 *
 */
public class CheckValidUtil {

	/**
	 * 判断email地址格式的流程如下：
	 *1、所有字符都必须在这个范围内：小写字母a到z 、大写字母A到Z、数字0到9、下划线_、减号-、半角小数点. 、以及@；
	 *2、必须含有1个@字符，且这个字符不在字符串最前面或者最后面；
	 *3、@字符到字符串末，中间必须要有一个半角小数点.；且这个小数点不紧挨@后，也不在最末位。
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		//字符必须在正则表达式的范围
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);


		return m.matches();
	}

	/**
	 * 判断手机号码是否有效
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		//默认为false
		boolean isValid = false;
		//判断手机号码格式是否正确的正则表达式
		String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
		isValid = true;
		}
		return isValid;
		}
	/**
	 * 判断学号是否为空
	 * @return
	 */
	public static boolean isNum(String numString){
		//默认为false
		boolean isValid = false;
		//判断学号格式是否正确的正则表达式
		String expression = "((^[A-Za-z0-9]{14}$))";
		CharSequence inputStr = numString;

		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
				isValid = true;
		}
		return isValid;
	}
	
	/**
	 * 判断密码是否为空
	 * @return
	 */
	public static boolean isPsw(String pswString){
		//默认为false
		boolean isValid = false;
		//判断密码格式是否正确的正则表达式
		String expression = "((^[A-Za-z0-9]{8,16}$))";
		CharSequence inputStr = pswString;

		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
				isValid = true;
		}
		return isValid;
	}

	private static boolean isEmpty(String str){
        return android.text.TextUtils.isEmpty(str);
    }

    public static boolean isAllNotEmpty(String ... str) {
        for (String s : str) {
            if(isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEqual(String str1,String str2) {
        return android.text.TextUtils.equals(str1,str2);
    }
}
