package persistance.dao;

import model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//STOP_CHECKSTYLE
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findById(Long id);
  User findByLogin(String login);
  //user repo.save User createUser(String login, String pass, String role);

}
//START_CHECKSTYLE