package com.jeremydyer.health;

import com.codahale.metrics.health.HealthCheck;
import com.jeremydyer.RealHbaseFakeStoreConfiguration;
import com.jeremydyer.util.PhoenixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Dropwizard health check to make sure that the Hbase/Phoenix is online.
 *
 * Created by Jeremy Dyer on 7/15/15.
 */
public class PhoenixHealthCheck
        extends HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(PhoenixHealthCheck.class);

    private Connection connection = null;
    private RealHbaseFakeStoreConfiguration configuration = null;

    public PhoenixHealthCheck(RealHbaseFakeStoreConfiguration configuration) {
        this.configuration = configuration;

        String jdbc = PhoenixUtil.createJDBCConnectionStringFromConfig(configuration);
        try {
            connection = DriverManager.getConnection(jdbc);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            logger.error("Failed to establish connection to: " + jdbc);
        }
    }

    @Override
    protected Result check() throws Exception {

        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM RECEIPTS LIMIT 1");
            if (pst.execute()) {
                return Result.healthy();
            } else {
                return Result.unhealthy("Failed to query Phoenix Receipts table.");
            }
        } catch (SQLException sqlEx) {
            logger.warn("Phoenix Health Check has encountered errors!");
            return Result.unhealthy("Failed to connect to Phoenix");
        }
    }
}
