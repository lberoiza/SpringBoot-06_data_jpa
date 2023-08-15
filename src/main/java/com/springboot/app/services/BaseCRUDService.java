package com.springboot.app.services;

import com.springboot.app.models.entity.Client;
import com.springboot.app.models.entity.EntityTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class BaseCRUDService<E extends EntityTable, R extends JpaRepository<E, Long>> {

  protected R repository;

  @Autowired
  private void setRepository(R repository){
    this.repository = repository;
  }

  public Optional<E> findById(Long entityId) {
    return repository.findById(entityId);
  }

  public Page<E> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public E saveOrUpdate(E entity) {
    return repository.save(entity);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  public void delete(E entity) {
    repository.delete(entity);
  }

  public void deleteAll() {
    repository.deleteAll();
  }

  public Long count(){
    return repository.count();
  }
}
