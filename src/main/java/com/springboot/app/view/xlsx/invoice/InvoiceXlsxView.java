package com.springboot.app.view.xlsx.invoice;

import com.springboot.app.exporters.xlsx.ExporterInvoice2XlsxService;
import com.springboot.app.models.entity.Invoice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.util.Date;
import java.util.Map;

@Component("invoice/show_details.xlsx")
public class InvoiceXlsxView extends AbstractXlsxView {

  private final ExporterInvoice2XlsxService exporterService;

  @Autowired
  public InvoiceXlsxView(ExporterInvoice2XlsxService exporterService) {
    super();
    this.exporterService = exporterService;
  }
  @Override
  protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
    Invoice invoice = (Invoice) model.get("invoice");
    String filename = "factura_view.xlsx";
    String filenameHeaderPattern = "attachment; filename=\"%d_%s\"";
    String header = String.format(filenameHeaderPattern, new Date().getTime(), filename);
    response.setHeader("Content-Disposition", header);
    exporterService.addDataToDocument(workbook, invoice, getMessageSourceAccessor());
  }
}
