package controller;

import model.Company;
import model.Computer;
import model.GenericBuilder;
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
  ComputerService computerService = new ComputerService();
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
    Computer c = new Computer();
    HttpSession session = request.getSession();
    try {

      c = GenericBuilder.of(Computer::new)
          .with(Computer::setId, Validate.parseLong(request.getParameter("id")))
          .with(Computer::setCompanyId, Validate.parseLong(request.getParameter("companyId")))
          .with(Computer::setIntroduced, Validate.parseDate(request.getParameter("introduced")))
          .with(Computer::setDiscontinued, Validate.parseDate(request.getParameter("discontinued")))
          .with(Computer::setName, request.getParameter("computerName"))
          .build();
      affectedRow = computerService.updateComputer(c);
    } catch (Exception ex) {
      logger.error("Error updating computer : " + ex.getMessage() + ex.getStackTrace() + ex.getClass());
    }
    if (affectedRow == 0) {
      session.setAttribute("messageHide", false);
      session.setAttribute("messageLevel", "warning");
      session.setAttribute("message", "Error updating computer");
    } else {
      session.setAttribute("messageHide", false);
      session.setAttribute("messageLevel", "info");
      session.setAttribute("message", "Computer updated, id : " + c.getId());
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
    ArrayList<Company> companies = companyService.getAllCompany();
    request.setAttribute("companies", companies);
    request.setAttribute("computer", computerService.getComputerbyId(id));
    request.getRequestDispatcher("/views/editComputer.jsp").forward(request, response);
  }
}
