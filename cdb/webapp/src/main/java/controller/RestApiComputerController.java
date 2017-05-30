package controller;

import model.Computer;
import model.DTO.ComputerDto;
import model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import services.CompanyService;
import services.ComputerService;
import services.Mapper;
import services.Validate;
import validator.ComputerValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestApiComputerController {
  private final CompanyService companyService;
  private final ComputerService computerService;
  private final ComputerValidator computerValidator;
  private final Mapper mapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(RestApiComputerController.class);

  //STOP_CHECKSTYLE
  @Autowired
  public RestApiComputerController(CompanyService companyService, ComputerService computerService, ComputerValidator computerValidator, Mapper mapper) {
    this.companyService = companyService;
    this.computerService = computerService;
    this.computerValidator = computerValidator;
    this.mapper = mapper;
  }
  //START_CHECKSTYLE

  @RequestMapping(value = "/computer", method = RequestMethod.POST)
  public ResponseEntity<?> create(@Valid ComputerDto computer, BindingResult bindingResult) {
    Computer c = mapper.fromDto(computer); //Utils.buildComputerFromParams(allRequestParams); //mapper.fromDto(computer);
    computerValidator.validate(c, bindingResult);
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(bindingResult.getAllErrors()); //error 422
    } else {
      c = computerService.createAndReturn(c);
      return ResponseEntity.ok(c);
    }
  }

  @RequestMapping(value = "/computer", method = RequestMethod.PUT)
  public ResponseEntity<?> edit(@Valid ComputerDto computer, BindingResult bindingResult) {
    Computer c = mapper.fromDto(computer); // Utils.buildComputerFromParams(allRequestParams); //mapper.fromDto(computer);
    computerValidator.validate(c, bindingResult);
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(bindingResult.getAllErrors()); //error 422
    } else {
      c = computerService.updateAndReturn(c);
      return ResponseEntity.ok(c);
    }
  }

  @RequestMapping(value = "/computer/{computerId}", method = RequestMethod.GET)
  public ResponseEntity<?> get(@PathVariable Long computerId) {
    Computer c = computerService.getById(computerId);
    if (c == null || c.getId() == 0) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
    } else {
      //return ResponseEntity.ok(Mapper.createComputerDto(c)); for DTO
      return ResponseEntity.ok(c);
    }
  }

  @RequestMapping(value = "/computers", method = RequestMethod.GET)
  public ResponseEntity<?> getPage(@RequestParam(value = "pageN", defaultValue = "0") Integer pageN,
                               @RequestParam(value = "perPage", defaultValue = "10") Integer perPage,
                               @RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "order[]", required = false) String[] order) {
    Page<Computer> page = new Page(perPage);
    page.setCurrentPage(pageN);

    if (search != null && search != "" && order == null) {
      page = computerService.getFiltered(page, search);
    } else if (order != null) {
      page = computerService.getFiltered(page, search, order);
    } else {
      page = computerService.getPaginated(page);
    }
    return ResponseEntity.ok(page);
  }

  @RequestMapping(value = "/computer", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@RequestParam(value = "selection") String selection) {
    String deleted = "", notFound = "";
    Boolean success = true;

    String[] idToDelete = selection.split(",");
    ArrayList<Long> ids = new ArrayList<>();
    for (String selected : idToDelete) {
      try { //TODO simplify, multi delete
        ids.add(Validate.parseLong(selected));
        if (computerService.delete(Validate.parseLong(selected)) > 0) {
          deleted = deleted + selected + ", ";
        } else {
          notFound = notFound + "" + selected + ", ";
          success = false;
        }
      } catch (NullPointerException ex) {
        notFound = notFound + "" + selected + ", ";
        success = false;
      }
    }

    Map<String, String> result = new HashMap<>();
    result.put("success", success.toString());
    result.put("deleted", deleted);
    result.put("notFound", notFound);


    return ResponseEntity.ok(result);
  }
}
