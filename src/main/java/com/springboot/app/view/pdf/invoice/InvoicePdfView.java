package com.springboot.app.view.pdf.invoice;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.app.exporters.pdf.ExporterInvoice2PDFService;
import com.springboot.app.models.entity.Invoice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.Map;

@Component("invoice/show_details")
public class InvoicePdfView extends AbstractPdfView {

  private final ExporterInvoice2PDFService exporterService;

  @Autowired
  public InvoicePdfView(ExporterInvoice2PDFService exporterService) {
    super();
    this.exporterService = exporterService;
  }

  @Override
  protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
    Invoice invoice = (Invoice) model.get("invoice");
    exporterService.addDataToDocument(document, invoice, getMessageSourceAccessor());
  }


}
