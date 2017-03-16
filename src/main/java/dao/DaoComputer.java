package main.java.dao;

import main.java.model.Computer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public enum DaoComputer implements DaoComputerI {
    INSTANCE;

    private Connection connect;


    public long create(Computer c){
        Timestamp intro = c.getIntroduced() == null ? null : Timestamp.valueOf( c.getIntroduced() );
        Timestamp disco = c.getDiscontinued() == null ? null : Timestamp.valueOf( c.getDiscontinued() );

        connect = Utils.getConnection(connect);
        long generatedKey = 0;
        try {

            PreparedStatement p = connect.prepareStatement("INSERT INTO computer " +
                    "(name, introduced, discontinued, company_id) " +
                    "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            p.setString(1, c.getName());
            p.setTimestamp(2, intro);
            p.setTimestamp(3, disco);
            p.setLong(4, c.getCompanyId());

            long affectedRows = p.executeUpdate();

            if(affectedRows > 0) {
                generatedKey = Utils.getGeneratedKey(p);
                c.setId(generatedKey);
            }
            p.close();
            logger.info(" Computer created, generated ID : " +generatedKey);
        }catch(SQLException e){
            logger.error("Error creating computer " + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }
        return generatedKey;
    }

    public void update(Computer c){
        Timestamp intro = c.getIntroduced() == null ? null : Timestamp.valueOf( c.getIntroduced() );
        Timestamp disco = c.getDiscontinued() == null ? null : Timestamp.valueOf( c.getDiscontinued() );

        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("UPDATE computer c SET " +
                    "name = ?, introduced = ?, discontinued = ?, companyId = ?"+
                    "WHERE c.id = ?");


            p.setString(1, c.getName());
            p.setTimestamp(2, intro);
            p.setTimestamp(3, disco);
            p.setLong(4, c.getId() );

            long affectedRows = p.executeUpdate();
            p.close();
            logger.info(affectedRows + " rows updated" );
        }catch(SQLException e){
            logger.error("Error updating computer of ID " + c.getId() + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }
    }

    public ArrayList<Computer> selectAll(long min, long max){
        ArrayList<Computer> resultList = new ArrayList<>();
        ResultSet rs;
        try {
            connect = Utils.getConnection(connect);
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
            p.close();
        }catch(SQLException e){
            logger.error("Error getting computers" + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }

        return resultList;
    }

    public Computer getById(long id){
        ResultSet rs;
        Computer c = new Computer();
        try {
            connect = Utils.getConnection(connect);
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

            p.close();

        }catch(SQLException e){
            logger.error("Error retrieving computer of ID "+ id + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }

        return c;
    }

    public void delete(long id){

        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("DELETE FROM computer WHERE computer.id = ?");
            p.setLong(1, id);

            long affectedRows = p.executeUpdate();

            p.close();
            logger.info(affectedRows + " rows updated" );
        }catch(SQLException e){
            logger.error("Error deleting computer of ID "+ id + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }

    }


}
