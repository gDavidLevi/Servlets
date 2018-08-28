package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Сервлет обрабатывает ссылку вида http://localhost:8080/Servlets/index
 */
public class Index extends HttpServlet {

    /*
    Отправка/получение данных.
    Данные передаются в url-строке!
    Для: отправки данных ограниченных длинной строки в 256 символов. Нет безопасности!
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter responseWriter = response.getWriter();
        responseWriter.println("<html>");
        responseWriter.println("<head>");
        responseWriter.println("<title>Servlet index</title>");
        responseWriter.println("</head>");
        responseWriter.println("<body>");
        responseWriter.println("<p>");
        responseWriter.println("<a href=\"http://localhost:8080/Servlets/CalcServlet?one=1&two=2&operation=add\" >CalcServlet</a>");
        responseWriter.println("<p>");
        responseWriter.println("<a href=\"http://localhost:8080/Servlets/CheckOperationsServlet\">CheckOperationsServlet</a>");
        responseWriter.println("</body>");
        responseWriter.println("</html>");
        responseWriter.close();
    }
}
