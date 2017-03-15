package main.java.dao;

import main.java.model.Company;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DaoCompany {

    private static final String url = "jdbc:mysql://localhost:3306/computer-database-db?useSSL=false";

    private static final String user = "root";

    private static final String pass = "pass";

    private Connection connect;

    private Connection getInstance(){
        if(connect == null){
            try {
                connect = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }

    public long create(Company c){
        this.connect = getInstance();
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
            System.out.println("Generated key : " + generatedKey);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return generatedKey;
    }


    public void update(Company c){
        try {
            this.connect = getInstance();
            PreparedStatement p = connect.prepareStatement("UPDATE company c SET " +
                    "name = ? "+
                    " WHERE c.id = ?");
            p.setString(1, c.getName());

            long affectedRows = p.executeUpdate();

            connect.close();
            connect = null;
            System.out.println("Affected rows : " + affectedRows);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Company> selectAll(long min, long max){
        ArrayList<Company> resultList = new ArrayList<>();
        ResultSet rs;
        try {
            this.connect = getInstance();
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
            e.printStackTrace();
        }

        return resultList;
    }

    public Company getById(long id){
        ResultSet rs;
        Company c = new Company();
        try {
            this.connect = getInstance();
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
            e.printStackTrace();
        }

        return c;
    }

    public void delete(long id){
        try {
            this.connect = getInstance();
            PreparedStatement p = connect.prepareStatement("DELETE FROM company WHERE computer.id = ?");
            p.setLong(1, id);

            long affectedRows = p.executeUpdate();

            connect.close();
            connect = null;
            System.out.println("Affected rows : " + affectedRows);
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}
