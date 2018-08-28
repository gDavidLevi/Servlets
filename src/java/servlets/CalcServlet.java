package servlets;

import calc.CalcOperations;
import calc.OperationType;
import calc.TestObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервлет обрабатывает ссылку вида
 * http://localhost:8080/Servlets/CalcServlet?one=1&two=2&operation=add
 */
public class CalcServlet extends HttpServlet {

    /*
    Отправка/получение данных.
    Данные (параметры) передаются в url-строке!
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
        return "Сервлет - калькулятор";
    }

    // имя атрибута сессии
    private static final String SESSION_MAP = "sessionMap";

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // инициируем таблицу сессий через getAttribute(SESSION_MAP)
        Map<String, List> sessionMap = (Map<String, List>) request.getServletContext().getAttribute(SESSION_MAP);
        // если не удалось инициировать переменную (то есть запустили первый раз сервлет), то создаем ее с нуля.
        if (sessionMap == null) {
            sessionMap = new HashMap<>();
        }

        PrintWriter responseWriter = response.getWriter();
        responseWriter.println("<html>");
        responseWriter.println("<head>");
        responseWriter.println("<title>Servlet CalcServlet</title>");
        responseWriter.println("</head>");
        responseWriter.println("<body>");

        try {
            // Создание уникальной сессии для пользователя
            HttpSession session = request.getSession(true);

            // Локальная потокобезопасная переменная. Хранит список операций.
            ArrayList<String> listOperations;

            // установили атрибут в контексте сервлета для того, чтобы обмениваться объектами между сервлетами
            request.getServletContext().setAttribute("obj", new TestObject("TestName"));
            System.out.println(((TestObject) request.getServletContext().getAttribute("obj")).getName()); // получааем значение атрибута

            // для новой сессии создаем новый список иначе нет
            if (session.isNew()) {
                listOperations = new ArrayList<>();
            } else {
                listOperations = (ArrayList<String>) session.getAttribute("formula");
            }

            // считывание параметров
            // Параметры передаются в запросе, параметры формирует клиент.
            double one = Double.valueOf(request.getParameter("one"));
            double two = Double.valueOf(request.getParameter("two"));
            String operation = String.valueOf(request.getParameter("operation"));

            // получение типа операции
            OperationType operType = OperationType.valueOf(operation.toUpperCase());

            // добавление новой операции в список
            listOperations.add(one + " " + operType.getStringValue() + " " + two + " = " + new CalcOperations().calcResult(operType, one, two));

            // добавление атрибута в уровень сессии "void setAttribute(String var1, Object var2);"
            // Атрибуты сессии хранятся: в сессии, в запросе, в контексте (глобальные значения)
            // Назначение: передача объектов между страницами, запросами клиента в сессии.
            session.setAttribute("formula", listOperations);

            responseWriter.println("<table cellpadding=\"20\">");
            responseWriter.println("<tr>");
            responseWriter.println("<td style=\"vertical-align:top;\">");

            // вывод всех операций
            responseWriter.println("<h1>Текущая JSESSIONID = " + session.getId() + "</h1>");
            //
            responseWriter.println("<h3>Список операций (всего: " + listOperations.size() + ") </h3>");
            for (String oper : listOperations) {
                responseWriter.println("<h3>" + oper + "</h3>");
            }

            // в таблицу сессий положим список опираций для каждой сессии
            sessionMap.put(session.getId(), listOperations);
            // сохраним как атрибут
            getServletContext().setAttribute(SESSION_MAP, sessionMap);

            responseWriter.println("</td>");
            responseWriter.println("<td style=\"vertical-align:top;\">");

            // выведем все сессии во сторой колонке
            for (Map.Entry<String, List> entry : sessionMap.entrySet()) {
                String sessionId = entry.getKey();
                List listOper = entry.getValue();
                responseWriter.println("<h1 style=\"color:red\"> Другие JSESSIONID = " + sessionId + "</h1>");
                for (Object str : listOper) {
                    responseWriter.println("<h3>" + str + "</h3>");
                }
            }
            responseWriter.println("</td>");
            responseWriter.println("</tr>");
            responseWriter.println("</table>");
        } catch (Exception ex) {
            // Отправка ошибки код 400 (Запрос не может быть понят сервером из-за некорректного синтаксиса (получен код 400 Bad Request)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            responseWriter.println("</body>");
            responseWriter.println("</html>");
            responseWriter.close();
        }
    }
}
