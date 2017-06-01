package controller;

import model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.CompanyService;
import services.ComputerService;
import services.Mapper;
import validator.ComputerValidator;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/companies")
public class RestApiCompanyController {
  private final CompanyService companyService;
  private final ComputerService computerService;
  private final ComputerValidator computerValidator;
  private final Mapper mapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(RestApiCompanyController.class);

  //STOP_CHECKSTYLE
  @Autowired
  public RestApiCompanyController(CompanyService companyService, ComputerService computerService, ComputerValidator computerValidator, Mapper mapper) {
    this.companyService = companyService;
    this.computerService = computerService;
    this.computerValidator = computerValidator;
    this.mapper = mapper;
  }


  @GetMapping
  public ResponseEntity<?> getCompanies() {
    List<Company> companies =  companyService.getAll();
    return ResponseEntity.ok(companies);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getCompany(@PathVariable Long id) {
    Company c = companyService.getById(id);
    if (c == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
    }
    return ResponseEntity.ok(c);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    Integer result = companyService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Computers deleted by deleting the company " + id + " : " + result);
  }

} //START_CHECKSTYLE
