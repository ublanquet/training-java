package controller;

import model.Company;
import model.Computer;
import model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.CompanyService;
import services.ComputerService;
import services.Mapper;
import validator.ComputerValidator;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiCompanyController {
  private final CompanyService companyService;
  private final ComputerService computerService;
  private final ComputerValidator computerValidator;
  private final Mapper mapper;

  private static final Logger logger = LoggerFactory.getLogger(RestApiCompanyController.class);

  //STOP_CHECKSTYLE
  @Autowired
  public RestApiCompanyController(CompanyService companyService, ComputerService computerService, ComputerValidator computerValidator, Mapper mapper) {
    this.companyService = companyService;
    this.computerService = computerService;
    this.computerValidator = computerValidator;
    this.mapper = mapper;
  }
  //START_CHECKSTYLE

  @RequestMapping(value = "/companies", method = RequestMethod.GET)
  public ResponseEntity<?> getCompanies() {
    List<Company> companies =  companyService.getAll();
    return ResponseEntity.ok(companies);
  }

  @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getCompany(@PathVariable Long id) {
    Company c = companyService.getById(id);
    return ResponseEntity.ok(c);
  }

  @RequestMapping(value = "/company/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable Long id) {
    Integer result = companyService.delete(id);

    return ResponseEntity.ok("Computers deleted by deleting the company " + id + " : " + result);
  }

}
