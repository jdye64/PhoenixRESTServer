package com.hortonworks.resource;

import com.codahale.metrics.annotation.Timed;
import com.hortonworks.PhoenixRESTServerConfiguration;
import com.hortonworks.util.PhoenixQueryUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Map REST requests into their correlating Phoenix commands.
 *
 * Created by Jeremy Dyer on 7/15/15.
 */
@Path("/phoenix/ddl")
@Produces(MediaType.APPLICATION_JSON)
public class PhoenixDDLProxyResource {

    private static final Logger logger = LoggerFactory.getLogger(PhoenixDMLProxyResource.class);

    private ComboPooledDataSource cpds = null;

    public PhoenixDDLProxyResource(PhoenixRESTServerConfiguration configuration) throws PropertyVetoException {

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "org.apache.phoenix.jdbc.PhoenixDriver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:phoenix:" + configuration.getPhoenixRESTServer().host() + ":" + configuration.getPhoenixRESTServer().port() + ":/" + configuration.getPhoenixRESTServer().hbaseDatabase());

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(25);

    }

    @POST
    @Timed
    public String createDDL(String jsonBody) {

        logger.debug("Raw JSON: " + jsonBody);
        JSONObject requestJson = new JSONObject(jsonBody);
        String query = requestJson.getString("query");

        Connection conn;
        long start = System.currentTimeMillis();
        logger.debug("Query: " + query);

        JSONObject json = new JSONObject();
        json.put("query", query);

        //JDBC driver doesn't like semicolons -- remove it if necessary
        query = PhoenixQueryUtil.removeTailingSemiColon(query);

        try
        {
            conn = cpds.getConnection();
            json.put("response", conn.createStatement().executeUpdate(query));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        json.put("time_ms", new Long(System.currentTimeMillis() - start).toString());

        return json.toString();
    }

    @DELETE
    @Timed
    public String deleteDDL(@QueryParam("query") String query) {

        Connection conn;
        long start = System.currentTimeMillis();
        logger.debug("Delete Query: " + query);

        JSONObject json = new JSONObject();
        json.put("query", query);

        //JDBC driver doesn't like semicolons -- remove it if necessary
        query = PhoenixQueryUtil.removeTailingSemiColon(query);

        try
        {
            conn = cpds.getConnection();
            json.put("response", conn.createStatement().executeUpdate(query));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        json.put("time_ms", new Long(System.currentTimeMillis() - start).toString());

        return json.toString();
    }
}
