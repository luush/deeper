package org.lszjaf.deeper.utils;

import org.lszjaf.deeper.baseBean.JdbcBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Joybana
 * @date 2018-12-06
 */
public class JdbcPool {
    private final static Logger logger = LoggerFactory.getLogger(JdbcPool.class);

    private static int poolCoreSize = 3;

    private static int poolMaxSize = 6;

    private static int realSize = 3;

    private static List<Connection> connectionPool;

    private static String username;

    private static String password;

    private static String url;

    private static String driverClass;

    static {
        connectionPool = Collections.synchronizedList(new LinkedList<Connection>());
    }

    public static void initConnectionPool(JdbcBean jdbcBean) {
        if (jdbcBean == null) {
            throw new ExceptionInInitializerError("Deeper can't obtain database message,initConnectionPool failed!");
        }
        //init filed
        url = jdbcBean.getUrl();
        username = jdbcBean.getUsername();
        password = jdbcBean.getPassword();
        driverClass = jdbcBean.getDriverClass();
        if (jdbcBean.getPoolCoreSize() > 0) {
            poolCoreSize = jdbcBean.getPoolCoreSize();
            realSize = poolCoreSize;
            poolMaxSize = 2 * poolCoreSize;
            if (jdbcBean.getPoolMaxSize() > 0) {
                poolMaxSize = jdbcBean.getPoolMaxSize();
            }

        }

        try {

            // load the jdbc driver
            Class.forName(driverClass);
            //build connection
            for (int i = 0; i < poolCoreSize; i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                connection.setAutoCommit(false);
                connectionPool.add(connection);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * once use , just to get or create
     *
     * @return
     */
    public static synchronized Connection getConnection() throws SQLException {
        //indicate there are some room to create connection
        if (connectionPool.size() <= 0 && realSize < poolMaxSize) {
            realSize++;
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            return connection;
            //clearly speaking,there is no more room to use or create
        } else if (connectionPool.size() <= 0 && realSize >= poolMaxSize) {
            logger.error("database is busy,can't get the connection!cause there is no more room to use or create!");
            throw new SQLException("database connection is busy,this operation is failed!");
        }
        return connectionPool.remove(0);
    }


    /**
     * after used,give it back to connection pool or close it
     *
     * @param connection
     */
    public static synchronized void giveBack(Connection connection) throws SQLException {
        commit(connection);//commit affair
        if (connectionPool.size() >= poolCoreSize) {
            close(connection);
            realSize--;
            //modify by Joybana in 2018-12-10 cause the serious wrong that is No operations allowed after connection closed!
            return;
        }
        connectionPool.add(connection);
    }

    /**
     * close database connection
     *
     * @param connection
     */
    public static void close(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            // roll back after commit had failed
            connection.rollback();
        }
    }

    /**
     * commit affair
     *
     * @param connection
     */
    public static void commit(Connection connection) throws SQLException {
        if (connection == null) {
            return;
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
        }
    }

    /**
     * rollback affair
     *
     * @param connection
     */
    public static void rollback(Connection connection) throws SQLException {
        if (connection == null) {
            return;
        }
        connection.rollback();
    }
}
