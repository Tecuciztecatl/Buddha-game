package Being;

import java.util.Vector;

import javax.media.opengl.GL;

public class Enemies {

	public Vector<Enemy> enemy;
	public int shipid;
	public int shipidC;
//	public int nDeads_Scapes;
//	public int nEnemies;

	public Enemies (int nEnemies) {
		//this.nEnemies = nEnemies;
		//this.nDeads_Scapes = 0;
		enemy = new Vector<Enemy>();
		for ( int i = 0; i < nEnemies; i++) {
			enemy.add(new Enemy(50, 5, Math.random() - 0.5, Math.random() - 0.5, -((Math.random()+0.01) * (nEnemies * 10))-50));
		}
/*		
		for ( int i = 0; i < 5; i++) {
			enemy.add(new Enemy(50, 5, Math.random() - 0.5, Math.random() - 0.5, -((Math.random()+0.01) * 50)-50));
		}
*/
		//enemy.add(new Enemy(50, 5, 0.1, 0.3, -(50.0)));
	}
	

	public void new_Enemies (int nEnemies) {
		//this.nEnemies = nEnemies;
		//this.nDeads_Scapes = 0;
		enemy = new Vector<Enemy>();
		for ( int i = 0; i < nEnemies; i++) {
			enemy.add(new Enemy(50, 5, Math.random() - 0.5, Math.random() - 0.5, -((Math.random()+0.01) * (nEnemies * 10))-50));
		}
		//enemy.add(new Enemy(50, 5, 0.1, 0.3, -(50.0)));
	}
	
	public void DrawEnemies (GL gl) {
		System.out.println(enemy.size());
		for (int i = 0; i < enemy.size(); i++) {
			System.out.println("Z - " + enemy.get(i).Z);
			//CHECAR LAX X Y Y, por que al multiplicar por una z negativa se volver‡n siempre negativas! D: horror!
			if (enemy.get(i).Z < -1 && enemy.get(i).Z > -100) {
				//System.out.println(enemy.get(i).X);
				//System.out.println(enemy.get(i).Y);
				//System.out.println(enemy.get(i).Z);
				gl.glPushMatrix();
				//gl.glLoadIdentity();
				//gl.glTranslated(enemy.get(i).X * (enemy.get(i).Z * -1), enemy.get(i).Y * (enemy.get(i).Z * -1), enemy.get(i).Z);
				gl.glTranslated(enemy.get(i).X , enemy.get(i).Y, enemy.get(i).Z);
				drawShip(gl);
				gl.glPopMatrix();
				//gl.glCallList(shipid);
			}
			if (enemy.get(i).Z < 0) {
				//enemy.get(i).move();
			}
		}
	}
	
	//CHECAR ESTA MADRE DE LAS COLISIONES! grrrr D:
	public static boolean check_collissions (double X, double Y, Enemy enemy) {
//		System.out.println("enemy x, y, z : "+enemy.X + ","+enemy.Y + ","+enemy.Z);
//		System.out.println("enemy x, y, by z : "+enemy.X*(enemy.Z*-1) + ","+enemy.Y*(enemy.Z*-1) + ","+enemy.Z);
//		System.out.println("target x, y : "+X + ","+Y);
//		System.out.println("target x, y by Z: "+X*(enemy.Z*-1) + ","+Y*(enemy.Z*-1));
//		if (X*(enemy.Z *-1)  < (enemy.X*(enemy.Z *-1)) + 3.5 && X*(enemy.Z *-1) > (enemy.X*(enemy.Z *-1)) - 3.5) {
//			if (Y*(enemy.Z*-1) < 0.7 + (enemy.Y*(enemy.Z *-1)) && Y*(enemy.Z-1) > (enemy.Y*(enemy.Z *-1)) - 0.2) {
		//Positive part, then the negative part! that is why I ain't multiplying by -1
		//Now I will need to add an offset to compensate for some parts!, 
		//the closer it is to an edge the bigger the offset because it is dependant
		//to the edges distance
		if (X  < (enemy.X - (4/(enemy.Z))) && X > (enemy.X + (4/(enemy.Z)))) {
			if (enemy.Y < 0.1) {
				if (Y < (enemy.Y - (1.2/((enemy.Z+1))) - (enemy.Y/enemy.Z)) && Y > (enemy.Y + (0.2/(enemy.Z)))) {
//					System.out.println("true;");
					return true;
				}
			}
			else {
				if (Y < (enemy.Y - (0.2/(enemy.Z))) && Y > (enemy.Y + (0.7/(enemy.Z)) + (enemy.Y/enemy.Z))) {
//					System.out.println("true;");
					return true;
				}
			}
		}

//		System.out.println("false;");
		return false;
	}
	

	
	public void drawShip(GL gl) {

		//gl.glLoadIdentity();
		//gl.glTranslated(0, 0, pos);


		gl.glNewList(shipid, GL.GL_COMPILE);
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		//Tapa de abajo!

			gl.glColor3d(0, 0, 1);
			gl.glVertex3d( 0.0, 0.0,-2.3);
			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.3,-0.2,-1.5);
			gl.glVertex3d(-0.3,-0.2,-1.5);
			gl.glColor3d(1, 1, 1);
			gl.glVertex3d( 0.3,-0.2, 0);
			gl.glVertex3d(-0.3,-0.2, 0);
		
