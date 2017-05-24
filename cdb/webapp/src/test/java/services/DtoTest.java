package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import model.Company;
import model.Computer;
import model.DTO.ComputerDto;
import model.GenericBuilder;
import model.Page;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@Ignore
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class DtoTest {
  @Resource
  protected ComputerService service;
  @Autowired
  Mapper mapper;
  protected Computer computer, computer2, computer10;
  protected Company company, company2, companyNull;
  protected ArrayList<Computer> list = new ArrayList<Computer>();
  protected Page<Computer> page;

  @Before
  public void setUp() {
    //service = ComputerService.getInstance();

    company = new Company( 1L, "Apple Inc.");
    companyNull = null;
    company2 = new Company(2L, "Thinking Machines");

    computer = GenericBuilder.of(Computer::new)
        .with(Computer::setId, 1L)
        .with(Computer::setName, "MacBook Pro 15.4 inch")
        .with(Computer::setCompany, company)
        .build();

    computer2 = GenericBuilder.of(Computer::new)
        .with(Computer::setId, 2L)
        .with(Computer::setName, "CM-2a")
        .with(Computer::setCompany, company2)
        .build();

    computer10 = GenericBuilder.of(Computer::new)
        .with(Computer::setId, 10L)
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

  @Test
  public void testFromDto() throws Exception {
    ComputerDto cDto = Mapper.createComputerDto(computer);
    Computer c = mapper.fromDto(cDto);
    assertEquals(computer, c);

  }
}
