package com.jeremydyer.conf;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * Configuration for the Phoenix Receipt Image Application
 *
 * Created by jeremydyer on 7/14/15.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface RealHbaseFakeStore
        extends Discoverable {
    /**
     * Username for the Phoenix connection.
     * @return
     */
    String username();

    /**
     * Password for the Phoenix connection.
     * @return
     */
    String password();

    /**
     * Hbase Master Service host used to build the JDBC connection to Hbase from Phoenix
     * @return
     */
    String host();

    /**
     * Hbase Master Service port used to build the JDBC connection to Hbase from Phoenix
     * @return
     */
    int port();

    /**
     * Zookeeper Z-Node used for the Hbase connection via Phoenix. For secure connection with Kerberos use /hbase
     * for unsecured connection without kerberos use /hbase-unsecure
     *
     * @return
     */
    String hbaseDatabase();
}
