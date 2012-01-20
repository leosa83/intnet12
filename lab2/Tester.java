import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;

public class Tester {
	
	public static String readResponse(HttpURLConnection conn) throws Exception {
		InputStream is = conn.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			int b = is.read();
			if (b == -1) break;
			baos.write(b);
		}
		
		is.close();
		conn.disconnect();
		
		return baos.toString();
	}

	public static void main(String[] args) throws Exception {
		float guess_avg = 0;
		String host = "localhost/~emil/guess.php";
		
		for (int i = 1; i <= 100; i++) {
			URL url = new URL("http://130.229.163.150:8080/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Map<String,List<String>> map = conn.getHeaderFields();
			List<String> cookies = map.get("Set-Cookie");
			String cookie = "";
			for (String c : cookies) {
				cookie += c + "; ";
			}
			
			readResponse(conn);

			int lower = 0;
			int upper = 100;
			int guess_count = 0;
			while (true) {
				guess_count++;
				int guess = (upper - lower)/2 + lower;
				//System.out.println("Guessing " + guess);
				url = new URL("http://130.229.163.150:8080/" + "?guess=" + guess);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Cookie", cookie);
				String html = readResponse(conn);
				
				if (html.indexOf("higher") != -1) { // guess was too low
					lower = guess;
				} else if (html.indexOf("lower") != -1) { // guess was too high
					upper = guess;
				} else if (html.indexOf("!!!") != -1) { // win
					//System.out.println("guess_count: " + guess_count);
					guess_avg = ((i-1) * guess_avg + guess_count) / i;
					break;
				} else {
					System.out.println("weird response: \n" + html);
					break;
				}
			}
		}
		
		System.out.println("guess_avg: " + guess_avg);
	}
	
}
