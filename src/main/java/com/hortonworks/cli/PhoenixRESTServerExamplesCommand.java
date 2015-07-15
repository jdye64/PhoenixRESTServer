package com.hortonworks.cli;

import com.hortonworks.PhoenixRESTServerConfiguration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * CLI command for showing a few examples of how to interact with PhoenixRESTServer. More examples to come. Maybe even
 * an API to make things really easy?
 * 
 * Created by Jeremy Dyer on 7/15/15.
 */
public class PhoenixRESTServerExamplesCommand
    extends ConfiguredCommand<PhoenixRESTServerConfiguration> {

    final static Logger logger =LoggerFactory.getLogger(PhoenixRESTServerExamplesCommand.class);

    public PhoenixRESTServerExamplesCommand(String name, String description) {
        super(name, description);
    }

    @Override
    protected void run(Bootstrap<PhoenixRESTServerConfiguration> configurationBootstrap, Namespace namespace, PhoenixRESTServerConfiguration configuration) {

        HttpClient client = new DefaultHttpClient();
        try {
            //Create a table in phoenix, insert a few rows, query those rows, delete the table
            HttpPost post = new HttpPost("http://localhost:8080/phoenix/ddl");

            JSONObject json = new JSONObject();
            json.put("query", "create table jeremytest(id bigint not null primary key, name varchar);");
            StringEntity input = new StringEntity( json.toString());

            post.setEntity(input);
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                logger.info(line);
            }

            //Insert a few rows into the table.
            post = new HttpPost("http://localhost:8080/phoenix/dml");

            json = new JSONObject();
            json.put("query", "upsert into jeremytest values(1, 'Jeremy Dyer')");
            input = new StringEntity( json.toString());

            post.setEntity(input);
            response = client.execute(post);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = "";
            while ((line = rd.readLine()) != null) {
                logger.info(line);
            }


            //Select all of the data from the table.
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("query", String.valueOf("select * from jeremytest;")));
            String paramString = URLEncodedUtils.format(params, "utf-8");

            String url = "http://localhost:8080/phoenix/dml?" + paramString;
            logger.info("GET Request URL: " + url);
            HttpGet get = new HttpGet(url);

            response = client.execute(get);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = "";
            while ((line = rd.readLine()) != null) {
                logger.info(line);
            }
            
            //Delete the entry from the test database
            params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("query", String.valueOf("delete from jeremytest where name='Jeremy Dyer';")));
            paramString = URLEncodedUtils.format(params, "utf-8");

            url = "http://localhost:8080/phoenix/dml?" + paramString;
            logger.info("DELETE Request URL: " + url);
            HttpDelete delete = new HttpDelete(url);

            response = client.execute(delete);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = "";
            while ((line = rd.readLine()) != null) {
                logger.info(line);
            }


            //Select all of the data from the table.
            params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("query", String.valueOf("select * from jeremytest;")));
            paramString = URLEncodedUtils.format(params, "utf-8");

            url = "http://localhost:8080/phoenix/dml?" + paramString;
            logger.info("GET Request URL: " + url);
            get = new HttpGet(url);

            response = client.execute(get);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = "";
            while ((line = rd.readLine()) != null) {
                logger.info(line);
            }

            //Drop the test database
            params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("query", String.valueOf("drop table jeremytest;")));
            paramString = URLEncodedUtils.format(params, "utf-8");

            url = "http://localhost:8080/phoenix/ddl?" + paramString;
            delete = new HttpDelete(url);

            response = client.execute(delete);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            line = "";
            while ((line = rd.readLine()) != null) {
                logger.info(line);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
