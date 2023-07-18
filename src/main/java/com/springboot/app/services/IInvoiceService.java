package com.springboot.app.services;

import com.springboot.app.models.entity.Invoice;

import java.util.Optional;

public interface IInvoiceService {

  Optional<Invoice> findById(Long id);

  void saveInvoice(Invoice invoice);

  void deleteById(Long id);

  Optional<Invoice> fetchInvoiceWithClientWithInvoiceItemsWithProduct(Long invoiceId);
}
