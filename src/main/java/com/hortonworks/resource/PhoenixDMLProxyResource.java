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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * REST proxy for Phoenix Data Manipulation Language (DML). Takes REST requests and translates them
 * into the appropriate JDBC calls to Phoenix.
 *
 * Created by Jeremy Dyer on 7/13/15.
 */
@Path("/phoenix/dml")
@Produces(MediaType.APPLICATION_JSON)
public class PhoenixDMLProxyResource {

    private static final Logger logger = LoggerFactory.getLogger(PhoenixDMLProxyResource.class);

    private ComboPooledDataSource cpds = null;

    public PhoenixDMLProxyResource(PhoenixRESTServerConfiguration configuration) throws PropertyVetoException {

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
    public String createDML(String jsonBody) {

        logger.debug(("createDML Raw JSON: " + jsonBody));
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
    public String deleteDML(@QueryParam("query") String query) {

        Connection conn;
        long start = System.currentTimeMillis();
        System.out.println("DELETE Query: " + query);

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


    @GET
    @Timed
    public String query(@QueryParam("query") String query) {

        Connection conn;
        long start = System.currentTimeMillis();
        logger.debug("Query: " + query);

        JSONObject json = new JSONObject();
        json.put("query", query);

        //JDBC driver doesn't like semicolons -- remove it if necessary
        query = PhoenixQueryUtil.removeTailingSemiColon(query);

        ArrayList<String[]> rows = new ArrayList<String[]>();
        try
        {
            conn = cpds.getConnection();
            ResultSet rst = conn.createStatement().executeQuery(query);

            //Write column headers
            ResultSetMetaData rms = rst.getMetaData();
            String[] cols = new String[rms.getColumnCount()];
            for (int i=0; i < cols.length; i++){ cols[i] = rms.getColumnName(i+1);}
            json.put("columns", cols);

            //Write each row from the result set
            while (rst.next()) {
                String[] row = new String[cols.length];
                for (int i=0; i < cols.length; i++){ row[i] = rst.getString(i+1); }
                rows.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        json.put("result", rows);
        json.put("time_ms", new Long(System.currentTimeMillis() - start).toString());

        return json.toString();
    }
}