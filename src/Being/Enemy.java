package Being;

public class Enemy {

	private double HP;
	private int AttackMAX;
	private double Strength;
	public double X;
	public double Y;
	public double Z;
	private double Speed;
	private boolean Visible;
	private boolean dead;
	
	public Enemy (double hp, int attackmax, double x, double y, double z) {
		this.setHP(hp);
		this.setAttackMAX(attackmax);
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setVisible(true);
		this.Speed = 0.1;
		this.dead = false;
		this.Strength = 0.08;
	}
	
	public void newEnemy (double hp, int attackmax, double x, double y, double z) {
		this.setHP(hp);
		this.setAttackMAX(attackmax);
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setVisible(true);
		this.Speed = 0.1;
		this.dead = false;
		this.Strength = 0.08;
	}
	
	public void move() {
		if (this.Speed > 0)
			this.Z = this.Z + this.Speed;
	}
	

	/**
	 * @return the hP
	 */
	public double getHP() {
		return HP;
	}

	/**
	 * @param hP the hP to set
	 */
	public void setHP(double hP) {
		HP = hP;
	}

	/**
	 * @return the attackMAX
	 */
	public int getAttackMAX() {
		return AttackMAX;
	}

	/**
	 * @param attackMAX the attackMAX to set
	 */
	public void setAttackMAX(int attackMAX) {
		AttackMAX = attackMAX;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return X;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		X = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return Y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		Y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return Z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		Z = z;
	}


	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return Visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		Visible = visible;
	}
	
	public void deal_damage(double strength) {
		this.HP -= strength;
	}
	
	public void Death_Always_Comes_for_you() {
		this.dead = true;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public double getStrength () {
		return this.Strength;
	}
	
	public void setStrength (double strength) {
		this.Strength = strength;
	}
	
	public void setSpeed(double speed) {
		this.Speed = speed;
	}
	
	public double getSpeed() {
		return this.Speed;
	}
	
}
