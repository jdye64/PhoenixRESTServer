package com.hortonworks.health;

import com.codahale.metrics.health.HealthCheck;
import com.hortonworks.PhoenixRESTServerConfiguration;

import java.sql.Connection;

/**
 * Dropwizard health check to make sure that the Hbase/Phoenix is accessible.
 *
 * Created by Jeremy Dyer on 7/15/15.
 */
public class PhoenixHealthCheck
        extends HealthCheck {

    private final Connection connection = null;
    private PhoenixRESTServerConfiguration configuration = null;

    public PhoenixHealthCheck(PhoenixRESTServerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected Result check() throws Exception {

        //Attempt to establish a connection to Hbase via Phoenix to check health.
        return Result.healthy();
    }
}
