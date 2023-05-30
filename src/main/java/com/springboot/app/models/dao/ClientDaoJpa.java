package com.springboot.app.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.springboot.app.models.entity.Client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Primary
@Repository("ClientDaoJpa")
public class ClientDaoJpa implements IClientDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Client> findAll() {
    return em.createQuery("from Client", Client.class).getResultList();
  }

  @Override
  public Optional<Client> findById(Long id) {
    return Optional.ofNullable(em.find(Client.class, id));
  }

  @Override
  public void save(Client client) {
    em.persist(client);
  }

  @Override
  public void update(Client client) {
    if (!client.hasValidId()) {
      throw new IllegalStateException(String.format("The User has not a correrct Id: (%s)", client.getId()));
    }
    em.merge(client);
  }

  @Override
  public void deleteById(Long id) {
    findById(id).ifPresent(client -> em.remove(client));
  }

}
