package com.jeremydyer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeremydyer.conf.RealHbaseFakeStore;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by jdyer on 8/17/15.
 */
public class RealHbaseFakeStoreConfiguration
        extends Configuration {

    @JsonProperty
    @NotNull
    @Valid
    private RealHbaseFakeStore phoenixImageServer;

    public RealHbaseFakeStore getPhoenixRESTServer() {
        return phoenixImageServer;
    }

    public void setPhoenixRESTServer(RealHbaseFakeStore phoenixImageServer) {
        this.phoenixImageServer = phoenixImageServer;
    }
}
