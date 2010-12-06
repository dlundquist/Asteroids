import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class DedicatedServer {
	public static final int SERVER_PORT = 4444;
	private static final long FRAME_RATE = 1000 / 60; /* 60 FPS */
	
	public static void main(String[] args) {
		new DedicatedServer();
	}


	private ServerSocket serverSocket;
	private boolean running;
	private java.util.Vector<NetworkPlayer> players;
	private java.util.Vector<Actor> updates;
	
	public DedicatedServer() {
		running = true;
		players = new java.util.Vector<NetworkPlayer>();
		updates = new java.util.Vector<Actor>();
		
		
		try {
		    serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
		    System.out.println("Could not listen on port: " + SERVER_PORT);
		    System.exit(-1);
		}
	
		/* Start a thread to listen for clients */
		new ListenThread(this).start();
		
		/* Start a timer to handle our per frame updates */
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new UpdateTask(), 0, FRAME_RATE);

		/* Run our little CLI */
		Scanner kbd = new Scanner(System.in);
	
		System.out.println("Server started");
		
		while (running) {
			System.out.print("server# ");
			processCommand(kbd.next());
		}
		
		/* Clean up */
		timer.cancel();
		
		try {
			/* Close the socket so the listener thread will stop blocking */
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processCommand(String cmd) {
		cmd.trim();
		
		if (cmd.startsWith("exit")) {
			System.out.println("Exiting...");
			running = false;			
		} else {
			System.out.println("Unsupported command " + cmd);
		}
	}

	/* Called by our timer every "frame" */
	public void update() {
		Actor.collisionDetection();
		Actor.updateActors();
		
		for (Actor a: updates) {
			// Remove the old copy of this actor if it exists from the servers actor vector
			Actor.removeActorId(a.id);
			// Replace it with the updated client version
			Actor.actors.add(a);
		}
		updates.clear();
	}

	/**
	 * This class handles accepting new client connections
	 * and spawns a ServerConnectionThread for each client
	 * @author Dustin Lundquist
	 */
	private class ListenThread extends Thread {
		private DedicatedServer server;
		
		/* We need need a reference to this server, so we can pass it on to the connection thread */
		public ListenThread(DedicatedServer dedicatedServer) {
			server = dedicatedServer;
		}

		public void run() {
			while (running) {
				try {
					Socket client = serverSocket.accept();
					new ServerConnectionThread(client, server).start();
				} catch (SocketException e) {
					if (e.getMessage().equals("Socket closed") && running == false) {
						System.err.println("Server socket closed, shutting down.");
					} else {
						e.printStackTrace();						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class UpdateTask extends TimerTask {
		public void run() {
			update();
		}
	}

	public void addPlayer(NetworkPlayer player) {
		players.add(player);
		player.start();
	}

	public void updateActor(Actor a) {
		updates.add(a);
	}
}
