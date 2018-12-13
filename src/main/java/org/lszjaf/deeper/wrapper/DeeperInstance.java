package org.lszjaf.deeper.wrapper;

import org.lszjaf.deeper.core.DeeperCoreHandler;

import java.lang.reflect.Proxy;
/**
 * @author Joybana
 * @date 2018-12-06
 */
public class DeeperInstance {

    public static <T> T newInstance(Class<T> cClass) {
        return (T) Proxy.newProxyInstance(DeeperInstance.class.getClassLoader(), new Class[]{cClass}, new DeeperCoreHandler());
    }
}
