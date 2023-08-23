package com.springboot.app.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serial;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem implements EntityTable {

  @Serial
  private static final long serialVersionUID = 7273747430179859851L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer quantity;

  @JoinColumn(name = "product_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Product product;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Product getProduct() {
    return product;
  }

  public Long getProductId() {
    return this.product.getId();
  }

  public String getProductName() {
    return this.product.getName();
  }

  public Double getProductPrice() {
    return this.product.getPrice();
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Double getAmount() {
    return this.quantity.doubleValue() * this.product.getPrice();
  }

}
