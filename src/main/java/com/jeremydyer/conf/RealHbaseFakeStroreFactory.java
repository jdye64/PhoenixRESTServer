package com.jeremydyer.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by Jeremy Dyer on 7/14/15.
 */
@JsonTypeName("phoenix")
public class RealHbaseFakeStroreFactory
    implements RealHbaseFakeStore {

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
        if (host == null) {
            return "localhost";
        } else {
            return host;
        }
    }

    public int port() {
        return 2181;
    }

    public String hbaseDatabase() {
        if (hbaseDatabase == null) {
            return "/hbase-unsecure";
        } else {
            return hbaseDatabase;
        }
    }

}
