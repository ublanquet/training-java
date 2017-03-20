package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "IndexServlet", urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    /**
     * r.
     * @param request d
     * @param response d
     * @throws ServletException d
     * @throws IOException d
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * Default.
     * @param request r
     * @param response r
     * @throws ServletException r
     * @throws IOException r
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("test", 10);
        ArrayList<String> list = new ArrayList<String>();
        list.add("testList");
        list.add("restList2");
        request.setAttribute("list", list);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
