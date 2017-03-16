package main.java.dao;

import main.java.model.Company;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DaoCompany implements DaoCompanyI {
    INSTANCE;

    private Connection connect = null;

    private Connection getConnection(){
        if(connect == null){
            try {
                connect = DriverManager.getConnection(url, user, pass);
                logger.debug("Getting connection");
            } catch (SQLException e) {
                logger.error("Error getting connection" + e.getMessage() + e.getSQLState() + e.getStackTrace() );
            }
        }
        return connect;
    }

    public long create(Company c){
        connect = getConnection();
        long generatedKey = 0;
        try {

            PreparedStatement p = connect.prepareStatement("INSERT INTO company " +
                    "(name) " +
                    "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            p.setString(1, c.getName());
            long affectedRows = p.executeUpdate();

            if(affectedRows > 0) {
                ResultSet rs = p.getGeneratedKeys();
                rs.next();
                generatedKey = rs.getLong(1);
                c.setId(generatedKey);
            }
            connect.close();
            connect = null;
            logger.info(" Company created, generated ID : " +generatedKey);
        }catch(SQLException e){
            logger.error("Error creating company" + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }
        return generatedKey;
    }


    public void update(Company c){
        try {
            connect = getConnection();
            PreparedStatement p = connect.prepareStatement("UPDATE company c SET " +
                    "name = ? "+
                    " WHERE c.id = ?");
            p.setString(1, c.getName());

            long affectedRows = p.executeUpdate();

            connect.close();
            connect = null;
            logger.info(affectedRows + " rows updated" );
        }catch(SQLException e){
            logger.error("Error updating entry" + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }
    }

    public ArrayList<Company> selectAll(long min, long max){
        ArrayList<Company> resultList = new ArrayList<>();
        ResultSet rs;
        try {
            connect = getConnection();
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
            connect.close();
            connect = null;
        }catch(SQLException e){
            logger.error("Error retrieving companies" + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }

        return resultList;
    }

    public Company getById(long id){
        ResultSet rs;
        Company c = new Company();
        try {
            connect = getConnection();
            PreparedStatement p = connect.prepareStatement("SELECT * FROM computer WHERE computer.id = ?");
            p.setLong(1, id);

            rs = p.executeQuery();

            while (rs.next()) {
                LocalDateTime intro = rs.getTimestamp("introduced") == null ? null : rs.getTimestamp("introduced").toLocalDateTime();
                LocalDateTime disco = rs.getTimestamp("discontinued") == null ? null : rs.getTimestamp("discontinued").toLocalDateTime();

                c = new Company(rs.getLong("id"),
                        rs.getString("name"));
            }

            connect.close();
            connect = null;

        }catch(SQLException e){
            logger.error("Error retrieving company of ID "+ id + "%n" + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }

        return c;
    }

    public void delete(long id){
        try {
            connect = getConnection();
            PreparedStatement p = connect.prepareStatement("DELETE FROM company WHERE computer.id = ?");
            p.setLong(1, id);

            long affectedRows = p.executeUpdate();

            connect.close();
            connect = null;
            logger.info(affectedRows + " rows updated" );
        }catch(SQLException e){
            logger.error("Error deleting company of ID "+ id + "%n" + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }

    }
}
