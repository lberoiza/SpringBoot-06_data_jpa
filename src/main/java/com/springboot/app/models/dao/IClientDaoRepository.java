package com.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Client;
import org.springframework.data.jpa.repository.Query;

public interface IClientDaoRepository extends JpaRepository<Client, Long>{

//  @Query("select c from Client c left join fetch c.invoices i join fetch i.invoiceItems pos join fetch pos.product where c.id = ?1")
  @Query("select c from Client c left join fetch c.invoices where c.id = ?1")
  Client fetchClientByIdWithInvoices(Long clientId);
}
