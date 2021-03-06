package Being;

import javax.media.opengl.GL;

public class Energy extends Enemy {

	private int pattern;
	private boolean reverse;
	private double offsetX, offsetY;
	private int angle;
	
	public Energy(double hp, int attackmax, double x, double y, double z, int pattern) {
		super(hp, attackmax, x, y, z);
		this.pattern = pattern;
		this.setStrength(0.25);
		this.offsetX = 0;
		this.offsetY = 0;
		this.angle = 0;
	}

	public boolean drawEnergyPattern (GL gl) {
		if (this.Z < 0)
			this.move();
		else
			return false;
		if (this.pattern == 0) {
			if (reverse) {
				if (this.offsetX - 0.01 > (-0.44) )
					this.offsetX -= 0.01;
				else
					reverse = false;
			}
			else {
				if (this.offsetX + 0.01 < (0.44) )
					this.offsetX += 0.01;
				else
					reverse = true;
			}
		}
		else if (this.pattern == 1) {
			if (reverse) {
				if (this.offsetY - 0.01 > (-0.44) )
					this.offsetY -= 0.01;
				else
					reverse = false;
			}
			else {
				if (this.offsetY + 0.01 < (0.44) )
					this.offsetY += 0.01;
				else
					reverse = true;
			}
		}
		else if (this.pattern == 2) {
			this.offsetX = 0.2 * Math.cos(Math.toRadians(angle));
			this.offsetY = 0.2 * Math.sin(Math.toRadians(angle));
			angle += 2;
		}
		gl.glTranslated((this.X+this.offsetX) * this.Z *-1, (this.Y + this.offsetY) * this.Z *-1, this.Z);
		return true;
	}
	

	public boolean check_collissions (double X, double Y) {
		//Positive part, then the negative part! that is why I ain't multiplying by -1
		//Now I will need to add an offset to compensate for some parts!, 
		//the closer it is to an edge the bigger the offset because it is dependent
		//to the edges distance
		if (X  < ((this.X + this.offsetX) - (1/(this.Z))) && X > ((this.X + this.offsetX) + (1/(this.Z)))) {
				if (Y < ((this.Y  + this.offsetY) - (1/(this.Z))) && Y > ((this.Y  + this.offsetY) + (1/(this.Z)))) {
					return true;
				}
		}
		return false;
	}
	
}
