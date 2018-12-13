package org.lszjaf.deeper.baseBean;

import java.io.Serializable;

/**
 * @author Joybana
 * @date 2018-12-06
 */
public class JdbcBean implements Serializable {
    private String username;

    private String password;

    private String url;

    private String driverClass;

    private int poolCoreSize;

    private int poolMaxSize;

    public JdbcBean() {
    }

    public JdbcBean(String username, String password, String url, String driverClass,
                    int poolCoreSize, int poolMaxSize) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driverClass = driverClass;
        this.poolCoreSize = poolCoreSize;
        this.poolMaxSize = poolMaxSize;
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
