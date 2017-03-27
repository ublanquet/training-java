package dao;

import model.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public enum DaoCompany implements DaoCompanyI {
    INSTANCE;

    private Connection connect = null;

    /**
     * create company in db.
     * @param c company obj
     * @return generated id
     */
    public Long create(Company c) {
        connect = Utils.getConnection(connect);
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
            connect.close();
            LOGGER.info(" Company created, generated ID : " + generatedKey);
        } catch (SQLException e) {
            LOGGER.error("Error creating company" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }
        return generatedKey;
    }

    /**
     * uate company in db.
     * @param c company obj
     */
    public void update(Company c) {
        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("UPDATE company c SET " +
                    "name = ? " +
                    " WHERE c.id = ?");
            p.setString(1, c.getName());

            long affectedRows = p.executeUpdate();

            p.close();
            connect.close();
            LOGGER.info(affectedRows + " rows updated");

        } catch (SQLException e) {
            LOGGER.error("Error updating entry" + e.getMessage() + e.getSQLState() + e.getStackTrace());
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
        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("SELECT * FROM company c " +
                    "LIMIT ?, ?");
            p.setLong(1, min);
            p.setLong(2, max);

            rs = p.executeQuery();

            while (rs.next()) {
                Company c = new Company(rs.getLong("id"),
                        rs.getString("name"));
                resultList.add(c);
            }
            p.close();
            connect.close();
        } catch (SQLException e) {
            LOGGER.error("Error retrieving companies" + e.getMessage() + e.getSQLState() + e.getStackTrace());
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
        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("SELECT * FROM company;");

            rs = p.executeQuery();

            while (rs.next()) {
                Company c = new Company(rs.getLong("id"),
                    rs.getString("name"));
                resultList.add(c);
            }
            p.close();
            connect.close();
        } catch (SQLException e) {
            LOGGER.error("Error retrieving companies" + e.getMessage() + e.getSQLState() + e.getStackTrace());
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
        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("SELECT * FROM computer WHERE computer.id = ?");
            p.setLong(1, id);

            rs = p.executeQuery();

            while (rs.next()) {
                LocalDateTime intro = rs.getTimestamp("introduced") == null ? null : rs.getTimestamp("introduced").toLocalDateTime();
                LocalDateTime disco = rs.getTimestamp("discontinued") == null ? null : rs.getTimestamp("discontinued").toLocalDateTime();

                c = new Company(rs.getLong("id"),
                        rs.getString("name"));
            }

            p.close();
            connect.close();
        } catch (SQLException e) {
            LOGGER.error("Error retrieving company of ID " + id + "%n" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }

        return c;
    }

    /**
     * delete from db.
     * @param id id
     */
    public void delete(Long id) {
        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("DELETE FROM company WHERE computer.id = ?");
            p.setLong(1, id);

            long affectedRows = p.executeUpdate();

            p.close();
            connect.close();
            LOGGER.info(affectedRows + " rows updated");
        } catch (SQLException e) {
            LOGGER.error("Error deleting company of ID " + id + "%n" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }

    }
}
