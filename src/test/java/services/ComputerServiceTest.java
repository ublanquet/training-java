package services;

import org.junit.Ignore;
import org.mockito.Mockito;
import persistance.dao.DaoComputer;
import persistance.dao.DaoComputerI;
import persistance.model.Company;
import persistance.model.Computer;
import persistance.model.GenericBuilder;
import persistance.model.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
@Ignore
public class ComputerServiceTest {
    protected ComputerService service;
    protected Computer computer, computer2, computer10, computerToCreate,invalidComputer, invalidComputer2;
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

        //testmodif
      /*computer = GenericBuilder.of(Computer::new)
          .with(Computer::setId, (long)6)
          .with(Computer::setName, "MacBook Pro")
          .with(Computer::setCompany, company)
          .build();*/

        computer10 = GenericBuilder.of(Computer::new)
                .with(Computer::setId, (long)10)
                .with(Computer::setName, "Apple IIc Plus")
                .with(Computer::setCompany, companyNull)
                .build();

        computerToCreate = GenericBuilder.of(Computer::new)
            .with(Computer::setName, "Apple IIc Plus")
            .with(Computer::setCompany, companyNull)
            .build();

      invalidComputer = GenericBuilder.of(Computer::new)
          .with(Computer::setName, null)
          .with(Computer::setCompany, companyNull)
          .build();

      invalidComputer2 = GenericBuilder.of(Computer::new)
          .with(Computer::setName, "Invalid")
          .with(Computer::setCompany, companyNull)
          .with(Computer::setIntroduced, LocalDateTime.of(2014, Month.FEBRUARY, 27, 0, 0) )
          .with(Computer::setDiscontinued, LocalDateTime.of(2011, Month.FEBRUARY, 25, 0, 0) )
          .build();

        page = new Page<>(10);
    }

    @After
    public void tearDown() {
    }

    @Test
    @Ignore
    public void testGetAllComputer() throws Exception {
        ArrayList<Computer> computers = service.getAll( (long) 0, (long) 10);

        assertEquals(computer.toString(), computers.get(0).toString());
        //assertEquals(computer2.toString(), computers.get(1).toString());
        //assertEquals(computer10.toString(), computers.get(9).toString());
    }

    @Test
    public void testGetAllComputerPaginated() throws Exception {
        page = service.getPaginated(page);

        assertEquals(computer.toString(), page.list.get(0).toString());
        //assertEquals(computer2.toString(), page.list.get(1).toString());
        //assertEquals(computer10.toString(), page.list.get(9).toString());

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

           /*
      final Computer mockComputer = Mockito.mock(Computer.class);
      final ComputerService cServiceMock = Mockito.mock(ComputerService.class);
      Mockito.doThrow(new SQLException()).when(cServiceMock).create(mockComputer);
      assertEquals((long)cServiceMock.create(mockComputer), (long)0);
      */
/*
      ComputerService cService = ComputerService.getInstance();
      cService = Mockito.spy(cService);
      service = Mockito.spy(service);
      final Computer c = Mockito.mock(Computer.class);
      Mockito.doReturn(0).when(cService).create(c);

      // Test
      cService.create(c);
      Mockito.verify(cService, Mockito.atLeastOnce()).create(c);
*/
      createdId = service.create(invalidComputer);
      //Mockito.verify(service, Mockito.atLeastOnce()).create(invalidComputer);
      if (createdId != 0) {
        fail("Invalid computer got created");
      }
      createdId = service.create(invalidComputer2);
      if (createdId != 0) {
        fail("Invalid computer got created");
      }
    }

  @Test
  @Ignore
  public void testDao() throws Exception {
    /*DaoComputer daoComputer = DaoComputerI.getInstance();
    daoComputer = Mockito.spy(daoComputer);
    final Computer c = Mockito.mock(Computer.class);
    Mockito.doReturn(0).when(daoComputer).create(c);
    // Test
    daoComputer.create(c);
    Mockito.verify(daoComputer, Mockito.atLeastOnce()).create(c);
    */
  }

}
