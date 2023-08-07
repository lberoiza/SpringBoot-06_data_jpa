package com.springboot.app.services;

import com.springboot.app.models.dao.IUserDao;
import com.springboot.app.models.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class JpaUserDetailsService implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(JpaUserDetailsService.class);

  private final IUserDao userDao;

  @Autowired
  public JpaUserDetailsService(IUserDao userDao) {
    this.userDao = userDao;
  }


  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.findUser(username);
    List<GrantedAuthority> authorities = this.getAuthorities(user);
    return this.createUserDetails(user, authorities);
  }


  private User findUser(String username) throws UsernameNotFoundException {
    User user = userDao.findByUsername(username);

    if (user == null) {
      String error = String.format("User '%s' was not found", username);
      log.error(error);
      throw new UsernameNotFoundException(error);
    }

    return user;
  }

  private List<GrantedAuthority> getAuthorities(User user) throws UsernameNotFoundException {
    List<GrantedAuthority> authorities = new ArrayList<>();
    user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getAuthority())));

    if (authorities.isEmpty()) {
      String error = String.format("The User '%s' has not authorities", user.getUsername());
      log.error(error);
      throw new UsernameNotFoundException(error);
    }

    return authorities;
  }


  private UserDetails createUserDetails(User user, List<GrantedAuthority> authorities) {
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        user.getEnabled(),
        true,
        true,
        true,
        authorities
    );
  }


}
