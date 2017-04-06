package controller;

import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import persistance.model.Page;
import services.ComputerService;
import services.Mapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "DashboardAjaxServlet", urlPatterns = "/dashboard/ajax")
public class DashboardAjaxServlet extends HttpServlet {
  ComputerService computerService = ComputerService.getInstance();
  private static Map<String, Page> cachedResult = new HashMap<>();

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
    String cacheId = "";
    for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
      String key = entry.getKey();
      String[] value = entry.getValue();
      cacheId += key + Arrays.toString(value);
    }

    if (cachedResult.containsKey(cacheId)) {
      Page<ComputerDto> pageDto = cachedResult.get(cacheId);
      request.setAttribute("list", pageDto.getListPage());
      request.setAttribute("filteredCount", computerService.getCount(request.getParameter("search")));
      request.getRequestDispatcher("/views/dashboardTable.jsp").forward(request, response);
      return;
    }
    if (request.getParameter("pageN") != null) {
      int pageN = Integer.parseInt(request.getParameter("pageN"));
      page.setCurrentPage(pageN);
    }
    if (request.getParameter("perPage") != null) {
      int perPage = Integer.parseInt(request.getParameter("perPage"));
      page.setNbEntries(perPage);
    }
    if (request.getParameter("search") != null && request.getParameter("search") != "" && request.getParameter("order[]") == null) {
      page = computerService.getFiltered(page, request.getParameter("search"));
      request.setAttribute("filteredCount", computerService.getCount(request.getParameter("search")));
    } else if (request.getParameter("order[]") != null) { //TODO restruct ifs to be cleaner, only one one method really needed
      page = computerService.getFiltered(page, request.getParameter("search"), request.getParameter("order[]").split(","));
      request.setAttribute("filteredCount", computerService.getCount(request.getParameter("search")));
    } else {
      page = computerService.getPaginated(page);
    }
    Page<ComputerDto> pageDto = Mapper.convertPageDto(page);
    request.setAttribute("list", pageDto.getListPage());
    cachedResult.put(cacheId, pageDto);
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

  /**
   * Invalidate cache.
   */
  public static void invalidateCache() {
    cachedResult = new HashMap<>();
  }
}
