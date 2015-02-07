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

//FALTA HACER QUE SE CIERRE, AVERIGUAR COMO HACER NEW_GAME! ;)
//AMM Y PUES QUE CHARCHE EL JUEGO< CARGAR A BUDDHA, ETC!


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
//	private int firstListId;
//	private int Target;
//	private Target target;// = new Target();
	private You you;
	private Enemies enemies;
	private Evil_Buddha ebuddha;
	private int nEnemies;
	
	boolean menu = true;//, firing = false;
	double accelx, accely = 0;
	double accelpf = 0.01;
	

	//double targetX = 0, targetY = 0, targetZ = -0;
	
	TextRenderer renderer;
	int RESUME = 0, NEW_GAME = 1, EXIT = 2;
	int menuitem = 1;
	
	public Buddha() {
		// TODO Auto-generated constructor stub
		// Crear una animación de 20 cuadros por segundo
		animator = new FPSAnimator(this, 80);
		animator.start();
		// Los eventos básicos de OpenGL (init, display, reshape)
		// los manejará esta misma clase
		addGLEventListener(this);
		addKeyListener(this);
	}

	public Buddha(GLCapabilities arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub

		// Crear una animación de 20 cuadros por segundo
		animator = new FPSAnimator(this, 80);
		animator.start();
		// Los eventos básicos de OpenGL (init, display, reshape)
		// los manejará esta misma clase
		addGLEventListener(this);
		addKeyListener(this);
	}

	public Buddha(GLCapabilities arg0, GLCapabilitiesChooser arg1, GLContext arg2,
			GraphicsDevice arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub

		// Crear una animación de 20 cuadros por segundo
		animator = new FPSAnimator(this, 80);
		animator.start();
		// Los eventos básicos de OpenGL (init, display, reshape)
		// los manejará esta misma clase
		addGLEventListener(this);
		addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode());

		if (e.getKeyCode() == KeyEvent.VK_SPACE && !menu && !you.isAttacking()) {
			you.setAttacking(true);// = true;
		}
		
		if((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && !menu) { 
			you.target.UP = true; 
			//targetY += 0.05;
			if (you.target.DOWN) { you.target.DOWN = false;  accely = 0; }
			//else 
				//accely += accelpf;
		}
		else if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && !menu) {
			you.target.DOWN = true; //targetY -= 0.05;
			if (you.target.UP) { you.target.UP = false; accely = 0;} 
			//else 
				//accely += accelpf;
		}
		if((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && !menu) {
			you.target.LEFT = true; //targetX -= 0.05;
			if (you.target.RIGHT) { you.target.RIGHT = false;  accelx = 0; }
			//else 
				//accelx += accelpf;
		}
		else if((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && !menu) {
			you.target.RIGHT = true;	//targetX += 0.05;
			if (you.target.LEFT) { you.target.LEFT = false;  accelx = 0; }
			//else 
				//accelx += accelpf;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			menu = !menu;
		}
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
		if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			you.target.DOWN = false;
			accely = 0;
			if (menu)
				if (menuitem < EXIT)
					menuitem++;
				else
					menuitem = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			you.target.LEFT = false;
			accelx = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			you.target.RIGHT = false;
			accelx = 0;
		}
		//System.out.println(e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			//firing = false;
			you.setAttacking(false);
		}
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
		gl.glLoadIdentity();
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		gl.glColor3d(0.1f, 0.1f, 0.1f);
			gl.glVertex3d(-0.5, 0.5, -1);
			gl.glVertex3d(-0.5,-0.5, -1);
			gl.glVertex3d( 0.5, 0.5, -1);
			gl.glVertex3d( 0.5,-0.5, -1);
		gl.glEnd();
		
		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		if (you.isDead()) {
			renderer.setColor(0.6f, 0.9f, 0.2f, 1f);
			renderer.draw3D("I IS DEAD!", (drawable.getWidth()/2) - 100, (drawable.getHeight()/2) + 60, 0, 1);
		}
		else if (ebuddha.isDead()) {
			renderer.setColor(0.6f, 0.9f, 0.2f, 1f);
			renderer.draw3D("I IS ALIVE!", (drawable.getWidth()/2) - 100, (drawable.getHeight()/2) + 60, 0, 1);
		}
		
	    // optionally set the color
		if (menuitem == RESUME)
			renderer.setColor(1.0f, 0.2f, 0.2f, 1f);
		else
			renderer.setColor(0.0f, 0.0f, 1.0f, 1f);
	    //renderer.draw("Text to draw", (drawable.getWidth()/2) - 100, drawable.getHeight()/2);
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
		// Obtener acceso a las funciones de OpenGL
		GL gl = drawable.getGL();
		// Limpiar el búfer de color (detalles más adelante) 
		// con el color de fondo establecido en init() 
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		// Todo lo que se vaya a dibujar, se invoca desde aqui
		
		//Para que no gire lo que no quiero que gire! D:
		gl.glLoadIdentity();
		
		if (you.isDead() || ebuddha.isDead()) {
			menu = true;
		}
		if (menu) {
			this.menu(drawable, gl);
		}
		else {
			gl.glLoadIdentity();
			
			this.DrawEnemies(gl, enemies.enemy);

			gl.glLoadIdentity();
			you.drawBuddha(gl);
			
		}
		if (angle > 360) angle = 0;
		//this.triangleB(gl, 4);
		//gl.glFrontFace(GL.GL_CCW);
