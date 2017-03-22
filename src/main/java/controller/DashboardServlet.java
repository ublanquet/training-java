package controller;

import model.Computer;
import model.DTO.ComputerDto;
import model.Page;
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
import java.util.ArrayList;

@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
  private ComputerService computerService = new ComputerService();

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
          if (computerService.deleteComputer(Validate.parseLong(selection)) > 0) {
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
    //Page<Computer> page = new Page(10);
    Page<ComputerDto> page;
    page = Mapper.convertPageDto(computerService.getPaginatedComputers(new Page<Computer>(10)));
    request.setAttribute("list", page.getListPage());
    request.setAttribute("totalCount", computerService.getCount());
    Long pageCount = computerService.getCount() / 10;
    ArrayList<String> pageNums = new ArrayList();
    for (long i = 0; i < pageCount + 1; i++) {
      pageNums.add("" + i);
    }
    request.setAttribute("totalPages", pageCount);
    request.setAttribute("totalNums", pageNums);

    Utils.cleanMessage(session);

    request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
  }
}
