package academy.prog;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import com.google.gson.Gson;

public class GetUsersServlet extends HttpServlet {
    private List<String> users = new LinkedList<String>();
    private MessageList msgList = MessageList.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashSet<String> users = new HashSet<>(); // список уникальных юзеров
        Gson gson = new Gson();
        for(int i=0; i < msgList.getList().size(); i++) {
            //System.out.println(msgList.getList().get(i).getFrom());
            users.add(msgList.getList().get(i).getFrom());
        }
//        System.out.print("Count of users: ");
//        System.out.println(users.size());
//        System.out.println("Users:");
//        for (String user : users) {
//            System.out.println(user);
//        }
        if (users.size() != 0) {
            OutputStream os = resp.getOutputStream();
            byte[] buf = gson.toJson(users).getBytes(StandardCharsets.UTF_8);
            os.write(buf);
        }
        System.out.println(gson.toJson(users));

    }
}
