package Being;

import javax.media.opengl.GL;

public class Target {
	public double targetX = 0, targetY = 0, targetZ = -0;
	public boolean UP, DOWN, RIGHT, LEFT = false;
	
	public Target () {
		targetX = 0; targetY = 0; targetZ = -0;
		UP = false; DOWN = false; RIGHT = false; LEFT = false;
	}
	
	public void resetTarget () {
		targetX = 0; targetY = 0; targetZ = -0;
		UP = false; DOWN = false; RIGHT = false; LEFT = false;
	}

	public void drawTarget(GL gl) {
		if (DOWN) targetY -= 0.015;
		else if (UP) targetY += 0.015;
		if (RIGHT) targetX += 0.015;
		else if (LEFT) targetX -= 0.015;

		if (targetX >= 0.469) targetX = 0.469;
		else if (targetX <= -0.469) targetX = -0.469;
		if (targetY >= 0.455) targetY = 0.455;
		else if (targetY <= -0.455) targetY = -0.455;
	
	gl.glLoadIdentity();
	gl.glTranslated(targetX, targetY, targetZ-1);
			gl.glBegin(GL.GL_LINE_LOOP);
				gl.glColor3d(1, 0.9, 0);
				gl.glVertex3d(  0.02, 0.016, 0);
				gl.glVertex3d( 0.016,  0.02,  0);
				gl.glVertex3d(-0.016,  0.02,  0);
				gl.glVertex3d( -0.02, 0.016,  0);
				gl.glVertex3d( -0.02,-0.016,  0);
				gl.glVertex3d(-0.016, -0.02,  0);
				gl.glVertex3d( 0.016, -0.02,  0);
				gl.glVertex3d(  0.02,-0.016,  0);
			gl.glEnd();

			gl.glBegin(GL.GL_LINE_LOOP);
				gl.glVertex3d( 0.007, 0.007, 0);
				gl.glVertex3d(-0.007,-0.007, 0);
				gl.glEnd();
				gl.glBegin(GL.GL_LINE_LOOP);
				gl.glVertex3d(-0.007, 0.007, 0);
				gl.glVertex3d( 0.007,-0.007, 0);
			gl.glEnd();
	}
}
