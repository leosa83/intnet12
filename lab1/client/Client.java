import java.net.*;
import java.io.*;

class Client {
	
	public static void main(String[] args) {
		String address = args[0];
		int port = 0;
		try {
			port = Integer.parseInt(args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// connect to server
			System.out.println("connecting...");
			final Socket socket = new Socket(address, port);
			System.out.println("connected");
			// thread to read input from user
			Runnable readThread = new Runnable() {				
				public void run() {
					// read input from socket
					try {
						while (true) {
							InputStream is = socket.getInputStream();
							while (true) {
								char c = (char) is.read();
								System.out.print(c);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(readThread).start();

			try {
				while (true) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
					String line = reader.readLine();
					OutputStream os = socket.getOutputStream();
					os.write(line.getBytes());
					os.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
