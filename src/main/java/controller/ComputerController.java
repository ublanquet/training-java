package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import persistance.model.Company;
import persistance.model.Computer;
import services.CompanyService;
import services.ComputerService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class ComputerController {
  @Autowired
  CompanyService companyService;
  @Autowired
  ComputerService computerService;
  Logger logger = LoggerFactory.getLogger("controller.EditComputerServlet");


  /**
   * .
   * @param model .
   * @param session .
   * @return .
   */
  @GetMapping("/addcomputer")
  public String addComputerForm(Model model, HttpSession session) {
    ArrayList<Company> companies = companyService.getAll();
    model.addAttribute("companies", companies);
    return "addComputer";
  }

  /**
   * .
   * @param model .
   * @param session .
   * @param allRequestParams .
   * @return .
   */
  @PostMapping("/addcomputer")
  public String addComputer(Model model, HttpSession session,
                            @RequestParam Map<String, String> allRequestParams) {
    Long newId = null;
    Computer c = Utils.buildComputerFromParams(allRequestParams);
    newId = computerService.create(c);
    if (newId == null || newId == 0) {
      Utils.setMessage("warning", "Error creating computer", session);
    } else {
      Utils.setMessage("info", "Computer created, id : " + newId, session);
    }
    return "redirect:/dashboard";
  }

  /**
   * .
   * @param model .
   * @param session .
   * @param allRequestParams .
   * @return .
   */
  @PostMapping("/editcomputer")
  public String editComputer(Model model, HttpSession session,
                            @RequestParam Map<String, String> allRequestParams) {

    int affectedRow = 0;
    Computer c = Utils.buildComputerFromParams(allRequestParams);
    try {
      affectedRow = computerService.update(c);
    } catch (Exception ex) {
      logger.error("Error updating computer : " + ex.getMessage() + ex.getStackTrace() + ex.getClass());
    }
    if (affectedRow == 0) {
      Utils.setMessage("warning", "Error updating computer", session);
    } else {
      Utils.setMessage("info", "Computer updated, id : " + c.getId(), session);
    }

    return "redirect:/dashboard";
  }

  /**
   * .
   * @param model .
   * @param id .
   * @return .
   */
  @GetMapping("/editcomputer")
  public String editComputerForm(Model model, @RequestParam(value = "id") Long id) {
    ArrayList<Company> companies = companyService.getAll();
    model.addAttribute("companies", companies);
    model.addAttribute("computer", computerService.getById(id));
    return "editComputer";
  }

}
