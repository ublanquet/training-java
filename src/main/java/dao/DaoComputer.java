package main.java.dao;

import main.java.model.Computer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DaoComputer {

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

    public void create(Computer c){
        Timestamp intro = c.getIntroduced() == null ? null : Timestamp.valueOf( c.getIntroduced() );
        Timestamp disco = c.getIntroduced() == null ? null : Timestamp.valueOf( c.getIntroduced() );

        this.connect = getInstance();
        try {

            PreparedStatement p = connect.prepareStatement("INSERT INTO computer c " +
                    "(c.name, c.introduced, c.discontinued, c.companyId) " +
                    "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            p.setString(1, c.getName());
            if(intro != null) {
                p.setTimestamp(2, intro);
            }
            else{
                //p.setNull(2, java.sql.Types.TIMESTAMP);
                p.setString(2, "NULL");
                //p.setTimestamp(2, null);
            }
            if(disco != null) {
                p.setTimestamp(3, disco);
            }
            else{
                //p.setString(3, "NULL");
                //p.setTimestamp(3, null);
                //p.setNull(3, java.sql.Types.TIMESTAMP);
            }
            p.setLong(4, c.getCompanyId());

            long generatedKey = p.executeUpdate();

            connect.close();
            connect = null;
            System.out.println("Generated ID : " + generatedKey);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void update(Computer c){
        Timestamp intro = c.getIntroduced() == null ? null : Timestamp.valueOf( c.getIntroduced() );
        Timestamp disco = c.getIntroduced() == null ? null : Timestamp.valueOf( c.getIntroduced() );

        try {
            this.connect = getInstance();
            PreparedStatement p = connect.prepareStatement("UPDATE computer c SET " +
                    "name = ?, introduced = ?, discontinued = ?, companyId = ?"+
                    "WHERE c.id = ?");


            p.setString(1, c.getName());
            if(intro != null) {
                p.setTimestamp(2, intro);
            }
            else{
                p.setNull(2, java.sql.Types.TIMESTAMP);
            }
            if(disco != null) {
                p.setTimestamp(3, disco);
            }
            else{
                p.setNull(3, java.sql.Types.TIMESTAMP);
            }
            p.setLong(4, c.getCompanyId());
            p.setLong(5, c.getId());

            long affectedRows = p.executeUpdate();
            connect.close();
            connect = null;
            System.out.println("Affected Rows : " + affectedRows);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Computer> selectAll(long min, long max){
        ArrayList<Computer> resultList = new ArrayList<>();
        ResultSet rs;
        try {
            this.connect = getInstance();
            PreparedStatement p = connect.prepareStatement("SELECT * FROM computer c " +
                    "LIMIT ?, ?");
            p.setLong(1, min);
            p.setLong(2, max);

            rs = p.executeQuery();

            while (rs.next()) {
                Computer c;
                LocalDateTime intro = rs.getTimestamp("introduced") == null ? null : rs.getTimestamp("introduced").toLocalDateTime();
                LocalDateTime disco = rs.getTimestamp("discontinued") == null ? null : rs.getTimestamp("discontinued").toLocalDateTime();

                c = new Computer(rs.getLong("id"),
                        rs.getLong("company_id"),
                        rs.getString("name"),
                        intro,
                        disco);
                resultList.add(c);
            }
            connect.close();
            connect = null;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return resultList;
    }

    public Computer getById(long id){
        ResultSet rs;
        Computer c = new Computer();
        try {
            this.connect = getInstance();
            PreparedStatement p = connect.prepareStatement("SELECT * FROM computer WHERE computer.id = ?");
            p.setLong(1, id);

            rs = p.executeQuery();

            while (rs.next()) {
                LocalDateTime intro = rs.getTimestamp("introduced") == null ? null : rs.getTimestamp("introduced").toLocalDateTime();
                LocalDateTime disco = rs.getTimestamp("discontinued") == null ? null : rs.getTimestamp("discontinued").toLocalDateTime();

                c = new Computer(rs.getLong("id"),
                        rs.getLong("company_id"),
                        rs.getString("name"),
                        intro,
                        disco);
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
            PreparedStatement p = connect.prepareStatement("DELETE FROM computer WHERE computer.id = ?");
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
