package model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "user")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String login;
  private String pass;

  /* Spring Security related fields*/
  @OneToMany(fetch = FetchType.EAGER)
  private List<Role> authorities;
  @Transient
  private boolean accountNonExpired = true;
  @Transient
  private boolean accountNonLocked = true;
  @Transient
  private boolean credentialsNonExpired = true;
  private boolean enabled = true;

  /**
   * .
   */
  public User() {
  }


  /**
   * .
   * @param login .
   * @param pass .
   */
  public User(String login, String pass) {
    this.login = login;
    this.pass = pass;
  }

  @Override
  public String getPassword() {
    return this.pass;
  }

  @Override
  public String getUsername() {
    return this.login;
  }

  public List<Role> getAuthorities() {
    return authorities;
  }
  public void setAuthorities(List<Role> authorities) {
    this.authorities = authorities;
  }
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }
  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }
  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }
  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }
  public boolean isEnabled() {
    return enabled;
  }
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}

@Entity
@Table(name = "role")
class Role implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthority() {
    return this.name;
  }
}