package com.springboot.app.exporters.xlsx;

import com.springboot.app.exporters.ExporterService;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service("ExporterInvoice2XlsxService")
public class ExporterInvoice2XlsxService implements ExporterService<Workbook, Invoice> {

  private static final IndexedColors CUSTOMER_DATA_HEADER_RB_GCOLOR = IndexedColors.PALE_BLUE;
  private static final IndexedColors INVOICE_DATA_HEADER_BG_COLOR = IndexedColors.GOLD;
  private static final IndexedColors INVOICE_DETAILS_HEADER_BG_COLOR = IndexedColors.SKY_BLUE;


  @Override
  public void addDataToDocument(Workbook workbook, Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    Sheet sheet = workbook.createSheet();
    this.addCustomerInfoToWorkbook(sheet, invoice, messageSourceAccessor);
    this.addInvoiceInfoToWorkbook(sheet, invoice, messageSourceAccessor);
    this.addInvoiceDetailsToWorkbook(sheet, invoice, messageSourceAccessor);
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

  private CellStyle createHeaderStyle(Sheet sheet, IndexedColors bgColor) {
    CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
    headerStyle.setFillForegroundColor(bgColor.index);
    headerStyle.setBorderTop(BorderStyle.MEDIUM);
    headerStyle.setBorderRight(BorderStyle.MEDIUM);
    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);
    Font font = this.createFont(sheet, IndexedColors.BLACK);
    font.setBold(true);
    headerStyle.setFont(font);
    return headerStyle;
  }

  private Font createFont(Sheet sheet) {
    Font font = sheet.getWorkbook().createFont();
    return font;
  }

  private Font createFont(Sheet sheet, IndexedColors color) {
    Font font = this.createFont(sheet);
    font.setColor(color.getIndex());
    return font;
  }

  private void addCustomerInfoToWorkbook(Sheet sheet, Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    String text = messageSourceAccessor.getMessage("text.invoice.show.data.client");
    Row row = sheet.createRow(0);

    CellStyle cellHeaderStyle = this.createHeaderStyle(sheet, CUSTOMER_DATA_HEADER_RB_GCOLOR);
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

    CellStyle cellHeaderStyle = this.createHeaderStyle(sheet, INVOICE_DATA_HEADER_BG_COLOR);
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

  private void addCellBorders(CellStyle headerStyle) {
    headerStyle.setBorderTop(BorderStyle.THIN);
    headerStyle.setBorderRight(BorderStyle.THIN);
    headerStyle.setBorderBottom(BorderStyle.THIN);
    headerStyle.setBorderLeft(BorderStyle.THIN);
  }

  private void addInvoiceDetailsToWorkbook(Sheet sheet, Invoice invoice, MessageSourceAccessor messageSourceAccessor) {
    CellStyle cellHeaderStyle = this.createHeaderStyle(sheet, INVOICE_DETAILS_HEADER_BG_COLOR);
    Font font = this.createFont(sheet, IndexedColors.WHITE);;
    font.setBold(true);
    cellHeaderStyle.setFont(font);
    this.addCellBorders(cellHeaderStyle);

    Integer startRow = 10;
    Row row = sheet.createRow(startRow++);
    String text = messageSourceAccessor.getMessage("text.invoice.form.productNr");
    Cell headerCell = this.createCell(row, 0, text);
    headerCell.setCellStyle(cellHeaderStyle);

    text = messageSourceAccessor.getMessage("text.invoice.form.item.productName");
    headerCell = this.createCell(row, 1, text);
    headerCell.setCellStyle(cellHeaderStyle);

    text = messageSourceAccessor.getMessage("text.invoice.form.item.price");
    headerCell = this.createCell(row, 2, text);
    headerCell.setCellStyle(cellHeaderStyle);

    text = messageSourceAccessor.getMessage("text.invoice.form.item.quantity");
    headerCell = this.createCell(row, 3, text);
    headerCell.setCellStyle(cellHeaderStyle);

    text = messageSourceAccessor.getMessage("text.invoice.form.item.total");
    headerCell = this.createCell(row, 4, text);
    headerCell.setCellStyle(cellHeaderStyle);

    String patternPrice = messageSourceAccessor.getMessage("pattern.price");
    CellStyle detailCellStyleLeft = sheet.getWorkbook().createCellStyle();
    detailCellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
    this.addCellBorders(detailCellStyleLeft);

    CellStyle detailCellStyleCenter = sheet.getWorkbook().createCellStyle();
    detailCellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
    this.addCellBorders(detailCellStyleCenter);

    CellStyle detailCellStyleRight = sheet.getWorkbook().createCellStyle();
    detailCellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
    this.addCellBorders(detailCellStyleRight);

    this.addCellBorders(detailCellStyleLeft);
    for (InvoiceItem item : invoice.getInvoiceItems()) {
      row = sheet.createRow(startRow++);
      Cell detailCell = this.createCell(row, 0, item.getId().toString());
      detailCell.setCellStyle(detailCellStyleRight);

      detailCell = this.createCell(row, 1, item.getProductName());
      detailCell.setCellStyle(detailCellStyleLeft);

      detailCell = this.createCell(row, 2, String.format(patternPrice, item.getProductPrice()));
      detailCell.setCellStyle(detailCellStyleRight);

      detailCell = this.createCell(row, 3, item.getQuantity().toString());
      detailCell.setCellStyle(detailCellStyleCenter);

      detailCell = this.createCell(row, 4, String.format(patternPrice, item.getAmount()));
      detailCell.setCellStyle(detailCellStyleRight);
    }

    row = sheet.createRow(startRow);
    text = messageSourceAccessor.getMessage("text.invoice.form.total");
    Cell totalCell = this.createCell(row, 3, text);
    totalCell.setCellStyle(detailCellStyleRight);

    totalCell = this.createCell(row, 4, String.format(patternPrice, invoice.getTotal()));
    totalCell.setCellStyle(detailCellStyleRight);
  }


}
