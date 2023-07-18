package com.springboot.app.services;

import com.springboot.app.models.entity.Invoice;

import java.util.Optional;

public interface IInvoiceService {

  public Optional<Invoice> findById(Long id);

  public void saveInvoice(Invoice invoice);
}
