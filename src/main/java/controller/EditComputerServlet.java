package controller;

import persistance.model.Company;
import persistance.model.Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.CompanyService;
import services.ComputerService;
import services.Validate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "EditComputerServlet", urlPatterns = "/editcomputer")
public class EditComputerServlet extends HttpServlet {
  CompanyService companyService = new CompanyService();
  ComputerService computerService = ComputerService.getInstance();
  Logger logger = LoggerFactory.getLogger("controller.EditComputerServlet");


  /**
   * r.
   * @param request r
   * @param response r
   * @throws ServletException r
   * @throws IOException r
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int affectedRow = 0;
    HttpSession session = request.getSession();
    Computer c = Utils.buildComputerFromParams(request);
    try {
      affectedRow = computerService.update(c);
    } catch (Exception ex) {
      logger.error("Error updating computer : " + ex.getMessage() + ex.getStackTrace() + ex.getClass());
    }
    if (affectedRow == 0) {
      Utils.setMessage("warning", "Error updating computer", session);
    } else {
      DashboardAjaxServlet.invalidateCache();
      Utils.setMessage("info", "Computer updated, id : " + c.getId(), session);
    }

    response.sendRedirect("/dashboard");
  }

  /**
   * r.
   * @param request r
   * @param response r
   * @throws ServletException r
   * @throws IOException r
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    long id = 0;
    try {
      id = Validate.parseLong(request.getParameter("id"));
    } catch (Exception ex) {
      response.sendRedirect("/dashboard");
    }
    ArrayList<Company> companies = companyService.getAll();
    request.setAttribute("companies", companies);
    request.setAttribute("computer", computerService.getById(id));
    request.getRequestDispatcher("/views/editComputer.jsp").forward(request, response);
  }
}
