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
    Page<Computer> page = new Page(10);
    page = computerService.getPaginatedComputers(page);
    request.setAttribute("list", page.getListPage());
    request.setAttribute("totalCount", computerService.getCount());
    long pageCount = computerService.getCount() / 10;
    ArrayList<String> pageNums = new ArrayList();
    for (long i = 0; i < pageCount; i++) {
      pageNums.add("" + i);
    }
    request.setAttribute("totalPages", pageCount);
    request.setAttribute("totalNums", pageNums);
    request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
  }
}
