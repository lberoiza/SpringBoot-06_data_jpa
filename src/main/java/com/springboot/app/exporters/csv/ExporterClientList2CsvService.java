package com.springboot.app.exporters.csv;


import com.springboot.app.exporters.ExporterService;
import com.springboot.app.models.entity.Client;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;

@Service("ExporterClientList2CsvService")
public class ExporterClientList2CsvService implements ExporterService<HttpServletResponse, Page<Client>> {
  @Override
  public void addDataToDocument(HttpServletResponse response, Page<Client> pageOfClients, MessageSourceAccessor messageSourceAccessor) {
    try (ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)){
      String[] header = {"id", "name", "surname", "email", "createdAt", "updatedAt"};
      beanWriter.writeHeader(header);
      for(Client client : pageOfClients){
        beanWriter.write(client, header);
      }
    }catch (IOException ioException) {
      System.err.println(ioException.toString());
    }
  }
}
