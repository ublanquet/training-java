package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import persistance.model.ComputerValidator;
import persistance.model.Company;
import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import services.CompanyService;
import services.ComputerService;
import services.Mapper;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/computer")
public class ComputerController {
  final
  CompanyService companyService;
  final
  ComputerService computerService;
  final
  ComputerValidator computerValidator;
  final
  Mapper mapper;

  Logger logger = LoggerFactory.getLogger("controller.EditComputerServlet");

  //STOP_CHECKSTYLE
  @Autowired
  public ComputerController(CompanyService companyService, ComputerService computerService, ComputerValidator computerValidator, Mapper mapper) {
    this.companyService = companyService;
    this.computerService = computerService;
    this.computerValidator = computerValidator;
    this.mapper = mapper;
  }
  //START_CHECKSTYLE


  /**
   * .
   * @param model .
   * @return .
   */
  @GetMapping("/add")
  public String addComputerForm(Model model) {
    ArrayList<Company> companies = companyService.getAll();
    model.addAttribute("form", new ComputerDto());
    model.addAttribute("companies", companies);
    return "addComputer";
  }

  /**
   * .
   * @param model .
   * @param redirectAttributes .
   * @param computer .
   * @param bindingResult .
   * @return .
   */
  @PostMapping("/add")
  public String addComputer(Model model, RedirectAttributes redirectAttributes,
                            @Valid ComputerDto computer, BindingResult bindingResult) {
    Long newId = null;
    Computer c = mapper.fromDto(computer);
    computerValidator.validate(c, bindingResult);
    if (bindingResult.hasErrors()) {
      String errorString = "";
      for (ObjectError error : bindingResult.getAllErrors()) {
        errorString += error.getObjectName() + " - " + error.getDefaultMessage() + "<br/>";
      }
      Utils.setMessage("warning", "Error creating computer, invalid param : " + errorString, redirectAttributes);
      return "redirect:/dashboard";
    }
    newId = computerService.create(c);
    if (newId == null || newId == 0) {
      Utils.setMessage("warning", "Error creating computer", redirectAttributes);
    } else {
      Utils.setMessage("info", "Computer created, id : " + newId, redirectAttributes);
    }
    return "redirect:/dashboard";
  }

  /**
   * .
   * @param model .
   * @param redirectAttrs .
   * @param allRequestParams .
   * @return .
   */
  @PostMapping("/edit")
  public String editComputer(Model model, @RequestParam Map<String, String> allRequestParams, RedirectAttributes redirectAttrs) {

    int affectedRow = 0;
    Computer c = Utils.buildComputerFromParams(allRequestParams);
    try {
      affectedRow = computerService.update(c);
    } catch (Exception ex) {
      logger.error("Error updating computer : " + ex.getMessage() + ex.getStackTrace() + ex.getClass());
    }
    if (affectedRow == 0) {
      Utils.setMessage("warning", "Error updating computer", redirectAttrs);
    } else {
      Utils.setMessage("info", "Computer updated, id : " + c.getId(), redirectAttrs);
    }

    return "redirect:/dashboard";
  }

  /**
   * .
   * @param model .
   * @param id .
   * @return .
   */
  @GetMapping("/edit")
  public String editComputerForm(Model model, @RequestParam(value = "id") Long id) {
    ArrayList<Company> companies = companyService.getAll();
    model.addAttribute("companies", companies);
    model.addAttribute("computer", Mapper.createComputerDto(computerService.getById(id)));
    return "editComputer";
  }

}
