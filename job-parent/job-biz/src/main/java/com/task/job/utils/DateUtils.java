package com.task.job.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;


public class DateUtils {
	public final static String DATE_FORMAT_NORMAL = "yyyyMMddHHmmss";
	public final static String DATE_FORMAT_SEQ = "yyyyMMddHHmmssmmm";
	public final static String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
	public final static String DATE_FORMAT_SOLIDUS = "yyyy/MM/dd";
	public final static String DATE_FORMAT_YEAR = "yyyy";
	public final static String DATE_FORMAT_MON = "yyyyMM";
	public final static String DATE_FORMAT_COMPACT = "yyyyMMdd";
	public final static String DATE_FORMAT_UTC_DEFAULT = "MM-dd-yyyy";
	public final static String DATE_FORMAT_UTC_SOLIDUS = "MM/dd/yyyy";
	public final static String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
	public final static String DATE_FORMAT_CHINESE_TO_MIN = "yyyy年MM月dd日 hh:mm";
	public final static String DATE_TIME_FORMAT_CHINESE = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String MON_FORMAT_CHINESE = "yyyy年MM月";
	public final static String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_TIME_FORMAT_SOLIDUS = "yyyy/MM/dd HH:mm:ss";
	public final static String DATE_TIME_FORMAT_UTC_DEFAULT = "MM-dd-yyyy HH:mm:ss";
	public final static String DATE_TIME_FORMAT_UTC_SOLIDUS = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String CMS_DRAW_SEQUENCE_FORMAT = "yyyyMMddhhmmss";
	public final static int   SECONDS_PER_DAY  = 60*60*24;
	public final static int   SECONDS_PER_HOUR = 60*60;
	public final static int   SECONDS_PER_HALF_HOUR = 60*30;
	public final static int   SECONDS_PER_TEN_MIN = 60*10;
	public final static int   SECONDS_PER_FIVE_MIN = 60*6;
	public final static int   SECONDS_PER_MIN  = 60;
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
	private static Map<String, String> dateFormatRegisterMap = new HashMap<String, String>();
	private static Map<String, SimpleDateFormat> dateFormatMap = new HashMap<String, SimpleDateFormat>();

	static {
		dateFormatRegisterMap.put(DATE_FORMAT_COMPACT, "^\\d{8}$");
		dateFormatRegisterMap.put(DATE_FORMAT_DEFAULT, "^\\d{4}-\\d{1,2}-\\d{1,2}$");
		dateFormatRegisterMap.put(DATE_FORMAT_SOLIDUS, "^\\d{4}/\\d{1,2}/\\d{1,2}$");
		dateFormatRegisterMap.put(DATE_FORMAT_UTC_DEFAULT, "^\\d{1,2}-\\d{1,2}-\\d{4}$");
		dateFormatRegisterMap.put(DATE_FORMAT_UTC_SOLIDUS, "^\\d{1,2}/\\d{1,2}/\\d{4}$");
		dateFormatRegisterMap.put(DATE_TIME_FORMAT_DEFAULT, "^\\d{4}-\\d{1,2}-\\d{1,2}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");
		dateFormatRegisterMap.put(DATE_TIME_FORMAT_SOLIDUS, "^\\d{4}/\\d{1,2}/\\d{1,2}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");
		dateFormatRegisterMap.put(DATE_TIME_FORMAT_UTC_DEFAULT,
				"^\\d{1,2}-\\d{1,2}-\\d{4}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");
		dateFormatRegisterMap.put(DATE_TIME_FORMAT_UTC_SOLIDUS,
				"^\\d{1,2}/\\d{1,2}/\\d{4}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");

		dateFormatMap.put(DATE_FORMAT_DEFAULT, new SimpleDateFormat(DATE_FORMAT_DEFAULT));
		dateFormatMap.put(DATE_FORMAT_SOLIDUS, new SimpleDateFormat(DATE_FORMAT_SOLIDUS));
		dateFormatMap.put(DATE_FORMAT_COMPACT, new SimpleDateFormat(DATE_FORMAT_COMPACT));
		dateFormatMap.put(DATE_FORMAT_UTC_DEFAULT, new SimpleDateFormat(DATE_FORMAT_UTC_DEFAULT));
		dateFormatMap.put(DATE_FORMAT_UTC_SOLIDUS, new SimpleDateFormat(DATE_FORMAT_UTC_SOLIDUS));
		dateFormatMap.put(DATE_TIME_FORMAT_DEFAULT, new SimpleDateFormat(DATE_TIME_FORMAT_DEFAULT));
		dateFormatMap.put(DATE_TIME_FORMAT_SOLIDUS, new SimpleDateFormat(DATE_TIME_FORMAT_SOLIDUS));
		dateFormatMap.put(DATE_TIME_FORMAT_UTC_DEFAULT, new SimpleDateFormat(DATE_TIME_FORMAT_UTC_DEFAULT));
		dateFormatMap.put(DATE_TIME_FORMAT_UTC_SOLIDUS, new SimpleDateFormat(DATE_TIME_FORMAT_UTC_SOLIDUS));
	}

