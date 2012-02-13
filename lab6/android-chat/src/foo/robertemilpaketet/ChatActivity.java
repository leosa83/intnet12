package foo.robertemilpaketet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChatActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button sendButton = (Button) findViewById(R.id.send_button);
		final EditText sendTextView = (EditText) findViewById(R.id.send_textview);
		final EditText receiveTextView = (EditText) findViewById(R.id.receive_textview);

		String address = "130.229.147.227";
		int port = 1234;

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
							final ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while (baos.size() == 0 || is.available() > 0) {
								int c = is.read();
								if (c == -1)
									throw new IOException();
								baos.write(c);
							}

							
							receiveTextView.post(new Runnable() {
								
								public void run() {
									// print the received message
									receiveTextView.append("\n> " + baos.toString());
								}
							});
							
						}
					} catch (IOException e) {
						System.out.println("Server closed connection.");
						System.exit(0);
					}
				}
			};
			new Thread(readThread).start();

			sendButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					try {
						CharSequence cs = sendTextView.getText();
						String s = cs.toString();
						sendTextView.setText("");
						receiveTextView.append("\n" + s);
						
						OutputStream os = socket.getOutputStream();
						os.write(s.getBytes());
						//os.write("\n".getBytes());
						os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}