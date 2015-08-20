package com.jeremydyer.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Object that represents a singular Receipt object in the Hbase backing store.
 *
 * Created by jdyer on 8/18/15.
 */
public class Receipt
    implements Serializable {

    private long transactionID;
    private long sku;
    private Date date;
    private double price;
    private String base64ImageString;

    public Receipt() {
    }

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public long getSku() {
        return sku;
    }

    public void setSku(long sku) {
        this.sku = sku;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getBase64ImageString() {
        return base64ImageString;
    }

    public void setBase64ImageString(String base64ImageString) {
        this.base64ImageString = base64ImageString;
    }
}
