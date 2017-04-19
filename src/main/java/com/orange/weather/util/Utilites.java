package com.orange.weather.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilites {
	
	/**
	 * validate email address
	 * @param email
	 * @return
	 */
	public static boolean vaidateEmail(String email)
	{
		
		final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		return validate(email, EMAIL_PATTERN);
	}
	
	/**
	 * validate mobile number
	 * @param mobile
	 * @return
	 */
	public static boolean vaidateMobile(String mobile)
	{
		
		final String MOBILE_PATTERN ="^01\\d{9}$";

		return validate(mobile, MOBILE_PATTERN);
	}
	
	/**
	 * user reg exp to validate string with reg exp pattern
	 * @param str
	 * @param regexpPattern
	 * @return
	 */
	private static boolean validate(String str, String regexpPattern)
	{
		Pattern pattern;
		Matcher matcher;
		
		pattern = Pattern.compile(regexpPattern);
		
		matcher = pattern.matcher(str);
		return matcher.matches();

	}
	
}
