package persistance.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import persistance.model.Computer;

import java.util.List;
//STOP_CHECKSTYLE

public interface ComputerRepository extends JpaRepository<Computer, Long> {

  List<Computer> findByName(String name);


  Computer findById(Long id);
}
//START_CHECKSTYLE
