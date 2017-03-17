package dao;

import model.Company;
import model.Computer;
import model.GenericBuilder;
import model.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public enum DaoComputer implements DaoComputerI {
    INSTANCE;

    private Connection connect;

    /**
     * create in db.
     * @param c computer
     * @return generated id
     */
    public long create(Computer c) {
        connect = Utils.getConnection(connect);
        long generatedKey = 0;
        try {

            PreparedStatement p = connect.prepareStatement("INSERT INTO computer " +
                    "(name, introduced, discontinued, company_id) " +
                    "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            p.setString(1, c.getName());
            p.setTimestamp(2, c.getIntroducedTimestamp());
            p.setTimestamp(3, c.getDiscontinuedTimestamp());
            p.setLong(4, c.getCompanyId());

            long affectedRows = p.executeUpdate();

            if (affectedRows > 0) {
                generatedKey = Utils.getGeneratedKey(p);
                c.setId(generatedKey);
            }
            p.close();
            LOGGER.info(" Computer created, generated ID : " + generatedKey);
        } catch (SQLException e) {
            LOGGER.error("Error creating computer " + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }
        return generatedKey;
    }

    /**
     * update.
     * @param c computer
     */
    public void update(Computer c) {
        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("UPDATE computer c SET " +
                    "name = ?, introduced = ?, discontinued = ?, companyId = ?" +
                    "WHERE c.id = ?");


            p.setString(1, c.getName());
            p.setTimestamp(2, c.getIntroducedTimestamp());
            p.setTimestamp(3, c.getDiscontinuedTimestamp());
            p.setLong(4, c.getId());

            long affectedRows = p.executeUpdate();
            p.close();
            LOGGER.info(affectedRows + " rows updated");
        } catch (SQLException e) {
            LOGGER.error("Error updating computer of ID " + c.getId() + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }
    }

    /**
     * select all.
     * @param min offset
     * @param max nb to return
     * @return list
     */
    public ArrayList<Computer> selectAll(long min, long max) {
        ArrayList<Computer> resultList = new ArrayList<>();
        ResultSet rs;
        try {
            connect = Utils.getConnection(connect);
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
                        .with(Computer::setCompany, new Company(rs.getLong("company.id"), rs.getString("company.name")))
                        .build();
                resultList.add(c);
                resultList.add(c);
            }
            p.close();
        } catch (SQLException e) {
            LOGGER.error("Error getting computers" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }

        return resultList;
    }

    /**
     * get computer list paged.
     * @param page the page
     * @return the filled page
     */
    public Page<Computer> selectPaginated(Page page) {
        ArrayList<Computer> resultList = new ArrayList<>();
        ResultSet rs;
        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("SELECT * FROM computer LEFT JOIN company on computer.company_id = company.id " +
                    "LIMIT ? OFFSET ?");
            p.setLong(1, page.getNbEntries());
            p.setLong(2, page.getFirstEntryIndex());

            rs = p.executeQuery();

            while (rs.next()) {
                Computer c = GenericBuilder.of(Computer::new)
                        .with(Computer::setId, rs.getLong("computer.id"))
                        .with(Computer::setName, rs.getString("computer.name"))
                        .with(Computer::setIntroducedTimestamp, rs.getTimestamp("introduced"))
                        .with(Computer::setDiscontinuedTimestamp, rs.getTimestamp("discontinued"))
                        .with(Computer::setCompanyId, rs.getLong("company_id"))
                        .with(Computer::setCompany, new Company(rs.getLong("company.id"), rs.getString("company.name")))
                        .build();
                resultList.add(c);
            }
            p.close();
        } catch (SQLException e) {
            LOGGER.error("Error getting computers" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }

        page.setList(resultList);

        return page;
    }

    /**
     * get by id.
     * @param id id
     * @return computer
     */
    public Computer getById(long id) {
        ResultSet rs;
        Computer c = new Computer();
        try {
            connect = Utils.getConnection(connect);
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
                        .with(Computer::setCompany, new Company(rs.getLong("company.id"), rs.getString("company.name")))
                        .build();
            }

            p.close();

        } catch (SQLException e) {
            LOGGER.error("Error retrieving computer of ID " + id + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }

        return c;
    }

    /**
     * delete.
     * @param id id
     */
    public void delete(long id) {

        try {
            connect = Utils.getConnection(connect);
            PreparedStatement p = connect.prepareStatement("DELETE FROM computer WHERE computer.id = ?");
            p.setLong(1, id);

            long affectedRows = p.executeUpdate();

            p.close();
            LOGGER.info(affectedRows + " rows updated");
        } catch (SQLException e) {
            LOGGER.error("Error deleting computer of ID " + id + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }

    }


}
