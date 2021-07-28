package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.util.Pair;

// 提供日期相关功能的辅助类
public class dateHandler {

	/**
	 * 将输入的日期转换为long类型
	 * 
	 * @param dateString 日期字符串
	 * @return long类型日期表示
	 */
	public static long parseDate(String dateString) {
		Date date;
		long result = -1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(dateString);
			result = date.getTime();
		} catch (ParseException e) {
			System.out.println("日期格式错误：" + dateString);
		}
		return result;
	}

	/**
	 * 将long类型表示的日期转换为String类型（yyyy-MM-dd)
	 * 
	 * @param millisecond long类型表示的日期
	 * @return String类型表示的日期
	 */
	public static String parseLong(long millisecond) {
		Date date = new Date(millisecond);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	/**
	 * 将long类型表示的日期增加n天
	 * 
	 * @param millisecond long类型表示的日期
	 * @param n 增加天数
	 * @return 增加后的long表示
	 */
	public static long incnDayLong(long millisecond, int n) {
		return millisecond + 86400000L * n;
	}
	
	/**
	 * 将long类型表示的日期减少n天
	 * 
	 * @param millisecond long类型表示的日期
	 * @param n 减少天数
	 * @return 减少后的long表示
	 */
	public static long decnDayLong(long millisecond, int n) {
		return millisecond - 86400000L * n;
	}
	
	/**
	 * 将课程安排中的星期和时间段转换为long表示的时间
	 * 
	 * @param day 星期
	 * @param hour 时间段
	 * @return long表示的时间
	 */
	public static Pair<Long, Long> getCourseTime(int day, String hour) {
		long start = -1L;
		long end;
		if (hour.equals("8-10")) {
			start = 14 * 3600 * 1000;
		} else if (hour.equals("10-12")) {
			start = 16 * 3600 * 1000;
		} else if (hour.equals("13-15")) {
			start = 18 * 3600 * 1000;
		} else if (hour.equals("15-17")) {
			start = 20 * 3600 * 1000;
		} else if (hour.equals("19-21")) {
			start = 22 * 3600 * 1000;
		}
		start = start >= 0 ? start + incnDayLong(0, day-1) : start;
		end = start + 7200000L;
		return new Pair<>(start, end);
	}
	
	/**
	 * 将long表示课程时间转换为课程开始时间（时）
	 * 
	 * @param min 起始时间
	 * @param t 课程时间long表示
	 * @return 课程开始时间（时）
	 */
	public static int convertCourseTimeToSchedule(long min, long t) {
		if (t == min + 14 * 3600 * 1000) { // 8-10
			return 8;
		} else if (t == min + 16 * 3600 * 1000) { // 10-12
			return 10;
		} else if (t == min + 18 * 3600 * 1000) { // 13-15
			return 13;
		} else if (t == min + 20 * 3600 * 1000) { // 15-17
			return 15;
		} else if (t == min + 22 * 3600 * 1000) { // 19-21
			return 19;
		}
		return -1;
	}

	/**
	 * 获取指定long表示日期是一周的第几天
	 * 
	 * @param time long表示日期
	 * @return 对应一周的第几天
	 */
	public static int calcWeekDay(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

}
