package com.jeremydyer.service.impl;

import com.jeremydyer.RealHbaseFakeStoreConfiguration;
import com.jeremydyer.core.Receipt;
import com.jeremydyer.service.ReceiptService;
import com.jeremydyer.util.PhoenixUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * ReceiptService implementation using Hbase/Phoenix as the backend store.
 *
 * Created by jdyer on 8/18/15.
 */
public class ReceiptServicePhoenixImpl
    implements ReceiptService{

    private static final Logger logger = LoggerFactory.getLogger(ReceiptServicePhoenixImpl.class);

    private Connection connection = null;

    public ReceiptServicePhoenixImpl(RealHbaseFakeStoreConfiguration configuration) {
        String jdbcString = PhoenixUtil.createJDBCConnectionStringFromConfig(configuration);

        try {
            this.connection = DriverManager.getConnection(jdbcString);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }


    public String getReceiptImage(long transactionID) {
        logger.info("Retrieving Receipt image for TransactionID: " + transactionID);
        Receipt r = this.getReceipt(transactionID);
        return r.getBase64ImageString();
    }

    public Receipt getReceipt(long transactionId) {

        Receipt r = null;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM RECEIPT WHERE TRANSACTION_ID = ?");
            statement.setLong(1, transactionId);

            ResultSet rset = statement.executeQuery();

            while (rset.next()) {
                r = new Receipt();
                r.setTransactionID(rset.getLong("TRANSACTION_ID"));
                r.setSku(rset.getLong("SKU"));
                r.setPrice(rset.getDouble("PRICE"));
                r.setDate(rset.getDate("DATE"));
                //r.setBase64Image(Base64.encodeBase64(rset.getBytes("IMAGE")));
                r.setBase64ImageString(Base64.encodeBase64String(rset.getBytes("IMAGE")));

                break;
            }

            statement.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return r;
    }

    public ArrayList<Receipt> searchReceipts(long sku, Date transactionDate, double price) {

        ArrayList<Receipt> receipts = new ArrayList<Receipt>();

        try {

            java.sql.Date sqlDate = null;

            if (transactionDate != null) {
                sqlDate = new java.sql.Date(transactionDate.getTime());
            } else {
                System.out.println("System date was not present");
                sqlDate = new java.sql.Date(0);
            }


            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM RECEIPTS WHERE SKU = ? OR DATE = ? OR PRICE = ?");
            statement.setLong(1, sku);
            statement.setDate(2, sqlDate);
            statement.setDouble(3, price);

            ResultSet rset = statement.executeQuery();

            while (rset.next()) {
                Receipt r = new Receipt();
                r.setTransactionID(rset.getLong("TRANSACTION_ID"));
                r.setSku(rset.getLong("SKU"));
                r.setPrice(rset.getDouble("PRICE"));
                r.setDate(rset.getDate("DATE"));
                //r.setBase64Image(Base64.encodeBase64(rset.getBytes("IMAGE")));
                r.setBase64ImageString(Base64.encodeBase64String(rset.getBytes("IMAGE")));

                receipts.add(r);
            }

            statement.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return receipts;
    }
}
