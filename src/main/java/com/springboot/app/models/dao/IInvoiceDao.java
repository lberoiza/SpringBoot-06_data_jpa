package com.springboot.app.models.dao;

import com.springboot.app.models.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoiceDao extends JpaRepository<Invoice, Long>{

}
