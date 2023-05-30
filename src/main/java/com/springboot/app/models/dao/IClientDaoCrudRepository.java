package com.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Client;

public interface IClientDaoCrudRepository extends CrudRepository<Client, Long>{

}
