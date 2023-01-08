package academy.prog;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

public class GetUsers {
    public static void getUsers() throws IOException {
        Gson gson = new Gson();

        URL url = new URL(Utils.getURL() + "/users");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        InputStream is = http.getInputStream();
        try {
            byte[] buf = GetThread.responseBodyToArray(is);
            String strBuf = new String(buf, StandardCharsets.UTF_8);

            ArrayList<String> userList = gson.fromJson(strBuf, ArrayList.class);
            if (userList != null) {
                System.out.println("There is list of users");
                for (String user : userList) {
                    System.out.println("@" + user);
                }
            } else System.out.println("There are no users");

        }  finally {
            is.close();
        }
    }

//    private byte[] responseBodyToArray(InputStream is) throws IOException {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        byte[] buf = new byte[10240];
//        int r;
//
//        do {
//            r = is.read(buf);
//            if (r > 0) bos.write(buf, 0, r);
//        } while (r != -1);
//
//        return bos.toByteArray();
//    }
}
