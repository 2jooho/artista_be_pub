package com.artista.main.global.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReplaceStringUtil {
    private static final String REPLACE_STRING_NR = "[\r\n]";

    public static String replaceStringCRLF (Object obj) {

        if (obj == null) {
            return null;
        }

        String retStr = null;
        if(obj instanceof Map) {
            retStr = obj.toString().replaceAll(REPLACE_STRING_NR, "");
        } else if(obj instanceof List) {
            retStr = obj.toString().replaceAll(REPLACE_STRING_NR, "");
        } else if(obj instanceof String) {
            retStr = obj.toString().replaceAll(REPLACE_STRING_NR, "");
        } else if(obj instanceof Exception) {
            retStr = Arrays.toString(((Exception) obj).getStackTrace()).replaceAll(REPLACE_STRING_NR, "");
        }
        return retStr;
    }
}
