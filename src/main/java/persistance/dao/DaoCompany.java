package persistance.dao;

import persistance.model.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DaoCompany implements DaoCompanyI {
    /**
     * create company in db.
     * @param c company obj
     * @return generated id
     */
    public Long create(Company c) {
        Connection connect = Utils.getConnection();
        long generatedKey = 0;
        try {

            PreparedStatement p = connect.prepareStatement("INSERT INTO company " +
                    "(name) " +
                    "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            p.setString(1, c.getName());
            long affectedRows = p.executeUpdate();

            if (affectedRows > 0) {
                generatedKey = Utils.getGeneratedKey(p);
                c.setId(generatedKey);
            }
            p.close();
            LOGGER.info(" Company created, generated ID : " + generatedKey);
        } catch (SQLException e) {
            LOGGER.error("Error creating company" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        } finally {
          try {
            connect.close();
          } catch (SQLException ex) {
            LOGGER.error("Error closing connection");
          }
        }
        return generatedKey;
    }

    /**
     * uate company in db.
     * @param c company obj
     */
    public void update(Company c) {
      Connection connect = Utils.getConnection();
        try {
            PreparedStatement p = connect.prepareStatement("UPDATE company SET " +
                    "name = ? " +
                    " WHERE company.id = ?");
            p.setString(1, c.getName());

            long affectedRows = p.executeUpdate();

            p.close();
            LOGGER.info(affectedRows + " rows updated");
        } catch (SQLException e) {
            LOGGER.error("Error updating entry" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        } finally {
          try {
            connect.close();
          } catch (SQLException ex) {
            LOGGER.error("Error closing connection");
          }
        }
    }

    /**
     * retrieve all companies.
     * @param min offset
     * @param max nb to return
     * @return companies list
     */
    public ArrayList<Company> selectAll(Long min, Long max) {
        ArrayList<Company> resultList = new ArrayList<>();
        ResultSet rs;
      Connection connect = Utils.getConnection();
      try {
            PreparedStatement p = connect.prepareStatement("SELECT * FROM company " +
                    "LIMIT ?, ?");
            p.setLong(1, min);
            p.setLong(2, max);

            rs = p.executeQuery();

            while (rs.next()) {
                Company c = new Company(rs.getLong("id"),
                        rs.getString("name"));
                resultList.add(c);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            LOGGER.error("Error retrieving companies" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        } finally {
          try {
            connect.close();
          } catch (SQLException ex) {
            LOGGER.error("Error closing connection");
          }
        }

        return resultList;
    }

    /**
     * retrieve all companies.
     * @return companies list
     */
    public ArrayList<Company> selectAll() {
        ArrayList<Company> resultList = new ArrayList<>();
        ResultSet rs;
      Connection connect = Utils.getConnection();
      try {
            PreparedStatement p = connect.prepareStatement("SELECT * FROM company;");

            rs = p.executeQuery();

            while (rs.next()) {
                Company c = new Company(rs.getLong("id"),
                    rs.getString("name"));
                resultList.add(c);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            LOGGER.error("Error retrieving companies" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        } finally {
          try {
            connect.close();

          } catch (SQLException ex) {
            LOGGER.error("Error closing connection");
          }
        }

        return resultList;
    }

    /**
     * retrieve company by id.
     * @param id id
     * @return company obj
     */
    public Company getById(Long id) {
        ResultSet rs;
        Company c = new Company();
      Connection connect = Utils.getConnection();
      try {
            PreparedStatement p = connect.prepareStatement("SELECT * FROM company WHERE company.id = ?");
            p.setLong(1, id);

            rs = p.executeQuery();

            while (rs.next()) {
                LocalDateTime intro = rs.getTimestamp("introduced") == null ? null : rs.getTimestamp("introduced").toLocalDateTime();
                LocalDateTime disco = rs.getTimestamp("discontinued") == null ? null : rs.getTimestamp("discontinued").toLocalDateTime();

                c = new Company(rs.getLong("id"),
                        rs.getString("name"));
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            LOGGER.error("Error retrieving company of ID " + id + "%n" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        } finally {
          try {
            connect.close();
          } catch (SQLException ex) {
            LOGGER.error("Error closing connection");
          }
        }

        return c;
    }

    /**
     * delete from db.
     * @param id id
     * @return deleted rows numbers
     */
    public int delete(Long id) {
      Connection connect = Utils.getConnection();
      try {
            PreparedStatement p = connect.prepareStatement("DELETE FROM company WHERE company.id = ?");
            p.setLong(1, id);
            int affectedRows = p.executeUpdate();
            p.close();
            LOGGER.info(affectedRows + " company deleted, id : " + id);
            return affectedRows;
        } catch (SQLException e) {
            LOGGER.error("Error deleting company of ID " + id + "%n" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        } finally {
            try {
              connect.close();
            } catch (SQLException ex) {
                LOGGER.error("Error closing connection");
            }
        }
        return 0;
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
