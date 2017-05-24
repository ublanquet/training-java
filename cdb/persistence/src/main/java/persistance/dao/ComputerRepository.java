package persistance.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import model.Computer;
import org.springframework.stereotype.Repository;

import java.util.List;
//STOP_CHECKSTYLE
@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {

  List<Computer> findByName(String name);


  Computer findById(Long id);

  //use Pageable obj created by new PageRequest
  Page<Computer> findAll(Pageable pageRequest);

  Page<Computer> findByName(Pageable pageRequest, String name);
  Page<Computer> findByNameOrCompanyName(Pageable pageRequest, String name);



  Long countByName(String name);

  //can use @Query to write straight sql (use EntityName instead of table), can use OrderBy and Like in name, if name too long, use @Query, can use pageable objects for select all, they can use sort objects
  //perf issues : eager/lazy fetching, number of columns, pagination
}
//START_CHECKSTYLE
