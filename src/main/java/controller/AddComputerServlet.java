package controller;

import model.Company;
import model.Computer;
import model.GenericBuilder;
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
      Computer c = GenericBuilder.of(Computer::new)
          .with(Computer::setCompanyId, Long.valueOf(request.getParameter("companyId")))
          .with(Computer::setIntroduced, Validate.parseDate(request.getParameter("introduced")))
          .with(Computer::setDiscontinued, Validate.parseDate(request.getParameter("discontinued")))
          .with(Computer::setName, request.getParameter("computerName"))
          .build();
      newId = computerService.createComputer(c);
      if (newId == 0 || newId == null) {
        session.setAttribute("messageHide", false);
        session.setAttribute("messageLevel", "warning");
        session.setAttribute("message", "Error creating computer");
      } else {
        session.setAttribute("messageHide", false);
        session.setAttribute("messageLevel", "info");
        session.setAttribute("message", "Computer created, id : " + newId);
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
