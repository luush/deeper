package org.lszjaf.deeper.utils;

import org.lszjaf.deeper.common.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 创建对象
 *
 * @author Joybana
 * @date 2018-12-05
 */
public class ObjectUtils {
    private final static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

    /**
     * check object is basic type or a object
     *
     * @param object a object
     * @return 1 - basic type
     */
    public static int isGoon(Object object) {
        String simpleName = object.getClass().getSimpleName();
        return isGoon(simpleName);
    }

    /**
     * @param cls a class
     * @return 1 - basic type
     */
    public static int isGoon(Class cls) {
        String simpleName = cls.getSimpleName();
        return isGoon(simpleName);
    }


    public static int isGoon(String simpleName) {
        switch (simpleName) {
            case SystemConstants.INTEGER:
            case SystemConstants.BOOLEAN:
            case SystemConstants.INT:
            case SystemConstants.BOOLEAN_S:
            case SystemConstants.DOUBLE:
            case SystemConstants.STRING:
            case SystemConstants.CHARACTER:
            case SystemConstants.LONG:
            case SystemConstants.BYTE:
            case SystemConstants.SHORT:
            case SystemConstants.FLOAT:
            case SystemConstants.DOUBLE_S:
            case SystemConstants.CHAR:
            case SystemConstants.LONG_S:
            case SystemConstants.BYTE_S:
            case SystemConstants.SHORT_S:
            case SystemConstants.FLOAT_S:
                return 1;
            case SystemConstants.C_LIST:
            case SystemConstants.C_ARRAYLIST:
            case SystemConstants.C_LINKEDLIST:
            case SystemConstants.C_ABSTRACTLIST:
            case SystemConstants.C_ABSTRACTSEQUENTIALLIST:
                return 2;
            case SystemConstants.C_SET:
            case SystemConstants.C_HASHSET:
            case SystemConstants.C_TREESET:
            case SystemConstants.C_ABSTRACTSET:
            case SystemConstants.C_SORTEDSET:
                return 3;
            default:
                return -1;
        }
    }

}
