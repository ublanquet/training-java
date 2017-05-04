package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import persistance.model.Page;
import services.ComputerService;
import services.Mapper;
import services.Validate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Locale;

@Controller
public class DashboardController {
  @Autowired
  private ComputerService computerService;

  /**
   * .
   * @param model .
   * @param session .
   * @param locale .
   * @return .
   */
  @GetMapping("/dashboard")
  public String dashboard(Model model, HttpSession session, Locale locale) {
    Page<ComputerDto> page;
    page = Mapper.convertPageDto(computerService.getPaginated(new Page<Computer>(10)));
    model.addAttribute("page", page);
    model.addAttribute("list", page.getListPage());
    model.addAttribute("totalCount", computerService.getCount());
    Long pageCount = computerService.getCount() / 10;
    model.addAttribute("totalPages", pageCount);

    Utils.cleanMessage(session);
    return "dashboard";
  }


  /**
   * .
   * @param model  .
   * @param session .
   * @param pageN .
   * @param perPage .
   * @param search .
   * @param order .
   * @param locale .
   * @return .
   */
  @PostMapping("/dashboard/ajax")
  public String dashboardAjax(Model model, HttpSession session, Locale locale,
                              @RequestParam(value = "pageN", defaultValue = "0") Integer pageN,
                              @RequestParam(value = "perPage", defaultValue = "10") Integer perPage,
                              @RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "order[]", required = false) String[] order) {
    Page<Computer> page = new Page(10);
    page.setCurrentPage(pageN);
    page.setNbEntries(perPage);

    if (search != null && search != "" && order == null) {
      page = computerService.getFiltered(page, search);
      model.addAttribute("filteredCount", computerService.getCount(search));
    } else if (order != null) {
      page = computerService.getFiltered(page, search, order);
      model.addAttribute("filteredCount", computerService.getCount(search));
    } else {
      page = computerService.getPaginated(page);
    }
    Page<ComputerDto> pageDto = Mapper.convertPageDto(page);
    model.addAttribute("list", pageDto.getListPage());

    return "dashboardTable";
  }

  /**
   * .
   * @param session .
   * @param selection .
   * @return .
   */
  @PostMapping("/dashboard")
  public String delete(HttpSession session, @RequestParam(value = "selection") String selection) {
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
    Utils.setMessage("info", message, session);
    return "dashboard";
  }


}
