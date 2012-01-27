import java.net.*;
import javax.net.ssl.*;
import java.io.*;

public class POPClient {
	static BufferedReader in;
	static PrintWriter out;
	
	public static void main(String[] args) throws IOException {
		String mailServer = "pop.gmx.com";
		String username = "emilpemil@gmx.com";
		String password = "!234qwerASDF";

		SSLSocket ss = (SSLSocket) SSLSocketFactory.getDefault().createSocket(mailServer, 995);
		System.out.println("Connected");
		in = new BufferedReader( new InputStreamReader(ss.getInputStream()));
		out = new PrintWriter(ss.getOutputStream(), true);
		
		login(username, password);
		
		listMails();
		
		BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.print("Enter an email ID to view it: ");
			String input = userReader.readLine();
			if (input.equals("q")) break;
			if (input.equals("list")) {
				listMails();
				continue;
			}
			
			getMail(input);
		}
	}

	public static void login(String username, String password) throws IOException {
		out.println("USER " + username);
		in.readLine();
		out.println("PASS " + password);
		in.readLine();
	}

	public static void listMails() throws IOException {
		out.println("LIST");
		String line = "";
		while(!(line = in.readLine()).equals("."))
			System.out.println(line);
	}

	public static void getMail(String id) throws IOException {
		out.println("RETR " + id);
		String line = "";
		while(!(line = in.readLine()).equals("."))
			System.out.println(line);
	}
}

/*
emilpemil@gmx.com
132774756
!234qwerASDF
*/