package controller;

import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import persistance.model.Page;
import services.ComputerService;
import services.Mapper;
import services.Validate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
  private ComputerService computerService = ComputerService.getInstance();

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
      for (String selection : idToDelete) {
        try {
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
