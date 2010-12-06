import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class DedicatedServer {
	public static final int SERVER_PORT = 4444;
	private static final long FRAME_RATE = 1000 / 60; /* 60 FPS */

	private ServerSocket serverSocket;
	private boolean running;
	private java.util.Vector<NetworkPlayer> players;
	
	public DedicatedServer() {
		running = true;
		
		try {
		    serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
		    System.out.println("Could not listen on port: " + SERVER_PORT);
		    System.exit(-1);
		}
	
		/* Start a thread to listen for clients */
		new ListenThread(this).start();
		
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new UpdateTask(), 0, FRAME_RATE);

		Scanner kbd = new Scanner(System.in);
	
		System.out.println("Server started");
		
		while (running) {
			System.out.print("server# ");
			processCommand(kbd.next());
		}
		timer.cancel();
		
		try {
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

	public void update() {
		Asteroids.update();
	}

	
	private class ListenThread extends Thread {
		private DedicatedServer server;
		
		public ListenThread(DedicatedServer dedicatedServer) {
			server = dedicatedServer;
		}

		public void run() {
			while (running) {
				try {
					Socket client = serverSocket.accept();
					new ServerClientThread(client, server).start();
				} catch (SocketException e) {
					if (e.getMessage().equals("Socket closed") && running == false) {
						System.err.println("Server socket closed, shutting down.");
					} else {
						e.printStackTrace();						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
	
	private class UpdateTask extends TimerTask {
		public void run() {
			update();
		}
	}
	
	public static void main(String[] args) {
		new DedicatedServer();
	}

	public void addPlayer(NetworkPlayer player) {
		players.add(player);
		player.start();
	}
}
