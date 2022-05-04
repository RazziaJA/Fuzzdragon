import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.Font;
import java.util.Random;

public class Mastermind extends Applet implements MouseMotionListener, MouseListener, KeyListener 
{
	Graphics bg;
	Image offscreen;
	Dimension dim;
	int pressedx,pressedy,releasedx,releasedy,draggedx,draggedy,curx,cury;
	double dragdistance;
	int rowheight, columnwidth;

	int[] code = {0,0,0,0,0};
	int[][] guesses = new int[11][5];
	int[][] feedback = new int[11][5];
	Color black = Color.black;
	Color white = Color.white;
	Color bgcolor = new Color(128,128,128);
	Color[] colors = {bgcolor, Color.red, Color.blue, Color.green, 
		Color.magenta, Color.yellow, Color.cyan};

	Random gen = new Random();

	int guessrow,guesscolumn,curcolor;
  
	public void init() 
	{
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		dim = getSize();
		offscreen = createImage(dim.width,dim.height);
		bg = offscreen.getGraphics();
		rowheight = 40;
		columnwidth = 40;
		resetgame();
	}
  
	public void paint(Graphics g) 
	{
		drawboard();
		drawkey();

		bg.setColor(white);
		bg.drawRect( (guesscolumn-1)*columnwidth+1, (10-guessrow)*rowheight-1, 40,40);

		g.drawImage(offscreen,0,0,this);
	}
	public void update(Graphics g) {paint(g);}
  
	public void mouseMoved(MouseEvent me) 
	{
 		curx = me.getX();
		cury = me.getY();
		repaint();
	}
	public void mouseDragged(MouseEvent me) {}
  
	public void mouseClicked(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
	public void mousePressed(MouseEvent me) 
	{
		pressedx = me.getX();
 		pressedy = me.getY();
	}
	public void mouseReleased(MouseEvent me) 
	{
		releasedx = me.getX();
		releasedy = me.getY();
		draggedx = Math.abs(releasedx - pressedx);
		draggedy = Math.abs(releasedy - pressedy);
		dragdistance = Math.sqrt(draggedx*draggedx + draggedy*draggedy);
    
		if (dragdistance <= 10 && dragdistance != 0) 
		{
      
		}
		repaint();
	}
	public void mouseExited(MouseEvent me) {}

	public void keyPressed(KeyEvent ke) {}

	public void keyReleased(KeyEvent ke) 
	{
		if (ke.getKeyCode() == KeyEvent.VK_LEFT) { leftArrow(); }
		else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) { rightArrow(); }
		else if (ke.getKeyCode() == KeyEvent.VK_1) { numKey(1); }
		else if (ke.getKeyCode() == KeyEvent.VK_2) { numKey(2); }
		else if (ke.getKeyCode() == KeyEvent.VK_3) { numKey(3); }
		else if (ke.getKeyCode() == KeyEvent.VK_4) { numKey(4); }
		else if (ke.getKeyCode() == KeyEvent.VK_5) { numKey(5); }
		else if (ke.getKeyCode() == KeyEvent.VK_6) { numKey(6); }
		else if (ke.getKeyCode() == KeyEvent.VK_ENTER) { enter(); }
		else if (ke.getKeyCode() == KeyEvent.VK_SPACE) { resetgame(); }
		repaint();
	}

	public void keyTyped(KeyEvent ke) {}

	public void drawboard()
	{
		bg.setColor(bgcolor);
		bg.fillRect(0,0,dim.width,dim.height);

		for (int i = 1; i <= 10; ++i)
		{
			for (int j = 1; j <= 4; ++j) 
			{
				bg.setColor(colors[guesses[i][j]]);
				bg.fillArc( (j-1)*columnwidth + 5, (10-i)*rowheight + 5,
					30,30,0,360);
				bg.setColor(black);
				bg.drawArc( (j-1)*columnwidth + 5, (10-i)*rowheight + 5,
					30,30,0,360);
			
				if (feedback[i][j] == 0) {bg.setColor(bgcolor);}
				else if (feedback[i][j] == 1) {bg.setColor(white);}
				else if (feedback[i][j] == 2) {bg.setColor(black);}
				
				int xoffset = 0;
				int yoffset = 0;
				if (j == 1 || j == 3) {xoffset = 0;}
				else {xoffset = 20;}
				if (j == 1 || j == 2) {yoffset = 0;}
				else {yoffset = 20;}
				
				bg.fillArc( 4*columnwidth + xoffset + 2, 
					(10-i)*rowheight + yoffset + 2,
					16,16,0,360);
				bg.setColor(black);
				bg.drawArc( 4*columnwidth + xoffset + 2, 
					(10-i)*rowheight + yoffset + 2,
					16,16,0,360);
			}
		}
	}

	public void drawkey() 
	{
		bg.setFont(new Font("TimesRoman",Font.BOLD,16));

		for (int k = 1; k <= 6; ++k)
		{
			bg.setColor(colors[k]);
			bg.fillArc(215,15+(k-1)*rowheight,30,30,0,360);
			bg.drawString("" + k,250,40+(k-1)*rowheight);
			bg.setColor(black);
			bg.drawArc(215,15+(k-1)*rowheight,30,30,0,360);
		}
		
		bg.setFont(new Font("TimesRoman",Font.BOLD,12));
		bg.setColor(white);
		bg.drawString("Enter:",210,280);
		bg.drawString("Submit Guess",210,295);
		bg.drawString("Space:",210,325);
		bg.drawString("New Game",210,340);
		bg.drawString("L/R Arrows:",210,370);
		bg.drawString("Change Column",210,385);
	}

	public void leftArrow() { if (guesscolumn != 1) {guesscolumn--;} }
	
	public void rightArrow() { if (guesscolumn != 4) {guesscolumn++;} }

	public void numKey (int num) 
	{ 
		curcolor = num; 
		guesses[guessrow][guesscolumn] = curcolor;
	}

	public void enter ()
	{
		for (int k = 1; k <= 4; ++k)
		{
			int gc = guesses[guessrow][k]; //gc = guessedcolor
			if (gc == code[k]) { feedback[guessrow][k] = 2; }
			else if (gc == code[1] || gc == code[2]
				|| gc == code[3] || gc == code[4])
				{ feedback[guessrow][k] = 1; }
			else { feedback[guessrow][k] = 0; }
		}
		
		for (int k = 1; k <= 7; ++k)
		{
			int temp = countcolors(guesses[guessrow], k) - countcolors(code, k);

			int tempindex = 4;
			while (temp > 0)
			{
				if (guesses[guessrow][tempindex] == k)
				{ 
					feedback[guessrow][tempindex] = 0; 
					--temp;
				}
				--tempindex;
			}
		}
		
		
		if (guessrow != 10) { guessrow++; guesscolumn = 1;}

	}

	public int countcolors(int[] combo, int num)
	{
		int count = 0;
		for (int k = 1; k <=4; ++k)
		{
			if (combo[k] == num) { ++count; }
		}
		return count;
	}

	public void resetgame()
	{
		guessrow = guesscolumn = curcolor = 1;

		for (int i = 1; i <= 10; ++i)
		{
			for (int j = 1; j <= 4; ++j) 
			{
				guesses[i][j] = 0;
				feedback[i][j] = 0;
			}
		}

		for (int k = 1; k <= 4; ++k)
		{
			code[k] = gen.nextInt(6)+1;
			System.out.print(" "+code[k]);
		}
		System.out.println(" ");
	}

} //end of class Mastermind