	public static String format(String formatString, Date date) {
		return new SimpleDateFormat(formatString).format(date);
	}



	

   	public static String formatDateSeq(Date date) {
		return formatDate(date, DATE_FORMAT_SEQ);
	}

    public static String formatTimeDateDefault(Date date) {
        if (date == null) {
            return "";
        }
        return formatDate(date, DATE_TIME_FORMAT_DEFAULT);
	}
    public static String curTimeDateDefault() {
    	return formatDate(new Date(), DATE_TIME_FORMAT_DEFAULT);
    }

	public static String formatDateSolidus(Date date) {
		return formatDate(date, DATE_FORMAT_SOLIDUS);
	}

	public static String formatDateOfYear(Date date) {
		return formatDate(date, DATE_FORMAT_YEAR);
	}
	
	public static String formatDateOfMon(Date date) {
		return formatDate(date, DATE_FORMAT_MON);
	}
	
	public static String formatMonToChinese(Date date) {
		return DateUtils.formatDate(date, MON_FORMAT_CHINESE);
	}
	

	public static Date parseDateSolidus(String dateString) {
		return parseDate(dateString, DATE_FORMAT_SOLIDUS);
	}

	public static Date parseDate(String src, String dateTemplate) {
		if (BeanUtils.isEmpty(src)) {
			return null;
		}

		try {
			return getSimpleDateFormat(dateTemplate).parse(src);
		} catch (ParseException e) {
			throw new RuntimeException(String.format("unsupported date template:%s", src), e);
		}
	}

	public static <T> T parseDate(String src, Class<T> dateClazz) {

		if (BeanUtils.isEmpty(src)) {
			return null;
		}
		return null;
	}

	public static <T> T parseDate(String src, String dateTemplate, Class<T> dateClazz) {

		if (BeanUtils.isEmpty(src)) {
			return null;
		}

		return convertDate(parseDate(src, dateTemplate), dateClazz);
	}

	public static Date fishForParseDate(String src) {

		if (BeanUtils.isEmpty(src)) {
			return null;
		}

		return fishForParseDate(src, Date.class);
	}

	public static <T> T fishForParseDate(Object obj, Class<T> dateClazz) {

		if (BeanUtils.isEmpty(obj)) {
			return null;
		}

		if (Date.class.isAssignableFrom(obj.getClass())) {
			return convertDate((Date) obj, dateClazz);
		}

		if (DateTime.class.isAssignableFrom(obj.getClass())) {
			return convertDate((DateTime) obj, dateClazz);
		}

		String src = obj.toString();

		for (Map.Entry<String, String> entry : dateFormatRegisterMap.entrySet()) {
			if (src.matches(entry.getValue())) {
				return convertDate(parseDate(src, entry.getKey()), dateClazz);
			}
		}

		throw new RuntimeException(String.format("unsupported date string format:%s", src));
	}

	public static boolean isDate(Object obj) {

		if (BeanUtils.isEmpty(obj)) {
			return false;
		}
		return isDateClass(obj.getClass());
	}

	public static boolean isMondayToFriday(Date date) {
		int dayOfWeek = new DateTime(date).getDayOfWeek();
		return dayOfWeek != 6 && dayOfWeek != 7;
	}

	public static boolean isDateClass(Class<?> clazz) {
		return (Date.class.isAssignableFrom(clazz) || DateTime.class.isAssignableFrom(clazz));
	}

	public static String formatDate(Date date) {

		if (BeanUtils.isEmpty(date)) {
			return null;
		}

		return formatDate(date, DATE_FORMAT_DEFAULT);
	}

