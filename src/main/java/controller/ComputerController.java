package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import persistance.model.Company;
import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import persistance.model.Page;
import services.CompanyService;
import services.ComputerService;
import services.Mapper;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class ComputerController {
  @Autowired
  CompanyService companyService;
  @Autowired
  ComputerService computerService;

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
   * @return .
   */
  @PostMapping("/addcomputer")
  public String addComputer(Model model, HttpSession session,
                            @RequestParam Map<String,String> allRequestParams ) {
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

  
}
