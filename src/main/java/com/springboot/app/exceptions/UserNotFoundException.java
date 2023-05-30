package com.springboot.app.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;
  private Integer userId;
  private Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  private Date timestamp;

  private UserNotFoundException(Integer id) {
    this.userId = id;
    this.timestamp = new Date();
  }

  static public UserNotFoundException fromId(Integer id) {
    UserNotFoundException uNfEx = new UserNotFoundException(id);
    return uNfEx;
  }

  @Override
  public String getMessage() {
    return String.format("The User with id '%d' was not found", this.userId);
  }

  public Integer getUserId() {
    return userId;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public Date getTimestamp() {
    return timestamp;
  }

}
