package com.example.lgauthservice.shared.utils;

import com.example.lgauthservice.shared.constants.Constants;
import com.example.lgauthservice.shared.domain.exception.InternalServerException;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.GregorianCalendar;

import static com.example.lgauthservice.shared.utils.TimeUtils.*;


/**
 * Thread-safe, immutable formatters, không dùng legacy Date/Calendar
 */
public final class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
    // ====================== ZONE ======================
    public static final ZoneId SYSTEM_DEFAULT_ZONE = ZoneId.of(Constants.STRING_DEFAULT_ZONE_ID);
    public static final ZoneId SYSTEM_UTC_ZONE = ZoneId.of(Constants.STRING_UTC_ZONE_ID);

    // ====================== FORMATTERS (thread-safe) ======================
    public static final DateTimeFormatter DF_DD_MM_YYYY = DateTimeFormatter.ofPattern(Constants.SHORT_DATE_FORMAT_DDMMYYYY_HYPHEN);
    public static final DateTimeFormatter DF_DDMMYYYY = DateTimeFormatter.ofPattern(Constants.SHORT_DATE_FORMAT_DDMMYYYY);
    public static final DateTimeFormatter DF_YYYY_MM_DD = DateTimeFormatter.ofPattern(Constants.SHORT_DATE_FORMAT_YYYYMMDD_HYPHEN);
    public static final DateTimeFormatter DF_YYYYMMDD = DateTimeFormatter.ofPattern(Constants.SHORT_DATE_FORMAT_YYYYMMDD);
    public static final DateTimeFormatter DF_SLASH_DD_MM_YYYY = DateTimeFormatter.ofPattern(Constants.SHORT_DATE_FORMAT_DDMMYYYY_SLASH);
    public static final DateTimeFormatter DF_SLASH_YYYY_MM_DD = DateTimeFormatter.ofPattern(Constants.SHORT_DATE_FORMAT_YYYYMMDD_SLASH);
    public static final DateTimeFormatter DF_FULL_DATETIME = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_DDMMYYYY_HYPHEN);
    public static final DateTimeFormatter DF_ISO_DATETIME = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_WITH_ZONE);
    public static final DateTimeFormatter DF_SLASH_DDMMYYYY_HHMM_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ====================== ENUM NGÀY TRONG TUẦN ======================
    public enum DaysWeekEnum {
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;

        public static DaysWeekEnum from(DayOfWeek dayOfWeek) {
            return values()[dayOfWeek.getValue() - 1]; // DayOfWeek: MON=1 → Monday (index 0)
        }

        public static DaysWeekEnum fromLocalDate(LocalDate date) {
            return from(date.getDayOfWeek());
        }
    }

    // ====================== PRIVATE CONSTRUCTOR ======================
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class - cannot instantiate");
    }

    public static LocalDateTime startOfDayLocal(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate().atStartOfDay();
    }

    public static LocalDateTime endOfDayLocal(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    // ====================== PLUS OPERATIONS ======================
    public static Instant plusDays(Instant instant, long days) {
        return instant == null ? null : instant.plus(Duration.ofDays(days));
    }

    public static Instant plusMonths(Instant instant, long months) {
        if (instant == null) return null;
        return instant.atZone(SYSTEM_DEFAULT_ZONE).plusMonths(months).toInstant();
    }

    public static Instant plusYears(Instant instant, long years) {
        if (instant == null) return null;
        return instant.atZone(SYSTEM_DEFAULT_ZONE).plusYears(years).toInstant();
    }

    // ====================== CONVERSIONS ======================
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, SYSTEM_DEFAULT_ZONE);
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(SYSTEM_DEFAULT_ZONE).toInstant();
    }

    public static Timestamp toTimestamp(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }

    public static Instant toInstant(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant();
    }

    public static LocalDate toLocalDate(String yyyy_MM_dd) {
        if (yyyy_MM_dd == null || yyyy_MM_dd.isBlank()) return null;
        return LocalDate.parse(yyyy_MM_dd.trim(), DF_YYYY_MM_DD);
    }

    public static LocalDate toLocalDateFromYyyyMMdd(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.isBlank()) return null;
        return LocalDate.parse(yyyymmdd.trim(), DF_YYYYMMDD);
    }

    // ====================== FORMATTING ======================
    public static String formatDdMmYyyy(LocalDate date) {
        return date == null ? null : DF_DD_MM_YYYY.format(date);
    }

    public static String formatDdMmYyyy(LocalDateTime dateTime) {
        return dateTime == null ? null : DF_DD_MM_YYYY.format(dateTime);
    }

    public static String formatYyyyMmDd(LocalDate date) {
        return date == null ? null : DF_YYYY_MM_DD.format(date);
    }

    public static String formatYyyyMMdd(LocalDate date) {
        return date == null ? null : DF_YYYYMMDD.format(date);
    }

    public static String formatSlashDdMmYyyy(LocalDate date) {
        return date == null ? null : DF_SLASH_DD_MM_YYYY.format(date);
    }

    public static String formatFullDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : DF_FULL_DATETIME.format(dateTime);
    }

    public static String convertDateFormat(
            String dateString,
            DateTimeFormatter inputFormatter,
            DateTimeFormatter outputFormatter) {

        if (dateString == null || inputFormatter == null || outputFormatter == null) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateString, inputFormatter);
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            log.error("convertDateFormat: Lỗi chuyển đổi ngày tháng. Chi tiết: {}", e.getMessage());
            throw new InternalServerException();
        }
    }

    // ====================== DAY OF WEEK ======================
    public static DaysWeekEnum getDayOfWeek(String yyyy_MM_dd) {
        LocalDate date = toLocalDate(yyyy_MM_dd);
        return date == null ? null : DaysWeekEnum.from(date.getDayOfWeek());
    }

    public static DaysWeekEnum getDayOfWeek(LocalDate date) {
        return date == null ? null : DaysWeekEnum.from(date.getDayOfWeek());
    }

    // ====================== COMPARISON ======================
    public static boolean isSameDay(Instant i1, Instant i2) {
        if (i1 == null || i2 == null) return false;
        return i1.atZone(SYSTEM_DEFAULT_ZONE).toLocalDate().isEqual(i2.atZone(SYSTEM_DEFAULT_ZONE).toLocalDate());
    }

    public static long daysBetween(Instant startInclusive, Instant endExclusive) {
        if (startInclusive == null || endExclusive == null) return 0;
        return Duration.between(startOfDay(startInclusive), startOfDay(endExclusive)).toDays();
    }

    // ====================== LEGACY SUPPORT (XML, SOAP, etc.) ======================
    public static XMLGregorianCalendar toXmlGregorianCalendar(Instant instant) {
        if (instant == null) return null;
        try {
            ZonedDateTime zdt = instant.atZone(SYSTEM_DEFAULT_ZONE);
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(GregorianCalendar.from(zdt));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Instant to XMLGregorianCalendar", e);
        }
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(LocalDate date) {
        if (date == null) return null;
        return toXmlGregorianCalendar(date.atStartOfDay(SYSTEM_DEFAULT_ZONE).toInstant());
    }

    // ====================== UTILS NHỎ ======================
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    public static int getDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public static LocalDate nowDate() {
        return LocalDate.now(SYSTEM_DEFAULT_ZONE);
    }

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(SYSTEM_DEFAULT_ZONE);
    }

    public static String convertInstantToString(Instant date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(SYSTEM_DEFAULT_ZONE);
            return formatter.format(date);
        } catch (Exception e) {
            return Strings.EMPTY;
        }
    }
}
