package servlets;

import calc.TestObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Сервлет получает информацию из сервета CalcServlet из атрибута formula
 *
 * Сервлет обрабатывает ссылку вида
 * http://localhost:8080/Servlets/CheckOperationsServlet
 */
public class CheckOperationsServlet extends HttpServlet {

    /*
    Отправка/получение данных.
    Данные передаются в url-строке!
    Для: отправки данных ограниченных длинной строки в 256 символов. Нет безопасности!
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /*
    Отправка/получение данных.
    Данные передаются внутри body!
    Для: отправка данных любого объема.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter responseWriter = response.getWriter();
        try {
            responseWriter.println("<html>");
            responseWriter.println("<head>");
            responseWriter.println("<title>Servlet CheckAttributeServlet</title>");
            responseWriter.println("</head>");
            responseWriter.println("<body>");

            // определение или создание сессии
            HttpSession session = request.getSession(true);

            // получаем значение атрибута установленного в CalcServlet
            System.out.println(((TestObject) request.getServletContext().getAttribute("obj")).getName());

            // получаем доступ к атрибуту сессии
            Object sessionAttribute = session.getAttribute("formula");
            if (sessionAttribute instanceof ArrayList) {
                ArrayList list = (ArrayList) sessionAttribute;
                responseWriter.println("<h1>Список операций:</h1>");
                for (Object string : list) {
                    responseWriter.println("<h3>" + string + "</h3>");
                }
            } else {
                responseWriter.println("<h1>Операции не найдены</h1>");
            }
        } finally {
            responseWriter.println("</body>");
            responseWriter.println("</html>");
            responseWriter.close();
        }
    }
}
