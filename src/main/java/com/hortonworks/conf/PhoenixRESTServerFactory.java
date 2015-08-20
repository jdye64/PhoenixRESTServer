package com.hortonworks.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by Jeremy Dyer on 7/14/15.
 */
@JsonTypeName("phoenix")
public class PhoenixRESTServerFactory
    implements PhoenixRESTServer {

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private String host;

    @JsonProperty
    private int port;

    @JsonProperty
    private String hbaseDatabase;

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    public String host() {
        return "localhost";
    }

    public int port() {
        return 2181;
    }

    public String hbaseDatabase() { return "hbase-unsecure"; };

}
