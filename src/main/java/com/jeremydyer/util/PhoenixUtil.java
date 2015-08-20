package com.jeremydyer.util;

import com.jeremydyer.RealHbaseFakeStoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to assist in connections to Phoenix.
 *
 * Created by jdyer on 8/20/15.
 */
public class PhoenixUtil {

    private static final Logger logger = LoggerFactory.getLogger(PhoenixUtil.class);

    /**
     * Creates the Phoenix JDBC connection string from the configuration object instance.
     *
     * @param conf
     *  Configuration object.
     *
     * @return
     *  JDBC connection URL.
     */
    public static String createJDBCConnectionStringFromConfig(RealHbaseFakeStoreConfiguration conf) {
        StringBuilder sb = new StringBuilder();

        sb.append("jdbc:phoenix:");
        sb.append(conf.getPhoenixRESTServer().host());
        sb.append(":");
        sb.append(conf.getPhoenixRESTServer().port());
        sb.append(":");
        sb.append(conf.getPhoenixRESTServer().hbaseDatabase());

        logger.info("Phoenix JDBC Connection String: " + sb.toString());

        return sb.toString();
    }
}
