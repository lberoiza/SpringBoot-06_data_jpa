package com.springboot.app.models.dao;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.entity.Client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Primary
@Repository("ClientDaoJpa")
public class ClientDaoJpa implements IClientDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  @Transactional(readOnly = true)
  public List<Client> findAll() {
    return em.createQuery("from Client", Client.class).getResultList();
  }

  @Override
  @Transactional
  public void save(Client client) {
    em.persist(client);
  }

}
