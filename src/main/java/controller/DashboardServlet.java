package controller;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import persistance.model.Page;
import services.ComputerService;
import services.Mapper;
import services.Validate;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

//@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
  private ComputerService computerService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    ApplicationContext ac = (ApplicationContext) config.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

    this.computerService = (ComputerService) ac.getBean("computerService");
  }

  /**
   * r.
   *
   * @param request  r
   * @param response r
   * @throws ServletException r
   * @throws IOException      r
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (request.getParameter("selection") != null) {
      String message = "Computers deleted, id : ";
      String[] idToDelete =  request.getParameter("selection").split(",");
      ArrayList<Long> ids = new ArrayList<>();
      for (String selection : idToDelete) {
        try {
          ids.add(Validate.parseLong(selection));
          if (computerService.delete(Validate.parseLong(selection)) > 0) {
            message = message + selection + ", ";
          } else {
            message = message + " id not found : '" + selection + "', ";
          }
        } catch (NullPointerException ex) {
          message = message + " INVALID ID : '" + selection + "', ";
        }
      }
      Utils.setMessage("info", message, session);
    }
    doGet(request, response);
  }

  /**
   * r.
   *
   * @param request  r
   * @param response r
   * @throws ServletException r
   * @throws IOException      r
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    Page<ComputerDto> page;
    page = Mapper.convertPageDto(computerService.getPaginated(new Page<Computer>(10)));
    request.setAttribute("page", page);
    request.setAttribute("list", page.getListPage());
    request.setAttribute("totalCount", computerService.getCount());
    Long pageCount = computerService.getCount() / 10;
    request.setAttribute("totalPages", pageCount);

    Utils.cleanMessage(session);

    request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
  }
}
