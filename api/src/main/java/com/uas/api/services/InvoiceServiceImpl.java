package com.uas.api.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uas.api.models.entities.Orders;
import com.uas.api.models.entities.StockToOrders;
import com.uas.api.repositories.StockToOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final StockToOrdersRepository stockToOrdersRepository;

    @Autowired
    public InvoiceServiceImpl(StockToOrdersRepository stockToOrdersRepository) {
        this.stockToOrdersRepository = stockToOrdersRepository;
    }

    @Override
    public boolean generatePDF(Orders givenOrder){
        String fileName = "src/main/resources/invoices/order_"+givenOrder.getOrderID()+".pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            Image img = Image.getInstance("src/main/resources/img/logo.png");
            img.scaleAbsolute(160,50);
            img.setAbsolutePosition(10,782);
            document.add(img);

            Paragraph orderNum = new Paragraph("\n\nOrder #"+givenOrder.getOrderID(), boldFont);
            orderNum.setAlignment(Element.ALIGN_RIGHT);
            document.add(orderNum);

            Paragraph addressLabel = new Paragraph("\nShip to:",boldFont);
            addressLabel.setAlignment(Element.ALIGN_RIGHT);
            document.add(addressLabel);

            if(givenOrder.getLocationName().getAddressLine2()==null){
                givenOrder.getLocationName().setAddressLine2("");
            } else {
                givenOrder.getLocationName().setAddressLine2("\n"+givenOrder.getLocationName().getAddressLine2());
            }
            Paragraph address = new Paragraph(
                    "Sierra Nevada Corporation Mission Systems UK \n"
                    +givenOrder.getLocationName().getAddressLine1()
                    +givenOrder.getLocationName().getAddressLine2()+"\n"
                    +givenOrder.getLocationName().getPostcode()+"\n"
                    +givenOrder.getLocationName().getCountry()+"\n\n");
            address.setAlignment(Element.ALIGN_RIGHT);
            document.add(address);

            float[] columnWidths = {5f,5f,5f};

            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);
            table.addCell("Part Number");
            table.addCell("Part Name");
            table.addCell("Quantity");
            table.completeRow();

            List<StockToOrders> totalOrder = stockToOrdersRepository.findAllByOrderID(givenOrder.getOrderID());
            for(StockToOrders order : totalOrder){
                table.addCell("\n"+order.getPartID().getId().toString());
                table.addCell("\n"+order.getPartID().getPartName().getName());
                table.addCell("\n"+order.getQuantity());
                table.completeRow();
            }



            document.add(table);




            document.close();





            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }


    }

}
