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
			System.out.println("Accepted connection.");
			clientSockets.add(socket);
			
			// spawn thread to handle communication with client
			Runnable clientRunnable = new Runnable() {
				public void run() {
					System.out.println("Spawned");
					try {
						while (true) {
							// read from socket
							InputStream is = socket.getInputStream();
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while (baos.size() == 0 || is.available() > 0) {
							 	baos.write(is.read());
							}
							
							System.out.println("Received: " + baos.toString());
						
							// write received data to all chat clients
							synchronized (clientSockets) {
								for (Socket s : clientSockets) {
									OutputStream os = s.getOutputStream();
									os.write(baos.toByteArray());
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						// client probably shut down, or some other read error occurred
						// close socket and remove it
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
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
