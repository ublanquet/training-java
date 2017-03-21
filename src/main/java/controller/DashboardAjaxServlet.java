package controller;

import model.Computer;
import model.Page;
import services.ComputerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DashboardAjaxServlet", urlPatterns = "/dashboard/ajax")
public class DashboardAjaxServlet extends HttpServlet {
  ComputerService computerService = new ComputerService();

  /**
   * post.
   *
   * @param request  r
   * @param response r
   * @throws ServletException r
   * @throws IOException      r
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Page<Computer> page = new Page(10);
    if (request.getParameter("pageN") != null) {
      int pageN = Integer.parseInt(request.getParameter("pageN"));
      page.setCurrentPage(pageN);
      //request.setAttribute("pageN", pageN);
    }
    if (request.getParameter("perPage") != null) {
      int perPage = Integer.parseInt(request.getParameter("perPage"));
      page.setNbEntries(perPage);
      //request.setAttribute("perPage", perPage);
    }

    if (request.getParameter("search") != null && request.getParameter("search") != "") {
      //request.setAttribute("search", request.getParameter("search"));
      page = computerService.getFilteredComputers(page, request.getParameter("search"));
      request.setAttribute("filteredCount", computerService.getCount(request.getParameter("search")));
    } else {
      page = computerService.getPaginatedComputers(page);
    }


    request.setAttribute("list", page.getListPage());
    request.getRequestDispatcher("/views/dashboardTable.jsp").forward(request, response);
  }

  /**
   * get.
   *
   * @param request  r
   * @param response r
   * @throws ServletException r
   * @throws IOException      r
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }
}
