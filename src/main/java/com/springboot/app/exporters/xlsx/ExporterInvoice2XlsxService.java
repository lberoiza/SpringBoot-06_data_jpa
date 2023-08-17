package com.springboot.app.exporters.xlsx;

import com.lowagie.text.pdf.PdfPCell;
import com.springboot.app.exporters.ExporterService;
import com.springboot.app.models.entity.Invoice;
import org.apache.poi.ss.usermodel.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.Color;
import java.text.SimpleDateFormat;

@Service("ExporterInvoice2XlsxService")
public class ExporterInvoice2XlsxService implements ExporterService<Workbook, Invoice> {

  private static final IndexedColors CUSTOMER_DATA_HEADE_RB_GCOLOR = IndexedColors.AQUA;
  private static final IndexedColors INVOICE_DATA_HEADER_BG_COLOR = IndexedColors.CORAL;
  private static final IndexedColors INVOICE_DETAILS_HEADER_BG_COLOR = IndexedColors.BLUE;


  @Override
  public void addDataToDocument(Workbook workbook, Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    Sheet sheet = workbook.createSheet();
    this.addCustomerInfoToWorkbook(sheet, invoice, messageSourceAccessor);
    this.addInvoiceInfoToWorkbook(sheet, invoice, messageSourceAccessor);
//    this.addInvoiceDetailsToWorkbook(sheet, invoice, messageSourceAccessor);
  }

  private Cell createCell(Row row, int columnPosition, Object valueOfCell) {

    if (valueOfCell == null) {
      valueOfCell = "";
    }

    Cell cell = row.createCell(columnPosition);
    if (valueOfCell instanceof Integer) {
      cell.setCellValue((Integer) valueOfCell);
    } else if (valueOfCell instanceof Long) {
      cell.setCellValue((Long) valueOfCell);
    } else if (valueOfCell instanceof String) {
      cell.setCellValue((String) valueOfCell);
    } else {
      cell.setCellValue((Boolean) valueOfCell);
    }
    return cell;
  }

  private CellStyle createHeaderStyle(Workbook workbook, IndexedColors bgColor) {
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(bgColor.index);
    headerStyle.setBorderTop(BorderStyle.MEDIUM);
    headerStyle.setBorderRight(BorderStyle.MEDIUM);
    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return headerStyle;
  }


  private CellStyle createTableCellStyle(Workbook workbook) {
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setBorderTop(BorderStyle.THIN);
    headerStyle.setBorderRight(BorderStyle.THIN);
    headerStyle.setBorderBottom(BorderStyle.THIN);
    headerStyle.setBorderLeft(BorderStyle.THIN);
    return headerStyle;
  }


  private void addCustomerInfoToWorkbook(Sheet sheet, Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    String text = messageSourceAccessor.getMessage("text.invoice.show.data.client");
    Row row = sheet.createRow(0);

    CellStyle cellHeaderStyle = this.createHeaderStyle(sheet.getWorkbook(), CUSTOMER_DATA_HEADE_RB_GCOLOR);
    Cell headerCell = this.createCell(row, 0, text);
    headerCell.setCellStyle(cellHeaderStyle);

    text = messageSourceAccessor.getMessage("text.view.export.client.fullname");
    row = sheet.createRow(1);
    this.createCell(row, 0, text);
    this.createCell(row, 1, invoice.getClientFullName());

    text = messageSourceAccessor.getMessage("text.templates.client.email");
    row = sheet.createRow(2);
    this.createCell(row, 0, text);
    this.createCell(row, 1, invoice.getClient().getEmail());
  }


  private void addInvoiceInfoToWorkbook(Sheet sheet, Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    String datePattern = messageSourceAccessor.getMessage("pattern.date");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);


    String text = messageSourceAccessor.getMessage("text.invoice.show.title");
    Row row = sheet.createRow(4);

    CellStyle cellHeaderStyle = this.createHeaderStyle(sheet.getWorkbook(), INVOICE_DATA_HEADER_BG_COLOR);
    Cell headerCell = this.createCell(row, 0, String.format(text, invoice.getId(), invoice.getClientFullName()));
    headerCell.setCellStyle(cellHeaderStyle);

    text = messageSourceAccessor.getMessage("text.client.invoice.number");

    row = sheet.createRow(5);
    this.createCell(row, 0, text);
    this.createCell(row, 1, invoice.getId().toString());

    text = messageSourceAccessor.getMessage("text.client.invoice.description");
    row = sheet.createRow(6);
    this.createCell(row, 0, text);
    this.createCell(row, 1, invoice.getDescription());

    text = messageSourceAccessor.getMessage("text.client.invoice.createdAt");
    row = sheet.createRow(7);
    this.createCell(row, 0, text);
    this.createCell(row, 1, simpleDateFormat.format(invoice.getCreatedAt()));

    if (invoice.hasObs()) {
      text = messageSourceAccessor.getMessage("text.invoice.form.obs");
      row = sheet.createRow(8);
      this.createCell(row, 0, text);
      this.createCell(row, 1, invoice.getObs());
    }

  }
//
//
//  private PdfPCell createInvoiceDetailsTableHeader(String text){
//    Color bgHeaderColor = new Color(13, 110, 253);
//    Color fontHeaderColor = Color.white;
//    PdfPCell headerCell = this.createHeaderCell(text);
//    headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//    headerCell.setBackgroundColor(bgHeaderColor);
//    headerCell.getPhrase().getFont().setColor(fontHeaderColor);
//    return headerCell;
//  }
//
//  private PdfPTable createInvoiceDetailsTable(Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
//    String datePattern = messageSourceAccessor.getMessage("pattern.date");
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
//
//    PdfPTable tableInvoiceDetails = new PdfPTable(5);
//    tableInvoiceDetails.setWidths(new float[] {1f, 3f, 1f, 1f, 1f});
//
//    String text = messageSourceAccessor.getMessage("text.invoice.form.productNr");
//    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
//    text = messageSourceAccessor.getMessage("text.invoice.form.item.productName");
//    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
//    text = messageSourceAccessor.getMessage("text.invoice.form.item.price");
//    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
//    text = messageSourceAccessor.getMessage("text.invoice.form.item.quantity");
//    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
//    text = messageSourceAccessor.getMessage("text.invoice.form.item.total");
//    tableInvoiceDetails.addCell(this.createInvoiceDetailsTableHeader(text));
//
//    String patternPrice = messageSourceAccessor.getMessage("pattern.price");
//    for (InvoiceItem item : invoice.getInvoiceItems()) {
//      tableInvoiceDetails.addCell(item.getId().toString());
//      tableInvoiceDetails.addCell(item.getProductName());
//      tableInvoiceDetails.addCell(String.format(patternPrice, item.getProductPrice()));
//      PdfPCell quantityCell = this.createCell(item.getQuantity().toString());
//      quantityCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//      tableInvoiceDetails.addCell(quantityCell);
//      tableInvoiceDetails.addCell(String.format(patternPrice, item.getAmount()));
//    }
//
//    text = messageSourceAccessor.getMessage("text.invoice.form.total");
//    PdfPCell footerTotal = this.createCell(text);
//    footerTotal.setColspan(tableInvoiceDetails.getNumberOfColumns() - 1);
//    footerTotal.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//    tableInvoiceDetails.addCell(footerTotal);
//    tableInvoiceDetails.addCell(String.format(patternPrice, invoice.getTotal()));
//
//    return tableInvoiceDetails;
//  }


}
