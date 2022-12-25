package academy.prog;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {

    static final String TEMPLATE = "<html>" +
            "<head><title>Prog.kiev.ua</title></head>" +
            "<body><h1>Hello, %s</h1></body></html>";

    // GET request: http://localhost:8888/hello?name=user
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html"); // Content-Type: text/html
        PrintWriter pw = resp.getWriter();
        pw.println(String.format(TEMPLATE, req.getParameter("name")));
    }
}
