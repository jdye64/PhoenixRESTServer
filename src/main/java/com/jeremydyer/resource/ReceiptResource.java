package com.jeremydyer.resource;

import com.codahale.metrics.annotation.Timed;
import com.jeremydyer.RealHbaseFakeStoreConfiguration;
import com.jeremydyer.core.Receipt;
import com.jeremydyer.service.impl.ReceiptServicePhoenixImpl;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.*;
import java.util.List;

/**
 * Resource for creating and searching for Receipt(s) in Hbase via Phoenix.
 *
 * Created by jdyer on 8/17/15.
 */
@Path("/v1/receipt")
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptResource {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptResource.class);

    private ReceiptServicePhoenixImpl rService = null;
    private RealHbaseFakeStoreConfiguration configuration = null;

    public ReceiptResource(RealHbaseFakeStoreConfiguration configuration) {
        this.configuration = configuration;
        rService = new ReceiptServicePhoenixImpl(configuration);
    }

    @POST
    @Timed
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createReceiptEntry(@FormDataParam("receiptImage") InputStream fileInputStream,
                                       @FormDataParam("receiptImage") FormDataContentDisposition contentDispositionHeader,
                                       @FormParam("SKU") long sku,
                                       @FormParam("date") Date date,
                                       @FormParam("price") double price) {

        logger.info("SKU: " + sku + " Date: " + date + " Price: " + price);
        return Response.ok().build();
    }

    @GET
    @Timed
    @Path("/{transactionID}")
    public Receipt getReceipt(@PathParam("transactionID") long transactionID) {
        logger.info("Getting receipt for TransactionID: " + transactionID);
        return rService.getReceipt(transactionID);
    }

    @GET
    @Timed
    @Path("/{transactionID}/image")
    public Response downloadReceiptImage(@PathParam("transactionID") long transactionID) {

        String fileName = transactionID + "_receiptimage.jpg";
        String receiptImage = rService.getReceiptImage(transactionID);
        return Response
                .ok(receiptImage, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = " + fileName)
                .build();
    }

    @GET
    @Timed
    public List<Receipt> searchReceipts(@QueryParam("transactionID") long transactionId,
                                   @QueryParam("SKU") long sku,
                                   @QueryParam("date") Date date,
                                   @QueryParam("price") double price) {

        logger.info("TransactionID: " + transactionId + " SKU: " + sku + " Date: " + date + " Price: " + price);
        List<Receipt> receipts = rService.searchReceipts(sku, date, price);
        logger.info(receipts.size() + " receipts located");
        return rService.searchReceipts(sku, date, price);
    }
}
