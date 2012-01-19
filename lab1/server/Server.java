import java.util.*;
import java.net.*;
import java.io.*;

class Server {

	public static void main(String[] args) throws IOException {
		final ArrayList<Socket> clientSockets = new ArrayList<Socket>();
		ServerSocket ss = new ServerSocket(1234);
		
		while (true) {
			// wait for connection
			System.out.println("Waiting for connection...");
			final Socket socket = ss.accept();
			System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress());
			clientSockets.add(socket);
			
			// spawn thread to handle communication with client
			Runnable clientRunnable = new Runnable() {
				public void run() {
					try {
						while (true) {
							// read from socket
							InputStream is = socket.getInputStream();
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while (baos.size() == 0 || is.available() > 0) {
							 	int c = is.read();
								if (c == -1) throw new IOException();
								baos.write(c);
							}
							
							System.out.print("Received message: " + baos.toString());
							
							// write received data to all chat clients
							// except the one that sent the message
							synchronized (clientSockets) {
								ArrayList<Socket> deadSockets = new ArrayList<Socket>();
								for (Socket s : clientSockets) {
									if (s == socket) continue;
									try {
										OutputStream os = s.getOutputStream();
										os.write(baos.toByteArray());
									} catch (SocketException e) {
										deadSockets.add(s);
									}
								}
								clientSockets.removeAll(deadSockets);
							}
						}
					} catch (IOException e) {
						// client probably shut down, or some other 
						// read error occurred. close socket and remove it
						try {
							socket.close();
						} catch (IOException e1) {
							// fine
						}
						
						synchronized (clientSockets) {
							clientSockets.remove(socket);
						}
					}
				}
			};
			new Thread(clientRunnable).start();
		}
	}
}
