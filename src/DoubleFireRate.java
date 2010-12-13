
public abstract class DoubleFireRate {
	public static int doubleShotsLeft;
	public static int banditDoubleShots;
	public static void setDoubleShotsLeft(int bullets){
		if (bullets <= 0)bullets = 0;
		doubleShotsLeft = bullets;
	}
	public static void setBanditDoubleShotsLeft(int bullets){
		if (bullets <= 0)banditDoubleShots = bullets;
	}
	public static boolean isDoubleFireRate(){
		if (doubleShotsLeft>0)return true;
		else return false;
	}
	public static boolean isBanditDoubleFireRate(){
		if (banditDoubleShots>0)return true;
		else return false;
	}
	public static int getDoubleShotsLeft(){
		return doubleShotsLeft;
	}
	public static int getBanditDoubleShotsLeft(){
		return banditDoubleShots;
	}
}
