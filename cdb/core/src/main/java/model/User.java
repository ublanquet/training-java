package model;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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
  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> role;
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
    return role;
  }
  public void setAuthorities(List<Role> authorities) {
    this.role = authorities;
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
