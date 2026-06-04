package com.example.lgauthservice.shared.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.util.Strings;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtils {
    public static final String EMPTY = "";
    public static final String XXXX = "xxxx";

    public String toSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public String extractFieldName(String path) {
        if (path == null || path.isEmpty()) return "unknown";
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }

    public String getOrDefault(String input, String value) {
        return org.apache.commons.lang3.StringUtils.isBlank(input) ? value : input;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /***
     * Kiem tra chuoi co ky tu dac biet
     * @return boolean
     */
    public static boolean isContainSpecialCharacter(String inputStr) {

        if (inputStr == null) {
            return false;
        }
        inputStr = inputStr.replace(" ", "");
        if (inputStr.isEmpty()) {
            return false;
        }

        Pattern p = Pattern.compile("[~!@#$%^&*()-=+_?><,|]");
        Matcher m = p.matcher(inputStr);

        return m.find();
    }

    public static boolean isNullOrEmpty(String str) {
        return ((str == null) || (str.trim().isEmpty()));
    }

    public static boolean isNotNullAndEmpty(String str) {
        return (!(isNullOrEmpty(str)));
    }

    public static boolean isNotNullAndEmpty(Object str) {
        if (str == null) return false;
        return (!(isNullOrEmpty(str.toString())));
    }

    public static boolean isNullOrEmpty(Object str) {
        if (str == null) return true;
        return isNullOrEmpty(str.toString());
    }

    public static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; ++i) {
            if (!(Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return (!(isBlank(str)));
    }

    public static boolean equals(String str1, String str2) {
        return (str1 != null && (str2 == null || str1.equals(str2)));
    }

    public static boolean checkRegexStr(String str, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean checkRegexStrFind(String str, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean checkLength(String str, int min, int max) {
        if ((str == null) || (str.isEmpty())) {
            return false;
        } else return str.length() >= min && str.length() <= max;
    }

    public static boolean checkMinLength(String str, int min) {
        if ((str == null) || (str.isEmpty())) {
            return false;
        }

        return str.length() >= min;
    }

    public static boolean checkMaxLength(String str, int max) {
        if ((str == null) || (str.isEmpty())) {
            return true;
        } else return str.length() <= max;
    }

    public static String replace(String value, String oldPart, String newPart) {
        if (value == null || value.isEmpty() || oldPart == null || oldPart.isEmpty())
            return value;
        //
        int oldPartLength = oldPart.length();
        String oldValue = value;
        StringBuilder retValue = new StringBuilder();
        int pos = oldValue.indexOf(oldPart);
        while (pos != -1) {
            retValue.append(oldValue, 0, pos);
            if (newPart != null && !newPart.isEmpty())
                retValue.append(newPart);
            oldValue = oldValue.substring(pos + oldPartLength);
            pos = oldValue.indexOf(oldPart);
        }
        retValue.append(oldValue);
        return retValue.toString();
    }

    public static boolean isHasWhiteSpaceBeginEnd(String str) {
        if ((str == null) || (str.isEmpty()))
            return false;
        return (str.endsWith(" ") || str.startsWith(" "));
    }

    public static boolean isHasWhiteSpace(String str) {
        if ((str == null) || (str.isEmpty()))
            return false;
        return (str.contains(" "));
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static String valueOfTimestamp(Date timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String result = "";
        if (timestamp != null) {
            result = dateFormat.format(new Date(timestamp.getTime()));
        }
        return result;
    }

    public static String valueOfTimestamp(Timestamp timestamp, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(timestamp.getTime()));
    }

    public static String valueOfTimestamp(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String doubleFormat(double value) {
        String result = "";
        NumberFormat formatter = new DecimalFormat("#0.00");
        result = formatter.format(value);
        return result;
    }

    public static String doubleFormat(double value, String format) {
        String result = "";
        NumberFormat formatter = new DecimalFormat(format);
        result = formatter.format(value);
        return result;
    }

    public static String priceWithoutDecimal(double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    public static String trim(String stringToTrim, String stringToRemove) {
        String answer = stringToTrim;

        while (answer.startsWith(stringToRemove)) {
            answer = answer.substring(stringToRemove.length());
        }

        while (answer.endsWith(stringToRemove)) {
            answer = answer.substring(0, answer.lastIndexOf(stringToRemove));
        }

        return answer;
    }

    public static List<String> split(String values, String regex) {
        List<String> result = new ArrayList<>();
        try {
            result = Arrays.asList(values.split(regex));
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static String replaceChar(String str, char ch, int index) {
        char[] chars = str.toCharArray();
        chars[index] = ch;
        return String.valueOf(chars);
    }

    public static String numberToText(double number) {
        double numberRound;
        String[] so = new String[]{"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] hang = new String[]{"", "nghìn", "triệu", "tỷ"};
        int length, j, donvi, chuc, tram;
        StringBuilder str = new StringBuilder(" ");
        boolean boolSoAm = false;

        numberRound = Math.round(number);
        if (numberRound < 0) {
            numberRound = -numberRound;
            boolSoAm = true;
        }

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);
        String[] words = df.format(numberRound).split("\\.");
        //region Phần nguyên
        length = words[0].length();
        if (length == 0 || Double.parseDouble(words[0]) == 0)
            str.insert(0, so[0]);
        else {
            j = 0;
            while (length > 0) {
                donvi = Integer.parseInt(words[0].substring(length - 1, length));
                length--;
                if (length > 0)
                    chuc = Integer.parseInt(words[0].substring(length - 1, length));
                else
                    chuc = -1;
                length--;
                if (length > 0)
                    tram = Integer.parseInt(words[0].substring(length - 1, length));
                else
                    tram = -1;
                length--;
                if ((donvi > 0) || (chuc > 0) || (tram > 0) || (j == 3))
                    str.insert(0, hang[j]);
                j++;
                if (j > 3) j = 1;
                if ((donvi == 1) && (chuc > 1))
                    str.insert(0, "mốt ");
                else {
                    if ((donvi == 5) && (chuc > 0))
                        str.insert(0, "lăm ");
                    else if (donvi > 0)
                        str.insert(0, so[donvi] + " ");
                }
                if (chuc < 0)
                    break;
                else {
                    if ((chuc == 0) && (donvi > 0)) str.insert(0, "linh ");
                    if (chuc == 1) str.insert(0, "mười ");
                    if (chuc > 1) str.insert(0, so[chuc] + " mươi ");
                }
                if (tram < 0) break;
                else {
                    if ((tram > 0) || (chuc > 0) || (donvi > 0)) str.insert(0, so[tram] + " trăm ");
                }
                str.insert(0, " ");
            }
        }
        //endregion
        //region Thập phân
        if (words.length > 1) {
            String thapPhan = words[1];
            if (Double.parseDouble(thapPhan) != 0) {
                // Remove số 0 ở cuối
                length = thapPhan.length();
                j = 0;
                while (length > 0) {
                    if (thapPhan.charAt(length - 1) == '0') {
                        thapPhan = replaceChar(thapPhan, Character.MIN_VALUE, length - 1);
                        length--;
                    } else break;
                }

                str.append("phẩy");

                // String thập phân
                StringBuilder str_dec = new StringBuilder();
                length = thapPhan.length();
                ;
                while (length > 0) {
                    donvi = Integer.parseInt(thapPhan.substring(length - 1, length));
                    length--;
                    if (length > 0)
                        chuc = Integer.parseInt(thapPhan.substring(length - 1, length));
                    else
                        chuc = -1;
                    length--;
                    if (length > 0)
                        tram = Integer.parseInt(thapPhan.substring(length - 1, length));
                    else
                        tram = -1;
                    length--;
                    if ((donvi > 0) || (chuc > 0) || (tram > 0) || (j == 3))
                        str_dec.insert(0, hang[j]);
                    j++;
                    if (j > 3) j = 1;
                    if ((donvi == 1) && (chuc > 1))
                        str_dec.insert(0, "một ");
                    else {
                        if ((donvi == 5) && (chuc > 0))
                            str_dec.insert(0, "lăm ");
                        else if (donvi > 0)
                            str_dec.insert(0, so[donvi] + " ");
                    }
                    if (chuc < 0)
                        break;
                    else {
                        if ((chuc == 0) && (donvi > 0)) str_dec.insert(0, "không ");
                        if (chuc == 1) str_dec.insert(0, "mười ");
                        if (chuc > 1) str_dec.insert(0, so[chuc] + " mươi ");
                    }
                    if (tram < 0) break;
                    else {
                        if ((tram > 0) || (chuc > 0) || (donvi > 0)) str_dec.insert(0, so[tram] + " trăm ");
                    }
                }
                str.append(" ").append(str_dec);
            }
        }
        //endregion

        str = new StringBuilder(str.toString().trim());
        if (boolSoAm) {
            str.insert(0, "Âm ");
        } else {
            str = new StringBuilder(str.substring(0, 1).toUpperCase() + str.substring(1));
        }

        //Truncate multiple space to single space
        str = new StringBuilder(str.toString().replaceAll("\\s+", " "));
        return str.toString();
    }

    public static String numberToText(String number) {
        if (isNullOrEmpty(number)) return "";
        return numberToText(Double.parseDouble(number));
    }

    public static String getSubString(String text, String charater, int beginIndex, int endIndex) {
        if (org.apache.commons.lang3.StringUtils.isBlank(text)) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        int index = text.indexOf(charater) + 1;
        return text.substring(index + beginIndex, index + beginIndex + endIndex);
    }

    public static List<String> strCommaToList(String text) {
        if (org.apache.commons.lang3.StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        return Arrays.asList(text.split(","));
    }

    public static String swapChars(String str, int lIdx, int rIdx) {
        StringBuilder sb = new StringBuilder(str);
        char l = sb.charAt(lIdx), r = sb.charAt(rIdx);
        sb.setCharAt(lIdx, r);
        sb.setCharAt(rIdx, l);
        return sb.toString();
    }

    public static String getDocIdFromUrlUAT(String url) {

        if (org.apache.commons.lang3.StringUtils.isBlank(url)) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        int index = url.indexOf("=") + 1;
        return url.substring(index);
    }

    public static boolean checkLookingAt(String str, String regex) {
        Pattern pattern1 = Pattern.compile(regex);
        Matcher matcher = pattern1.matcher(str);
        return matcher.lookingAt();
    }

    public static int lengthUtf8(CharSequence sequence) {
        int count = 0;
        for (int i = 0, len = sequence.length(); i < len; i++) {
            char ch = sequence.charAt(i);
            if (ch <= 0x7F) {
                count++;
            } else if (ch <= 0x7FF) {
                count += 2;
            } else if (Character.isHighSurrogate(ch)) {
                count += 4;
                ++i;
            } else {
                count += 3;
            }
        }
        return count;
    }

    public static String formatVnPhoneNumber(String phoneNumber, String prefix) {
        if (isBlank(phoneNumber)) {
            return phoneNumber;
        }
        prefix = isNotBlank(prefix) ? prefix : "84";
        return phoneNumber.replaceFirst("^(0|\\+84|84)", prefix);
    }

    public static String buildUrl(Map<String, Object> map, String... keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String value = getString(map, key);
            if (isNotBlank(value)) {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    public static String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (ObjectUtils.allNull(value)) {
            return Strings.EMPTY;
        }
        return value.toString();
    }

    public static String maskPhone(String phone) {
        if (StringUtils.isBlank(phone)) return Strings.EMPTY;
        if (phone.length() < 7) return phone;
        String prefix = phone.substring(0, 3);
        String suffix = phone.substring(phone.length() - 3);
        return prefix + XXXX + suffix;
    }

    public static String maskCitizenPid(String citizenPid) {
        if (org.apache.commons.lang3.StringUtils.isBlank(citizenPid)) return Strings.EMPTY;
        if (citizenPid.length() < 8) return citizenPid;

        String prefix = citizenPid.substring(0, 4);
        String suffix = citizenPid.substring(citizenPid.length() - 4);
        return prefix + XXXX + suffix;
    }
}
