package game_design;

import javax.swing.*;
//import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;;

class SnakeUnit{
	private int posx;
	private int posy;
	private int xd;
	private int yd;
	private int side=10;
	public SnakeUnit(int x,int y,int xdir,int ydir) {
		posx=x;
		posy=y;
		xd=xdir;
		yd=ydir;
	}
	
	public void draw(Graphics2D g,Color col) {
		g.setColor(col);
		g.fillRect(posx, posy, side, side);
	}
	
	public void setxdr(int d) {
		xd=d;
	}
	
	public void setydr(int d) {
		yd=d;
	}
	
	public int getxdr() {
		return xd;
	}
	
	public int getydr() {
		return yd;
	}
	
	public int getx() {
		return posx;
	}
	public int gety() {
		return posy;
	}
	public void setx(int x) {
		posx=x;
	}
	public void sety(int y) {
		posy=y;
	}
	
	public boolean equals(Object o) {
		if(o==this) return true;
		if(!(o instanceof SnakeUnit)) return false;
		SnakeUnit temp=(SnakeUnit)o;
		if(posx==temp.getx()&&posy==temp.gety()) return true;
		return false;
		
	}
}

class GamePlay extends JPanel implements KeyListener,ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean play=false;
	private int length=1;
	private int delay=100;
	private Timer timer;
	private int hPosX=180;
	private int hPosY=200;
	private int fposx;
	private int fposy;
	private int xdir=10;
	private int ydir=0;
	private boolean changed=false;
	private List<SnakeUnit> snake;
	
	
	public GamePlay() {
		addKeyListener(this);
		setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		snake=new ArrayList<SnakeUnit>();
		hPosX=(int)(Math.random()*60000)/100;
		hPosY=(int)(Math.random()*60000)/100;
		fposx=(int)(Math.random()*400)+100;
		fposy=(int)(Math.random()*400)+100;
		snake.add(new SnakeUnit(hPosX,hPosY,xdir,ydir));
		timer=new Timer(delay,this);
		timer.start();
		//xdir=1;
		//ydir=0;
	}
	@Override
	public void paint(Graphics g) {
		//panel
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		
		//snake
		for(int i=0;i<snake.size();i++) {
			snake.get(i).draw((Graphics2D)g,Color.WHITE);
		}
		
		//food
		g.setColor(Color.YELLOW);
		g.fillOval(fposx, fposy, 10, 10);
		
		//If game over
		if(snake.get(0).getx()<0||snake.get(0).getx()>590||snake.get(0).gety()<0||snake.get(0).gety()>580||play==false) {
			play=false;
			
			for(int i=0;i<snake.size();i++) {
				snake.get(i).draw((Graphics2D)g,Color.GRAY);
			}
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.BOLD,40));
			g.drawString("GAME OVER ☻♥♦♣♠•", 50, 250);
			g.setColor(Color.MAGENTA);
			g.setFont(new Font("Bookman",Font.BOLD,35));
			g.drawString("SCORE : "+length,200, 300);
			length=0;
					}
		
		
		g.dispose();
	}
	
	//Action event handling
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();  //starting the timer after each delay
	
		//if player presses a key
		
		if(play) {
			
			//head of the snake moves ahead
			snake.get(0).setx(snake.get(0).getx()+xdir);
			snake.get(0).sety(snake.get(0).gety()+ydir);
			
			//each unit of the snake follows the one in front of it
			for(int i=snake.size()-1;i>0;i--) {
				snake.get(i).setxdr(snake.get(i-1).getxdr());
				snake.get(i).setydr(snake.get(i-1).getydr());
				snake.get(i).setx(snake.get(i).getx()+snake.get(i-1).getxdr());
				snake.get(i).sety(snake.get(i).gety()+snake.get(i-1).getydr());
			}
			
			//if direction is changed
			if(changed) {
				snake.get(0).setxdr(xdir);
				snake.get(0).setydr(ydir);
			}//changing the direction of the head
			
			
		
			for(int i=1;i<snake.size();i++) {
				int x0=snake.get(0).getx();					//if the snake head intersects itself
				int y0=snake.get(0).gety();
				int xt=snake.get(i).getx();
				int yt=snake.get(i).gety();
				Rectangle r0=new Rectangle(x0,y0,10,10);
				Rectangle rt=new Rectangle(xt,yt,10,10);
				if(r0.intersects(rt)) play=false;
			}
			
			
			//if the snake head gets a food item
			Rectangle foodrect=new Rectangle(fposx,fposy,10,10);
			Rectangle snakehead=new Rectangle(snake.get(0).getx(),snake.get(0).gety(),10,10);
			if(foodrect.intersects(snakehead)) {
				fposx=(int)(Math.random()*300)+100;
				fposy=(int)(Math.random()*400)+100;
				int X=snake.get(0).getx();
				int Y=snake.get(0).gety();
				int xd=snake.get(0).getxdr();
				int yd=snake.get(0).getydr();
				snake.add(0,new SnakeUnit(X+xdir,Y+ydir,xd,yd));
				length++;
			}
			
			//the direction remains the same
			changed=false;
			
		}
		
		//repaint the updated values
		revalidate();
		repaint();
	}
	
	//Key Pressing event handing
	
	@Override
	public void keyPressed(KeyEvent e) {
		//RIGHT
		
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			play=true;
			if(xdir==0) {//if no x movement before=>changed dir
				changed=true;
				xdir=10;
				ydir=0;
			}
			else if(xdir==-10) {
				xdir=-10;
				ydir=0;
			}
			
		}
		
		//LEFT
		
		
		else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			play=true;
			if(xdir==0) {//if no x movement before=>changed dir
				changed=true;
				xdir=-10;
				ydir=0;
			}
			else if(xdir==10) {
				xdir=10;
				ydir=0;
			}
			
		}
		
		//DOWN
		
		
		else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			play=true;
			if(ydir==0) {//if no y movement before=>changed dir
				changed=true;
				ydir=10;
				xdir=0;
			}
			else if(ydir==-10) {
				ydir=-10;
				xdir=0;
			}
			
		}
		
		
		//UP
		
		else if(e.getKeyCode()==KeyEvent.VK_UP) {
			play=true;
			if(ydir==0) {//if no y movement before=>changed dir
				changed=true;
				ydir=-10;
				xdir=0;
			}
			else if(ydir==10) {
				ydir=10;
				xdir=0;
			}
		}
		
		
		//NEWGAME
		
		
		else if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			if(!play) {
				play=true;
				
				snake.clear();
				hPosX=(int)(Math.random()*60000)/100;
				hPosY=(int)(Math.random()*60000)/100;
				fposx=(int)(Math.random()*400)+100;
				fposy=(int)(Math.random()*400)+100;
				snake.add(new SnakeUnit(hPosX,hPosY,xdir,ydir));
			}
		}
	}
	
	
	//unused methods
	
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	
}

public class SnakeMain {

	public static void main(String[] args) {
		JFrame obj=new JFrame();
		GamePlay gameobj=new GamePlay();
		obj.setBounds(10, 10, 600, 600);
		
		obj.setResizable(false);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.setVisible(true);
		obj.add(gameobj);
	}

}