///		gl.glEnable(GL.GL_CULL_FACE);
		//this.drawShip(gl, -3);
		
	}

	public void DrawEnemies (GL gl, Vector<Enemy> enemy) {
		//System.out.println(enemy.size());
		boolean hit = false;
		gl.glLoadIdentity();
		
		gl.glPushMatrix();
		
		if (!ebuddha.isDead() && ebuddha.Z < -50.0) {
			ebuddha.move();
		}
		if (ebuddha.Z >= -100) {
//			System.out.println(ebuddha.Z);
//			System.out.println("ebuddha drawn!");
			if (ebuddha.Z >= -50.0 && you.isAttacking() && !you.recharging) {
				ebuddha.checkEnergyCollissions(you.getTargetX(), you.getTargetY(), you.getStrength());
					hit = ebuddha.check_collissions(you.getTargetX(), you.getTargetY());
//					System.out.println(hit);
				if (hit) {
					ebuddha.isHit();
					gl.glColor3d(1, 0.9, 0);
					//gl.glCallList(this.enemies.shipidC);
					ebuddha.deal_damage(you.getStrength());
					if (ebuddha.getHP() <= 0) {
						//enemy.remove(i);
						ebuddha.Death_Always_Comes_for_you();
					}
				}
				else {
					ebuddha.notHit();
				}
			}
			ebuddha.drawBuddha(gl);
			//CHECK THE ENERGY BALLS... IF THEY ARE HITTING YOU! -.-
			ebuddha.checkEnergyHittingYou(you);
		}
		gl.glPopMatrix();

		gl.glLoadIdentity();
		hit = false;
//		for (int i = 0; i < enemy.size(); i++) {
//		System.out.println(enemy.size());
		for (int i = enemy.size()-1; i >= 0; i--) {
			hit = false;
			//System.out.println("Z - " + enemy.get(i).Z);
			//CHECAR LAX X Y Y, por que al multiplicar por una z negativa se volver�n siempre negativas! D: horror!
			if (enemy.get(i).Z < -1 && enemy.get(i).Z > -150 && !enemy.get(i).isDead()) {
				gl.glPushMatrix();

				gl.glTranslated(enemy.get(i).X * (enemy.get(i).Z * -1), enemy.get(i).Y * (enemy.get(i).Z * -1), enemy.get(i).Z);
				if (you.isAttacking() && !you.recharging && enemy.get(i).Z > -50 && enemy.get(i).Z < -4) {
					hit = Enemies.check_collissions(you.getTargetX(), you.getTargetY(), enemy.get(i));					
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
			if (enemy.get(i).Z > -7 && enemy.get(i).Z < -1 && !enemy.get(i).isDead()){
				you.deal_damage(enemy.get(i).getStrength());
				if (you.getHP() <= 0) {
					you.Death_Always_Comes_for_you();
				}
			}
			if (enemy.get(i).Z < 0 && !enemy.get(i).isDead()) {
				enemy.get(i).move();
			}
			else if (enemy.get(i).Z > 0 || enemy.get(i).isDead()) {
				enemy.remove(i);
			}
		}
		
//		for (int i = enemy.size()-1; i >= 0; i--) {
//			if (enemy.get(i).Z > 0 || enemy.get(i).isDead()) {
//				enemy.remove(i);
//			}
//		}
		
	}
	
	// En la versión actual JOGL, este método es irrelevante
	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub	
	}

	public void New_Game() {
		enemies.new_Enemies(nEnemies);
		ebuddha.new_Evil_Buddha(1000, 3, 0, 0, (-nEnemies*10) - 70, 25, 30);
		you.newYou(100, false, 5, 0, 0, 0);
		//you.target.resetTarget();
		menu = !menu;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		// Obtener acceso a las funciones de OpenGL 
		GL gl = drawable.getGL();
		gl.glEnable(GL_DEPTH_TEST);
		// Establecer el color de fondo 
		//gl.glClearColor(0.9f, 0.8f, 0.6f, 1.0f);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		//firstListId = gl.glGenLists(2);	//El 1 es el número de listas!
		nEnemies = 20;
		ebuddha = new Evil_Buddha(1000, 3, 0, 0, (-nEnemies*10) - 80, 25, 30);
		enemies = new Enemies(nEnemies);
		enemies.shipid = gl.glGenLists(3);
		enemies.drawShip(gl);
		enemies.shipidC = gl.glGenLists(3);
		enemies.drawShipD(gl);

		ebuddha.energyBall = gl.glGenLists(3);
		ebuddha.drawEnergy(gl, 50, 50);

		//Target = gl.glGenLists(2);
///		this.Target(gl);
		//his.triangleStrip(gl, 10);
		//this.sphere(gl, 30, 30);
		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
		you = new You(100, false, 5, 0, 0, 0);
		//this.New_Game();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		// TODO Auto-generated method stub
		// Obtener acceso a las funciones de OpenGL
		GL gl = drawable.getGL();
		// Establecer el puerto de despliegue
		// El escenario generado cubrirá todo el área de la ventana 
		// reservado para el contexto gráfico de OpenGL 
		gl.glViewport(0, 0, w, h);
		// Calcular la relación de aspecto del contexto gráfico 
		double aspect = (double) w / h;
		// Se modificará la matriz de Proyección 
		gl.glMatrixMode(GL.GL_PROJECTION);
		// Reiniciar la matriz de Proyección
		gl.glLoadIdentity();
		// Establecer proyección en perspectiva 
		glu.gluPerspective(50, aspect, 1, 500); 
		//Debe de tener mínimo para ser visible 1 en Z, y máximo 500
		//Ojo, la z está en negativos hacia el fondo, los positivos están atrás del observador!
		

		// Se modificará la matriz de VIEW 
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("OPENGLEANDO");
		frame.setSize(800, 800);
		Buddha a = new Buddha();
		frame.getContentPane().add(a);
		frame.addKeyListener(a);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}
	
	
	

}
