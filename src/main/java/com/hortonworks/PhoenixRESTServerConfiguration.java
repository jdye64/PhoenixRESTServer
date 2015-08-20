package com.hortonworks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hortonworks.conf.PhoenixRESTServer;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * PhoenixRESTServer Configuration. Reads configurations from the user defined .yml file.
 *
 * Created by Jeremy Dyer on 7/13/15.
 */
public class PhoenixRESTServerConfiguration
        extends Configuration {

    @JsonProperty
    @NotNull
    @Valid
    private PhoenixRESTServer phoenixRESTServer;

    public PhoenixRESTServer getPhoenixRESTServer() {
        return phoenixRESTServer;
    }

    public void setPhoenixRESTServer(PhoenixRESTServer phoenixRESTServer) {
        this.phoenixRESTServer = phoenixRESTServer;
    }
}
