package io.renren.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {

	// 日期格式化
	public static String dateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}
	// 日期格式化
	public static String dateToStringYYHHDD(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(date);
		return dateString;
	}

	// 日期格式化为年月日
	public static String dateToYearMonthDay(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	// 日期格式化为时分秒
	public static String dateToHoursMinutesSeconde(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	// 时分秒相减
	public static Long substractTime(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
//		date1 = date1.substring(11, 19);
//		date2 = date2.substring(11, 19);
		long diff = 0;
		try {
			Date d1 = df.parse(date1);
			Date d2 = df.parse(date2);
			diff = d1.getTime() - d2.getTime();

		} catch (Exception e) {
		}

		return diff / 60000;
	}

	// 时间比较
	// 如果第一个时间比第二个时间早返回true 否则返回 false
	public static Boolean compareDate(String oldStrDate, String newStrDate) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//		oldStrDate = oldStrDate.substring(11, 19);
//		newStrDate = newStrDate.substring(11, 19);
		try {
			Date olDate = dateFormat.parse(oldStrDate);
			Date newDate = dateFormat.parse(newStrDate);
			if (olDate.getTime() > newDate.getTime()) {
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String calculation(Long punishmentTime) {
		if (punishmentTime==null) {
			punishmentTime = 0l;
		}
		Long hours = punishmentTime / 60;
		Long minutes = punishmentTime - hours * 60;
		String absenteeism = hours + " 小时 " + minutes + " 分钟";
		return absenteeism;
	}
	
	public static Date stringToDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date utilDate = null;
		try {
			utilDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return utilDate;
	}

	public static void main(String[] args) {
		Boolean date = compareDate("13:00:00","18:00:00");
		
		Long substractTime = substractTime("12:42:13","11:41:13");
		System.out.println(date);
		System.out.println(substractTime);
	}
}
