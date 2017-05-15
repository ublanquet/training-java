package persistance.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import persistance.model.Company;
import persistance.model.GenericBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class DaoCompany implements DaoCompanyI {
  @Autowired
  private JdbcTemplate jdbcTemplate;


  /**
     * create company in db.
     * @param c company obj
     * @return generated id
     */
    public Long create(Company c) {
      long generatedKey = 0;
      try {
        SimpleJdbcInsert inserter =
            new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("company")
                .usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("name", c.getName());
        Number newId = inserter.executeAndReturnKey(parameters);
        generatedKey = newId.longValue();
        c.setId(generatedKey);
        LOGGER.info(" Company created, generated ID : " + generatedKey);
      } catch (Exception e) {
        LOGGER.error("Error creating company " + e.getMessage() + e.getStackTrace());
      }
      return generatedKey;
    }

    /**
     * uate company in db.
     * @param c company obj
     */
    public void update(Company c) {
      int affectedRows = 0;
      try {
        Object[] params = {c.getName(), c.getId()};
        String sql = "UPDATE company SET " +
            "name = ?" +
            " WHERE company.id = ?";

        affectedRows = jdbcTemplate.update(sql, params);
        LOGGER.info(affectedRows + " rows updated");
      } catch (Exception e) {
        LOGGER.error("Error updating company of ID " + c.getId() + e.getMessage() + e.getStackTrace());
      }
      //return affectedRows;
    }

    /**
     * retrieve all companies.
     * @param min offset
     * @param max nb to return
     * @return companies list
     */
    public ArrayList<Company> selectAll(Long min, Long max) {
      ArrayList<Company> resultList = new ArrayList<>();
      try {
        String sql = "SELECT * FROM company " +
            "LIMIT ?, ?";
        resultList = (ArrayList<Company>) this.jdbcTemplate.query(
            sql, new Object[] {min, max}, new CompanyMapper());
      } catch (Exception e) {
        LOGGER.error("Error getting companies" + e.getMessage() + e.getStackTrace());
      }

      return resultList;
    }

    /**
     * retrieve all companies.
     * @return companies list
     */
    public ArrayList<Company> selectAll() {
      ArrayList<Company> resultList = new ArrayList<>();
      try {
        String sql = "SELECT * FROM company ";
        resultList = (ArrayList<Company>) this.jdbcTemplate.query(
            sql, new Object[] {}, new CompanyMapper());
      } catch (Exception e) {
        LOGGER.error("Error getting companies" + e.getMessage() + e.getStackTrace());
      }

      return resultList;
    }

    /**
     * retrieve company by id.
     * @param id id
     * @return company obj
     */
    public Company getById(Long id) {
      Company c = new Company();
      try {
        String sql = "SELECT * FROM company WHERE company.id = ?";

        c = (Company) this.jdbcTemplate.queryForObject(
            sql, new Object[] {id }, new CompanyMapper());
      } catch (Exception e) {
        LOGGER.error("Error retrieving company of ID " + id + e.getMessage() + e.getStackTrace());
      }

      return c;
    }

    /**
     * delete from db.
     * @param id id
     * @return deleted rows numbers
     */
    public int delete(Long id) {
      int affectedRows = 0;
      try {
        String sql = "DELETE FROM company WHERE company.id = ?";
        Object[] params = {id};
        affectedRows = jdbcTemplate.update(sql, params);
        LOGGER.info(affectedRows + " rows updated");
      } catch (Exception e) {
        LOGGER.error("Error deleting company of ID " + id + e.getMessage() + e.getStackTrace());
      }
      return affectedRows;
    }
}

class CompanyMapper implements RowMapper<Company> {
  /**
   * JDBCTemplate row to computer object mapper.
   * @param rs rs
   * @param rowNum rownum
   * @return computer
   * @throws SQLException ex
   */
  public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
    Company c = GenericBuilder.of(Company::new)
        .with(Company::setId, rs.getLong("company.id"))
        .with(Company::setName, rs.getString("company.name"))
        .build();
    return c;
  }
}