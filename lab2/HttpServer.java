import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.HashMap;

public class HttpServer {
    public static void main(String[] args) throws IOException {
		System.out.println("Skapar Serversocket");
		ServerSocket ss = new ServerSocket(8080);
		while(true) {
		    System.out.println("Väntar på klient...");
		    Socket s = ss.accept();
		    System.out.println("Klient är ansluten");
		    BufferedReader request = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    String str = request.readLine();
		    
		    if(str.indexOf("favicon.ico") >= 0) {
				s.close();
				continue;				
			}
		    
		    System.out.println(str);
		    StringTokenizer tokens = new StringTokenizer(str," ?");
		    tokens.nextToken(); // Ordet GET
		    String requestedDocument = tokens.nextToken();
		    
		   	Integer guess = null;
		   	int number = 0;
		   	int tries = 0;

			// See if user guessed a number.
			String qs[] = tokens.nextToken().split("=");
		    if(qs[0].equals("guess") && qs.length == 2) {
			    guess = new Integer(qs[1]);
		    	System.out.println("User guessed " + guess);
		    }

			HashMap<String, String> cookies = new HashMap<String, String>();

		    // Read rest of request.
		    while((str = request.readLine()) != null && str.length() > 0) {
			    String property[] = str.split(":", 2);
			    // Extract cookie arguments.
			    if(property[0].equals("Cookie")) {
				    String cookieProperties[] = property[1].trim().split(";");
				    for(String cookieProperty : cookieProperties) {
					    String c[] = cookieProperty.split("=");
					    cookies.put(c[0].trim(), c[1].trim());
						System.out.println(c[0] + "=>" + c[1]);
					}
				}
				//System.out.println(str);
		    }
	
			if(guess != null) {
				//tries = Integer.parseInt(cookies.get("tries"));
				String session = cookies.get("SESSIONID");
				System.out.println("COOKIE:" + session);
				String sss[] = session.split("\\|");
				number = Integer.parseInt(sss[0]);
				tries = Integer.parseInt(sss[1]);
				++tries;
			}
			
			//if(cookies.containsKey("number"))
			//	number = Integer.parseInt(cookies.get("number"));
			
		    // Generate random number if none exists.
		    if(tries == 0) {
			    number = (int)(Math.random() * 100);
			    System.out.println("New number: " + number);
			}

		    System.out.println("Förfrågan klar.");
		    s.shutdownInput();

		    PrintStream response = new PrintStream(s.getOutputStream());
		    response.println("HTTP/1.1 200 OK");
		    response.println("Server : Slask 0.1 Alfa");
		    if(requestedDocument.indexOf(".html") != -1)
				response.println("Content-Type: text/html");
		    if(requestedDocument.indexOf(".gif") != -1)
				response.println("Content-Type: image/gif");
		    
		    if(guess != null && guess == number) {
			    int newNumber = (int)(Math.random() * 100);
			     response.println("Set-Cookie: number=" + newNumber +"|0; expires=Wednesday,31-Dec-12 21:00:00 GMT");
		    				    System.out.println("New number: " + newNumber);


		    } else {
			   		  	  	response.println("Set-Cookie: SESSIONID=" + number + "|" + tries  + "; expires=Wednesday,31-Dec-12 21:00:00 GMT");
			}
	
		    response.println();
		    
		    response.println("<html><head><title>Number Guess Game</title><script type = \"text/javascript\">function inputfocus(form){form.guess.focus()}</script></head><body onLoad=\"inputfocus(document.guessform);\"> ");

		   if(guess == null)
		   		response.println("Welcome to the Number Guess Game. Guess a number between 1 and 100.");
		   else {
			   	response.println("You have made " + tries + " guess(es)<br>");

			   	if(guess > number)
			   		response.println("Nope, guess lower<br>");
			   	else if(guess < number)
			   		response.println("Nope, guess higher<br>");
			   	else {
			   		response.println("You made it!!! ");
			   	}
		   	}
		   
		  response.println("<form name=\"guessform\"><input type=text name=guess><input type=submit value=\"Guess\"></form>");
		    s.shutdownOutput();
		    s.close();
		}
    }
}
