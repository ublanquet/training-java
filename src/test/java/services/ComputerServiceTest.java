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
import static org.junit.Assert.fail;

public class ComputerServiceTest {
    protected ComputerService service;
    protected Computer computer, computer2, computer10, computerToCreate;
    protected Company company, company2, companyNull;
    protected Page<Computer> page;

    @Before
    public void setUp() {
        service = ComputerService.getInstance();

        company = new Company( (long)1, "Apple Inc.");
        companyNull = null;
        company2 = new Company((long)2, "Thinking Machines");

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

        computerToCreate = GenericBuilder.of(Computer::new)
            .with(Computer::setName, "Apple IIc Plus")
            .with(Computer::setCompany, companyNull)
            .build();

        page = new Page<>(10);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetAllComputer() throws Exception {
        ArrayList<Computer> computers = service.getAll( (long) 0, (long) 10);

        assertEquals(computer.toString(), computers.get(0).toString());
        assertEquals(computer2.toString(), computers.get(1).toString());
        assertEquals(computer10.toString(), computers.get(9).toString());
    }

    @Test
    public void testGetAllComputerPaginated() throws Exception {
        page = service.getPaginated(page);

        assertEquals(computer.toString(), page.list.get(0).toString());
        assertEquals(computer2.toString(), page.list.get(1).toString());
        assertEquals(computer10.toString(), page.list.get(9).toString());

        page = null;
        try{
            page = service.getPaginated(page);
            fail("if page null, should throw nullpointer");
        }catch (NullPointerException ex) {}

    }

    @Test
    public void testCreateDeleteComputer() throws Exception {
        long createdId = service.create(computerToCreate);
        if (createdId == 0) {
            fail("failed computer creation");
        }
        Computer created = service.getById(createdId);
        assertEquals(computerToCreate.toString(), created.toString());

        long nbDelete = service.delete(computerToCreate.getId());
        if (nbDelete == 0) {
            fail("failed computer delete");
        }
    }
}
