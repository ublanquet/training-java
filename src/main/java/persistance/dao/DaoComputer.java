package persistance.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import persistance.model.Company;
import persistance.model.Computer;
import persistance.model.GenericBuilder;
import persistance.model.Page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class DaoComputer implements IDaoComputer {
  private final JdbcTemplate jdbcTemplate;

  //STOP_CHECKSTYLE
  @Autowired
  public DaoComputer(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  //START_CHECKSTYLE

  /**
   * create in db.
   *
   * @param c computer
   * @return generated id
   */
  public Long create(Computer c) {
    long generatedKey = 0;
    if (c.getCompanyId() == null || c.getCompanyId() == 0) {
      c.setCompanyId(null);
    }
    try {
      SimpleJdbcInsert inserter =
          new SimpleJdbcInsert(jdbcTemplate)
              .withTableName("computer")
              .usingGeneratedKeyColumns("id");
      SqlParameterSource parameters = new MapSqlParameterSource()
          .addValue("name", c.getName())
          .addValue("introduced", c.getIntroducedTimestamp())
          .addValue("discontinued", c.getDiscontinuedTimestamp())
          .addValue("company_id", c.getCompanyId());
      Number newId = inserter.executeAndReturnKey(parameters);
      generatedKey = newId.longValue();
      c.setId(generatedKey);
      LOGGER.info(" Computer created, generated ID : " + generatedKey);
    } catch (Exception e) {
      LOGGER.error("Error creating computer " + e.getMessage() + e.getStackTrace());
    }
    return generatedKey;
  }

  /**
   * update.
   *
   * @param c computer
   * @return nb affected rows, 0 fail, 1 success
   */
  public int update(Computer c) {
    int affectedRows = 0;
    try {
      Long companyId = null;
      if (c.getCompanyId() != null && c.getCompanyId() != 0) {
        companyId = c.getCompanyId();
      }
      Object[] params = {c.getName(), c.getIntroducedTimestamp(), c.getDiscontinuedTimestamp(), companyId, c.getId()};
      String sql = "UPDATE computer SET " +
          "name = ?, introduced = ?, discontinued = ?, company_id = ?" +
          " WHERE computer.id = ?";

      affectedRows = jdbcTemplate.update(sql, params);
      LOGGER.info(affectedRows + " rows updated");
    } catch (Exception e) {
      LOGGER.error("Error updating computer of ID " + c.getId() + e.getMessage() + e.getStackTrace());
    }
    return affectedRows;
  }

  /**
   * select all.
   *
   * @param min offset
   * @param max nb to return
   * @return list
   */
  public ArrayList<Computer> selectAll(Long min, Long max) {
    ArrayList<Computer> resultList = new ArrayList<>();
    try {
      String sql = "SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "LIMIT ?, ?";
      resultList = (ArrayList<Computer>) this.jdbcTemplate.query(
          sql, new Object[] {min, max}, new ComputerMapper());
    } catch (Exception e) {
      LOGGER.error("Error getting computers" + e.getMessage() + e.getStackTrace());
    }

    return resultList;
  }

  /**
   * Get the number of computers in DB.
   *
   * @return count
   */
  public Long getCount() {
    Long count = null;
    try {
      count = this.jdbcTemplate.queryForObject(" SELECT COUNT(*) FROM computer;", Long.class);
    } catch (Exception e) {
      LOGGER.error("Error getting computers count " + e.getMessage() + e.getStackTrace());
    }
    return count;
  }

  /**
   * Get the number of computers in DB with name like param.
   *
   * @param name name
   * @return count
   */
  public Long getCount(String name) {
    Long count = null;
    try {
      count = this.jdbcTemplate.queryForObject(" SELECT COUNT(*) FROM computer LEFT JOIN company ON computer.company_id = company.id " +
          "WHERE ( computer.name LIKE ? " +
          " OR company.name LIKE ? ) ",
          new Object[] {"%" + name + "%", "%" + name + "%"}, Long.class);
    } catch (Exception e) {
      LOGGER.error("Error getting computers count by name " + e.getMessage() + e.getStackTrace());
    }
    return count;
  }

  /**
   * get computer list paged.
   *
   * @param page the page
   * @return the filled page
   */
  public Page<Computer> selectPaginated(Page page) {
    ArrayList<Computer> resultList = new ArrayList<>();
    try {
      String sql = "SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "LIMIT ? OFFSET ?";
      resultList = (ArrayList<Computer>) this.jdbcTemplate.query(
          sql, new Object[] {page.getNbEntries(), page.getFirstEntryIndex()}, new ComputerMapper());
    } catch (Exception e) {
      LOGGER.error("Error getting computers" + e.getMessage() + e.getStackTrace());
    }
    page.setList(resultList);

    return page;
  }

  /**
   * get computer list paged and filtered by name.
   *
   * @param name name filter
   * @param page the page
   * @return the filled page
   */
  public Page<Computer> selectFiltered(Page page, String name) {
    ArrayList<Computer> resultList = new ArrayList<>();
    try {
      String sql = "SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "WHERE ( computer.name LIKE ? " +
          " OR company.name LIKE ? ) " +
          "LIMIT ? OFFSET ?";

      resultList = (ArrayList<Computer>) this.jdbcTemplate.query(
          sql, new Object[] {"%" + name + "%", "%" + name + "%", page.getNbEntries(), page.getFirstEntryIndex()}, new ComputerMapper());

      LOGGER.debug("search filter : " + name);
    } catch (Exception e) {
      LOGGER.error("Error getting computers" + e.getMessage() + e.getStackTrace());
    }

    page.setList(resultList);

    return page;
  }

  /**
   * get computer list paged and filtered by name.
   *
   * @param name name filter
   * @param page the page
   * @param order order
   * @return the filled page
   */
  public Page<Computer> selectFiltered(Page page, String name, String order) {
    ArrayList<Computer> resultList = new ArrayList<>();
    try {

      String sql = "SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "WHERE ( computer.name LIKE ? " +
          " OR company.name LIKE ? ) " +
          "ORDER BY ";
      sql += order;
      sql += " LIMIT ? OFFSET ?";

      resultList = (ArrayList<Computer>) this.jdbcTemplate.query(
          sql, new Object[] {"%" + name + "%", "%" + name + "%", page.getNbEntries(), page.getFirstEntryIndex()}, new ComputerMapper());
    } catch (Exception e) {
      LOGGER.error("Error getting computers" + e.getMessage() + e.getStackTrace());
    }

    page.setList(resultList);

    return page;
  }

  /**
   * get by id.
   *
   * @param id id
   * @return computer
   */
  public Computer getById(Long id) {
    Computer c = new Computer();
    try {
      String sql = "SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.id = ?";

      c = (Computer) this.jdbcTemplate.queryForObject(
          sql, new Object[] {id }, new ComputerMapper());
    } catch (Exception e) {
      LOGGER.error("Error retrieving computer of ID " + id + e.getMessage() + e.getStackTrace());
    }

    return c;
  }

  /**
   * delete.
   *
   * @param id id
   * @return nb affected rows, 0 fail, 1 success
   */
  public int delete(Long id) {
    int affectedRows = 0;
    try {
      String sql = "DELETE FROM computer WHERE computer.id = ?";
      Object[] params = {id};
      affectedRows = jdbcTemplate.update(sql, params);
      LOGGER.info(affectedRows + " rows updated");
    } catch (Exception e) {
      LOGGER.error("Error deleting computer of ID " + id + e.getMessage() + e.getStackTrace());
    }
    return affectedRows;
  }

  /**
   * delete.
   *
   * @param ids ids
   * @return nb affected rows, 0 fail, 1 success
   */
  public int delete(ArrayList<Long> ids) {
    int affectedRows = 0;
    try {
      String sql = "DELETE FROM computer WHERE computer.id IN (";
      int i;
      for (i = 1; i < ids.size(); i++) {
        sql += "?,";
      }
      sql += "?)";

      ArrayList<Object> paramsList = new ArrayList<>();
      for (i = 0; i < ids.size(); i++) {
        paramsList.add(ids.get(i));
      }

      Object[] params = paramsList.toArray();
      affectedRows = jdbcTemplate.update(sql, params);
      LOGGER.info(affectedRows + " rows updated");
    } catch (Exception e) {
      LOGGER.error("Error deleting computers, ids :" + ids.toString() + e.getMessage() + e.getStackTrace());
    }
    return affectedRows;
  }

  /**
   * delete.
   *
   * @param id id
   * @return nb affected rows, 0 fail, 1 success
   */
  public int deleteByCompanyId(Long id) {
    int affectedRows = 0;
    Connection connect = Utils.getConnection();
    try {
      String sql = "DELETE FROM computer WHERE computer.company_id = ?";
      affectedRows = jdbcTemplate.update(sql, new Object[] {id });

      LOGGER.info(affectedRows + " rows updated");
    } catch (Exception e) {
      LOGGER.error("Error deleting computer of ID " + id + e.getMessage() + e.getStackTrace());
    }
    return affectedRows;
  }
}

class ComputerMapper implements RowMapper<Computer> {
  /**
   * JDBCTemplate row to computer object mapper.
   * @param rs rs
   * @param rowNum rownum
   * @return computer
   * @throws SQLException ex
   */
  public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
    Computer c = GenericBuilder.of(Computer::new)
        .with(Computer::setId, rs.getLong("computer.id"))
        .with(Computer::setName, rs.getString("computer.name"))
        .with(Computer::setIntroducedTimestamp, rs.getTimestamp("introduced"))
        .with(Computer::setDiscontinuedTimestamp, rs.getTimestamp("discontinued"))
        .with(Computer::setCompanyId, rs.getLong("company_id"))
        .with(Computer::setCompany, rs.getLong("company.id") != 0 ? new Company(rs.getLong("company.id"), rs.getString("company.name")) : null)
        .build();
    return c;
  }
}