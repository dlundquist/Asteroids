import java.net.*;
import java.util.Random;
import java.io.*;

/**
 * This class provides a the network client for the game to join a server
 * @author Dustin Lundquist
 */
public class NetworkClientThread extends Thread {
	// TODO GUI interface to chose a server
	public static final String SERVER_HOSTNAME = "localhost";
	
	private Socket server;
	private String playerName;

	public NetworkClientThread(InetAddress addr, String name) {
		
		try {
			server = new Socket(addr, DedicatedServer.SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			out = new ObjectOutputStream(server.getOutputStream());
			in = new ObjectInputStream(server.getInputStream());
	
			out.writeObject(new NetworkUpdate(playerName));
			
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
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				server.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getPlayerShipId() {
		return Asteroids.getPlayer().id;
	}

	public static void joinGame(PlayerShip ship) {
		// TODO player name GUI interface
		Random rand = new Random();
		String name = "Player" + rand.nextInt(4);
		try {
			InetAddress addr = InetAddress.getByName(SERVER_HOSTNAME);
			
			new NetworkClientThread(addr, name).start();
		} catch (UnknownHostException e) {
			System.err.println("Unable to resolve " + SERVER_HOSTNAME);	
		}
	}
}
