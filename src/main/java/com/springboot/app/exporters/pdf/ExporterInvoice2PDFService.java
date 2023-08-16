package com.springboot.app.exporters.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Service("ExporterInvoice2PDFService")
public class ExporterInvoice2PDFService implements ExporterPDFService<Invoice> {


  private final MessageSource messageSource;

  @Autowired
  public ExporterInvoice2PDFService(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public void addDataToDocument(Document pdfDocument, Invoice invoice, Locale locale) {
    PdfPTable tableCustomer = this.createCustomerInfoTable(invoice, locale);
    tableCustomer.setSpacingAfter(20);

    PdfPTable tableInvoice = this.createInvoiceInfoTable(invoice, locale);
    tableInvoice.setSpacingAfter(10);

    PdfPTable tableInvoiceDetails = this.createInvoiceDetailsTable(invoice, locale);

    pdfDocument.add(tableCustomer);
    pdfDocument.add(tableInvoice);
    pdfDocument.add(tableInvoiceDetails);
  }

  private PdfPCell createCell(String cellText) {
    return new PdfPCell(new Phrase(cellText));
  }

  private PdfPCell createHeaderCell(String cellText) {
    PdfPCell headerCell = createCell(cellText);
    headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    headerCell.setPadding(8f);
    return headerCell;
  }

  private PdfPTable createCustomerInfoTable(Invoice invoice, Locale locale) {
    PdfPTable tableCustomer = new PdfPTable(2);
    tableCustomer.setWidths(new float[] {1f, 3f});

    String text = this.messageSource.getMessage("text.invoice.show.data.client", null, locale);
    PdfPCell headerCell = this.createHeaderCell(text);
    headerCell.setColspan(tableCustomer.getNumberOfColumns());
    headerCell.setBackgroundColor(new Color (184, 218, 255));
    tableCustomer.addCell(headerCell);

    text = this.messageSource.getMessage("text.view.export.client.fullname", null, locale);
    tableCustomer.addCell(text);
    tableCustomer.addCell(invoice.getClientFullName());

    text = this.messageSource.getMessage("text.templates.client.email", null, locale);
    tableCustomer.addCell(text);
    tableCustomer.addCell(invoice.getClient().getEmail());
    return tableCustomer;
  }


  private PdfPTable createInvoiceInfoTable(Invoice invoice, Locale locale) {
    String datePattern = this.messageSource.getMessage("pattern.date", null, locale);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    PdfPTable tableInvoice = new PdfPTable(2);
    tableInvoice.setWidths(new float[] {1f, 3f});

    String text = this.messageSource.getMessage("text.invoice.show.title", null, locale);
    PdfPCell headerCell = this.createHeaderCell(String.format(text, invoice.getId(), invoice.getClientFullName()));
    headerCell.setColspan(tableInvoice.getNumberOfColumns());
    headerCell.setBackgroundColor(new Color (195, 230, 203));
    tableInvoice.addCell(headerCell);

    text = this.messageSource.getMessage("text.client.invoice.number", null, locale);
    tableInvoice.addCell(text);
    tableInvoice.addCell(invoice.getId().toString());

    text = this.messageSource.getMessage("text.client.invoice.description", null, locale);
    tableInvoice.addCell(text);
    tableInvoice.addCell(invoice.getDescription());

    text = this.messageSource.getMessage("text.client.invoice.createdAt", null, locale);
    tableInvoice.addCell(text);
    tableInvoice.addCell(simpleDateFormat.format(invoice.getCreatedAt()));

    if (invoice.hasObs()) {
      text = this.messageSource.getMessage("text.invoice.form.obs", null, locale);
      tableInvoice.addCell(text);
      tableInvoice.addCell(invoice.getObs());
    }

    return tableInvoice;
  }


  private PdfPCell createInvoiceDetailsTableHeader(String text){
    Color bgHeaderColor = new Color(13, 110, 253);
    Color fontHeaderColor = Color.white;
    PdfPCell headerCell = this.createHeaderCell(text);
    headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    headerCell.setBackgroundColor(bgHeaderColor);
    headerCell.getPhrase().getFont().setColor(fontHeaderColor);
    return headerCell;
  }

  private PdfPTable createInvoiceDetailsTable(Invoice invoice, Locale locale) {
    String datePattern = this.messageSource.getMessage("pattern.date", null, locale);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    PdfPTable tableInvoiceDetails = new PdfPTable(5);
    tableInvoiceDetails.setWidths(new float[] {1f, 3f, 1f, 1f, 1f});

    String text = this.messageSource.getMessage("text.invoice.form.productNr", null, locale);
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = this.messageSource.getMessage("text.invoice.form.item.productName", null, locale);
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = this.messageSource.getMessage("text.invoice.form.item.price", null, locale);
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = this.messageSource.getMessage("text.invoice.form.item.quantity", null, locale);
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = this.messageSource.getMessage("text.invoice.form.item.total", null, locale);
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));

    String patternPrice = this.messageSource.getMessage("pattern.price", null, locale);
    for (InvoiceItem item : invoice.getInvoiceItems()) {
      tableInvoiceDetails.addCell(item.getId().toString());
      tableInvoiceDetails.addCell(item.getProductName());
      tableInvoiceDetails.addCell(String.format(patternPrice, item.getProductPrice()));
      PdfPCell quantityCell = this.createCell(item.getQuantity().toString());
      quantityCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      tableInvoiceDetails.addCell(quantityCell);
      tableInvoiceDetails.addCell(String.format(patternPrice, item.getAmount()));
    }

    text = this.messageSource.getMessage("text.invoice.form.total", null, locale);
    PdfPCell footerTotal = this.createCell(text);
    footerTotal.setColspan(tableInvoiceDetails.getNumberOfColumns() - 1);
    footerTotal.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
    tableInvoiceDetails.addCell(footerTotal);
    tableInvoiceDetails.addCell(String.format(patternPrice, invoice.getTotal()));

    return tableInvoiceDetails;
  }


}
