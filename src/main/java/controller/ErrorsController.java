package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController {
  /**
   * .
   * @param httpRequest .
   * @return .
   */
  @RequestMapping(value = "errors")
  public String renderErrorPage(HttpServletRequest httpRequest, Model model) {

    Integer httpErrorCode = getErrorCode(httpRequest);

    Throwable throwable = (Throwable) httpRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (httpErrorCode == 500) {
      model.addAttribute("stacktrace", throwable.getStackTrace());
      model.addAttribute("message", throwable.getMessage());
    }

    String errorPage = httpErrorCode.toString();
    return errorPage;
  }

  /**
   * return error code of request.
   * @param httpRequest .
   * @return error code
   */
  private Integer getErrorCode(HttpServletRequest httpRequest) {
    return (Integer) httpRequest
        .getAttribute("javax.servlet.error.status_code");
  }
}
