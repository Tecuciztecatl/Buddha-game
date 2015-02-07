package Being;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Evil_Buddha extends Enemy {

	private double maxHP;
	private boolean Attacking;
	private double Attacks;
	public boolean recharging;
	Texture buddhaNormal = null, buddhaFiring = null;
	public Vector<Energy> energy;
	private boolean left;
	public int energyBall;
	private boolean ishit;
	private int energyhp;
	
	public Evil_Buddha(double hp, int attackmax, double x, double y, double z, double strength, int energyhp) {
		super(hp, attackmax, x, y, z);
		this.maxHP = hp;
		this.Attacking = false;
		this.Attacks = 0;
		this.setStrength(strength);
		this.recharging = false;
		this.left = false;
		this.energy = new Vector<Energy>();
		this.ishit = false;
		this.energyhp = energyhp;

		try {
			buddhaNormal = TextureIO.newTexture(new File("src/Textures/evil_buddha_n.png"), false);
			buddhaFiring = TextureIO.newTexture(new File("src/Textures/evil_buddha_a.png"), false);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void new_Evil_Buddha(double hp, int attackmax, double x, double y, double z, double strength, int energyhp) {
		super.newEnemy(hp, attackmax, x, y, z);
		this.maxHP = hp;
		this.Attacking = false;
		this.Attacks = 0;
		this.setStrength(strength);
		this.recharging = false;
		this.left = false;
		this.energy = new Vector<Energy>();
		this.ishit = false;
		this.energyhp = energyhp;
	}


	/**
	 * @return the ishit
	 */
	public void isHit() {
		this.ishit = true;
	}


	/**
	 * @param ishit the ishit to set
	 */
	public void notHit() {
		this.ishit = false;;
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
	

	
	public void drawBuddha(GL gl) {


		
		if (this.X >= 3.14) {
			this.left = true;
		}
		else if (this.X <= -3.14) {
			this.left = false;
		}
		if (this.left == true) {
			this.X -= 0.03;
			this.Y = -Math.sin(this.X);
		}
		else {
			this.X += 0.03;
			this.Y = Math.sin(this.X);
		}

		gl.glLoadIdentity();
		gl.glPushMatrix();
			gl.glTranslated(-0.48, 0, -1);
			
			//Life BAR
			gl.glBegin(GL.GL_LINE_LOOP);
				gl.glColor3d(1, .1, 0);
				gl.glVertex3d( 0, 0.455, 0);
				gl.glVertex3d(this.getHP()/this.maxHP, 0.455, 0);
			gl.glEnd();
		gl.glPopMatrix();

		gl.glPushMatrix();
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		gl.glTranslated(this.X * ((this.Z /7) * -1), (this.Y * ((this.Z /7) * -1)), this.Z);
		
		if (this.Attacks <= 0)
			 this.recharging = true;
		if (this.Attacks >= 3 && !this.recharging && (this.energy.size() == 0 || this.energy.isEmpty())) {
			this.attack();
		}
		else {
			if(this.Attacks < this.getAttackMAX()) {
				this.Attacks+=0.01;
				if (this.Attacks >= this.getAttackMAX())
					this.recharging = false;
			}
		}

		if (this.ishit) {
			buddhaFiring.bind();
		}
		else {
			buddhaNormal.bind();
		}
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBegin(GL.GL_QUAD_STRIP);
			gl.glTexCoord2d(1, 1);
			gl.glVertex3d( 2.0,-3.0, 0);
			gl.glTexCoord2d(1, 0);
			gl.glVertex3d( 2.0, 3.0, 0);
			gl.glTexCoord2d(0, 1);
			gl.glVertex3d(-2.0,-3.0, 0);
			gl.glTexCoord2d(0, 0);
			gl.glVertex3d(-2.0, 3.0, 0);
		
		gl.glEnd();
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glDisable(GL.GL_BLEND);

		gl.glPopMatrix();
		gl.glPushMatrix();
		
		
		if (!this.energy.isEmpty()){
			if (this.energy.size() != 0) {
				for (int i = this.energy.size() - 1; i >= 0; i--) {
	
					if (!this.energy.get(i).isDead()) {
						if (this.energy.get(i).drawEnergyPattern(gl)) {
							gl.glCallList(this.energyBall);
							gl.glPopMatrix();
							gl.glPushMatrix();
						}
						else {
							this.energy.remove(i);
							this.Attacks--;
						}
					}
					else {
						this.Attacks--;
						this.energy.remove(i);
					}
				}
			}
			
		}
		
		
		//this.attack(gl);
	}


	public void attack() {
		this.energy = new Vector<Energy>();
		for (int i = 0; i < 3; i++) {
			if ( i == 0)
				this.energy.add(new Energy(this.energyhp, 0, 0, Math.random() - 0.5, this.Z, i));
			else if ( i == 1)
				this.energy.add(new Energy(this.energyhp, 0, Math.random() - 0.5, 0, this.Z, i));
			else 
				this.energy.add(new Energy(this.energyhp, 0, 0, 0, this.Z, i));
			this.energy.get(i).setSpeed(0.09);
		}
		
	}
	
	public boolean check_collisions (double X, double Y) {
		//Positive part, then the negative part! that is why I ain't multiplying by -1
		//Now I will need to add an offset to compensate for some parts!, 
		//the closer it is to an edge the bigger the offset because it is dependent
		//to the edges distance
		if (X  < ((this.X/7) - (2/(this.Z))) && X > ((this.X/7) + (2/(this.Z)))) {
				if (Y < ((this.Y/7 ) - (3/(this.Z))) && Y > ((this.Y/7 ) + (3/(this.Z)))) {
					return true;
				}
		}
		return false;
	}
	
	public void checkEnergyCollissions(double X, double Y, double youStrength) {
		if (this.energy.size() != 0) {
			boolean hit = false;
			for (int i = this.energy.size() -1; i >= 0; i-- ) {
				hit = this.energy.get(i).check_collissions(X, Y);
				if (hit) {
					this.energy.get(i).deal_damage(youStrength);
				}
				if (this.energy.get(i).getHP() <= 0) {
					this.energy.get(i).Death_Always_Comes_for_you();
				}
				hit = false;
			}
		}
		else
			return;
	}

	//IT IS THE PART THAT WILL CHECK IF THE ENERGY IS HITTING YOU!
	public void checkEnergyHittingYou(You you) {
		if (this.energy.size() != 0) {
			for (int i = this.energy.size() -1; i >= 0; i-- ) {
				if (this.energy.get(i).Z > -7 && this.energy.get(i).Z < -1 && !this.energy.get(i).isDead()) {
					you.deal_damage(this.energy.get(i).getStrength());
					if (you.getHP() <= 0) {
						you.Death_Always_Comes_for_you();
					}
				}
			}
		}
		else
			return;
	}
	
	public void drawEnergy(GL gl, int parallels, int meridians) {
		double dphi = Math.PI/parallels;
		double dtheta = 2*Math.PI/meridians;
		double phi = 0;
		double theta = 0;
		boolean c = false;
		
		gl.glNewList(energyBall, GL.GL_COMPILE);		
		for(int p = 1; p <= parallels; p ++, phi += dphi){
			//if(p%2 == 1) continue;
			gl.glBegin(GL.GL_TRIANGLE_STRIP);
			theta = 0;
				
			for(int m = 1; m <= meridians + 1; m ++, theta += dtheta){
				double x = Math.sin(phi) * Math.cos(theta);
				double y = Math.sin(phi) * Math.sin(theta);
				double z = Math.cos(phi);
				gl.glVertex3d(x, y, z);

				if (c) {
					gl.glColor3d(0, 0, 0);
					c = !c;
				}
				else {
					gl.glColor3d(1, 0, 1);
					c = !c;
				}
				double x1 = Math.sin(phi + Math.PI/parallels) * Math.cos(theta);
				double y1 = Math.sin(phi + Math.PI/parallels) * Math.sin(theta);
				double z1 = Math.cos(phi + Math.PI/parallels);
				gl.glVertex3d(x1, y1, z1);
			}
			gl.glEnd();
		}	
		gl.glEndList();
	}
	
}
