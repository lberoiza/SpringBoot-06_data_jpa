package com.springboot.app.services;

import com.springboot.app.models.dao.IInvoiceDao;
import com.springboot.app.models.entity.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InvoiceService extends BaseCRUDService<Invoice, IInvoiceDao> {

  @Transactional(readOnly = true)
  public Optional<Invoice> fetchInvoiceWithClientWithInvoiceItemsWithProduct(Long invoiceId) {
    return Optional.of(repository.fetchInvoiceWithClientWithInvoiceItemsWithProduct(invoiceId));
  }

}
