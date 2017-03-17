package services;

import model.Company;
import model.Computer;
import model.GenericBuilder;
import model.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ComputerServiceTest {
    protected ComputerService service;
    protected Computer computer, computer2, computer10;
    protected Company company, company2, companyNull;
    protected GenericBuilder builder;
    protected Page<Computer> page;

    @Before
    public void setUp() {
        service = new ComputerService();

        company = new Company( 1, "Apple Inc.");
        companyNull = null;
        company2 = new Company(2, "Thinking Machines");


        computer = GenericBuilder.of(Computer::new)
                .with(Computer::setId, (long)1)
                .with(Computer::setName, "MacBook Pro 15.4 inch")
                .with(Computer::setCompany, company)
                .build();

        computer2 = GenericBuilder.of(Computer::new)
                .with(Computer::setId, (long)2)
                .with(Computer::setName, "CM-2a")
                .with(Computer::setCompany, company2)
                .build();

        computer10 = GenericBuilder.of(Computer::new)
                .with(Computer::setId, (long)10)
                .with(Computer::setName, "Apple IIc Plus")
                .with(Computer::setCompany, companyNull)
                .build();

        page = new Page<Computer>(10);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetAllComputer() throws Exception {
        ArrayList<Computer> computers = service.getAllComputers(0, 10);

        assertEquals(computer.toString(), computers.get(0).toString());
        assertEquals(computer2.toString(), computers.get(1).toString());
        assertEquals(computer10.toString(), computers.get(9).toString());
    }

    @Test
    public void testGetAllComputerPaginated() throws Exception {

        page = service.getPaginatedComputers(page);

        assertEquals(computer.toString(), page.list.get(0).toString());
        assertEquals(computer2.toString(), page.list.get(1).toString());
        assertEquals(computer10.toString(), page.list.get(9).toString());
    }
}
