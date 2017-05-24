package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import model.Computer;
import model.DTO.ComputerDto;
import model.Page;
import services.ComputerService;
import services.Mapper;
import services.Validate;

import java.util.ArrayList;
import java.util.Locale;


@Controller
public class DashboardController {
  private final ComputerService computerService;
  Logger logger = LoggerFactory.getLogger("controller.EditComputerServlet");

  //STOP_CHECKSTYLE
  @Autowired
  public DashboardController(ComputerService computerService) {
    this.computerService = computerService;
  }
  //START_CHECKSTYLE

  /**
   * .
   * @param model .
   * @param locale  .
   * @param message .
   * @param messageHide .
   * @param messageLevel .
   * @return .
   */
  @GetMapping("/dashboard")
  public String dashboard(Model model, Locale locale,
                          @ModelAttribute(value = "message") String message,
                          @ModelAttribute(value = "messageHide") String messageHide,
                          @ModelAttribute(value = "messageLevel") String messageLevel) {
    Page<ComputerDto> page;
    page = Mapper.convertPageDto(computerService.getPaginated(new Page<Computer>(10)));
    model.addAttribute("page", page);

    // for operations feedback messages (crud success/failure)
    model.addAttribute("message", message);
    model.addAttribute("messageHide", !messageHide.equals("false"));
    model.addAttribute("messageLevel", messageLevel);

    model.addAttribute("list", page.getListPage());
    model.addAttribute("totalCount", page.getAllPagesItemCount());
    Long pageCount = (long) page.getMaxPage(); // 10 is default amount per page
    model.addAttribute("totalPages", pageCount);
    return "dashboard";
  }


  /**
   * .
   * @param model  .
   * @param pageN .
   * @param perPage .
   * @param search .
   * @param order .
   * @param locale .
   * @return .
   */
  @PostMapping("/dashboard/ajax")
  public String dashboardAjax(Model model, Locale locale,
                              @RequestParam(value = "pageN", defaultValue = "0") Integer pageN,
                              @RequestParam(value = "perPage", defaultValue = "10") Integer perPage,
                              @RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "order[]", required = false) String[] order) {
    Page<Computer> page = new Page(10);
    page.setCurrentPage(pageN);
    page.setNbEntries(perPage);

    if (search != null && search != "" && order == null) {
      page = computerService.getFiltered(page, search);
      model.addAttribute("filteredCount", page.getAllPagesItemCount()); // computerService.getCount(search));
    } else if (order != null) {
      page = computerService.getFiltered(page, search, order);
      model.addAttribute("filteredCount", page.getAllPagesItemCount()); // computerService.getCount(search));
    } else {
      page = computerService.getPaginated(page);
    }
    Page<ComputerDto> pageDto = Mapper.convertPageDto(page);
    model.addAttribute("list", pageDto.getListPage());

    return "dashboardTable";
  }

  /**
   * .
   * @param redir .
   * @param selection .
   * @return .
   */
  @PostMapping("/dashboard")
  public String delete(RedirectAttributes redir, @RequestParam(value = "selection") String selection) {
    String message = "Computers deleted, id : ";
    String[] idToDelete = selection.split(",");
    ArrayList<Long> ids = new ArrayList<>();
    for (String selected : idToDelete) {
      try {
        ids.add(Validate.parseLong(selected));
        if (computerService.delete(Validate.parseLong(selected)) > 0) {
          message = message + selected + ", ";
        } else {
          message = message + " id not found : '" + selected + "', ";
        }
      } catch (NullPointerException ex) {
        message = message + " INVALID ID : '" + selected + "', ";
      }
    }
    Utils.setMessage("info", message, redir);
    return "redirect:/dashboard";
  }


}
