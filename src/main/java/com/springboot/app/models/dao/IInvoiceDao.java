package com.springboot.app.models.dao;

import com.springboot.app.models.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IInvoiceDao extends JpaRepository<Invoice, Long>{

  @Query("select i from Invoice i join fetch i.client c join fetch i.invoiceItems pos join fetch pos.product where i.id = ?1")
  Invoice fetchInvoiceWithClientWithInvoiceItemsWithProduct(Long invoiceId);

}
