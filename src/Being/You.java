package Being;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class You {

	private double HP;
	private double maxHP;
	private boolean Attacking;
	private int AttackMAX;
	private double strength;
	private double X;
	private double Y;
	private double Z;
	Texture buddhaNormal = null, buddhaFiring = null;
	public Target target;
	private double attacks;
	public boolean recharging;
	private boolean dead;
	
	/**
	 * 
	 * @param hP User's life
	 * @param attacking Will tell if you are attacking or not
	 * @param attackMAX max number of possible attacks (stamina)
	 * @param x Your position in X
	 * @param y Your position in Y
	 * @param z your position in Z
	 */
	public You(double hP, boolean attacking, int attackMAX, double x, double y, double z) {
		HP = hP;
		maxHP = hP;
		Attacking = attacking;
		AttackMAX = attackMAX;
		strength = 2.2;
		X = x;
		Y = y;
		Z = z;
		recharging = false;
		this.attacks = 999;
		this.dead = false;
		
		try {
			buddhaNormal = TextureIO.newTexture(new File("src/Textures/buddha2.png"), false);
			buddhaFiring = TextureIO.newTexture(new File("src/Textures/buddha.png"), false);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		target = new Target();
	}
	
	public void newYou(double hP, boolean attacking, int attackMAX, double x, double y, double z) {
		HP = hP;
		maxHP = hP;
		Attacking = attacking;
		AttackMAX = attackMAX;
		X = x;
		Y = y;
		Z = z;
		recharging = false;
		this.attacks = 999;
		this.dead = false;
		
		target.resetTarget();
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
	public void setHP(int hP) {
		HP = hP;
	}


	/**
	 * @return the attacking
	 */
	public boolean isAttacking() {
		return Attacking;
	}


	/**
	 * @param attacking the attacking to set
	 */
	public void setAttacking(boolean attacking) {
		Attacking = attacking;
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
	
	public void setStrength (double strength) {
		this.strength = strength;
	}
	
	public double getStrength () {
		return this.strength;
	}
	
	public double getTargetX() {
		return this.target.targetX;
	}
	
	public double getTargetY() {
		return this.target.targetY;
	}
	
	public void deal_damage(double damage) {
		this.HP -= damage;
	}
	
	public void Death_Always_Comes_for_you() {
		this.dead = true;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public void drawBuddha(GL gl) {

		gl.glLoadIdentity();
		gl.glPushMatrix();
		//AMMO BAR
			gl.glTranslated(-0.48, 0, this.Z-1);
			gl.glBegin(GL.GL_LINE_LOOP);
				gl.glColor3d(1, 0.9, 0);
				gl.glVertex3d( 0, 0.46, 0);
				gl.glVertex3d(this.attacks/1000.0, 0.46, 0);
			gl.glEnd();
			
			//Life BAR
			gl.glBegin(GL.GL_LINE_LOOP);
				gl.glColor3d(1, .1, 0);
				gl.glVertex3d( 0, 0.465, 0);
				gl.glVertex3d(this.HP/this.maxHP, 0.465, 0);
			gl.glEnd();
		gl.glPopMatrix();

		gl.glPushMatrix();
		target.drawTarget(gl);		
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		if (this.attacks <= 0)
			 this.recharging = true;
		if (this.isAttacking() && this.attacks > 0 && !this.recharging) {
			this.attack(gl);
			gl.glTranslated(0, -2.5, this.Z-7);
			buddhaFiring.bind();
			this.attacks-=1.2;
		}
		else {
			gl.glTranslated(0, -2.5, this.Z-7);
			buddhaNormal.bind();
			if(this.attacks <= 996) {
				this.attacks+=2;
				if (this.attacks > 600)
					this.recharging = false;
			}
		}
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBegin(GL.GL_QUAD_STRIP);
			gl.glColor4d(1, 1, 1, 0.5);
			gl.glTexCoord2d(1, 1);
			gl.glVertex3d( 0.5,-0.75, 0);
			gl.glTexCoord2d(1, 0);
			gl.glVertex3d( 0.5, 0.75, 0);
			gl.glTexCoord2d(0, 1);
			gl.glVertex3d(-0.5,-0.75, 0);
			gl.glTexCoord2d(0, 0);
			gl.glVertex3d(-0.5, 0.75, 0);
		gl.glEnd();
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glDisable(GL.GL_BLEND);

		gl.glColor3d(1, 1, 1);

		gl.glPopMatrix();
	}

	public void attack(GL gl) {
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glColor3d(1, 0.9, 0);
			gl.glVertex3d( 0, -2.5, this.Z-7);
			gl.glVertex3d(this.target.targetX*8,this.target.targetY*8, this.target.targetZ-8);
		gl.glEnd();
	}
	
}