	public static String formatDateCompact(Date date) {

		if (BeanUtils.isEmpty(date)) {
			return null;
		}

		return formatDate(date, DATE_FORMAT_COMPACT);
	}

	public static String formatDate(Date date, String dateTemplate) {
		if (BeanUtils.isEmpty(date) || BeanUtils.isEmpty(dateTemplate)) {
			return null;
		}
		return getSimpleDateFormat(dateTemplate).format(date);
	}

	public static <T> T convertDate(Date src, Class<T> dateClazz) {

		if (BeanUtils.isEmpty(src)) {
			return null;
		}

		try {

			return dateClazz.getConstructor(long.class).newInstance(src.getTime());
		} catch (Exception e) {
			String errorMessage = String.format("unsupported date type:%s", dateClazz.getName());
			logger.error(errorMessage,e);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static <T> T convertDate(DateTime src, Class<T> dateClazz) {

		if (BeanUtils.isEmpty(src)) {
			return null;
		}

		try {

			return dateClazz.getConstructor(long.class).newInstance(src.getMillis());
		} catch (Exception e) {
			String errorMessage = String.format("unsupported date type:%s", dateClazz.getName());
			logger.error(errorMessage,e);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static SimpleDateFormat getSimpleDateFormat(String dateTemplate) {
		return new SimpleDateFormat(dateTemplate);
	}

	private static long render(long i, int j, int k) {
		return (i + (i > 0 ? j : -j)) / k;
	}

	public static long diffSecond(Date start, Date end) {
		return render(end.getTime() - start.getTime(), 999, 1000);
	}

	public static long diffMinute(Date end) {
		return diffMinute(new Date(System.currentTimeMillis()), end);
	}

	public static long diffMinute(Date start, Date end) {
		return render(diffSecond(start, end), 59, 60);
	}

	public static long diffHour(Date start, Date end) {
		return render(diffMinute(start, end), 59, 60);
	}

	public static long diffDay(Date start, Date end) {
		return offset(start, end, Calendar.DAY_OF_YEAR);
	}

	public static long diffMonth(Date start, Date end) {
		return offset(start, end, Calendar.MONTH) + diffYear(start, end);
	}

	public static long diffYear(Date start, Date end) {
		Calendar s = Calendar.getInstance();
		Calendar e = Calendar.getInstance();

		s.setTime(start);
		e.setTime(end);

		return e.get(Calendar.YEAR) - s.get(Calendar.YEAR);
	}

	private static long offset(Date start, Date end, int offsetCalendarField) {

		boolean bool = start.before(end);
		long rtn = 0;
		Calendar s = Calendar.getInstance();
		Calendar e = Calendar.getInstance();

		s.setTime(bool ? start : end);
		e.setTime(bool ? end : start);

		rtn -= s.get(offsetCalendarField);
		rtn += e.get(offsetCalendarField);

		while (s.get(Calendar.YEAR) < e.get(Calendar.YEAR)) {
			rtn += s.getActualMaximum(offsetCalendarField);
			s.add(Calendar.YEAR, 1);
		}

		return bool ? rtn : -rtn;
	}

	public static Date add(Date date, int n, int calendarField) {
		if(null!=date){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(calendarField, n);
			return c.getTime();
		}
		return null;
	}
	
	public static Date addDay(Date date,int n){
		if(date == null){
			return null;
		}
		return add(date, n, Calendar.DAY_OF_MONTH);
	}

	public static Date addMonth(Date date,int n){
		if(date == null){
			return null;
		}
		return add(date, n, Calendar.MONTH);
	}
	public static Date addYear(Date date,int n){
		if(date == null){
			return null;
		}
		return add(date, n, Calendar.YEAR);
	}

	public static String formatDateAsCmsDrawSequence(Date date) {
		return formatDate(date, CMS_DRAW_SEQUENCE_FORMAT);
	}



	public static Date startOfToday() {
		return startOfDay(new Date());
	}

	public static String formatDateTime(Date date) {
		return (date == null) ? null : formatDate(date, DATE_TIME_FORMAT);
	}

	public static Date parseDateTime(String date) {
		return parseDate(date, DATE_TIME_FORMAT);
	}

	public static Date parseCMSDateTime(String date) {
		return parseDate(date, CMS_DRAW_SEQUENCE_FORMAT);
	}

	public static boolean beforeToday(Date date) {
		return date.compareTo(DateUtils.startOfToday()) < 0;
	}

	public static boolean afterToday(Date date) {
		return date.compareTo(DateUtils.startOfToday()) > 0;
	}

	public static Date startOfDay(Date date) {
		return startDateTimeOfDay(date).toDate();
	}

	public static Date startOfCurrentMonth() {
		return startOfMonth(new Date());
	}
	public static Date startOfMonth(Date date) {
		return startMonthOfYear(date).toDate();
	}

	public static Date startOfCurrentYear() {
		return startOfYear(new Date());
	}
	public static Date startOfYear(Date date) {
		return startYearOfCentury(date).toDate();
	}

    public static DateTime startDateTimeOfToday(){
        return startDateTimeOfDay(new Date());
    }

    private static DateTime startDateTimeOfDay(Date date) {
        return new DateTime(date).dayOfYear().roundFloorCopy();
    }
    private static DateTime startMonthOfYear(Date date) {
        return new DateTime(date).monthOfYear().roundFloorCopy();
    }
    private static DateTime startYearOfCentury(Date date) {
        return new DateTime(date).yearOfCentury().roundFloorCopy();
    }

    public static Date endOfToday() {
		return endOfDay(new Date());
	}

    public static DateTime endDateTimeOfToday() {
		return endDateTimeOfDay(new Date());
	}

	public static Date endOfDay(Date date) {
        return endDateTimeOfDay(date).toDate();
	}

    private static DateTime endDateTimeOfDay(Date date) {
        DateTime startDateTime = startDateTimeOfDay(date);
        return startDateTime.plusDays(1).minusMillis(1);
    }

    public static boolean isOnSameDayOfMonth(DateTime datetime, DateTime other) {
		return datetime.getDayOfMonth() == other.getDayOfMonth();
	}
    
    public static boolean isOnLastDay(Date date,int day) {
    	 if(date==null){
    		 return false;
    	 }
    	 Date future = addDay(date, day);
    	 return getMonth(future)>getMonth(date);
    }
    public static boolean isOnSameMonth(String mon,Date date) {
    	 if(date==null || StringUtils.isBlank(mon)){
    		 return false;
    	 }
    	return mon.equals(formatDateOfMon(date));
    }

	public static String getYearOfFourBits(Date date) {
		return new DateTime(date).getYear() + "";
	}

	public static String getMonthOfTwoBits(Date date) {
		String month = new DateTime(date).getMonthOfYear() + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		return month;
	}

	public static String getDayOfTwoBits(Date date) {
		String day = new DateTime(date).getDayOfMonth() + "";
		if (day.length() == 1) {
			day = "0" + day;
		}
		return day;
	}

	public static boolean compareTillSecond(Date oneDate, Date anotherDate) {
		if (oneDate == null || anotherDate == null)
			return false;
		return format(DATE_TIME_FORMAT_DEFAULT, oneDate).equals(format(DATE_TIME_FORMAT_DEFAULT, anotherDate));
	}

	public static boolean isSameDay(Date date, Date other) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(date);
		String otherString = format.format(other);
		return dateString.equals(otherString);
	}

	public static String formatDateToChineseToMin(Date date) {
		return DateUtils.formatDate(date, DATE_FORMAT_CHINESE_TO_MIN);
	}

	public static String formatDateToChinese(Date date) {
		return DateUtils.formatDate(date, DATE_FORMAT_CHINESE);
	}

	public static int getAge(Date birthdate) {
		return getAge(birthdate, Calendar.getInstance().getTime());
	}

	public static int getAge(Date birthdate, Date current) {
		Calendar calBirthDate = Calendar.getInstance();
		calBirthDate.setTime(birthdate);

		Calendar calCurrent = Calendar.getInstance();
		calCurrent.setTime(current);

		int age = calCurrent.get(Calendar.YEAR) - calBirthDate.get(Calendar.YEAR);
		int monthDiff = calCurrent.get(Calendar.MONTH) - calBirthDate.get(Calendar.MONTH);
		int dateDiff = calCurrent.get(Calendar.DATE) - calBirthDate.get(Calendar.DATE);

		if (monthDiff < 0) {
			age--;
		} else if (monthDiff == 0 && dateDiff < 0) {
			age--;
		}

		return age;
	}

	public static int getAgeFromIdNo(String idNo) {
		if (StringUtils.isEmpty(idNo)) {
			return -1;
		}
		Date birthDate = getBirthDateStr(idNo);
		if (birthDate == null) {
			return -1;
		}
		return DateUtils.getAge(birthDate);

	}

	public static Date getBirthDateStr(String idNo) {
		if (StringUtils.isEmpty(idNo)) {
			return null;
		}
		String strDate = null;
		if (idNo.length() == 15) {
			strDate = "19" + idNo.substring(6, 12);
		} else if (idNo.length() == 18) {
			strDate = idNo.substring(6, 14);
		} else {
			return null;
		}
		try {
			return new SimpleDateFormat("yyyyMMdd").parse(strDate);
		} catch (ParseException e) {
			logger.error("unable to parse date:" + strDate, e);
			throw new RuntimeException(" unable to parse date:" + strDate, e);
		}
	}
	
	public static Long getMonth(Date date){
		if(date!=null){
			 return Long.parseLong(format("yyyyMM", date)); 
		}
	    return null;
	}

	public static Long getLastMonth(Date date){
		if(date!=null){
			Date lastMon = add(date, -1, Calendar.MONTH);
			 return Long.parseLong(format("yyyyMM", lastMon)); 
		}
		return null;
	}
	public static String getLastMonth(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		return format("yyyyMM", c.getTime());
	}
	public static Date getLastMonthFirstDay(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DATE, 1);
		return c.getTime();
	}
	public static Date getCurMonthFirstDay(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		return c.getTime();
	}
	
	public static Date getFirstDayByMon(String mon){
		if(isMon(mon)){
			return parseDate(mon+"01","yyyyMMdd");
		}
		return null;
	}
	
	public static boolean isMon(String mon){
		if(StringUtils.isNotBlank(mon) && mon.matches("\\d{6}")){
			return true;
		}
		return false;
	}
	
	public static boolean isDateWithLine(String date){
		if(StringUtils.isBlank(date)){
			return false;
		}
		return date.matches(dateFormatRegisterMap.get(DATE_FORMAT_DEFAULT));
	}
	
	public static Timestamp conver2Timestamp(Date date){
		if(date!=null){
			return new Timestamp(date.getTime());
		}
		return null;
	}
	
	//Date 
	public static String formateDigit(String date,int num){
			String digit="";
			if(StringUtils.isNotBlank(date)){
				for(int i=0;i<date.length();i++){
					char x = date.charAt(i);
					if(Character.isDigit(x)){
						digit+=x;
						if(--num<=0){
							break;
						}
					}
				}
			}
			return digit;
	}

public static Date getNextFireTime(Date date ,String cronExpr){
	try {
		CronTrigger cronTrigger = new CronTrigger(cronExpr);
		SimpleTriggerContext simpleTriggerContext = new SimpleTriggerContext(null, null, date);
		return cronTrigger.nextExecutionTime(simpleTriggerContext);
	}catch (Exception e){
		logger.error(String.format(" getNextFireTime exception[date:%s,cronExpr:%s] ",date,cronExpr),e);
		return date;
	}
}

	/**
	 * 获取给定日期年份的第一天
	 * @param date
	 * @return
	 */
	public static Date getYearFirst(Date date,int n){
		Calendar calendar=Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		return getYearFirst(year+n);

	}

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date yearFirst = calendar.getTime();
		return yearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/**
	 * 后年年初
	 * @return
	 */
	public static Date getYearAfterYearFirst(){
		return getYearFirst(new Date(),2);
	}

		/**
		 * 后年年初
		 * @param date
		 * @return
		 */
	public static Date getYearAfterYearFirst(Date date){
		if(date == null){
			date = new Date();
		}
		return getYearFirst(date,2);
	}

public static void main(String[] args){
/*	System.out.println(DateUtils.isDateWithLine("2014-10-10"));
	String seq = DateUtils.formatDateSeq(new Date());
	System.out.println(seq);*/
/*	Date date = DateUtils.parseDateTime("2016-07-15 00:00:00");
	Date start = DateUtils.parseDateTime("2016-07-29 13:50:10");
	Date end = DateUtils.parseDateTime("2016-07-29 13:50:30");
	System.out.println(formatDateTime(getNextFireTime(date,"0 0 0 * * ?")));
	System.out.println(diffSecond(start,end));*/
	/*System.out.println(getYearAfterYearFirst(DateUtils.parseDateTime("2012-07-29 13:50:10")));
	System.out.println(getYearLast(1994));*/
	System.out.println(startOfCurrentYear());
}

}

