package com.hortonworks;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.hortonworks.cli.PhoenixRESTServerExamplesCommand;
import com.hortonworks.health.PhoenixHealthCheck;
import com.hortonworks.resource.PhoenixDDLProxyResource;
import com.hortonworks.resource.PhoenixDMLProxyResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.beans.PropertyVetoException;

/**
 * PhoenixRESTServer Applicatino entry
 *
 * Created by Jeremy Dyer on 7/13/15.
 */
public class PhoenixRESTServerApplication
        extends Application<PhoenixRESTServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new PhoenixRESTServerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<PhoenixRESTServerConfiguration> bootstrap) {
        bootstrap.addCommand(new PhoenixRESTServerExamplesCommand("restexample", "Demonstrate some simple rest calls and how they map to regular database calls"));
    }

    @Override
    public void run(PhoenixRESTServerConfiguration configuration,
                    Environment environment) {

        try {
            final PhoenixDMLProxyResource resource = new PhoenixDMLProxyResource(configuration);
            final PhoenixDDLProxyResource ddlResource = new PhoenixDDLProxyResource(configuration);

            //Register the resources with Jersey
            environment.jersey().register(resource);
            environment.jersey().register(ddlResource);
        } catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }

        //Register the application health checks.
        environment.healthChecks().register("PhoenixConnection", new PhoenixHealthCheck(configuration));

        //Enable the JMX metrics.
        MetricRegistry metricsRegistry = new MetricRegistry();
        final JmxReporter reporter = JmxReporter.forRegistry(metricsRegistry).build();
        reporter.start();
    }
}