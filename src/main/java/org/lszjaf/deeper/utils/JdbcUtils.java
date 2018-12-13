package org.lszjaf.deeper.utils;

import org.lszjaf.deeper.common.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * @author Joybana
 * @version 1.0.5
 * @date 2018-12-10
 */
public class JdbcUtils {
    private final static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    public static <T> T insert(String sql, Class<T> resultType, Connection connection
            , PreparedStatement preparedStatement) throws SQLException {
        return idu(preparedStatement, connection, resultType);
    }

    public static <T> T delete(String sql, Class<T> resultType, Connection connection
            , PreparedStatement preparedStatement) throws SQLException {
        return idu(preparedStatement, connection, resultType);
    }

    public static <T> T update(String sql, Class<T> resultType, Connection connection
            , PreparedStatement preparedStatement) throws SQLException {
        return idu(preparedStatement, connection, resultType);
    }

    public static <T> T select(String sql, Class<T> resultType, Connection connection,
                               PreparedStatement preparedStatement, Class<T> returnType) throws Exception {

        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> list = null;
        Object obj = null;

        if (resultType != returnType) {
            if (SystemConstants.C_LIST.equals(returnType.getSimpleName())) {
                List result = new ArrayList();
                subPacking(resultType, resultSet, sql, list, obj, result);
                return (T) result;

            } else if (SystemConstants.C_SET.equals(returnType.getSimpleName())) {
                Set set = new HashSet();
                subPacking(resultType, resultSet, sql, list, obj, set);
                return (T) set;
            } else {
                if (ObjectUtils.isGoon(resultType) == 1) {
                    while (resultSet.next()) {
                        return resultSet.getObject(1, resultType);
                    }
                } else {
                    //TODO need optimize
                    return null;
                }
            }


        } else {
            if (ObjectUtils.isGoon(resultType) == 1) {
                while (resultSet.next()) {
                    return resultSet.getObject(1, resultType);
                }
            } else {
                // if the return type is a object,get it
                return packing(list, sql, resultSet, obj, resultType, null);
            }
        }

        return null;
    }


    private static void subPacking(Class resultType, ResultSet resultSet, String sql
            , List<String> list, Object obj, Collection collection) throws Exception {
        if (ObjectUtils.isGoon(resultType) == 1) {
            while (resultSet.next()) {
                collection.add(resultSet.getObject(1, resultType));
            }
        } else {
            // if the return type is a object,get it
            packing(list, sql, resultSet, obj, resultType, collection);
        }
    }

    private static <T> T packing(List<String> list, String sql, ResultSet resultSet, Object obj
            , Class<T> resultType, Collection collection) throws Exception {
        // if the return type is a object,get it
        list = StringUtils.parseSuXing(sql);
        while (resultSet.next()) {
            int column = list.size();
            obj = resultType.newInstance();

            for (int i = 0; i < column; i++) {
                Method methodget = resultType.getMethod(SystemConstants.GET + StringUtils.toUpperCaseFirst(list.get(i)), null);
                Method method = resultType.getMethod(SystemConstants.SET + StringUtils.toUpperCaseFirst(list.get(i)), methodget.getReturnType());
                method.invoke(obj, resultSet.getObject(i + 1, methodget.getReturnType()));
            }
            if (collection == null) {
                continue;
            }
            collection.add(obj);
        }
        if (collection == null) {
            return (T) obj;
        }
        return null;
    }

    private static <T> T idu(PreparedStatement preparedStatement, Connection connection
            , Class<T> resultType) throws SQLException {
        Integer row = preparedStatement.executeUpdate();
        preparedStatement.close();
        if (SystemConstants.BOOLEAN.equals(resultType.getSimpleName())
                || SystemConstants.BOOLEAN_S.equals(resultType.getSimpleName())) {
            Boolean flag = row >= 1;
            return (T) flag;
        }
        if (SystemConstants.VOID.equals(resultType.getSimpleName())) {
            return (T) resultType;
        }
        return (T) row;

    }
}
