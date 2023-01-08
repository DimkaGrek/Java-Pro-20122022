package academy.prog;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			String to = "all";

			System.out.println("Enter your login: ");
			String login = scanner.nextLine();
	
			Thread th = new Thread(new GetThread());
			th.setDaemon(true);
			th.start();

			System.out.println("/users - list of users");
            System.out.println("Enter your message: ");
			while (true) {
				String text = scanner.nextLine();
				if (text.isEmpty()) break;

				// проверяем первый символ
				char fChar = text.charAt(0);
				// если ввели команду /users
				if (fChar == '/' && "/users".equals(text)) {
					GetUsers.getUsers();
				}
				else {
					// если ввели личное сообщение
					if (fChar == '@') {
						String[] words = text.split("\\s");
						to = words[0];
						text = text.substring(to.length() + 1);
						to = to.substring(1);
					}
					// отправляем сообщение на сервер
					Message m = new Message(login, text, to);
					int res = m.send(Utils.getURL() + "/add");

					if (res != 200) { // 200 OK
						System.out.println("HTTP error occurred: " + res);
						return;
					}
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
