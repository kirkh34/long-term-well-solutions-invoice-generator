package com.ltws;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class GenerateInvoice {
    private String fileName = "LTWS-Invoice";
    private String home = System.getProperty("user.home");
    private String DEST = home+"/Downloads/" + fileName + ".pdf";
    private Job job;
    private Person company;
    private Customer customer;
    private Double total;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");

    public GenerateInvoice(Job job, Customer customer, Person company, double total){
        this.job = job;
        this.customer = customer;
        this.company = company;
        this.total = total;
    }

    public void download() throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        buildDocument(new PdfDocument(new PdfWriter(DEST)));
    }



    public void buildDocument(PdfDocument pdfDoc) throws Exception {
        Document doc = new Document(pdfDoc, PageSize.A3);
        PdfFont helvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont helveticaBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        Table table = new Table(UnitValue.createPercentArray(7)).useAllAvailableWidth().setFixedLayout();
        table.setWidth(pdfDoc.getDefaultPageSize().getWidth() - 80);

        //Row 1
        Cell companyName = new Cell(1, 4).add(new Paragraph(company.getFirstName() + " " + company.getLastName())
            .setFont(helveticaBold)
            .setFontSize(25))
            .setTextAlignment(TextAlignment.LEFT)
            .setPadding(0f)
            .setPaddingTop(7.0f)
            .setBorder(Border.NO_BORDER);

        table.addCell(companyName);

        Cell invoiceLabel = new Cell(1, 3).add(new Paragraph("INVOICE")
            .setFont(helveticaBold)
            .setFontSize(30))
            .setTextAlignment(TextAlignment.RIGHT)
            .setPadding(0f)
            .setBorder(Border.NO_BORDER);
        table.addCell(invoiceLabel);

        //Row 2
        Cell companyStreet = new Cell(1, 7).add(new Paragraph(company.getStreet())
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);

        table.addCell(companyStreet);

        //Row 3
        Cell companyCityStateZip = new Cell(1, 4).add(new Paragraph(company.getCity()+ ", " + company.getState() + " "+ company.getZip())
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(companyCityStateZip);

        Cell invoiceNumberLabel = new Cell(1, 2).add(new Paragraph("Invoice #:")
            .setFont(helveticaBold)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.RIGHT)
            .setPaddingRight(8.0f)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f)
            .setPaddingRight(8f);
        table.addCell(invoiceNumberLabel);

        Cell invoiceNumber = new Cell(1, 1).add(new Paragraph(String.format("%05d", job.getID()))
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(invoiceNumber);

        //Row 4
        Cell companyPhone = new Cell(1, 4).add(new Paragraph(String.valueOf(company.getPhone()).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"))
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(companyPhone);

        Cell invoiceDateLabel = new Cell(1, 2).add(new Paragraph("Invoice Date:")
            .setFont(helveticaBold)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f)
            .setPaddingRight(8f);
        table.addCell(invoiceDateLabel);

        Cell invoiceDate = new Cell(1, 1).add(new Paragraph(formatter.format(LocalDate.now()))
            .setBorder(new SolidBorder(ColorConstants.WHITE, 1))
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(invoiceDate);

        //Row 5
        Cell companyEmail = new Cell(1, 7).add(new Paragraph(company.getEmail())
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(companyEmail);

        //Row 6
        Cell blank = new Cell(1, 7)
            .setBorder(Border.NO_BORDER)
            .setHeight(20f);
        table.addCell(blank);

        //Row 7
        Cell billToHeader = new Cell(1, 2).add(new Paragraph("\u00A0\u00A0Bill To")
            .setBackgroundColor(ColorConstants.BLACK)
            .setFont(helvetica)
            .setFontSize(16))
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(billToHeader);

        Cell blank2 = new Cell(1, 5)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(blank2);

        //Row 8
        Cell customerName = new Cell(1, 7).add(new Paragraph(customer.getFirstName() + " " + customer.getLastName())
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f)
            .setPaddingLeft(5f)
            .setPaddingTop(4f);
        table.addCell(customerName);

        //Row 9
        Cell customerStreet = new Cell(1, 7).add(new Paragraph(customer.getStreet())
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f)
            .setPaddingLeft(5f);
        table.addCell(customerStreet);

        //Row 10
        Cell customerCityStateZip = new Cell(1, 7).add(new Paragraph(customer.getCity()+ ", " + customer.getState() + " " + customer.getZip())
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f)
            .setPaddingLeft(5f);
        table.addCell(customerCityStateZip);

        //Row 11
        Cell customerPhone = new Cell(1, 7).add(new Paragraph(String.valueOf(customer.getPhone()).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"))
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f)
            .setPaddingLeft(5f);
        table.addCell(customerPhone);

        //Row 12
        Cell blank3 = new Cell(1, 7)
            .setBorder(Border.NO_BORDER)
            .setHeight(20f);
        table.addCell(blank3);

        //Row 13
        Cell itemTypeHeader = new Cell(1, 1).add(new Paragraph("\u00A0\u00A0Type")
            .setBackgroundColor(ColorConstants.BLACK)
            .setFont(helvetica)
            .setFontSize(16))
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(itemTypeHeader);

        Cell itemDescHeader = new Cell(1, 5).add(new Paragraph("\u00A0\u00A0Description")
            .setBackgroundColor(ColorConstants.BLACK)
            .setFont(helvetica)
            .setFontSize(16))
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(itemDescHeader);


        Cell itemPriceHeader = new Cell(1, 1).add(new Paragraph("Price\u00A0\u00A0")
            .setBackgroundColor(ColorConstants.BLACK)
            .setFont(helvetica)
            .setFontSize(16))
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(itemPriceHeader);


        // Generate Materials

        for(Job.Material mat : job.getMaterialOList()){
            Cell materialType = new Cell(1, 1).add(new Paragraph("Material")
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPadding(0f)
                .setPaddingTop(5f);
            table.addCell(materialType);

            Cell materialDesc = new Cell(1, 5).add(new Paragraph(mat.getItemDesc())
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPadding(5f);
            table.addCell(materialDesc);

            Cell materialPrice = new Cell(1, 1).add(new Paragraph("$" + String.format("%.2f", mat.getItemPrice()))
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPaddingRight(3f)
                .setPaddingTop(5f);
            table.addCell(materialPrice);
        }

        // Generate Labor
        for(Job.Labor labor : job.getLaborOList()){
            Cell laborType = new Cell(1, 1).add(new Paragraph("Labor")
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPadding(0f)
                .setPaddingTop(5f);
            table.addCell(laborType);

            Cell laborDesc = new Cell(1, 5).add(new Paragraph(labor.getItemDesc())
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPadding(5f);
            table.addCell(laborDesc);

            Cell laborPrice = new Cell(1, 1).add(new Paragraph("$" + String.format("%.2f", labor.getItemPrice()))
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPaddingRight(3f)
                .setPaddingTop(5f);
            table.addCell(laborPrice);
        }

        // Generate Fee
        for(Job.Fee fee : job.getFeeOList()){
            Cell feeType = new Cell(1, 1).add(new Paragraph("Fee")
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPadding(0f)
                .setPaddingTop(5f);
            table.addCell(feeType);

            Cell feeDesc = new Cell(1, 5).add(new Paragraph(fee.getItemDesc())
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPadding(5f);
            table.addCell(feeDesc);

            Cell feePrice = new Cell(1, 1).add(new Paragraph("$" + String.format("%.2f", fee.getItemPrice()))
                .setFont(helvetica)
                .setFontSize(16))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK,2f))
                .setPaddingRight(3f)
                .setPaddingTop(5f);
            table.addCell(feePrice);
        }


        //BLank before total
        Cell blank4 = new Cell(1, 7)
            .setBorder(Border.NO_BORDER)
            .setHeight(20f);
        table.addCell(blank4);

        //Spacer before total
        Cell spacer2 = new Cell(1, 5)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(spacer2);

        Cell totalHeader = new Cell(1, 1).add(new Paragraph("Total\u00A0\u00A0")
            .setBackgroundColor(ColorConstants.BLACK)
            .setFont(helvetica)
            .setFontSize(16))
            .setFontColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(Border.NO_BORDER)
            .setPadding(0f);
        table.addCell(totalHeader);

        Cell totalPrice = new Cell(1, 1).add(new Paragraph("$" + String.format("%.2f", total))
            .setFont(helvetica)
            .setFontSize(16))
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER)
            .setPaddingRight(3f);
        table.addCell(totalPrice);

        //Add Table and Close Document
        doc.add(table);
        doc.close();
    }

    public void email(String customerEmail) {

        String sender = "ltwsinvoices@gmail.com"; //replace this with a valid sender email address
        String recipient = customerEmail; //replace this with a valid recipient email address
        String content = "Hello " + job.getCustName() + ".  Attached to this E-mail is your invoice."; //this will be the text of the email
        String subject = "Your invoice from Long Term Well Solutions"; //this will be the subject of the email

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("ltwsinvoices@gmail.com","L0ngTermWe!!S0lutions");
                    }
                });

        ByteArrayOutputStream outputStream = null;

        try {
            //construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content);

            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();

            buildDocument(new PdfDocument(new PdfWriter(outputStream)));


            byte[] bytes = outputStream.toByteArray();

            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(fileName + "-" + String.format("%05d", job.getID()) + "-" + job.getCustName() + ".pdf");

            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);

            //create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress(sender);
            InternetAddress iaRecipient = new InternetAddress(recipient);

            //construct the mime message

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(iaSender);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
            mimeMessage.setContent(mimeMultipart);

            //send off the email
            Transport.send(mimeMessage);

            System.out.println("sent from " + sender +
                    ", to " + recipient);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            //clean off
            if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
        }
    }

}