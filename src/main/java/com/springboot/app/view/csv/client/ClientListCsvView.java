package com.springboot.app.view.csv.client;

import com.springboot.app.exporters.csv.ExporterClientList2CsvService;
import com.springboot.app.models.entity.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import java.util.Date;
import java.util.Map;

@Component("client/show_list.csv")
public class ClientListCsvView extends AbstractView {

  private final ExporterClientList2CsvService exportService;

  @Autowired
  public ClientListCsvView(ExporterClientList2CsvService exportService) {
    this.setContentType("text/csv");
    this.exportService = exportService;
  }

  @Override
  protected boolean generatesDownloadContent() {
    return true;
  }

  @Override
  protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    System.out.println("obtiene listado de clientes en csv");

    String filename = "ClientList.csv";
    String filenameHeaderPattern = "attachment; filename=\"%d_%s\"";
    String header = String.format(filenameHeaderPattern, new Date().getTime(), filename);
    response.setHeader("Content-Disposition", header);
    response.setContentType(this.getContentType());

    @SuppressWarnings("unchecked")
    Page<Client> clients = (Page<Client>) model.get("clients");
    this.exportService.addDataToDocument(response, clients, this.getMessageSourceAccessor());
  }


}