import javax.net.ssl.*;
import java.io.*;

public class Server{
	public static void main(String[] args) {
		SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		System.out.println("Stöder:");
		for(int i = 0; i < ssf.getSupportedCipherSuites().length; i++)
			System.out.println(ssf.getSupportedCipherSuites()[i]);
		SSLServerSocket ss = null;
		try {
			ss = (SSLServerSocket)ssf.createServerSocket(8080);
			String[] cipher = {"SSL_RSA_WITH_RC4_128_MD5"};
			ss.setEnabledCipherSuites(cipher);
			System.out.println("Vald:");
			for(int i = 0; i < ss.getEnabledCipherSuites().length; i++)
				System.out.println(ss.getEnabledCipherSuites()[i]);
			SSLSocket s = (SSLSocket)ss.accept();
			System.out.println("Accepted connection");
			BufferedReader infil = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String rad = null;
			while( !(rad=infil.readLine()).equals(""))
				System.out.println(rad);
			infil.close();

			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			out.print("HTTP/1.1 200 OK\r\n");
			out.print("Date: Mon, 23 May 2005 22:38:34 GMT\r\n");
			out.print("Server: SecureServer 13.37\r\n");
			out.print("Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\r\n");
			out.print("Content-Length: 11\r\n");
			out.print("Connection: close\r\n");
			out.print("Content-Type: text/html; charset=UTF-8\r\n");
			out.print("\r\n");
			out.print("TOP SECRET!\r\n");
			out.print("\r\n");

			out.close();
			//s.close();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}