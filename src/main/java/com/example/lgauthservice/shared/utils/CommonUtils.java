package com.example.lgauthservice.shared.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CommonUtils {


    /** Thực hiện phép trừ chính xác. */
    public static double minus(double first, double second) {
        BigDecimal bd1 = BigDecimal.valueOf(first);
        BigDecimal bd2 = BigDecimal.valueOf(second);
        return bd1.subtract(bd2).doubleValue();
    }

    /** Thực hiện phép cộng chính xác. */
    public static double plus(double first, double second) {
        BigDecimal bd1 = BigDecimal.valueOf(first);
        BigDecimal bd2 = BigDecimal.valueOf(second);
        return bd1.add(bd2).doubleValue();
    }

    /** Thực hiện phép nhân chính xác. */
    public static double mul(double first, double second) {
        BigDecimal bd1 = BigDecimal.valueOf(first);
        BigDecimal bd2 = BigDecimal.valueOf(second);
        return bd1.multiply(bd2).doubleValue();
    }

    /** Thực hiện phép chia chính xác. */
    public static double div(double first, double second) {
        if (second == 0) {
            throw new ArithmeticException("Division by zero");
        }
        BigDecimal bd1 = BigDecimal.valueOf(first);
        BigDecimal bd2 = BigDecimal.valueOf(second);
        return bd1.divide(bd2, 10, RoundingMode.HALF_UP).doubleValue();
    }


    /** Thay thế các ký tự đặc biệt bằng ký tự escape cho truy vấn SQL (LIKE). */
    public static String escapeCharForSearch(String str) {
        if (str == null) {
            return "";
        }
        return str.toLowerCase().replaceAll("([?%\\\\_])", "\\\\$1");
    }

    /** Định dạng số với num chữ số thập phân, trả về 0 nếu giá trị là null/0. */
    public static Double formatNumber(Number value, int num) {
        if (value == null || value.doubleValue() == 0) {
            return 0.0;
        }
        return new BigDecimal(value.doubleValue()).setScale(num, RoundingMode.HALF_UP).doubleValue();
    }

    /** Định dạng chỉ số KPI, trả về null nếu giá trị là null/0. */
    public static Double formatKpi(Number value, int num) {
        if (value == null || value.doubleValue() == 0) {
            return null;
        }
        return new BigDecimal(value.doubleValue()).setScale(num, RoundingMode.HALF_UP).doubleValue();
    }

    /** Tách phần username từ địa chỉ email (phần trước dấu '@'). */
    public static String getUsernameFromEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return "";
        }
        int index = email.indexOf('@');
        return (index > 0) ? email.substring(0, index) : "";
    }

    /** Định dạng giá trị thập phân. */
    public static Object customToFixed(Number value, int num, boolean isString) {
        if (value == null || value.doubleValue() == 0) {
            return value;
        }
        BigDecimal bd = new BigDecimal(value.doubleValue()).setScale(num, RoundingMode.HALF_UP);
        return isString ? bd.toString() : bd.doubleValue();
    }

    /** Định dạng số điện thoại VN, thay thế prefix (084, 0, +84, 84) bằng prefix mong muốn. */
    public static String formatVnPhoneNumber(String phoneNumber, String prefix) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return phoneNumber;
        }
        String p = prefix != null ? prefix : "84";
        return phoneNumber.replaceAll("^(084|0|\\+84|84)", p);
    }

    // --- Xử lý Ngày tháng (Thay thế Moment.js bằng java.time) ---

    /** Tính tổng số trang. */
    public static int getTotalPage(int totalRecord, int limit) {
        int actualLimit = (limit <= 0) ? 10 : limit;
        int actualTotalRecord = Math.max(totalRecord, 0);
        return (int) Math.ceil((double) actualTotalRecord / actualLimit);
    }

    /** Tách mảng thành các mảng con (chunks). */
    public static List<List<String>> chunkArray(List<String> array, int size) {
        if (array == null || array.isEmpty()) {
            return List.of();
        }
        int actualSize = (size <= 0) ? 100 : size;
        List<List<String>> chunkedList = new ArrayList<>();
        for (int i = 0; i < array.size(); i += actualSize) {
            chunkedList.add(array.subList(i, Math.min(i + actualSize, array.size())));
        }
        return chunkedList;
    }

    /** Hàm delay (sleep) đồng bộ/bất đồng bộ. */
    public static void sleep(long ms) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(ms);
    }

    /** Trích xuất giá trị cột từ mảng/List đối tượng dựa trên điều kiện. */
    public static <T, U, V> List<U> getValueObjWithCondition(
            List<T> data,
            java.util.function.Function<T, U> columnResult,
            java.util.function.Function<T, V> columnCondition,
            V condition
    ) {
        if (data == null) {
            return List.of();
        }
        return data.stream()
                .filter(item -> columnCondition.apply(item) != null && columnCondition.apply(item).equals(condition))
                .map(columnResult)
                .collect(Collectors.toList());
    }
}