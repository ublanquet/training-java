package controller;


import javax.servlet.http.HttpSession;

public class Utils {

  public static void setMessage(String level, String msg, HttpSession session) {
    session.setAttribute("messageHide", false);
    session.setAttribute("messageLevel", level);
    session.setAttribute("message", msg);
  }

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

}
