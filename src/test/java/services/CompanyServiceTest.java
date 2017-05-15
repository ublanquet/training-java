package services;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import persistance.model.Company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class CompanyServiceTest {
  @Resource
    protected CompanyService service;
    protected Company company, company10;

    @Before
    public void setUp() {
        company = new Company( 1L, "Apple Inc.");
        company10 = new Company( 10L, "Digital Equipment Corporation");
    }

    @After
    public void tearDown() {
    }

    @Ignore
    @Test
    public void testGetAllCompany() throws Exception {
        ArrayList<Company> companies = service.getAll( 0L, 10L);

        assertEquals(company.toString(), companies.get(0).toString());
        assertEquals(company10.toString(), companies.get(9).toString());
    }
}
