package com.uas.api.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Header;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.uas.api.models.entities.Orders;
import com.uas.api.requests.MoreStockRequest;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public boolean generatePDF(Orders givenOrder){
        String fileName = "src/main/resources/order.pdf";
        Document document = new Document();

        System.out.println(givenOrder.getOrderID());
        System.out.println(givenOrder.getLocationName());
        System.out.println(givenOrder.getOrderDateTime());
        System.out.println(givenOrder.getSupplierEmail());
        System.out.println(givenOrder.getTotalCost());

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            document.add(new Paragraph("Order invoice"));

            document.close();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

}
