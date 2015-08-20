package com.jeremydyer.cli;

import com.jeremydyer.RealHbaseFakeStoreConfiguration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * If you want to try out the "Jeremy's Bait Shop" demo this command will load up some
 * example in the Phoenix Hbase table.
 *
 * Created by jdyer on 8/17/15.
 */
public class LoadRealHbaseFakeStoreExamplesCommand
        extends ConfiguredCommand<RealHbaseFakeStoreConfiguration> {

    final static Logger logger = LoggerFactory.getLogger(LoadRealHbaseFakeStoreExamplesCommand.class);

    public LoadRealHbaseFakeStoreExamplesCommand(String name, String description) {
        super(name, description);
    }

    @Override
    protected void run(Bootstrap<RealHbaseFakeStoreConfiguration> configurationBootstrap, Namespace namespace, RealHbaseFakeStoreConfiguration configuration) {

        try {
            logger.info("Loading sample receipts into Hbase");

            Connection con = DriverManager.getConnection("jdbc:phoenix:sandbox.hortonworks.com:/hbase-unsecure");

            //Creates the Receipts Table if it doesn't exist.
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("ddl/ReceiptImageDBBootstrap.ddl");
            String ddlSQL = IOUtils.toString(is);
            logger.info("Running DDL: " + ddlSQL);
            PreparedStatement pst = con.prepareStatement(ddlSQL);
            pst.executeUpdate();


            // SKU 1234 - Fishing Pole Receipt
            is = this.getClass().getClassLoader().getResourceAsStream("receipts/SKU_1234.jpg");
            byte[] imageBytes = IOUtils.toByteArray(is);
            pst = con.prepareStatement("UPSERT INTO RECEIPTS(TRANSACTION_ID, SKU, PRICE, DATE, IMAGE) VALUES(?, ?, ?, ?, ?)");
            pst.setLong(1, System.currentTimeMillis());
            pst.setLong(2, 1234);
            pst.setDouble(3, 76.95);
            pst.setDate(4, new Date(System.currentTimeMillis()));
            pst.setBytes(5, imageBytes);
            pst.executeUpdate();

            // SKU 7896 - Mepps Spinning trout lure Receipt
            is = this.getClass().getClassLoader().getResourceAsStream("receipts/SKU_7896.jpg");
            imageBytes = IOUtils.toByteArray(is);
            pst = con.prepareStatement("UPSERT INTO RECEIPTS(TRANSACTION_ID, SKU, PRICE, DATE, IMAGE) VALUES(?, ?, ?, ?, ?)");
            pst.setLong(1, System.currentTimeMillis());
            pst.setLong(2, 7896);
            pst.setDouble(3, 3.49);
            pst.setDate(4, new Date(System.currentTimeMillis()));
            pst.setBytes(5, imageBytes);
            pst.executeUpdate();

            // SKU 5467 - Orvis Fishing Vest Receipt
            is = this.getClass().getClassLoader().getResourceAsStream("receipts/SKU_5467.jpg");
            imageBytes = IOUtils.toByteArray(is);
            pst = con.prepareStatement("UPSERT INTO RECEIPTS(TRANSACTION_ID, SKU, PRICE, DATE, IMAGE) VALUES(?, ?, ?, ?, ?)");
            pst.setLong(1, System.currentTimeMillis());
            pst.setLong(2, 5467);
            pst.setDouble(3, 36.99);
            pst.setDate(4, new Date(System.currentTimeMillis()));
            pst.setBytes(5, imageBytes);
            pst.executeUpdate();

            // SKU 1112 - Reddington Fishing Waders Receipt
            is = this.getClass().getClassLoader().getResourceAsStream("receipts/SKU_1112.jpg");
            imageBytes = IOUtils.toByteArray(is);
            pst = con.prepareStatement("UPSERT INTO RECEIPTS(TRANSACTION_ID, SKU, PRICE, DATE, IMAGE) VALUES(?, ?, ?, ?, ?)");
            pst.setLong(1, System.currentTimeMillis());
            pst.setLong(2, 1112);
            pst.setDouble(3, 96.99);
            pst.setDate(4, new Date(System.currentTimeMillis()));
            pst.setBytes(5, imageBytes);
            pst.executeUpdate();

            con.commit();
            pst.close();
            con.close();

            logger.info("Done loading sample receipts into HBase");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
