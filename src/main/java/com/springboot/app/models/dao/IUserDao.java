package com.springboot.app.models.dao;

import com.springboot.app.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User, Long> {

  public User findByUsername(String username);

}
