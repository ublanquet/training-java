package services;

import model.User;
import model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import persistance.dao.UserRepository;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = userRepository.findByLogin(s);
    if (user == null) {
      throw new UsernameNotFoundException(s);
    }
    return user;

  }

  /**
   * .
   * @param user .
   * @param authorities .
   * @return .
   */
  private org.springframework.security.core.userdetails.User buildUserForAuthentication(model.User user, List<GrantedAuthority> authorities) {
    String password = user.getPassword();
    return new org.springframework.security.core.userdetails.User(user.getUsername(), password, user.isEnabled(), true, true, true, authorities);
  }

  /**
   * .
   * @param userRoles .
   * @return .
   */
  private List<GrantedAuthority> buildUserAuthority(List<Role> userRoles) {
    Set<GrantedAuthority> setAuths = new HashSet<>();
    // Build user's authorities
     for (Role userRole : userRoles) {
        setAuths.add(new SimpleGrantedAuthority(userRole.getAuthority()));
       }
       List<GrantedAuthority> result = new ArrayList<>(setAuths);
     return result;
  }
}
