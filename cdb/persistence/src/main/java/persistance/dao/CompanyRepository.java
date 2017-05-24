package persistance.dao;

import model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//STOP_CHECKSTYLE
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
  Company findById(Long id);

}
//START_CHECKSTYLE
