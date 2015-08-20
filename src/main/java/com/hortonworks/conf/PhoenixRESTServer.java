package com.hortonworks.conf;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * Created by jeremydyer on 7/14/15.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface PhoenixRESTServer
        extends Discoverable {

    String username();
    String password();
    String host();
    int port();
    String hbaseDatabase();
}
