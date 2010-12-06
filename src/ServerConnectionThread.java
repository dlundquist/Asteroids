import java.net.*;
import java.io.*;

public class ServerConnectionThread extends Thread {
	private DedicatedServer server;
	private Socket client;
	private NetworkPlayer player;

	public ServerConnectionThread(Socket client, DedicatedServer server) {
		this.client = client;
		this.server = server;
	}

	public void run() {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
	
			NetworkUpdate inboundUpdate;
	
			while ((inboundUpdate = (NetworkUpdate)in.readObject()) != null) {
				inboundUpdate.applyUpdate(this);
				out.writeObject(new NetworkUpdate());
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (player != null)
					player.leaveGame();
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

	public void addPlayer(NetworkPlayer p) {
		player = p;
		server.addPlayer(player);
	}
	
	public int getPlayerShipId() {
		return player.getShipId();
	}

	public void updateActor(Actor a) {
		server.updateActor(a);
	}
}