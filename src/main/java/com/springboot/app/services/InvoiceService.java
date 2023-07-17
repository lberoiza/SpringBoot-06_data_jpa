package com.springboot.app.services;

import com.springboot.app.models.dao.IInvoiceDao;
import com.springboot.app.models.entity.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService implements IInvoiceService {

  @Autowired
  private IInvoiceDao invoiceDao;

  @Override
  @Transactional
  public void saveInvoice(Invoice invoice) {
    invoiceDao.save(invoice);
  }

}
