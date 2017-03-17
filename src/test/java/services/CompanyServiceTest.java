package services;
import model.Company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CompanyServiceTest {
    protected CompanyService service;
    protected Company company, company10;

    @Before
    public void setUp() {
        service = new CompanyService();
        company = new Company( 1, "Apple Inc.");
        company10 = new Company( 10, "Digital Equipment Corporation");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetAllCompany() throws Exception {
        ArrayList<Company> companies = service.getAllCompany(0, 10);

        assertEquals(company.toString(), companies.get(0).toString());
        assertEquals(company10.toString(), companies.get(9).toString());
    }
}
