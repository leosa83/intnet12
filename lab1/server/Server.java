import java.util.*;
import java.net.*;
import java.io.*;

class Server {

	public static void main(String[] args) throws IOException {
		final ArrayList<Socket> clientSockets = new ArrayList<Socket>();
		ServerSocket ss = new ServerSocket(1234);
		
		while (true) {
			// wait for connection
			final Socket socket = ss.accept();
			clientSockets.add(socket);
			
			// spawn thread to handle communication with client
			Runnable clientThread = new Runnable() {
				public void run() {
					try {
						while (true) {
							// read from socket
							InputStream is = socket.getInputStream();
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while (is.available() > 0) {
							 	baos.write(is.read());
							}
						
							// write received data to all chat clients
							synchronized (clientSockets) {
								for (Socket s : clientSockets) {
									OutputStream os = s.getOutputStream();
									os.write(baos.toByteArray());
								}
							}
						}
					} catch (IOException e) {
						// client probably shut down, or some other read error occurred
						// close socket and remove it
						try {
							socket.close();
						} catch (IOException e1) {}
						synchronized (clientSockets) {
							clientSockets.remove(socket);
						}
					}
				}
			};
		}
	}
}
