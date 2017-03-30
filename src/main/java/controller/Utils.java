package controller;


import persistance.model.Computer;
import persistance.model.GenericBuilder;
import services.Validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Utils {

  /**
   * Set dashboard feedback message.
   * @param level bootstrap class success/warning/info/danger
   * @param msg message text
   * @param session user session
   */
  public static void setMessage(String level, String msg, HttpSession session) {
    session.setAttribute("messageHide", false);
    session.setAttribute("messageLevel", level);
    session.setAttribute("message", msg);
  }

  /**
   * Clean dashboard feedback message if displayed at least once.
   * @param session user session
   */
  public static void cleanMessage(HttpSession session) {
    if (session.getAttribute("messageHide") == null) { // actions feedback messages
      session.setAttribute("messageHide", true);
    } else {
      if (session.getAttribute("messageDisplayNb") == null) {
        session.setAttribute("messageDisplayNb", 0);
      } else {
        session.setAttribute("messageDisplayNb", (int) session.getAttribute("messageDisplayNb") + 1);
      }
      if ((int) session.getAttribute("messageDisplayNb") > 0) {
        session.setAttribute("messageHide", true);
      }
    }
  }

  /**
   * Build a computer obj from httpRequest parameters.
   * @param request r
   * @return computer obj
   */
  public static Computer buildComputerFromParams(HttpServletRequest request) {
    GenericBuilder<Computer> builder = GenericBuilder.of(Computer::new);

    if (request.getParameter("id") != null) {
      builder = builder.with(Computer::setId, Validate.parseLong(request.getParameter("id")));
    }
    if (request.getParameter("companyId") != null) {
      builder = builder.with(Computer::setCompanyId, Validate.parseLong(request.getParameter("companyId")));
    }
    if (request.getParameter("introduced") != null) {
      builder = builder.with(Computer::setIntroduced, Validate.parseDate(request.getParameter("introduced")));
    }
    if (request.getParameter("discontinued") != null) {
      builder = builder.with(Computer::setDiscontinued, Validate.parseDate(request.getParameter("discontinued")));
    }
    if (request.getParameter("computerName") != null) {
      builder = builder.with(Computer::setName, request.getParameter("computerName"));
    }

    Computer c = builder.build();
    return c;
  }

}
