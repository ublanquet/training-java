package services;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import persistance.model.Company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class CompanyServiceTest {
  @Resource
    protected CompanyService service;
    protected Company company, company10;

    @Before
    public void setUp() {
        service = new CompanyService();
        company = new Company( (long) 1, "Apple Inc.");
        company10 = new Company( (long) 10, "Digital Equipment Corporation");
    }

    @After
    public void tearDown() {
    }

    @Ignore
    @Test
    public void testGetAllCompany() throws Exception {
        ArrayList<Company> companies = service.getAll( (long) 0, (long) 10);

        assertEquals(company.toString(), companies.get(0).toString());
        assertEquals(company10.toString(), companies.get(9).toString());
    }
}
