package com.example.lgauthservice.shared.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public final static String TIME_FORMAT = "HH:mm";
    public final static String FULL_TIME_FORMAT = "HH:mm:ss";
    public final static String DATETIME_FORMAT_WITH_ZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final static String FULL_DATETIME_FORMAT_WITH_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public final static String FULL_DATETIME_FORMAT_WITH_ZONE_DDMMYY_SLASH = "dd/MM/yyyy'T'HH:mm:ss.SSS'Z'";
    public final static String DATETIME_FORMAT_YYYYMMDD_SLASH = "yyyy/MM/dd HH:mm:ss";
    public final static String DATETIME_FORMAT_YYYYMMDD_HYPHEN = "yyyy-MM-dd HH:mm:ss";
    public final static String DATETIME_FORMAT_DDMMYYYY_SLASH = "dd/MM/yyyy HH:mm:ss";
    public final static String DATETIME_FORMAT_DDMMYYYY_HYPHEN = "dd-MM-yyyy HH:mm:ss";

    public final static String SHORT_DATE_FORMAT_YYYYMMDD_HYPHEN = "yyyy-MM-dd";
    public final static String SHORT_DATE_FORMAT_YYYYMMDD_SLASH = "yyyy/MM/dd";
    public final static String SHORT_DATE_FORMAT_DDMMYYYY_SLASH = "dd/MM/yyyy";
    public final static String SHORT_DATE_FORMAT_DDMMYYYY_HYPHEN = "dd-MM-yyyy";
    public final static String SHORT_DATE_FORMAT_MMDDYYYY_SLASH = "MM/dd/yyyy";
    public final static String SHORT_DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public final static String SHORT_DATE_FORMAT_DDMMYYYY = "ddMMyyyy";
    public final static String SHORT_DATETIME_FORMAT_STRING = "ddMMyyyyHHmmss";

    public final static String FORMAT_DOUBLE_VALUE = "#.#";
    public final static String COMMA = ",";
    public final static String COMMA_SPACE = ", ";
    public final static String SLASH = "/";
    public final static String VERTICAL_SLASH = "|";
    public final static String SPACE = " ";
    public final static String HYPHEN = "-";
    public final static String HYPHEN_SPACE = " - ";
    public final static String UNDERSCORE = "_";
    public final static String DOT = ".";
    public final static String CLONE = ":";
    public final static String SEMICOLON = ";";
    public final static String QUESTION_MARK = "?";

    public final static String ID = "id";
    public final static String ASC = "asc";
    public final static String DESC = "desc";

    public final static int EMAIL_MAX_LENGTH_DEFAULT = 100;
    public final static int LOG_BODY_MAX_PAYLOAD_LENGTH = 100_000;

    public final static int PAGE_SIZE_DEFAULT = 20;
    public final static int MAX_PAGE_SIZE_DEFAULT = 200;

    public final static String APPLICATION_SLASH_JSON = "application/json";
    public final static String CUSTOM = "Custom";
    public final static String BEARER_TOKEN_PREFIX = "Bearer ";
    public final static String MSB = "MSB";

    public final static String STRING_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    public final static String STRING_UTC_ZONE_ID = "UTC";

    public final static String NAME_CLIENT_INTERNAL_SUFFIX = "-internal";
    public final static String KEY_GLOBAL_EXCEPTION_HANDLER_STATUS_CODE = "KEY_GLOBAL_EXCEPTION_HANDLER_STATUS_CODE";
    public final static String USER_ID = "USER_ID";
    public final static String SYSTEM = "SYSTEM";

    public final static String REQUEST_ID = "request-id";
    public final static String REQUEST_TIME = "request-time";
    public final static String RESPCODE_000 = "0";
    public final static String RESPCODE_0118 = "0118";
    public final static String RESPCODE_0114 = "0114";
    public final static String INFO_DEFAULT = "INFO";
    public final static String CUSTOMER_TYPE_RB = "RB";
    public final static Integer ZERO = 0;
    public final static String VNEID = "vneid";
    public final static String SUCCESS = "Success";
    public final static String VNEID_EMPTY = "VNEID_EMPTY";

    public interface Headers {
        String X_REQUEST_TIME = "x-request-time";
        String CONTENT_TYPE = "Content-Type";
        String X_REQUEST_ID = "x-request-id";
        String X_TRANSACTION_ID = "X-Transaction-ID";
        String X_REQUEST_ID_2 = "x-request-id2";
        String CHANNEL_ID = "channel-id";
        String REQUEST_TYPE = "request-type";
        String PATH_STR = "path-str";
        String AUTHORIZATION = "Authorization";
        String COOKIE = "cookie";
        String SET_COOKIE = "set-cookie";
        String X_API_KEY = "x-api-key";
        String MSB_API_KEY = "Msb-Api-Key";
        String PROXY_AUTHORIZATION = "proxy-authorization";
        String X_DEVICE_ID = "X-device-id";
        String ACCEPT_LANGUAGE = "Accept-Language";
        String TOKEN = "TOKEN";
    }

    public static class ResponseStatus {
        public static final int SUCCESS = 200;
    }
}
