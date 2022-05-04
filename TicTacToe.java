import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TicTacToe extends Applet implements MouseMotionListener, MouseListener {
  Graphics buffergraphics;
  Image offscreen;
  Dimension dim;
  int curx,cury,pressedx,pressedy,releasedx,releasedy,winner,xwins,owins,ties;
  double dragx,dragy,dragdistance;
  Boolean pxmove,pomove,mouseclicked,resultcounted;
  int[] gridxcoords = {0,60,125,190,60,125,190,60,125,190};
  int[] gridycoords = {0,60,60,60,125,125,125,190,190,190};
  int[] board = {0,0,0,0,0,0,0,0,0,0};
  Font arial = new Font("Arial", Font.BOLD, 24);
  Font arialsmaller = new Font("Arial", Font.BOLD, 14);
  Rectangle newgamebutton;
  
  public void init() {
    addMouseMotionListener(this);
    addMouseListener(this);
    dim = getSize();
    offscreen = createImage(dim.width,dim.height);
    buffergraphics = offscreen.getGraphics();
    pxmove = true;
    pomove = false;
    buffergraphics.setFont(arial);
    xwins = 0;
    owins = 0;
    ties = 0;
    newgamebutton = new Rectangle(155,20,80,20);
    resultcounted = false;
  }
  
  public void paint(Graphics g) {
    buffergraphics.setColor(Color.white);
    buffergraphics.fillRect(0,0,250,60);
    buffergraphics.fillRect(0,0,60,250);
    buffergraphics.setColor(Color.black);
    drawboard();
    buffergraphics.setFont(arial);
    if (pxmove) {buffergraphics.drawString("X's Move",30,35);}
    if (pomove) {buffergraphics.drawString("O's Move",30,35);}
    if (winner == 1) {buffergraphics.drawString("X Wins!",30,35);}
    if (winner == 2) {buffergraphics.drawString("O Wins!",30,35);}
    if (winner == 3) {buffergraphics.drawString("Tie :(",30,35);}
    
    buffergraphics.setFont(arialsmaller);
    buffergraphics.drawString("Wins:",5,120);
    buffergraphics.drawString("X: "+xwins,5,140);
    buffergraphics.drawString("O: "+owins,5,160);
    buffergraphics.drawString("Ties: "+ties,5,180);
    buffergraphics.drawString("New Game",160,35);
    buffergraphics.drawRect(155,20,80,20);
    if (newgamebutton.contains(curx,cury)) {buffergraphics.drawRect(154,19,82,22);}
    g.drawImage(offscreen,0,0,this);
  }
  public void update(Graphics g) {paint(g);}
  
  public void drawboard() {
    buffergraphics.fillRect(120,60,5,190);
    buffergraphics.fillRect(185,60,5,190);
    buffergraphics.fillRect(60,120,190,5);
    buffergraphics.fillRect(60,185,190,5);
  }
  
  public void drawX(int x, int y) {
    x = x+5;
    y = y+5;
    int[] xCoords = {x+0,x+3,x+25,x+47,x+50,x+50,x+28,x+50,x+50,x+47,x+25,x+3,x+0,x+0,x+22,x+0,x+0};
    int[] yCoords = {y+0,y+0,y+22,y+0,y+0,y+3,y+25,y+47,y+50,y+50,y+28,y+50,y+50,y+47,y+25,y+3,y+0};
    Polygon symbolX = new Polygon(xCoords,yCoords,16);
    buffergraphics.fillPolygon(symbolX);
  }
  
  public void drawO(int x, int y) {
    buffergraphics.setColor(Color.black);
    buffergraphics.fillArc(x+5,y+5,50,50,0,360);
    buffergraphics.setColor(Color.white);
    buffergraphics.fillArc(x+10,y+10,40,40,0,360);
    buffergraphics.setColor(Color.black);
  }
  
  public int checksquare(int x, int y) {
    int squarenum = 0;
    for (int j = 1; j < 10; j++) {
      Rectangle square = new Rectangle(gridxcoords[j],gridycoords[j],60,60);
      if (square.contains(x,y)) {squarenum = j;}
    }
    return squarenum;
  }
  
  public void mouseMoved(MouseEvent me) {curx = me.getX(); cury = me.getY(); repaint();}
  public void mouseDragged(MouseEvent me) {}
  
  public void mouseClicked(MouseEvent me) {mouseclicked = true;}
  public void mouseEntered(MouseEvent me) {}
  public void mousePressed(MouseEvent me) {
    pressedx = me.getX();
    pressedy = me.getY();
  }
  public void mouseReleased(MouseEvent me) {
    releasedx = me.getX();
    releasedy = me.getY();
    dragx = releasedx - pressedx;
    dragy = releasedy - pressedy;
    dragdistance = Math.sqrt(dragx*dragx+dragy*dragy);
    if (newgamebutton.contains(pressedx,pressedy)) {resetgame();}
    
    if (mouseclicked) {dragdistance = 1;}
    mouseclicked = false;
    
    if (dragdistance <= 10) {
      int squareclicked = checksquare(pressedx,pressedy);
      if (board[squareclicked] == 0 && squareclicked != 0) {
        if (pxmove) {
          drawX(gridxcoords[squareclicked],gridycoords[squareclicked]);
          board[squareclicked] = 1;
        }
        if (pomove) {
          drawO(gridxcoords[squareclicked],gridycoords[squareclicked]);
          board[squareclicked] = 2;
        }
        if (board[squareclicked] == 1) {pxmove = false; pomove = true;}
        if (board[squareclicked] == 2) {pomove = false; pxmove = true;}
      }
      if (checkforwin() != 0) {winner = checkforwin(); pxmove = false; pomove = false;}
      if (!resultcounted) {
        if (winner == 1) {xwins = xwins + 1;resultcounted = true;}
        if (winner == 2) {owins = owins + 1;resultcounted = true;}
        if (winner == 3) {ties = ties +1;resultcounted = true;}
      }
    }
    repaint();
  }
  public void mouseExited(MouseEvent me) {}
  
  public void resetgame() {
    int[] newboard = {0,0,0,0,0,0,0,0,0,0};
    board = newboard;
    pxmove = true;
    pomove = false;
    winner = 0;
    resultcounted = false;
    buffergraphics.setColor(Color.white);
    buffergraphics.fillRect(0,0,dim.width,dim.height);
    buffergraphics.setColor(Color.black);
  }
  
  public int checkforwin(){
    int winner = 0;
    Boolean boardfull = true;
    for (int j = 1; j < 10; j++) {if (board[j] == 0) {boardfull = false;}}
    if (boardfull) {winner = 3;}
    if (board[1] == 1 && board[2] == 1 && board[3] == 1) {winner = 1;}
    if (board[4] == 1 && board[5] == 1 && board[6] == 1) {winner = 1;}
    if (board[7] == 1 && board[8] == 1 && board[9] == 1) {winner = 1;}
    if (board[1] == 1 && board[4] == 1 && board[7] == 1) {winner = 1;}
    if (board[2] == 1 && board[5] == 1 && board[8] == 1) {winner = 1;}
    if (board[3] == 1 && board[6] == 1 && board[9] == 1) {winner = 1;}
    if (board[1] == 1 && board[5] == 1 && board[9] == 1) {winner = 1;}
    if (board[3] == 1 && board[7] == 1 && board[5] == 1) {winner = 1;}
    if (board[1] == 2 && board[2] == 2 && board[3] == 2) {winner = 2;}
    if (board[4] == 2 && board[5] == 2 && board[6] == 2) {winner = 2;}
    if (board[7] == 2 && board[8] == 2 && board[9] == 2) {winner = 2;}
    if (board[1] == 2 && board[4] == 2 && board[7] == 2) {winner = 2;}
    if (board[2] == 2 && board[5] == 2 && board[8] == 2) {winner = 2;}
    if (board[3] == 2 && board[6] == 2 && board[9] == 2) {winner = 2;}
    if (board[1] == 2 && board[5] == 2 && board[9] == 2) {winner = 2;}
    if (board[3] == 2 && board[7] == 2 && board[5] == 2) {winner = 2;}
    return winner;
  }
} //end of class TicTacToe
/* 250x250 applet
 * 40x40 squares
 * width 10 lines
 * 
 * 
 * 
 * 
 * */