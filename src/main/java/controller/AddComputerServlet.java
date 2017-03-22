package controller;

import model.Company;
import model.Computer;
import services.CompanyService;
import services.ComputerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "AddComputerServlet", urlPatterns = "/addcomputer")
public class AddComputerServlet extends HttpServlet {
  CompanyService companyService = new CompanyService();
  ComputerService computerService = new ComputerService();

  /**
   * r.
   * @param request r
   * @param response r
   * @throws ServletException r
   * @throws IOException r
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Long newId = null;
    HttpSession session = request.getSession();

    if (request.getParameter("computerName") != null) {
      Computer c = Utils.buildComputerFromParams(request);
      newId = computerService.createComputer(c);
      if (newId == null || newId == 0) {
        Utils.setMessage("warning", "Error creating computer", session);
      } else {
        Utils.setMessage("info", "Computer created, id : " + newId, session);
      }
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
    ArrayList<Company> companies = companyService.getAllCompany();
    request.setAttribute("companies", companies);
    request.getRequestDispatcher("/views/addComputer.jsp").forward(request, response);
  }
}
