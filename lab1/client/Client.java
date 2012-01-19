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
						while (!socket.isInputShutdown()) {
							InputStream is = socket.getInputStream();
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while (baos.size() == 0 || is.available() > 0) {
								int c = is.read();
								if (c == -1) throw new IOException();
								baos.write(c);
							}
							
							// print the received message
							System.out.print("> " + baos.toString());
						}
					} catch (IOException e) {
						System.out.println("Server closed connection.");
						System.exit(0);
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
					os.write("\n".getBytes());
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
