package org.lszjaf.deeper.utils;

import org.lszjaf.deeper.common.SystemConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Joybana
 * @date 2018-12-06
 */
public class StringUtils {


    /**
     * get parameters's numbers in specified sql
     *
     * @param sql
     * @return
     */
    public static int getParamsNumsInSql(String sql) {
        if (sql == null || sql.trim().isEmpty() || !sql.contains(SystemConstants.PARAMS_SIGN1)) {
            return -1;
        }
        int length = sql.length();
        int alength = length - sql.replaceAll(SystemConstants.PARAMS_FORMATE1, SystemConstants.EMPTY).length();
        return alength / 2;
    }

    /**
     * parse sql ,replace ${xxx} to ?
     *
     * @param sql
     * @return
     */
    public static String parseSqlWhere(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }
        return sql.replaceAll(SystemConstants.PARAMS_FORMATE2, SystemConstants.PARAMS_REPLACE);
    }

    public static String toUpperCaseFirst(String string) {
        return string.replaceFirst(string.substring(0, 1), string.substring(0, 1).toUpperCase());
    }

    /**
     * get the field from select string
     *
     * @param string
     * @return
     */
    public static List<String> parseSuXing(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        string = string.trim();
        String ss = string.substring(string.indexOf(SystemConstants.BLANK_SPACE)).trim();
        String[] result = ss.split(SystemConstants.COMMA);
        List<String> list = new ArrayList<>(result.length);
        for (String s : result) {
            if (s.contains(SystemConstants.BLANK_SPACE)) {
                s = s.substring(0, s.indexOf(SystemConstants.BLANK_SPACE));
            }
            list.add(s.trim());
        }
        return list;
    }


    public static List<String> parseUpdate(String sql) {
        String[] ss = sql.split(SystemConstants.PARAMS_FORMATE3);
        List<String> list = new ArrayList<>();
        for (String s : ss) {
            if (!s.contains(SystemConstants.PARAMS_SIGN3)) {
                continue;
            }
            s = s.substring(0, s.indexOf(SystemConstants.PARAMS_SIGN3));
            list.add(s);
        }
        return list;
    }
}
