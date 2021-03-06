package br.com.markus.model;

import br.com.markus.enuns.LogTypeEnum;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by Markus on 28/07/2015.
 */
public final class LogData {
    public static final String APP_CODE = "appCode";
    public static final String TIMESTAMP = "timestamp";
    public static final String LOG_TYPE = "logType";
    public static final String DATA_LOGGED = "dataLogged";
    public static final String CUSTUMER_ID = "custumerID";

    private String appCode;
    private Date timestamp;
    private LogTypeEnum logType;
    private String dataLogged;
    private String custumerID;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public LogTypeEnum getLogType() {
        return logType;
    }

    public void setLogType(LogTypeEnum logType) {
        this.logType = logType;
    }

    public String getDataLogged() {
        return dataLogged;
    }

    public void setDataLogged(String dataLogged) {
        this.dataLogged = dataLogged;
    }

    public String getCustumerID() {
        return custumerID;
    }

    public void setCustumerID(String custumerID) {
        this.custumerID = custumerID;
    }

    @Override
    public String toString() {
        return "{" +
                APP_CODE +"='" + appCode + '\'' +
                ","+ TIMESTAMP +"='" + timestamp.getTime() + '\'' +
                ","+ LOG_TYPE +"='" +  logType.getDescription() + '\'' +
                ","+ DATA_LOGGED +"='" +  dataLogged + '\'' +
                ","+ CUSTUMER_ID +"='" +  custumerID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LogData logData = (LogData) o;

        return new EqualsBuilder()
                .append(getAppCode(), logData.getAppCode())
                .append(getTimestamp(), logData.getTimestamp())
                .append(getLogType(), logData.getLogType())
                .append(getDataLogged(), logData.getDataLogged())
                .append(getCustumerID(), logData.getCustumerID())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAppCode())
                .append(getTimestamp())
                .append(getLogType())
                .append(getDataLogged())
                .append(getCustumerID())
                .toHashCode();
    }
}
