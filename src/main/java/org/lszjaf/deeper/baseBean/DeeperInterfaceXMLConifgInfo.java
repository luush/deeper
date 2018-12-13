package org.lszjaf.deeper.baseBean;

import java.io.Serializable;
import java.util.Arrays;
/**
 * @author Joybana
 * @date 2018-12-06
 */
public class DeeperInterfaceXMLConifgInfo implements Serializable {
    private String jdbcType;
    private String methodId;
    private String sql;
    private String returnType;
    private String[] paramsType;

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(String[] paramsType) {
        this.paramsType = paramsType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public void updateSql(){

    }

    @Override
    public String toString() {
        return "DeeperInterfaceXMLConifgInfo{" +
                "jdbcType='" + jdbcType + '\'' +
                ", methodId='" + methodId + '\'' +
                ", sql='" + sql + '\'' +
                ", returnType='" + returnType + '\'' +
                ", paramsType=" + Arrays.toString(paramsType) +
                '}';
    }
}
