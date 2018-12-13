package org.lszjaf.deeper.inits;

import org.lszjaf.deeper.baseBean.JdbcBean;
import org.lszjaf.deeper.utils.JdbcPool;
import org.lszjaf.deeper.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joybana
 * @date 2018-12-06
 */
public class InitDeeper {
    private final static Logger logger = LoggerFactory.getLogger(InitDeeper.class);
    public String filePath;

    public String username;

    public String password;

    public String url;

    public String driverClass;

    public int poolCoreSize;

    public int poolMaxSize;

    /**
     * init deeper frame configure.
     * include read resources file about .xml & init database relation configure at present
     */
    public void init() {
        //1.init deeperDatabase....
        logger.info("init deeperDatabase....");
        JdbcPool.initConnectionPool(new JdbcBean(username, password, url, driverClass,poolCoreSize,poolMaxSize));

        //2.init deeperXML....
        logger.info("init deeperXML....");
        XMLUtils.initXML(filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public int getPoolCoreSize() {
        return poolCoreSize;
    }

    public void setPoolCoreSize(int poolCoreSize) {
        this.poolCoreSize = poolCoreSize;
    }

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }
}
