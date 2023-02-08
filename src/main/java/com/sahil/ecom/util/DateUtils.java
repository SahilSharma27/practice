package com.sahil.ecom.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtils {

    public static final String DATE_FORMAT_MMMM_YYYY = "MMM yyyy";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String M_D_YYYY = "M/d/yyyy";
    public static final String MMM_YY = "MMM-yy";
    public static final String MMM_DD_YYYY = "MMM-dd, yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String MMMM_YYYY = "MMMM-yyyy";
    public static final String DATE_FORMAT_MMM_YYYY = "MMM-yyyy";
    public static final String DATE_FORMAT_MMM_DD = "MMM-dd";
    public static final String DATE_FORMAT_MMM_DD_HH_A = "MMM-dd hh a";
    public static final String DATE_FORMAT1 = "yyyy-MM-dd";
    public static final String DATE_FORMAT2 = "MM/dd/yyyy";
    public static final String DATE_FORMAT3 = "dd MMM";
    public static final String DATE_FORMAT4 = "MMM-yyyy";
    public static final String DATE_FORMAT5 = "MMM dd, yyyy";
    public static final String DATE_FORMAT6 = "MM/yyyy";
    public static final String DATE_FORMAT7 = "MMMM yyyy";
    public static final String DATE_FORMAT8 = "dd-MMM-yy";
    public static final String DATE_FORMAT9 = "MMMM-yyyy";
    public static final String DATE_FORMAT10 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT11 = "dd-MMM-yyyy hh:mm a";
    public static final String DATE_FORMAT12 = "MMM-dd";
    public static final String DATE_FORMAT13 = "MMM-dd hh a";
    public static final String DATE_FORMAT_M_YYYY = "M-yyyy";
    public static final String FORMAT_MMMM_YYYY = "MMMM yyyy";
    public static final String FORMAT_MMMM_YYYY_WITH_COMMA = "MMMM, yyyy";
    public static final String START_DATE_TIME = " 00:00:00";
    public static final String END_DATE_TIME = " 23:59:59";
    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    private DateUtils() {
    }

    public static String convertDateStringFormat(@NotBlank String date, @NotBlank String format, @NotBlank String requiredFormat) {
        return formatDate(parseLocalDate(date, DateTimeFormatter.ofPattern(format)), DateTimeFormatter.ofPattern(requiredFormat));
    }

    private static String formatDate(@NotNull LocalDate localDateTime, @NotNull DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    public static String formatDateToString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    private static LocalDate parseLocalDate(@NotNull String date, @NotNull DateTimeFormatter formatter) {
        return LocalDate.parse(date, formatter);
    }

    public static int getDaysInMonth(Integer month, Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    public static String formatDate(@NotNull LocalDateTime localDateTime, @NotNull DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    public static int checkIfDatesAreOfSameMonthAndYear(Date startDate, Date endDate) {
        try {
            Calendar startCalender = Calendar.getInstance();
            Calendar endCalender = Calendar.getInstance();
            startCalender.setTime(startDate);
            endCalender.setTime(endDate);
            if (startCalender.get(Calendar.YEAR) == endCalender.get(Calendar.YEAR)) {
                return Math.abs((endCalender.get(Calendar.MONTH)) - (startCalender.get(Calendar.MONTH)));
            } else {
                int diff = Math.abs(((endCalender.get(Calendar.MONTH) + 1) % 12) - ((startCalender.get(Calendar.MONTH) + 1) % 12));
                if (diff == 0)
                    return 2;
                else
                    return diff;
            }
        } catch (Exception e) {
            log.info("Exception occurred while checking for month and year equality : " + e.getMessage());
            return -1;
        }
    }

    public static long getDaysInBetween(Date startDate, Date endDate) {
        return TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);

    }

    public static LocalDateTime parseDate(@NotNull String date, @NotNull DateTimeFormatter formatter) {
        return LocalDateTime.parse(date, formatter);
    }

    public static Date getNewDateMonthlyReportTwoDaysBack(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -2);
        date = calendar.getTime();
        return date;
    }

    public static int getMonthFromDate(Date date) {
        if (date==null) return 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYearFromDate(Date date) {
        if (date == null) return 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static String getMonthDate(Integer month, Integer year) {
        return convertDateStringFormat(month + "-" + year, DATE_FORMAT_M_YYYY, FORMAT_MMMM_YYYY);
    }

    public static Date parseDateMonthlyReport(String date, String pattern) {
        try {
                return new SimpleDateFormat(pattern).parse(date);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

//    public static <T extends MonthYear> String getDateFor(@NotNull T obj, @NotNull @NotBlank String format) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, obj.getMonth() - 1);
//        calendar.set(Calendar.YEAR, obj.getYear());
//        return new SimpleDateFormat(format).format(calendar.getTime());
//    }

    public static Date getDateFormat(@NotNull String date, @NotNull String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (Exception e) {
            log.info("Exception in DateUtils: " + e);
        }
        return new Date();
    }

    public static Date getFirstDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static String formatDate(@NotNull Date date, @NotNull String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date addToDate(Date date, int field, int n) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(field, n);
            return calendar.getTime();
        } catch (Exception e) {
            log.info("Exception occurred while parsing date : " + e.getMessage());
            return new Date();
        }
    }

    public static Date getLastDateOfPreviousMonth(Date date) {
        try {
            return getLastDateOfMonthUtil(date);
        } catch (Exception e) {
            log.info("Exception occurred while getting Last Date Of Previous Month of GIVEN Date.So returning Last Date of previous month of CURRENT date : " + e.getMessage());
            date = new Date();
            return getLastDateOfMonthUtil(date);
        }
    }

    public static Date getLastDateOfMonthUtil(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return setMaxTime(calendar.getTime());
    }

    public static Date setMaxTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        return calendar.getTime();
    }

    public static String convertToMonthString(String dateString) {
        return formatDate(parseDate(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")), DateTimeFormatter.ofPattern(DATE_FORMAT_MMM_YYYY));
    }

    public static String convertToDateString(String dateString) {
        return formatDate(parseDate(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")), DateTimeFormatter.ofPattern(DATE_FORMAT_MMM_DD));
    }

    public static String convertToHourString(String dateString) {
        return formatDate(parseDate(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH")), DateTimeFormatter.ofPattern(DATE_FORMAT_MMM_DD_HH_A));
    }

    public static String getMonthDate(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return formatDate(calendar.getTime(), DATE_FORMAT7);
    }

    public static Date clearTime(Date date) {
        if (date == null) return new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT10);
            String dateInString = dateFormat.format(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(dateInString));
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
