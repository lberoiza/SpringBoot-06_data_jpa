package com.springboot.app.view.pdf.invoice;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

@Component("invoice/show_details")
public class InvoicePdfView extends AbstractPdfView {

  private final LocaleResolver localeResolver;

  private final MessageSource messageSource;

  @Autowired
  public InvoicePdfView(LocaleResolver localeResolver, MessageSource messageSource) {
    super();
    this.localeResolver = localeResolver;
    this.messageSource = messageSource;
  }

  @Override
  protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
    Locale locale = this.localeResolver.resolveLocale(request);
    Invoice invoice = (Invoice) model.get("invoice");
    PdfPTable tableCustomer = this.createCustomerInfoTable(invoice, locale);
    tableCustomer.setSpacingAfter(20);

    PdfPTable tableInvoice = this.createInvoiceInfoTable(invoice, locale);
    tableInvoice.setSpacingAfter(10);

    PdfPTable tableInvoiceDetails = this.createInvoiceDetailsTable(invoice, locale);

    document.add(tableCustomer);
    document.add(tableInvoice);
    document.add(tableInvoiceDetails);
  }

  private PdfPTable createCustomerInfoTable(Invoice invoice, Locale locale) {
    PdfPTable tableCustomer = new PdfPTable(2);
    String text = this.messageSource.getMessage("text.invoice.show.data.client", null, locale);
    PdfPCell headerCell = new PdfPCell(new Phrase(text));
    headerCell.setColspan(tableCustomer.getNumberOfColumns());
    headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
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
    String text = this.messageSource.getMessage("text.invoice.show.title", null, locale);
    PdfPCell headerCell = new PdfPCell(new Phrase(String.format(text, invoice.getId(), invoice.getClientFullName())));
    headerCell.setColspan(tableInvoice.getNumberOfColumns());
    headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
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

    if(invoice.hasObs()){
      text = this.messageSource.getMessage("text.invoice.form.obs", null, locale);
      tableInvoice.addCell(text);
      tableInvoice.addCell(invoice.getObs());
    }

    return tableInvoice;
  }


  private PdfPTable createInvoiceDetailsTable(Invoice invoice, Locale locale) {
    String datePattern = this.messageSource.getMessage("pattern.date", null, locale);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    PdfPTable tableInvoiceDetails = new PdfPTable(5);

    String text = this.messageSource.getMessage("text.invoice.form.productNr", null, locale);
    tableInvoiceDetails.addCell(text);
    text = this.messageSource.getMessage("text.invoice.form.item.productName", null, locale);
    tableInvoiceDetails.addCell(text);
    text = this.messageSource.getMessage("text.invoice.form.item.price", null, locale);
    tableInvoiceDetails.addCell(text);
    text = this.messageSource.getMessage("text.invoice.form.item.quantity", null, locale);
    tableInvoiceDetails.addCell(text);
    text = this.messageSource.getMessage("text.invoice.form.item.total", null, locale);
    tableInvoiceDetails.addCell(text);

    String patternPrice = this.messageSource.getMessage("pattern.price", null, locale);
    for(InvoiceItem item : invoice.getInvoiceItems()){
      tableInvoiceDetails.addCell(item.getId().toString());
      tableInvoiceDetails.addCell(item.getProductName());
      tableInvoiceDetails.addCell(String.format(patternPrice, item.getProductPrice()));
      tableInvoiceDetails.addCell(item.getQuantity().toString());
      tableInvoiceDetails.addCell(String.format(patternPrice, item.getAmount()));
    }

    text = this.messageSource.getMessage("text.invoice.form.total", null, locale);
    PdfPCell footerTotal = new PdfPCell(new Phrase(text));
    footerTotal.setColspan(tableInvoiceDetails.getNumberOfColumns()-1);
    footerTotal.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
    tableInvoiceDetails.addCell(footerTotal);
    tableInvoiceDetails.addCell(String.format(patternPrice, invoice.getTotal()));

    return tableInvoiceDetails;
  }


}
