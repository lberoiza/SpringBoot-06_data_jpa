package com.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Client;

public interface IClientDaoRepository extends JpaRepository<Client, Long>{

}
