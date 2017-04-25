package persistance.dao;

import persistance.model.Company;
import persistance.model.Computer;
import persistance.model.GenericBuilder;
import persistance.model.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class DaoComputer implements DaoComputerI {

  /**
   * create in db.
   *
   * @param c computer
   * @return generated id
   */
  public Long create(Computer c) {
    Connection connect = Utils.getConnection();
    long generatedKey = 0;
    try {

      PreparedStatement p = connect.prepareStatement("INSERT INTO computer " +
          "(name, introduced, discontinued, company_id) " +
          "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      p.setString(1, c.getName());
      p.setTimestamp(2, c.getIntroducedTimestamp());
      p.setTimestamp(3, c.getDiscontinuedTimestamp());
      if (c.getCompanyId() != null && c.getCompanyId() != 0) {
        p.setLong(4, c.getCompanyId());
      } else {
        p.setNull(4, Types.BIGINT);
      }

      long affectedRows = p.executeUpdate();

      if (affectedRows > 0) {
        generatedKey = Utils.getGeneratedKey(p);
        c.setId(generatedKey);
      }
      p.close();
      LOGGER.info(" Computer created, generated ID : " + generatedKey);
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error creating computer " + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement("UPDATE computer SET " +
          "name = ?, introduced = ?, discontinued = ?, company_id = ?" +
          " WHERE computer.id = ?");
      p.setString(1, c.getName());
      p.setTimestamp(2, c.getIntroducedTimestamp());
      p.setTimestamp(3, c.getDiscontinuedTimestamp());
      if (c.getCompanyId() != null && c.getCompanyId() != 0) {
        p.setLong(4, c.getCompanyId());
      } else {
        p.setNull(4, Types.BIGINT);
      }
      p.setLong(5, c.getId());
      affectedRows = p.executeUpdate();
      p.close();
      LOGGER.info(affectedRows + " rows updated");
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error updating computer of ID " + c.getId() + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        connect.close();
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    ResultSet rs;
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement("SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "LIMIT ?, ?");
      p.setLong(1, min);
      p.setLong(2, max);

      rs = p.executeQuery();

      while (rs.next()) {
        Computer c = GenericBuilder.of(Computer::new)
            .with(Computer::setId, rs.getLong("computer.id"))
            .with(Computer::setName, rs.getString("computer.name"))
            .with(Computer::setIntroducedTimestamp, rs.getTimestamp("introduced"))
            .with(Computer::setDiscontinuedTimestamp, rs.getTimestamp("discontinued"))
            .with(Computer::setCompanyId, rs.getLong("company_id"))
            .with(Computer::setCompany, rs.getLong("company.id") != 0 ? new Company(rs.getLong("company.id"), rs.getString("company.name")) : null)
            .build();
        resultList.add(c);
      }
      rs.close();
      p.close();
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error getting computers" + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    ResultSet rs;
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement(" SELECT COUNT(*) FROM computer;");
      rs = p.executeQuery();
      while (rs.next()) {
        count = rs.getLong(1);
      }
      rs.close();
      p.close();
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error getting computers count " + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    ResultSet rs;
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement(" SELECT COUNT(*) FROM computer WHERE name LIKE ?;");
      p.setString(1, "%" + name + "%");
      rs = p.executeQuery();
      while (rs.next()) {

        count = rs.getLong(1);
      }
      rs.close();
      p.close();
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error getting computers count by name " + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    ResultSet rs;
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement("SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "LIMIT ? OFFSET ?");
      p.setLong(1, page.getNbEntries());
      p.setLong(2, page.getFirstEntryIndex());

      rs = p.executeQuery();
      //System.out.println(p);
      while (rs.next()) {
        Computer c = GenericBuilder.of(Computer::new)
            .with(Computer::setId, rs.getLong("computer.id"))
            .with(Computer::setName, rs.getString("computer.name"))
            .with(Computer::setIntroducedTimestamp, rs.getTimestamp("introduced"))
            .with(Computer::setDiscontinuedTimestamp, rs.getTimestamp("discontinued"))
            .with(Computer::setCompanyId, rs.getLong("company_id"))
            .with(Computer::setCompany, rs.getLong("company.id") != 0 ? new Company(rs.getLong("company.id"), rs.getString("company.name")) : null)
            .build();
        resultList.add(c);
      }
      rs.close();
      p.close();
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error getting computers" + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    ResultSet rs;
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement("SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "WHERE ( computer.name LIKE ? " +
          " OR company.name LIKE ? ) " +
          "LIMIT ? OFFSET ?");
      LOGGER.debug("search filter : " + name);
      p.setString(1, "%" + name + "%");
      p.setString(2, "%" + name + "%");
      p.setLong(3, page.getNbEntries());
      p.setLong(4, page.getFirstEntryIndex());

      rs = p.executeQuery();

      while (rs.next()) {
        Computer c = GenericBuilder.of(Computer::new)
            .with(Computer::setId, rs.getLong("computer.id"))
            .with(Computer::setName, rs.getString("computer.name"))
            .with(Computer::setIntroducedTimestamp, rs.getTimestamp("introduced"))
            .with(Computer::setDiscontinuedTimestamp, rs.getTimestamp("discontinued"))
            .with(Computer::setCompanyId, rs.getLong("company_id"))
            .with(Computer::setCompany, rs.getLong("company.id") != 0 ? new Company(rs.getLong("company.id"), rs.getString("company.name")) : null)
            .build();
        resultList.add(c);
      }
      rs.close();
      p.close();
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error getting computers" + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    ResultSet rs;
    Connection connect = Utils.getConnection();
    try {

      String sql = "SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "WHERE ( computer.name LIKE ? " +
          " OR company.name LIKE ? ) " +
          "ORDER BY ";
      sql += order;
      sql += " LIMIT ? OFFSET ?";
      PreparedStatement p = connect.prepareStatement(sql);
      p.setString(1, "%" + name + "%");
      p.setString(2, "%" + name + "%");
      p.setLong(3, page.getNbEntries());
      p.setLong(4, page.getFirstEntryIndex());

      System.out.println(p);
      rs = p.executeQuery();
      while (rs.next()) {
        Computer c = GenericBuilder.of(Computer::new)
            .with(Computer::setId, rs.getLong("computer.id"))
            .with(Computer::setName, rs.getString("computer.name"))
            .with(Computer::setIntroducedTimestamp, rs.getTimestamp("introduced"))
            .with(Computer::setDiscontinuedTimestamp, rs.getTimestamp("discontinued"))
            .with(Computer::setCompanyId, rs.getLong("company_id"))
            .with(Computer::setCompany, rs.getLong("company.id") != 0 ? new Company(rs.getLong("company.id"), rs.getString("company.name")) : null)
            .build();
        resultList.add(c);
      }
      rs.close();
      p.close();
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error getting computers" + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    ResultSet rs;
    Computer c = new Computer();
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement("SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
          "WHERE computer.id = ?");
      p.setLong(1, id);

      rs = p.executeQuery();

      while (rs.next()) {
        c = GenericBuilder.of(Computer::new)
            .with(Computer::setId, rs.getLong("computer.id"))
            .with(Computer::setName, rs.getString("computer.name"))
            .with(Computer::setIntroducedTimestamp, rs.getTimestamp("introduced"))
            .with(Computer::setDiscontinuedTimestamp, rs.getTimestamp("discontinued"))
            .with(Computer::setCompanyId, rs.getLong("company_id"))
            .with(Computer::setCompany, rs.getLong("company.id") != 0 ? new Company(rs.getLong("company.id"), rs.getString("company.name")) : null)
            .build();
      }
      rs.close();
      p.close();
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error retrieving computer of ID " + id + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          Utils.closeConnection();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    Connection connect = Utils.getConnection();
    try {
      PreparedStatement p = connect.prepareStatement("DELETE FROM computer WHERE computer.id = ?");
      p.setLong(1, id);

      affectedRows = p.executeUpdate();

      p.close();
      LOGGER.info(affectedRows + " rows updated");
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error deleting computer of ID " + id + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          connect.close();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
    Connection connect = Utils.getConnection();
    try {
      String sql = "DELETE FROM computer WHERE computer.id IN (";
      int i;
      for (i = 1; i < ids.size(); i++) {
        sql += "?,";
      }
      sql += "?)";


      PreparedStatement p = connect.prepareStatement(sql);

      for (i = 1; i <= ids.size(); i++) {
        p.setLong(i, ids.get(i));
      }

      affectedRows = p.executeUpdate();

      p.close();
      LOGGER.info(affectedRows + " rows updated");
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error deleting computers, ids :" + ids.toString() + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        if (connect.getAutoCommit()) {
          connect.close();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
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
      PreparedStatement p = connect.prepareStatement("DELETE FROM computer WHERE computer.company_id = ?");
      p.setLong(1, id);

      affectedRows = p.executeUpdate();

      p.close();
      LOGGER.info(affectedRows + " rows updated");
    } catch (SQLException e) {
      try {
        if (!connect.getAutoCommit()) {
          connect.rollback();
        }
      } catch (SQLException ex) {
        LOGGER.error("Error during transaction rollback");
      }
      LOGGER.error("Error deleting computer of ID " + id + e.getMessage() + e.getSQLState() + e.getStackTrace());
    } finally {
      try {
        connect.close();

      } catch (SQLException ex) {
        LOGGER.error("Error closing connection");
      }
    }
    return affectedRows;
  }


  /**
   * start transaction.
   */
  public void startTransaction() {
    Utils.startTransaction();
  }

  /**
   * commit transaction.
   */
  public void commitTransaction() {
    Utils.commitTransaction();
  }

}