		gl.glEnd();

		gl.glBegin(GL.GL_QUAD_STRIP);
			//El frente!
			gl.glColor3d(1, 1, 1);
			gl.glVertex3d( 0.3,-0.2, 0);
			gl.glVertex3d( 0.3, 0.2, 0);
			gl.glVertex3d(-0.3,-0.2, 0);
			gl.glVertex3d(-0.3, 0.2, 0);
			
			//Vamos hacia la cabina!
			gl.glColor3d(1, 0, 0);
			gl.glVertex3d(-0.3,-0.2,-0.9);
			gl.glVertex3d(-0.3, 0.3,-0.9);

			gl.glVertex3d(-0.3,-0.2,-1.5);
			gl.glVertex3d(-0.3, 0.3,-2.0);

			//la cola!
			gl.glColor3d(0, 0, 1);
			gl.glVertex3d( 0.0, 0.0,-2.3);
			gl.glVertex3d( 0.0, 0.7,-4.0);

			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.3,-0.2,-1.5);
			gl.glVertex3d( 0.3, 0.3,-2.0);

			//Hacia la cabina!
			gl.glVertex3d( 0.3,-0.2,-0.9);
			gl.glVertex3d( 0.3, 0.3,-0.9);
			

			gl.glColor3d(1, 1, 1);
			gl.glVertex3d( 0.3,-0.2, 0);
			gl.glVertex3d( 0.3, 0.2, 0);
		gl.glEnd();

		//HACIA LA CABINA!
		gl.glBegin(GL.GL_QUAD_STRIP);
			//El frente!
			gl.glColor3d(1, 1, 1);
			gl.glVertex3d(-0.3, 0.2, 0);
			gl.glVertex3d( 0.3, 0.2, 0);
			gl.glColor3d(1, 0, 0);
			gl.glVertex3d(-0.3, 0.3,-0.9);
			gl.glVertex3d( 0.3, 0.3,-0.9);
		
		gl.glEnd();

		//LA CABINA!
		gl.glBegin(GL.GL_TRIANGLE_FAN);
			//El frente!
			gl.glColor3d(0, 0, 0);
			gl.glVertex3d( 0.0, 0.6,-1.7);
			gl.glColor3d(0.0, 0.0, 0.7);
			gl.glVertex3d( 0.3, 0.3,-0.9);
			gl.glVertex3d( 0.3, 0.3,-2.0);
			gl.glVertex3d( 0.0, 0.7,-2.5);
			gl.glVertex3d(-0.3, 0.3,-2.0);
			gl.glVertex3d(-0.3, 0.3,-0.9);
			gl.glVertex3d( 0.3, 0.3,-0.9);
		
		gl.glEnd();
		
		//tapa de arriba de la cola
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.3, 0.3,-2.0);
			gl.glColor3d(0, 0, 1);
			gl.glVertex3d( 0.0, 0.7,-4.0);
			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.0, 0.7,-2.5);
			gl.glVertex3d(-0.3, 0.3,-2.0);
		
		gl.glEnd();

		//Ala derecha!
		gl.glBegin(GL.GL_QUAD_STRIP);

			gl.glVertex3d( 0.3, 0.1,-0.4);
			gl.glVertex3d( 0.3, 0.0,-0.4);
			
			gl.glVertex3d( 1.3, 0.1,-0.6);
			gl.glVertex3d( 1.3, 0.0,-0.6);
			
			gl.glVertex3d( 2.8, 0.1,-1.6);
			gl.glVertex3d( 2.8, 0.0,-1.6);
			
			//Punta Final
			gl.glVertex3d( 3.5, 0.1,-3.1);
			gl.glVertex3d( 3.5, 0.0,-3.1);

			gl.glVertex3d( 2.4, 0.1,-2.0);
			gl.glVertex3d( 2.4, 0.0,-2.0);
			
			gl.glVertex3d( 1.4, 0.1,-1.6);
			gl.glVertex3d( 1.4, 0.0,-1.6);
			
			gl.glVertex3d( 0.3, 0.1,-1.6);
			gl.glVertex3d( 0.3, 0.0,-1.6);
			
		gl.glEnd();

		//TAPAS ALA DERECHA
		gl.glBegin(GL.GL_QUAD_STRIP);

			gl.glVertex3d( 0.3, 0.1,-1.6);
			gl.glVertex3d( 0.3, 0.1,-0.4);
			

			gl.glVertex3d( 1.4, 0.1,-1.6);
			gl.glVertex3d( 1.3, 0.1,-0.6);

			gl.glVertex3d( 2.4, 0.1,-2.0);
			gl.glVertex3d( 2.8, 0.1,-1.6);

			gl.glVertex3d( 3.5, 0.1,-3.1);
			gl.glVertex3d( 3.5, 0.1,-3.1);
			

			gl.glEnd();
			gl.glBegin(GL.GL_QUAD_STRIP);
			
			gl.glVertex3d( 0.3, 0.0,-0.4);
			gl.glVertex3d( 0.3, 0.0,-1.6);
			
			gl.glVertex3d( 1.3, 0.0,-0.6);
			gl.glVertex3d( 1.4, 0.0,-1.6);
			
			gl.glVertex3d( 2.8, 0.0,-1.6);
			gl.glVertex3d( 2.4, 0.0,-2.0);
			
			//Punta Final
			gl.glVertex3d( 3.5, 0.0,-3.1);
			gl.glVertex3d( 3.5, 0.0,-3.1);
			
		gl.glEnd();
		
		//Ala izquierda
		gl.glBegin(GL.GL_QUAD_STRIP);
		
			gl.glVertex3d(-0.3, 0.1,-1.6);
			gl.glVertex3d(-0.3, 0.0,-1.6);
			
			gl.glVertex3d(-1.4, 0.1,-1.6);
			gl.glVertex3d(-1.4, 0.0,-1.6);

			gl.glVertex3d(-2.4, 0.1,-2.0);
			gl.glVertex3d(-2.4, 0.0,-2.0);
			
			//Punta Final
			gl.glVertex3d(-3.5, 0.1,-3.1);
			gl.glVertex3d(-3.5, 0.0,-3.1);
			
			gl.glVertex3d(-2.8, 0.1,-1.6);
			gl.glVertex3d(-2.8, 0.0,-1.6);
			
			gl.glVertex3d(-1.3, 0.1,-0.6);
			gl.glVertex3d(-1.3, 0.0,-0.6);
			
			gl.glVertex3d(-0.3, 0.1,-0.4);
			gl.glVertex3d(-0.3, 0.0,-0.4);
			
		gl.glEnd();

		//TAPAS ALA IZQUIERDA
		gl.glBegin(GL.GL_QUAD_STRIP);

		gl.glVertex3d(-0.3, 0.1,-0.4);
			gl.glVertex3d(-0.3, 0.1,-1.6);
			

			gl.glVertex3d(-1.3, 0.1,-0.6);
			gl.glVertex3d(-1.4, 0.1,-1.6);

			gl.glVertex3d(-2.8, 0.1,-1.6);
			gl.glVertex3d(-2.4, 0.1,-2.0);

			gl.glVertex3d(-3.5, 0.1,-3.1);
			gl.glVertex3d(-3.5, 0.1,-3.1);
			

			gl.glEnd();
			gl.glBegin(GL.GL_QUAD_STRIP);

			gl.glVertex3d(-0.3, 0.0,-1.6);
			gl.glVertex3d(-0.3, 0.0,-0.4);

			gl.glVertex3d(-1.4, 0.0,-1.6);
			gl.glVertex3d(-1.3, 0.0,-0.6);

			gl.glVertex3d(-2.4, 0.0,-2.0);
			gl.glVertex3d(-2.8, 0.0,-1.6);
			
			//Punta Final
			gl.glVertex3d(-3.5, 0.0,-3.1);
			gl.glVertex3d(-3.5, 0.0,-3.1);
			
		gl.glEnd();

		gl.glEndList();
		
	}
	

	public void drawShipD(GL gl) {

		//gl.glLoadIdentity();
		//gl.glTranslated(0, 0, pos);


		gl.glNewList(shipidC, GL.GL_COMPILE);
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		//Tapa de abajo!

//			gl.glColor3d(0, 0, 1);
			gl.glVertex3d( 0.0, 0.0,-2.3);
//			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.3,-0.2,-1.5);
			gl.glVertex3d(-0.3,-0.2,-1.5);
//			gl.glColor3d(1, 1, 1);
			gl.glVertex3d( 0.3,-0.2, 0);
			gl.glVertex3d(-0.3,-0.2, 0);
		
		gl.glEnd();

		gl.glBegin(GL.GL_QUAD_STRIP);
			//El frente!
//			gl.glColor3d(1, 1, 1);
			gl.glVertex3d( 0.3,-0.2, 0);
			gl.glVertex3d( 0.3, 0.2, 0);
			gl.glVertex3d(-0.3,-0.2, 0);
			gl.glVertex3d(-0.3, 0.2, 0);
			
			//Vamos hacia la cabina!
//			gl.glColor3d(1, 0, 0);
			gl.glVertex3d(-0.3,-0.2,-0.9);
			gl.glVertex3d(-0.3, 0.3,-0.9);

			gl.glVertex3d(-0.3,-0.2,-1.5);
			gl.glVertex3d(-0.3, 0.3,-2.0);

			//la cola!
//			gl.glColor3d(0, 0, 1);
			gl.glVertex3d( 0.0, 0.0,-2.3);
			gl.glVertex3d( 0.0, 0.7,-4.0);

//			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.3,-0.2,-1.5);
			gl.glVertex3d( 0.3, 0.3,-2.0);

			//Hacia la cabina!
			gl.glVertex3d( 0.3,-0.2,-0.9);
			gl.glVertex3d( 0.3, 0.3,-0.9);
			

//			gl.glColor3d(1, 1, 1);
			gl.glVertex3d( 0.3,-0.2, 0);
			gl.glVertex3d( 0.3, 0.2, 0);
		gl.glEnd();

		//HACIA LA CABINA!
		gl.glBegin(GL.GL_QUAD_STRIP);
			//El frente!
//			gl.glColor3d(1, 1, 1);
			gl.glVertex3d(-0.3, 0.2, 0);
			gl.glVertex3d( 0.3, 0.2, 0);
//			gl.glColor3d(1, 0, 0);
			gl.glVertex3d(-0.3, 0.3,-0.9);
			gl.glVertex3d( 0.3, 0.3,-0.9);
		
		gl.glEnd();

		//LA CABINA!
		gl.glBegin(GL.GL_TRIANGLE_FAN);
			//El frente!
//			gl.glColor3d(0, 0, 0);
			gl.glVertex3d( 0.0, 0.6,-1.7);
//			gl.glColor3d(0.0, 0.0, 0.7);
			gl.glVertex3d( 0.3, 0.3,-0.9);
			gl.glVertex3d( 0.3, 0.3,-2.0);
			gl.glVertex3d( 0.0, 0.7,-2.5);
			gl.glVertex3d(-0.3, 0.3,-2.0);
			gl.glVertex3d(-0.3, 0.3,-0.9);
			gl.glVertex3d( 0.3, 0.3,-0.9);
		
		gl.glEnd();
		
		//tapa de arriba de la cola
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
//			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.3, 0.3,-2.0);
//			gl.glColor3d(0, 0, 1);
			gl.glVertex3d( 0.0, 0.7,-4.0);
//			gl.glColor3d(1, 0, 0);
			gl.glVertex3d( 0.0, 0.7,-2.5);
			gl.glVertex3d(-0.3, 0.3,-2.0);
		
		gl.glEnd();

		//Ala derecha!
		gl.glBegin(GL.GL_QUAD_STRIP);

			gl.glVertex3d( 0.3, 0.1,-0.4);
			gl.glVertex3d( 0.3, 0.0,-0.4);
			
			gl.glVertex3d( 1.3, 0.1,-0.6);
			gl.glVertex3d( 1.3, 0.0,-0.6);
			
			gl.glVertex3d( 2.8, 0.1,-1.6);
			gl.glVertex3d( 2.8, 0.0,-1.6);
			
			//Punta Final
			gl.glVertex3d( 3.5, 0.1,-3.1);
			gl.glVertex3d( 3.5, 0.0,-3.1);

			gl.glVertex3d( 2.4, 0.1,-2.0);
			gl.glVertex3d( 2.4, 0.0,-2.0);
			
			gl.glVertex3d( 1.4, 0.1,-1.6);
			gl.glVertex3d( 1.4, 0.0,-1.6);
			
			gl.glVertex3d( 0.3, 0.1,-1.6);
			gl.glVertex3d( 0.3, 0.0,-1.6);
			
		gl.glEnd();

		//TAPAS ALA DERECHA
		gl.glBegin(GL.GL_QUAD_STRIP);

			gl.glVertex3d( 0.3, 0.1,-1.6);
			gl.glVertex3d( 0.3, 0.1,-0.4);
			

			gl.glVertex3d( 1.4, 0.1,-1.6);
			gl.glVertex3d( 1.3, 0.1,-0.6);

			gl.glVertex3d( 2.4, 0.1,-2.0);
			gl.glVertex3d( 2.8, 0.1,-1.6);

			gl.glVertex3d( 3.5, 0.1,-3.1);
			gl.glVertex3d( 3.5, 0.1,-3.1);
			

			gl.glEnd();
			gl.glBegin(GL.GL_QUAD_STRIP);
			
			gl.glVertex3d( 0.3, 0.0,-0.4);
			gl.glVertex3d( 0.3, 0.0,-1.6);
			
			gl.glVertex3d( 1.3, 0.0,-0.6);
			gl.glVertex3d( 1.4, 0.0,-1.6);
			
			gl.glVertex3d( 2.8, 0.0,-1.6);
			gl.glVertex3d( 2.4, 0.0,-2.0);
			
			//Punta Final
			gl.glVertex3d( 3.5, 0.0,-3.1);
			gl.glVertex3d( 3.5, 0.0,-3.1);
			
		gl.glEnd();
		
		//Ala izquierda
		gl.glBegin(GL.GL_QUAD_STRIP);
		
			gl.glVertex3d(-0.3, 0.1,-1.6);
			gl.glVertex3d(-0.3, 0.0,-1.6);
			
			gl.glVertex3d(-1.4, 0.1,-1.6);
			gl.glVertex3d(-1.4, 0.0,-1.6);

			gl.glVertex3d(-2.4, 0.1,-2.0);
			gl.glVertex3d(-2.4, 0.0,-2.0);
			
			//Punta Final
			gl.glVertex3d(-3.5, 0.1,-3.1);
			gl.glVertex3d(-3.5, 0.0,-3.1);
			
			gl.glVertex3d(-2.8, 0.1,-1.6);
			gl.glVertex3d(-2.8, 0.0,-1.6);
			
			gl.glVertex3d(-1.3, 0.1,-0.6);
			gl.glVertex3d(-1.3, 0.0,-0.6);
			
			gl.glVertex3d(-0.3, 0.1,-0.4);
			gl.glVertex3d(-0.3, 0.0,-0.4);
			
		gl.glEnd();

		//TAPAS ALA IZQUIERDA
		gl.glBegin(GL.GL_QUAD_STRIP);

		gl.glVertex3d(-0.3, 0.1,-0.4);
			gl.glVertex3d(-0.3, 0.1,-1.6);
			

			gl.glVertex3d(-1.3, 0.1,-0.6);
			gl.glVertex3d(-1.4, 0.1,-1.6);

			gl.glVertex3d(-2.8, 0.1,-1.6);
			gl.glVertex3d(-2.4, 0.1,-2.0);

			gl.glVertex3d(-3.5, 0.1,-3.1);
			gl.glVertex3d(-3.5, 0.1,-3.1);
			

			gl.glEnd();
			gl.glBegin(GL.GL_QUAD_STRIP);

			gl.glVertex3d(-0.3, 0.0,-1.6);
			gl.glVertex3d(-0.3, 0.0,-0.4);

			gl.glVertex3d(-1.4, 0.0,-1.6);
			gl.glVertex3d(-1.3, 0.0,-0.6);

			gl.glVertex3d(-2.4, 0.0,-2.0);
			gl.glVertex3d(-2.8, 0.0,-1.6);
			
			//Punta Final
			gl.glVertex3d(-3.5, 0.0,-3.1);
			gl.glVertex3d(-3.5, 0.0,-3.1);
			
		gl.glEnd();

		gl.glEndList();
		
	}
	
}
