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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;

/**
 * Class used to generate order invoices and send them to suppliers.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {
    /**
     * Gets the specific parts for a order to generate the order pdf.
     */
    private final StockToOrdersRepository stockToOrdersRepository;

    /**
     * Constructor.
     * @param stockToOrdersRepository Gets stock for an order.
     */
    @Autowired
    public InvoiceServiceImpl(StockToOrdersRepository stockToOrdersRepository) {
        this.stockToOrdersRepository = stockToOrdersRepository;
    }

    /**
     * Generates a pdf for a given order.
     * @param givenOrder The order that a pdf is being generated for.
     * @return A string with the document name or a string containing error.
     */
    @Override
    public String generatePDF(Orders givenOrder) {
        String fileName = "src/main/resources/invoices/order_"+givenOrder.getOrderID()+".pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            document.open();

            //adds the sncmsuk logo and sets its position
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

            //checks if the address line 2 is blank in the database, if it is it changes it from null
            // to nothing.
            if(givenOrder.getLocationName().getAddressLine2()==null){
                givenOrder.getLocationName().setAddressLine2("");
            } else {
                givenOrder.getLocationName().setAddressLine2("\n"+givenOrder.getLocationName().getAddressLine2());
            }
            //Displays the address delivery address
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

            //Creating a table for the parts that have been ordered.
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

            //gets a list stocktoorder objects and loops through adding rows to the table.
            List<StockToOrders> totalOrder = stockToOrdersRepository.findAllByOrderID(givenOrder.getOrderID());
            double totalCost = 0;
            for(StockToOrders order : totalOrder) {
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

            return fileName;

        } catch (Exception e) {
            System.out.println("Exception in generatePDF: "+e);
            return "error";
        }
    }

    /**
     * Emails the given invoice to the given address.
     * @param invoicePath The path of the invoice pdf.
     * @param recipientAddress The email adress the invoice is being sent to.
     * @return returns a boolean for success for failure.
     */
    public boolean emailInvoice(String invoicePath, String recipientAddress) {
        //Setting the mail server properties.
        Properties prop = new Properties();
        prop.put("mail.smtp.auth",true);
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port",587);
        prop.put("mail.smtp.starttls.enable",true);
        prop.put("mail.transport.protocol","smtp");

        //the email login of the account sending the email.
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("SNCMSUKTestEmail@gmail.com","UASProject1");
            }
        });

        try {
            Message message = new MimeMessage(session);

            Address recipient = new InternetAddress(recipientAddress);
            message.setRecipient(Message.RecipientType.TO, recipient);

            message.setSubject("New Order of parts");

            MimeMultipart body = new MimeMultipart();

            //attatches the given pdf.
            MimeBodyPart invoicePDF = new MimeBodyPart();
            invoicePDF.attachFile(new File(invoicePath));
            body.addBodyPart(invoicePDF);

            //sets a message body.
            MimeBodyPart messageContent = new MimeBodyPart();
            messageContent.setContent("<h4>Order from SNCMSUK</h4><p>Dear Sales Department,<br/><br/>We have attached the list of parts we would like to order with the delivery location. <br/><br/> Yours Sincerely,<br/> SNCMSUK </p>","text/html");
            body.addBodyPart(messageContent);

            message.setContent(body);

            //sends the email.
            Transport.send(message);
        } catch (Exception e) {
            System.out.println("Exception in emailInvoice: "+e);
            return false;
        }

        return true;
    }

}
