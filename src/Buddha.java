import static javax.media.opengl.GL.GL_DEPTH_TEST;

import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import Being.*;

/**
 * @author Tecuciztecatl
 *
 */
public class Buddha extends GLCanvas implements GLEventListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FPSAnimator animator;
	private GLU glu = new GLU();
	public double z = -1.0;
	public int inc = 1;
	private You you;
	private Enemies enemies;
	private Evil_Buddha ebuddha;
	private int nEnemies;
	
	boolean menu = true;
	double accelx, accely = 0;
	double accelpf = 0.01;
	
	TextRenderer renderer;
	int RESUME = 0, NEW_GAME = 1, EXIT = 2;
	int menuitem = 1;
	
	public Buddha() {
		// Create an animation of 20 fps.
		animator = new FPSAnimator(this, 80);
		animator.start();
		// animator handles all basic events of OpenGL (init, display, reshape)
		addGLEventListener(this);
		addKeyListener(this);
	}

	public Buddha(GLCapabilities arg0) {
		super(arg0);
		// Create an animation of 20 fps.
		animator = new FPSAnimator(this, 80);
		animator.start();

		addGLEventListener(this);
		addKeyListener(this);
	}

	public Buddha(GLCapabilities arg0, GLCapabilitiesChooser arg1, GLContext arg2,
			GraphicsDevice arg3) {
		super(arg0, arg1, arg2, arg3);
		// Create an animation of 20 fps.
		animator = new FPSAnimator(this, 80);
		animator.start();

		addGLEventListener(this);
		addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// This was a test code line to get the codes for the keys pressed.
		//System.out.println(e.getKeyCode());

		// if you are not attacking or in the menu, and you are pressing the spacebar, start attacking
		if (e.getKeyCode() == KeyEvent.VK_SPACE && !menu && !you.isAttacking()) {
			you.setAttacking(true);
		}
		
		// if you are pressing up or w, set target's upward acceleration to true.
		if((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && !menu) { 
			you.target.UP = true;
			// if you were previously pressing down, set downward acceleration to false.
			if (you.target.DOWN) { you.target.DOWN = false;  accely = 0; }
		}
		// if you are pressing down or s, set target's downward acceleration to true.
		else if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && !menu) {
			you.target.DOWN = true;
			// if you were previously pressing up, set upward acceleration to false.
			if (you.target.UP) { you.target.UP = false; accely = 0;}
		}
		// if you are pressing left or a, set target's leftward acceleration to true.
		if((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && !menu) {
			you.target.LEFT = true;
			// if you were previously pressing right, set rightward acceleration to false.
			if (you.target.RIGHT) { you.target.RIGHT = false;  accelx = 0; }
		}
		// if you are pressing right or d, set target's rightward acceleration to true.
		else if((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && !menu) {
			you.target.RIGHT = true;
			// if you were previously pressing left, set leftward acceleration to false.
			if (you.target.LEFT) { you.target.LEFT = false;  accelx = 0; }
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		// when the esc button is released, set the menu status contrary to it's current status.
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			menu = !menu;
		}
		// When the up arrow or w is released set target up to false and set y acceleration to 0,
		// and if you are inside the menu, move the pointer up one time.
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) { 
			you.target.UP = false;
			accely = 0;
			if (menu) {
				if (menuitem > 0)
					menuitem--;
				else
					menuitem = EXIT;
			}
		}
		// When the down arrow or s is released set target down to false and set y acceleration to 0,
		// and if you are inside the menu, move the pointer down one time.
		if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			you.target.DOWN = false;
			accely = 0;
			if (menu)
				if (menuitem < EXIT)
					menuitem++;
				else
					menuitem = 0;
		}
		// When the left arrow or a is released set target left to false and set x acceleration to 0,
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			you.target.LEFT = false;
			accelx = 0;
		}
		// When the right arrow or d is released set target right to false and set x acceleration to 0,
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			you.target.RIGHT = false;
			accelx = 0;
		}
		// When the spacebar is release, stop attacking
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			you.setAttacking(false);
		}
		// When the enter key is pressed and you are inside the menu, execute the menu option.
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (menuitem == RESUME)
				menu = !menu;
			else if (menuitem == EXIT) {
					this.setVisible(false);
					this.setEnabled(false);
					System.exit(0);
			}
			else if (menuitem == NEW_GAME) {
				this.New_Game();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void menu (GLAutoDrawable drawable, GL gl) {
		// This will draw the black background where the menu will be presented
		gl.glLoadIdentity();
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		gl.glColor3d(0.1f, 0.1f, 0.1f);
			gl.glVertex3d(-0.5, 0.5, -1);
			gl.glVertex3d(-0.5,-0.5, -1);
			gl.glVertex3d( 0.5, 0.5, -1);
			gl.glVertex3d( 0.5,-0.5, -1);
		gl.glEnd();
		
		// When you defeat the final boss (evil Buddha), the menu shows up showing the status of your adventure.
		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		if (you.isDead()) {
			renderer.setColor(0.6f, 0.9f, 0.2f, 1f);
			renderer.draw3D("I IS DEAD!", (drawable.getWidth()/2) - 100, (drawable.getHeight()/2) + 60, 0, 1);
		}
		else if (ebuddha.isDead()) {
			renderer.setColor(0.6f, 0.9f, 0.2f, 1f);
			renderer.draw3D("I IS ALIVE!", (drawable.getWidth()/2) - 100, (drawable.getHeight()/2) + 60, 0, 1);
		}
		
		// These will show the menu and will change the colour of the selected item.
		if (menuitem == RESUME)
			renderer.setColor(1.0f, 0.2f, 0.2f, 1f);
		else
			renderer.setColor(0.0f, 0.0f, 1.0f, 1f);
	    renderer.draw3D("Continue", (drawable.getWidth()/2) - 100, (drawable.getHeight()/2), 0, 1);
	    
		if (menuitem == NEW_GAME)
			renderer.setColor(1.0f, 0.2f, 0.2f, 1f);
		else
			renderer.setColor(0.0f, 0.0f, 1.0f, 1f);
	    renderer.draw3D("New Game", (drawable.getWidth()/2) - 100, (drawable.getHeight()/2) - 30, 0, 1);
	    
		if (menuitem == EXIT)
			renderer.setColor(1.0f, 0.2f, 0.2f, 1f);
		else
			renderer.setColor(0.0f, 0.0f, 1.0f, 1f);
	    renderer.draw3D("Quit", (drawable.getWidth()/2) - 100, (drawable.getHeight()/2) - 60, 0, 1);
	    // ... more draw commands, color changes, etc.
	    renderer.endRendering();
	}

	public double angle = 0;
	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		// Get access to the OpenGL functions
		GL gl = drawable.getGL();
		// Clean the buffer colour
		// with the established colour inside the init() method 
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		// Everything that should be drawn must be in here
		
		//This will stop the elements drawn from spinning.
		gl.glLoadIdentity();
		
		if (you.isDead() || ebuddha.isDead()) {
			menu = true;
		}
		if (menu) {
			this.menu(drawable, gl);
		}
		else {
			// Draw all enemies
			gl.glLoadIdentity();
			this.DrawEnemies(gl, enemies.enemy);

			// Draw yourself
			gl.glLoadIdentity();
			you.drawBuddha(gl);
			
		}
		if (angle > 360) angle = 0;		
	}

	public void DrawEnemies (GL gl, Vector<Enemy> enemy) {
		boolean hit = false;
		gl.glLoadIdentity();
		
		gl.glPushMatrix();
		
		// If evil Buddha is not dead and is close to you, start he will start moving around the screen.
		if (!ebuddha.isDead() && ebuddha.Z < -50.0) {
			ebuddha.move();
		}
		// If evil Buddha is still far from you, he should not be drawn
		if (ebuddha.Z >= -100) {
			// If you are attacking and evil Buddha is in range, check if you are hitting him.
			if (ebuddha.Z >= -50.0 && you.isAttacking() && !you.recharging) {
				// First check if there are any collisions with the energy that evil Buddha shoots.
				ebuddha.checkEnergyCollissions(you.getTargetX(), you.getTargetY(), you.getStrength());
				// Then check collisions with evil Buddha.
				hit = ebuddha.check_collisions(you.getTargetX(), you.getTargetY());
				// If evil Buddha is hit, make him brighter and deal damage to him.
				if (hit) {
					ebuddha.isHit();
					gl.glColor3d(1, 0.9, 0);
					ebuddha.deal_damage(you.getStrength());
					// If evil Buddha's hp reaches 0, destroy unit.
					if (ebuddha.getHP() <= 0) {
						ebuddha.Death_Always_Comes_for_you();
					}
				}
				else {
					ebuddha.notHit();
				}
			}
			// Draw evil Buddha.
			ebuddha.drawBuddha(gl);
			// CHECK THE ENERGY BALLS... IF THEY ARE HITTING YOU!
			ebuddha.checkEnergyHittingYou(you);
		}
		gl.glPopMatrix();

		gl.glLoadIdentity();
		hit = false;
		// Go thru the enemy array, and draw them.
		for (int i = enemy.size()-1; i >= 0; i--) {
			hit = false;
			// Check X and Y, because when you multiply them by a negative Z, they will remain negative.
			if (enemy.get(i).Z < -1 && enemy.get(i).Z > -150 && !enemy.get(i).isDead()) {
				gl.glPushMatrix();

				gl.glTranslated(enemy.get(i).X * (enemy.get(i).Z * -1), enemy.get(i).Y * (enemy.get(i).Z * -1), enemy.get(i).Z);
				if (you.isAttacking() && !you.recharging && enemy.get(i).Z > -50 && enemy.get(i).Z < -4) {
					hit = Enemies.check_collisions(you.getTargetX(), you.getTargetY(), enemy.get(i));					
				}
				if (hit) {
					gl.glColor3d(1, 0.9, 0);
					gl.glCallList(this.enemies.shipidC);
					enemy.get(i).deal_damage(you.getStrength());
					if (enemy.get(i).getHP() <= 0) {
						//enemy.remove(i);
						enemy.get(i).Death_Always_Comes_for_you();
					}
				}
				else {
					gl.glCallList(this.enemies.shipid);
				}
				gl.glPopMatrix();
			}
			// Check your HP and if you are hit by an enemy.
			if (enemy.get(i).Z > -7 && enemy.get(i).Z < -1 && !enemy.get(i).isDead()){
				you.deal_damage(enemy.get(i).getStrength());
				if (you.getHP() <= 0) {
					you.Death_Always_Comes_for_you();
				}
			}
			// Check if enemies's positions and move them, if they have gone past you or they were defeated,
			// disappear them
			if (enemy.get(i).Z < 0 && !enemy.get(i).isDead()) {
				enemy.get(i).move();
			}
			else if (enemy.get(i).Z > 0 || enemy.get(i).isDead()) {
				enemy.remove(i);
			}
		}		
	}
	
	// In the present version of JOGL, this method is irrelevant.
	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub	
	}

	public void New_Game() {
		enemies.new_Enemies(nEnemies);
		ebuddha.new_Evil_Buddha(1000, 3, 0, 0, (-nEnemies*10) - 70, 25, 30);
		you.newYou(100, false, 5, 0, 0, 0);
		menu = !menu;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// Get access to OpenGL's functions 
		GL gl = drawable.getGL();
		gl.glEnable(GL_DEPTH_TEST);
		// Set the background's colour
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		nEnemies = 20;
		ebuddha = new Evil_Buddha(1000, 3, 0, 0, (-nEnemies*10) - 80, 25, 30);
		enemies = new Enemies(nEnemies);
		enemies.shipid = gl.glGenLists(3);
		enemies.drawShip(gl);
		enemies.shipidC = gl.glGenLists(3);
		enemies.drawShipD(gl);

		ebuddha.energyBall = gl.glGenLists(3);
		ebuddha.drawEnergy(gl, 50, 50);

		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
		you = new You(100, false, 5, 0, 0, 0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		// Get access to OpenGL's functions
		GL gl = drawable.getGL();
		// Set the Viewport
		// This scenery will cover all the screen area,
		// it is reserved for the graphic context of OpenGL.
		gl.glViewport(0, 0, w, h);
		// Calculate the aspect size according to the Viewport. 
		double aspect = (double) w / h;
		// Set the Matrix mode to Projection.
		gl.glMatrixMode(GL.GL_PROJECTION);
		// Restart the projection matrix.
		gl.glLoadIdentity();
		// set the projection's perspective. 
		glu.gluPerspective(50, aspect, 1, 500); 
		/* this means that for an item to be visible in the screen,
		*  it should have a Z value of at least 1 and at most 500.
		*  Should be noted that Z is negatives, this means that positive
		*  values of Z are behind the camera (observer). */
		 
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("OPENGLEANDO");
		frame.setSize(800, 800);
		Buddha a = new Buddha();
		frame.getContentPane().add(a);
		frame.addKeyListener(a);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}
	
	
	

}
