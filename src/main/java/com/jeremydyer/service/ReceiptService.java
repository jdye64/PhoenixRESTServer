package com.jeremydyer.service;

import com.jeremydyer.core.Receipt;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jdyer on 8/18/15.
 */
public interface ReceiptService {

    /**
     * Pulls a particular Receipt from the Hbase receipt store.
     *
     * @param transactionId
     *  Transaction ID of the Receipt that should be pulled from the Receipt store.
     *
     * @return
     *  Receipt object representation.
     */
    public Receipt getReceipt(long transactionId);

    /**
     * Searches for a particular receipt
     *
     * @param sku
     * @param transactionDate
     * @param price
     *
     * @return
     */
    public ArrayList<Receipt> searchReceipts(long sku, Date transactionDate, double price);

    /**
     * Gets a specific Receipt image based on the TransactionID
     *
     * @param transactionID
     * @return
     */
    public String getReceiptImage(long transactionID);
}
