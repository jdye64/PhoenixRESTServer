package com.jeremydyer;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.jeremydyer.cli.LoadRealHbaseFakeStoreExamplesCommand;
import com.jeremydyer.health.PhoenixHealthCheck;
import com.jeremydyer.resource.ReceiptResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Dropwizard bootstrap process that creates the dropwizard application
 *
 * Created by jdyer on 8/17/15.
 */
public class RealHbaseFakeStoreApplication
        extends Application<RealHbaseFakeStoreConfiguration> {

    public static void main(String[] args) throws Exception {
        new RealHbaseFakeStoreApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<RealHbaseFakeStoreConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
        bootstrap.addCommand(new LoadRealHbaseFakeStoreExamplesCommand("LoadExamples", "Load examples into Hbase for Demo application"));
    }

    @Override
    public void run(RealHbaseFakeStoreConfiguration configuration,
                    Environment environment) {

        //Non-static content requests should be accepted through "/api/*" URL formats
        environment.jersey().setUrlPattern("/api/*");

        final ReceiptResource receiptResource = new ReceiptResource(configuration);

        //Register the resources with Jersey
        environment.jersey().register(receiptResource);

        //Register the application health checks.
        environment.healthChecks().register("PhoenixConnectionHealthCheck", new PhoenixHealthCheck(configuration));

        //Enable the JMX metrics.
        MetricRegistry metricsRegistry = new MetricRegistry();
        final JmxReporter reporter = JmxReporter.forRegistry(metricsRegistry).build();
        reporter.start();
    }
}