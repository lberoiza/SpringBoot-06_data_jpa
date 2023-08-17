package com.springboot.app.exporters.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.springboot.app.exporters.ExporterService;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.text.SimpleDateFormat;

@Service("ExporterInvoice2PDFService")
public class ExporterInvoice2PDFService implements ExporterService<Document, Invoice> {


  @Override
  public void addDataToDocument(Document pdfDocument, Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    PdfPTable tableCustomer = this.createCustomerInfoTable(invoice, messageSourceAccessor);
    tableCustomer.setSpacingAfter(20);

    PdfPTable tableInvoice = this.createInvoiceInfoTable(invoice, messageSourceAccessor);
    tableInvoice.setSpacingAfter(10);

    PdfPTable tableInvoiceDetails = this.createInvoiceDetailsTable(invoice, messageSourceAccessor);

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

  private PdfPTable createCustomerInfoTable(Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    PdfPTable tableCustomer = new PdfPTable(2);
    tableCustomer.setWidths(new float[] {1f, 3f});

    String text = messageSourceAccessor.getMessage("text.invoice.show.data.client");
    PdfPCell headerCell = this.createHeaderCell(text);
    headerCell.setColspan(tableCustomer.getNumberOfColumns());
    headerCell.setBackgroundColor(new Color (184, 218, 255));
    tableCustomer.addCell(headerCell);

    text = messageSourceAccessor.getMessage("text.view.export.client.fullname");
    tableCustomer.addCell(text);
    tableCustomer.addCell(invoice.getClientFullName());

    text = messageSourceAccessor.getMessage("text.templates.client.email");
    tableCustomer.addCell(text);
    tableCustomer.addCell(invoice.getClient().getEmail());
    return tableCustomer;
  }


  private PdfPTable createInvoiceInfoTable(Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    String datePattern = messageSourceAccessor.getMessage("pattern.date");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    PdfPTable tableInvoice = new PdfPTable(2);
    tableInvoice.setWidths(new float[] {1f, 3f});

    String text = messageSourceAccessor.getMessage("text.invoice.show.title");
    PdfPCell headerCell = this.createHeaderCell(String.format(text, invoice.getId(), invoice.getClientFullName()));
    headerCell.setColspan(tableInvoice.getNumberOfColumns());
    headerCell.setBackgroundColor(new Color (195, 230, 203));
    tableInvoice.addCell(headerCell);

    text = messageSourceAccessor.getMessage("text.client.invoice.number");
    tableInvoice.addCell(text);
    tableInvoice.addCell(invoice.getId().toString());

    text = messageSourceAccessor.getMessage("text.client.invoice.description");
    tableInvoice.addCell(text);
    tableInvoice.addCell(invoice.getDescription());

    text = messageSourceAccessor.getMessage("text.client.invoice.createdAt");
    tableInvoice.addCell(text);
    tableInvoice.addCell(simpleDateFormat.format(invoice.getCreatedAt()));

    if (invoice.hasObs()) {
      text = messageSourceAccessor.getMessage("text.invoice.form.obs");
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

  private PdfPTable createInvoiceDetailsTable(Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    String datePattern = messageSourceAccessor.getMessage("pattern.date");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    PdfPTable tableInvoiceDetails = new PdfPTable(5);
    tableInvoiceDetails.setWidths(new float[] {1f, 3f, 1f, 1f, 1f});

    String text = messageSourceAccessor.getMessage("text.invoice.form.productNr");
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = messageSourceAccessor.getMessage("text.invoice.form.item.productName");
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = messageSourceAccessor.getMessage("text.invoice.form.item.price");
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = messageSourceAccessor.getMessage("text.invoice.form.item.quantity");
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
    text = messageSourceAccessor.getMessage("text.invoice.form.item.total");
    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));

    String patternPrice = messageSourceAccessor.getMessage("pattern.price");
    for (InvoiceItem item : invoice.getInvoiceItems()) {
      tableInvoiceDetails.addCell(item.getId().toString());
      tableInvoiceDetails.addCell(item.getProductName());
      tableInvoiceDetails.addCell(String.format(patternPrice, item.getProductPrice()));
      PdfPCell quantityCell = this.createCell(item.getQuantity().toString());
      quantityCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      tableInvoiceDetails.addCell(quantityCell);
      tableInvoiceDetails.addCell(String.format(patternPrice, item.getAmount()));
    }

    text = messageSourceAccessor.getMessage("text.invoice.form.total");
    PdfPCell footerTotal = this.createCell(text);
    footerTotal.setColspan(tableInvoiceDetails.getNumberOfColumns() - 1);
    footerTotal.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
    tableInvoiceDetails.addCell(footerTotal);
    tableInvoiceDetails.addCell(String.format(patternPrice, invoice.getTotal()));

    return tableInvoiceDetails;
  }


}
