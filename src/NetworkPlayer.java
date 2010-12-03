public class NetworkPlayer  {
	private String name;
	private PlayerShip ship;
	private boolean started;
	
	public NetworkPlayer(String n) {
		name = n;
		ship = new PlayerShip();
		started = false;
	}
	
	public void start() {
		Actor.actors.add(ship);
		started = true;
	}
	
	public void leaveGame() {
		Actor.actors.remove(ship);
		started = false;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public String getName() {
		return name;
	}
	
	public int getShipId() {
		return ship.id;
	}
}
