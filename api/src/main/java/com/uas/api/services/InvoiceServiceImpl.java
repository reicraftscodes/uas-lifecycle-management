package com.uas.api.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uas.api.models.entities.Orders;
import com.uas.api.models.entities.StockToOrders;
import com.uas.api.repositories.StockToOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.math.BigDecimal;
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
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            document.open();

            Image img = Image.getInstance("src/main/resources/img/logo.png");
            img.scaleAbsolute(160,50);
            img.setAbsolutePosition(10,782);
            document.add(img);

            Paragraph orderNum = new Paragraph("\n\nOrder #"+givenOrder.getOrderID(), boldFont);
            document.add(orderNum);

            Paragraph email = new Paragraph("Recipient: "+givenOrder.getSupplierEmail());
            document.add(email);

            Paragraph date = new Paragraph("Generated on: "+givenOrder.getOrderDateTime().toString());
            document.add(date);

            Paragraph addressLabel = new Paragraph("\n\n\nShip to:",boldFont);
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
                    +givenOrder.getLocationName().getCountry()+"\n");
            address.setAlignment(Element.ALIGN_RIGHT);
            document.add(address);

            Paragraph partHeading = new Paragraph("Part Order: \n\n",boldFont);
            document.add(partHeading);

            float[] columnWidths = {2f,5f,2f,2f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            PdfPCell partNumCell = new PdfPCell(new Phrase("Part Number"));
            partNumCell.setPadding(3);
            table.addCell(partNumCell);

            PdfPCell partNameCell = new PdfPCell(new Phrase("Part Name (Varient)"));
            partNameCell.setPadding(3);
            table.addCell(partNameCell);

            PdfPCell partQuantityCell = new PdfPCell(new Phrase("Quantity"));
            partQuantityCell.setPadding(3);
            table.addCell(partQuantityCell);

            PdfPCell partCostCell = new PdfPCell(new Phrase("Cost"));
            partCostCell.setPadding(3);
            table.addCell(partCostCell);

            table.completeRow();

            List<StockToOrders> totalOrder = stockToOrdersRepository.findAllByOrderID(givenOrder.getOrderID());
            double totalCost = 0;
            for(StockToOrders order : totalOrder){
                double currentPartCost = order.getPartID().getPrice().doubleValue()*order.getQuantity();

                PdfPCell[] cells = new PdfPCell[4];

                cells[0] = new PdfPCell(new Phrase(order.getPartID().getId().toString()));
                cells[0].setPadding(5);
                table.addCell(cells[0]);

                cells[1] = new PdfPCell(new Phrase(order.getPartID().getPartName().getName()));
                cells[1].setPadding(5);
                table.addCell(cells[1]);

                cells[2] = new PdfPCell(new Phrase(""+order.getQuantity()));
                cells[2].setPadding(5);
                table.addCell(cells[2]);

                cells[3] = new PdfPCell(new Phrase("£"+ currentPartCost));
                cells[3].setPadding(5);
                table.addCell(cells[3]);

                table.completeRow();
                totalCost += currentPartCost;
            }

            document.add(table);


            Paragraph partCost = new Paragraph("\nTotal Cost: £"+totalCost,boldFont);
            partCost.setAlignment(Element.ALIGN_RIGHT);
            document.add(partCost);

            document.close();

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }


    }

}
