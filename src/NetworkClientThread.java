import java.net.*;
import java.io.*;

public class NetworkClientThread extends Thread {
	private Socket client;

	public NetworkClientThread(Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(client.getInputStream());
	
			
			
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
