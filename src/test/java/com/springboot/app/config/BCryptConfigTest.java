package com.springboot.app.config;

import com.springboot.app.SpringBootDataJpaApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class BCryptConfigTest {

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;



  @Test
  public void encodePass(){
    String pass = "user";
    String encodePass = passwordEncoder.encode(pass);

    Assertions.assertNotEquals(pass, encodePass, "The password are the same");
    Assertions.assertEquals(60, encodePass.length(), "The encoded password has not 60 characters");
  }

  @Test
  public void encodePassNotSame(){
    String pass = "user";
    String encodePass1 = passwordEncoder.encode(pass);
    String encodePass2 = passwordEncoder.encode(pass);

    Assertions.assertNotEquals(encodePass1, encodePass2, "The 2 encoded passwords are the same");
  }

}