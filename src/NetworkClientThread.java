import java.net.*;
import java.io.*;

public class NetworkClientThread extends Thread {
	private Socket client;

	public NetworkClientThread(Socket client) {
		this.client = client;
	}

	public void run() {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
	
			NetworkUpdate inboundUpdate;
	
			while ((inboundUpdate = (NetworkUpdate)in.readObject()) != null) {
				inboundUpdate.applyClientUpdate();
				out.writeObject(new NetworkUpdate());
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				client.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}