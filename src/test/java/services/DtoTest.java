package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import persistance.model.Company;
import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import persistance.model.GenericBuilder;
import persistance.model.Page;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class DtoTest {
  @Resource
  protected ComputerService service;
  protected Computer computer, computer2, computer10;
  protected Company company, company2, companyNull;
  protected ArrayList<Computer> list = new ArrayList<Computer>();
  protected Page<Computer> page;

  @Before
  public void setUp() {
    //service = ComputerService.getInstance();

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

    list.add(computer);
    list.add(computer2);
    list.add(computer10);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testToDto() throws Exception {
    ComputerDto cDto = Mapper.createComputerDto(computer);
    assertEquals(computer.toString().replace("null", "").replace("'", ""), cDto.toString().replace("'", ""));
    cDto = Mapper.createComputerDto(computer2);
    assertEquals(computer2.toString().replace("null", "").replace("'", ""), cDto.toString().replace("'", ""));
  }

  @Test
  public void testToPageDto() throws Exception {
    page = new Page<>(list);
    Page<ComputerDto> pageDto = Mapper.convertPageDto(page);

    for(int i = 0; i < page.getNbEntries(); i++) {
      assertEquals(page.getListPage().get(i).toString().replace("null", "").replace("'", ""), pageDto.getListPage().get(i).toString().replace("'", ""));
    }
  }
}
