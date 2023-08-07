package com.springboot.app.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(
    name = "authorities",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "authority"})}
)
public class Role implements Serializable {
  @Serial
  private static final long serialVersionUID = 7921655764507873733L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String authority;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }
}
