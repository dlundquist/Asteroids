import java.io.Serializable;
import java.util.Vector;

public class NetworkUpdate implements Serializable {
	private static final long serialVersionUID = -5954591417897423857L;
	private static final int MAX_AGE_TO_UPDATE_FROM_CLIENT = 50;

	private enum UpdateType {
		REQUEST,
		AKNOWLEDGE,
		UPDATE,
	};
	private Vector<Actor> actors;
	private UpdateType type;
	private String playerName;

	public NetworkUpdate() {
		actors = Actor.actors;
		type = UpdateType.UPDATE;
	}

	public NetworkUpdate(String playerName) {
		this.playerName = playerName;
		type = UpdateType.REQUEST;
	}

	/**
	 * Apply an update from the server to a client
	 * @param client
	 */
	public void applyUpdate(NetworkClientThread client) {
		switch (type) {
		case AKNOWLEDGE:
		case UPDATE:
			for (Actor a : actors) {
				if (a.id != client.getPlayerShipId() && (a.parentId != client.getPlayerShipId() || a.age >= MAX_AGE_TO_UPDATE_FROM_CLIENT)) {
					Actor.removeActorId(a.id);
					Actor.actors.add(a);
				}
				// else keep track of our own player ship and our bullets we just shot
			}
			break;
		default:
			System.err.println("Network protocol error");

		}
	}

	/**
	 * Apply an update form a client to the server
	 * @param server
	 */
	public void applyUpdate(ServerClientThread server) {
		switch (type) {
		case REQUEST:
			NetworkPlayer p = new NetworkPlayer(playerName);
			server.addPlayer(p);
			break;
		case UPDATE:
			for (Actor a : actors) {
				if (a.id == server.getPlayerShipId()) {
					Actor.removeActorId(a.id);
					Actor.actors.add(a);
				} else if (a.parentId == server.getPlayerShipId() && a.age < MAX_AGE_TO_UPDATE_FROM_CLIENT) {
					Actor.removeActorId(a.id);
					Actor.actors.add(a);					
				}
				// else do not accept updates for other objects from the client
			}
			break;
		default:
			System.err.println("Network protocol error");
		}
	}
}
