package org.lszjaf.deeper.core;

import org.lszjaf.deeper.baseBean.DeeperInterfaceXMLConifgInfo;
import org.lszjaf.deeper.common.SystemConstants;
import org.lszjaf.deeper.utils.JdbcUtils;
import org.lszjaf.deeper.utils.ObjectUtils;
import org.lszjaf.deeper.utils.StringUtils;
import org.lszjaf.deeper.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Joybana
 * @version 1.0.9
 * @date 2018-12-07
 */
public class DeeperDealSql {

    private final static Logger logger = LoggerFactory.getLogger(DeeperDealSql.class);

    public static Object dealSql(Method method, Object[] args, Connection connection) throws Throwable {
        //1.get the object about deeper interface's configure from the xml file.
        DeeperInterfaceXMLConifgInfo dixmlci = XMLUtils.parseXml(method.getDeclaringClass().getName(), method.getName());

        Class resultType = method.getReturnType();
        logger.info("test..........{}", resultType);
        if (dixmlci == null) {
            throw new Exception("there is no found about " + method.getName() + " relation message!");
        }
        Object[] params = null;
        int size = -1;
        //2.parse the sql.it's parameters's name & related value
        if (args.length > 1) {// more parameters
            params = args;
            size = args.length;
            return executeSql(dixmlci, connection, size, params, resultType);
        } else {//just one parameter
            return prepareData(args[0], dixmlci, connection, resultType, method);
        }
    }


    private static <T> T prepareData(Object object, DeeperInterfaceXMLConifgInfo dixmlci
            , Connection connection, Class<T> resultType, Method method) throws Throwable {
        Object[] params = null;
        int size = -1;

        int mark = ObjectUtils.isGoon(object);
        switch (mark) {
            case 1: // a basic type
                params = new Object[1];
                params[0] = object;
                size = StringUtils.getParamsNumsInSql(dixmlci.getSql());
                return (T) executeSql(dixmlci, connection, size, params, resultType);
            case 2: // a list type
            case 3: // a set type
                Collection set = (Collection) object;
                Object[] sets = set.toArray();
                return checkReturn(sets, resultType, dixmlci, connection, method);
            case -1: // a object not include list, set and so on...
                //2.parse the parameters's name from the sql in the deeperInterfaceXMLConifgInfo object.
                List<String> list = StringUtils.parseUpdate(dixmlci.getSql());
                if (list == null || list.size() <= 0) {
                    throw new Exception("there is no found about " + method.getName() + " parameter message!");
                }
                Class cls = object.getClass();

                int sizes = list.size();
                params = new Object[sizes];
                for (int j = 0; j < sizes; j++) {
                    Method method1 = cls.getMethod(SystemConstants.GET + StringUtils.toUpperCaseFirst(list.get(j)), null);
                    Object value = method1.invoke(object, null);//获取sql参数对应的值
                    params[j] = value;
                }
                size = sizes;
                return (T) executeSql(dixmlci, connection, size, params, resultType);
            default: // a error hit
                throw new Exception("there is no match about " + method.getName() + " parameters type!");
        }

    }


    private static <T> T checkReturn(Object[] parms, Class<T> resultType, DeeperInterfaceXMLConifgInfo dixmlci
            , Connection connection, Method method) throws Throwable {
        int length = parms.length;
        Set results = new HashSet(length);
        T o = null;
        //check the result type
        if (SystemConstants.BOOLEAN.equals(resultType.getSimpleName())
                || SystemConstants.BOOLEAN_S.equals(resultType.getSimpleName())) {
            T flag = null;
            for (int i = 0; i < length; i++) {
                o = prepareData(parms[i], dixmlci, connection, resultType, method);
                //TODO need optimize
                results.add(o);
                if (results.size() > 1) {//express it occurs fail in here
                    logger.error("this message {} is executed failed!", parms[i]);
                    flag = o;
                    results.remove(o);
                }
            }
            if (flag != null) {
                return flag;
            }
            return o;
        } else if (SystemConstants.INTEGER.equals(resultType.getSimpleName())
                || SystemConstants.INT.equals(resultType.getSimpleName())) {
            Integer sum = 0;
            for (int i = 0; i < length; i++) {
                o = prepareData(parms[i], dixmlci, connection, resultType, method);
                //TODO need optimize
                results.add(o);
                if (results.size() > 1) {//express it occurs fail in here
                    logger.error("this message {} is executed failed!", parms[i]);
                    sum++;
                    results.remove(o);
                }

            }
            return (T) Integer.valueOf(length - sum);
        }

        return null;
    }


    private static Object executeSql(DeeperInterfaceXMLConifgInfo dixmlci, Connection connection, int size
            , Object[] params, Class resultType) throws Throwable {
        //3.obtain the sql type.likely select,insert and so on.
        String jdbcType = dixmlci.getJdbcType();

        //4.get the sql that preparedStatement can execute
        String sql = StringUtils.parseSqlWhere(dixmlci.getSql());
        logger.info(sql);

        //5.set sql's parameters
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < size; i++) {//init every parameter
            preparedStatement.setObject(i + 1, params[i]);
        }

        //6.execute sql depend on the jdbcType
        switch (jdbcType) {
            case SystemConstants.SELECT:
                //get the return type
                String returnType = dixmlci.getReturnType();
                Class rType = Void.class;
                if (returnType != null) {
                    rType = Class.forName(returnType);
                }
                return JdbcUtils.select(sql, rType, connection, preparedStatement, resultType);
            case SystemConstants.INSERT:
                return JdbcUtils.insert(sql, resultType, connection, preparedStatement);
            case SystemConstants.UPDATE:
                return JdbcUtils.update(sql, resultType, connection, preparedStatement);
            case SystemConstants.DELETE:
                return JdbcUtils.delete(sql, resultType, connection, preparedStatement);
            default:
                //exception
                throw new Exception("there is no this " + jdbcType + " type operation!");
        }
    }
}
