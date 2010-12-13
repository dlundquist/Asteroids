
import java.awt.event.KeyEvent;



public class Input  {

	protected final static int REACTION_TIME = 2;
	protected static int leftTimer = 100;
	protected static int rightTimer = 100;
	protected static int upTimer = 100;
	protected static int downTimer = 100;
	protected static int spaceTimer = 100;
	protected static boolean ignoreUpDown;
	private static KeyboardInput keyboard = new KeyboardInput();
	static PlayerShip player = Asteroids.getPlayer();
	public static void update(){	
		
			KeyboardInput.poll();
//Simultaneous press functions-------------------------------------------------------------
		if(keyboard.keyDown(KeyEvent.VK_UP)&&(keyboard.keyDown(KeyEvent.VK_DOWN))){
			player.brakeShip();
			upTimer++;
			downTimer++;
			ignoreUpDown = true;
		}else ignoreUpDown = false;
		if (upTimer < REACTION_TIME && spaceTimer < REACTION_TIME){
			player.boostShip();
			upTimer = REACTION_TIME;
			spaceTimer = REACTION_TIME;
		}
		if (downTimer < REACTION_TIME && spaceTimer < REACTION_TIME){
			player.warpShip();
			downTimer = REACTION_TIME;
			spaceTimer = REACTION_TIME;
		}
		if (leftTimer < REACTION_TIME && rightTimer < REACTION_TIME){	
			player.flipShip();
			leftTimer = REACTION_TIME;
			rightTimer = REACTION_TIME;
		}
	
		if (upTimer < REACTION_TIME && downTimer < REACTION_TIME){
			player.superBrakeShip();
			upTimer = REACTION_TIME;
			downTimer = REACTION_TIME;
		}
		
//Key released functions-------------------------------------------------------------------
		if (keyboard.keyReleased(KeyEvent.VK_DOWN)){
			downTimer = REACTION_TIME;
		}
		if (keyboard.keyReleased(KeyEvent.VK_UP)){
			upTimer = REACTION_TIME;
		}
		if (keyboard.keyReleased(KeyEvent.VK_LEFT)){
			leftTimer = REACTION_TIME;
		}
		if (keyboard.keyReleased(KeyEvent.VK_RIGHT)){
			rightTimer = REACTION_TIME;
		}
		if (keyboard.keyReleased(KeyEvent.VK_SPACE)){
			spaceTimer = REACTION_TIME;
		}
//Key down functions---------------------------------------------------------------------
		if(keyboard.keyDown(KeyEvent.VK_DOWN)&& (ignoreUpDown == false)) {
			player.reverseThrust();
			downTimer++;
			System.out.println(downTimer);
			ignoreUpDown = false;
		}
		if(keyboard.keyDown(KeyEvent.VK_UP)&& (ignoreUpDown == false)) {
			player.forwardThrust();
			upTimer++;
			ignoreUpDown = false;
		}
		if(keyboard.keyDown(KeyEvent.VK_LEFT)){
			player.turnLeft();
			leftTimer++;
		}
		if(keyboard.keyDown(KeyEvent.VK_RIGHT)){
			player.turnRight();
			rightTimer++;
		}
		if(keyboard.keyDown(KeyEvent.VK_SPACE)){
			player.shoot();
			spaceTimer++;
		}
//Key down once functions----------------------------------------------------------
		if(keyboard.keyDownOnce(KeyEvent.VK_DOWN)) {
			player.reverseThrust();
			downTimer = 0;
			ignoreUpDown = false;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_UP)) {
			player.forwardThrust();
			upTimer = 0;
			ignoreUpDown = false;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_LEFT)){
			player.turnLeft();
			leftTimer = 0;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_RIGHT)){
			player.turnRight();
			rightTimer = 0;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)){
			player.shoot();
			spaceTimer = 0;
		}

		if(keyboard.keyDownOnce(KeyEvent.VK_W)){
			player.warpShip();
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_S)){
			player.flipShip();
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_A)){
			player.boostShip();
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_D)){
			player.superBrakeShip();
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_P)){
			Asteroids.togglePause();
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_PAUSE)){
			Asteroids.togglePause();
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_ESCAPE)){
			Asteroids.quitToMenu();
		}
//Regenerate-------------------------------------------------------------------------------
		if(keyboard.keyDown(KeyEvent.VK_SPACE) && (player.isAlive() == false)){
			player.regenerate();
		}
	}
}
