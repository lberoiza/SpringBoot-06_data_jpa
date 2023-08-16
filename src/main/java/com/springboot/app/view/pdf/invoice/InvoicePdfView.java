package com.springboot.app.view.pdf.invoice;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.app.models.entity.Invoice;
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
    tableInvoice.setSpacingAfter(20);

    document.add(tableCustomer);
    document.add(tableInvoice);
  }


  private PdfPTable createCustomerInfoTable(Invoice invoice, Locale locale){
    PdfPTable tableCustomer = new PdfPTable(1);
    String text = this.messageSource.getMessage("text.invoice.show.data.client", null, locale);
    tableCustomer.addCell(text);
    tableCustomer.addCell(invoice.getClientFullName());
    tableCustomer.addCell(invoice.getClient().getEmail());
    return tableCustomer;
  }

  private PdfPTable createInvoiceInfoTable(Invoice invoice, Locale locale){
    String datePattern = this.messageSource.getMessage("pattern.date", null, locale);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    PdfPTable tableInvoice = new PdfPTable(1);
    String text = this.messageSource.getMessage("text.invoice.show.title", null, locale);
    tableInvoice.addCell(String.format(text, invoice.getId(), invoice.getClientFullName()));
    text = this.messageSource.getMessage("text.client.invoice.number", null, locale);
    tableInvoice.addCell(String.format("%s: %d", text, invoice.getId()));
    text = this.messageSource.getMessage("text.client.invoice.description", null, locale);
    tableInvoice.addCell(String.format("%s: %s", text, invoice.getDescription()));
    text = this.messageSource.getMessage("text.client.invoice.createdAt", null, locale);
    tableInvoice.addCell(String.format("%s: %s", text, simpleDateFormat.format(invoice.getCreatedAt())));
    return tableInvoice;
  }


}
