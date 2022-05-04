import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.geom.*;

public class CBPegGame extends Applet implements MouseMotionListener, MouseListener {
  Graphics buffergraphics;
  Image offscreen;
  Dimension dim;
  int clickx,clicky,curx,cury;
  Color red = Color.red;
  Color blue = Color.blue;
  Color green = Color.green;
  Color magenta = Color.magenta;
  Color pink = Color.pink;
  Color white = Color.white;
  Color bgcolor = Color.orange;
  Color yellow = Color.yellow;
  Color cyan = Color.cyan;
  int[] pos = {0,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
  int[] posxcoord = {0,215,175,255,135,215,295,95,175,255,335,55,135,215,295,375, //begin discard pile
  310,340,370,400,430,460,350,380,410,440,470,390,420,450};
  int[] posycoord = {0,100,180,180,260,260,260,340,340,340,340,420,420,420,420,420, //begin discard pile
  10,10,10,10,10,10,90,90,90,90,90,170,170,170};
  Color[] pegcolors = {bgcolor,red,red,blue,white,blue,yellow,magenta,magenta,yellow,cyan,green,white,green,cyan};
  public static final int PIE = 1;
  Boolean choosingpegclick = true;
  Boolean choosingholeclick = false;
  int selectedpeg = 0;
  int selectedhole = 0;
  int discardedpeg = 0;
  int jumpedhole = 0;
  Rectangle newgameemptytop = new Rectangle(10,10,150,40);
  Rectangle newgameemptycenter = new Rectangle(10,60,150,40);

  Rectangle newgameemptybottomcenter = new Rectangle(10,110,100,40);
  Rectangle newgameemptybottomoffcenter = new Rectangle(10,160,100,40);

  double pressedx,pressedy,releasedx,releasedy,dragx,dragy,distance;
  
  public void init() {
    addMouseMotionListener(this);
    addMouseListener(this);
    dim = getSize();
    offscreen = createImage(dim.width,dim.height);
    buffergraphics = offscreen.getGraphics();
  }
  
  public void paint(Graphics g) {
    buffergraphics.drawImage(drawboardbg(),0,0,this);
    drawgame();
    if (choosingholeclick == true) {
      buffergraphics.setColor(Color.white); 
      buffergraphics.drawArc(posxcoord[selectedhole]+20,posycoord[selectedhole]+20,40,40,0,360);
      buffergraphics.setColor(Color.black);
    }
    
    buffergraphics.drawRect(10,10,150,40);
    buffergraphics.drawRect(10,60,150,40);
    buffergraphics.drawRect(10,110,100,40);
    buffergraphics.drawRect(10,160,100,40);
    buffergraphics.setColor(Color.white);
    if (newgameemptytop.contains(curx,cury)) {buffergraphics.drawRect(10,10,150,40);}
    if (newgameemptycenter.contains(curx,cury)) {buffergraphics.drawRect(10,60,150,40);}
    if (newgameemptybottomcenter.contains(curx,cury)) {buffergraphics.drawRect(10,110,100,40);}
    if (newgameemptybottomoffcenter.contains(curx,cury)) {buffergraphics.drawRect(10,160,100,40);}
    buffergraphics.setColor(Color.black);
    buffergraphics.drawString("New Game: Empty Top",15,35);
    buffergraphics.drawString("New Game: Empty Center",15,85);
    buffergraphics.drawString("New Game 3",15,135);
    buffergraphics.drawString("New Game 4",15,185);
    
    g.drawImage(offscreen,0,0,this);
  }
  public void update(Graphics g) {paint(g);}
  
  public void drawgame() {
    for (int j = 1; j < 16; j++) {
      buffergraphics.setColor(pegcolors[pos[j]]);
      buffergraphics.fillArc(posxcoord[j]+20,posycoord[j]+20,40,40,0,360);
      buffergraphics.setColor(Color.black);
      buffergraphics.drawArc(posxcoord[j]+20,posycoord[j]+20,40,40,0,360);
    }
    for (int j = 16; j < 30; j++) {
      buffergraphics.setColor(pegcolors[pos[j]]);
      drawdiscarded(posxcoord[j],posycoord[j]);
      buffergraphics.setColor(Color.black);
    }
  }
  public Image drawboardbg() {
    Image boardbg = createImage(dim.width,dim.height);
    Graphics bggraphics = boardbg.getGraphics();
    bggraphics.setColor(bgcolor);
    bggraphics.fillRect(0,0,dim.width,dim.height);
    bggraphics.setColor(Color.black);
    bggraphics.drawLine(0,510,255,0);
    bggraphics.drawLine(0,510,510,510);
    bggraphics.drawLine(255,0,510,510);
    return boardbg;
  }
  
  public void drawdiscarded(int x,int y) {
    buffergraphics.fillRect(x,y,30,80);
    buffergraphics.setColor(bgcolor);
    int[] xCoords = {x,x+2,x+4,x+6,x+8, //left curve
    x+12,x+15,x+18, //tip
    x+22,x+24,x+26,x+28,x+30, //right curve
    x+30,x,x}; //border
    int[] yCoords = {y+10,y+10,y+12,y+16,y+18, //left curve
    y+76,y+79,y+76, //tip
    y+18,y+16,y+12,y+10,y+10, //right curve
    y+80,y+80,y+10}; //border
    Polygon poly = new Polygon(xCoords,yCoords,15);
    buffergraphics.fillPolygon(poly);
  }
  
  public int checkhole(int x, int y) {
    int holenum = 0;
    for (int j = 1; j < 16; j++) {
      Arc2D.Double hole = new Arc2D.Double(posxcoord[j]+20,posycoord[j]+20,40,40,0,360,PIE);
      if (hole.contains(x,y)) {holenum = j;}
    }
    return holenum;
  }
  
  public void mouseMoved(MouseEvent me) {
    curx = me.getX();
    cury = me.getY();
    repaint();
  }
  public void mouseDragged(MouseEvent me) {mouseClicked(me);}
  
  public void mouseClicked(MouseEvent me) {
    clickx = me.getX();
    clicky = me.getY();
    
    if (choosingpegclick && pos[checkhole(clickx,clicky)] != 0) {
      for (int j = 1; j < 17; j++) {
        if (checkmoves(checkhole(clickx,clicky))[j]) {
          selectedhole = checkhole(clickx,clicky);
          choosingpegclick = false;
          choosingholeclick = true;
        }
      }
    }
    
    if (choosingholeclick && checkhole(clickx,clicky) == selectedhole) {
      selectedhole = 0;
      choosingholeclick = false;
      choosingpegclick = true;
    }
    if (choosingholeclick && checkmoves(selectedhole)[checkhole(clickx,clicky)]) {
      makemove(selectedhole,checkhole(clickx,clicky));
      selectedpeg = 0;
      choosingholeclick = false;
      choosingpegclick = true;
    }
    
    if (newgameemptytop.contains(clickx,clicky)) {resetgame(1);}
    if (newgameemptycenter.contains(clickx,clicky)) {resetgame(2);}
    if (newgameemptybottomcenter.contains(clickx,clicky)) {resetgame(3);}
    if (newgameemptybottomoffcenter.contains(clickx,clicky)) {resetgame(4);}
    
    repaint();
  }
  
  public void makemove(int hole1, int hole2) {
    if (hole1 == 1 && hole2 == 4) {jumpedhole = 2;}
    if (hole1 == 1 && hole2 == 6) {jumpedhole = 3;}
    if (hole1 == 2 && hole2 == 7) {jumpedhole = 4;}
    if (hole1 == 2 && hole2 == 9) {jumpedhole = 5;}
    if (hole1 == 3 && hole2 == 8) {jumpedhole = 5;}
    if (hole1 == 3 && hole2 == 10) {jumpedhole = 6;}
    if (hole1 == 4 && hole2 == 1) {jumpedhole = 2;}
    if (hole1 == 4 && hole2 == 6) {jumpedhole = 5;}
    if (hole1 == 4 && hole2 == 13) {jumpedhole = 8;}
    if (hole1 == 4 && hole2 == 11) {jumpedhole = 7;}
    if (hole1 == 5 && hole2 == 12) {jumpedhole = 8;}
    if (hole1 == 5 && hole2 == 14) {jumpedhole = 9;}
    if (hole1 == 6 && hole2 == 1) {jumpedhole = 3;}
    if (hole1 == 6 && hole2 == 4) {jumpedhole = 5;}
    if (hole1 == 6 && hole2 == 13) {jumpedhole = 9;}
    if (hole1 == 6 && hole2 == 15) {jumpedhole = 10;}
    if (hole1 == 7 && hole2 == 2) {jumpedhole = 4;}
    if (hole1 == 7 && hole2 == 9) {jumpedhole = 8;}
    if (hole1 == 8 && hole2 == 3) {jumpedhole = 5;}
    if (hole1 == 8 && hole2 == 10) {jumpedhole = 9;}
    if (hole1 == 9 && hole2 == 2) {jumpedhole = 5;}
    if (hole1 == 9 && hole2 == 7) {jumpedhole = 8;}
    if (hole1 == 10 && hole2 == 3) {jumpedhole = 6;}
    if (hole1 == 10 && hole2 == 8) {jumpedhole = 9;}
    if (hole1 == 11 && hole2 == 4) {jumpedhole = 7;}
    if (hole1 == 11 && hole2 == 13) {jumpedhole = 12;}
    if (hole1 == 12 && hole2 == 5) {jumpedhole = 8;}
    if (hole1 == 12 && hole2 == 14) {jumpedhole = 13;}
    if (hole1 == 13 && hole2 == 11) {jumpedhole = 12;}
    if (hole1 == 13 && hole2 == 4) {jumpedhole = 8;}
    if (hole1 == 13 && hole2 == 6) {jumpedhole = 9;}
    if (hole1 == 13 && hole2 == 15) {jumpedhole = 14;}
    if (hole1 == 14 && hole2 == 12) {jumpedhole = 13;}
    if (hole1 == 14 && hole2 == 5) {jumpedhole = 9;}
    if (hole1 == 15 && hole2 == 13) {jumpedhole = 14;}
    if (hole1 == 15 && hole2 == 6) {jumpedhole = 10;}
    
    pos[hole2] = pos[hole1];
    pos[hole1] = 0;
    int j = 16;
    while (pos[j] != 0) {j = j + 1;}
    pos[j] = pos[jumpedhole];
    pos[jumpedhole] = 0;
  }
  
  public Boolean[] checkmoves(int hole) {
    Boolean[] moves = new Boolean[16];
    for (int j = 1; j<16;j++) {moves[j] = false;}
    if (hole == 1 && pos[2] != 0 && pos[4] == 0) {moves[4] = true;}
    if (hole == 1 && pos[3] != 0 && pos[6] == 0) {moves[6] = true;}
    if (hole == 2 && pos[4] != 0 && pos[7] == 0) {moves[7] = true;}
    if (hole == 2 && pos[5] != 0 && pos[9] == 0) {moves[9] = true;}
    if (hole == 3 && pos[5] != 0 && pos[8] == 0) {moves[8] = true;}
    if (hole == 3 && pos[6] != 0 && pos[10] == 0) {moves[10] = true;}
    if (hole == 4 && pos[2] != 0 && pos[1] == 0) {moves[1] = true;}
    if (hole == 4 && pos[5] != 0 && pos[6] == 0) {moves[6] = true;}
    if (hole == 4 && pos[7] != 0 && pos[11] == 0) {moves[11] = true;}
    if (hole == 4 && pos[8] != 0 && pos[13] == 0) {moves[13] = true;}
    if (hole == 5 && pos[8] != 0 && pos[12] == 0) {moves[12] = true;}
    if (hole == 5 && pos[9] != 0 && pos[14] == 0) {moves[14] = true;}
    if (hole == 6 && pos[3] != 0 && pos[1] == 0) {moves[1] = true;}
    if (hole == 6 && pos[5] != 0 && pos[4] == 0) {moves[4] = true;}
    if (hole == 6 && pos[9] != 0 && pos[13] == 0) {moves[13] = true;}
    if (hole == 6 && pos[10] != 0 && pos[15] == 0) {moves[15] = true;}
    if (hole == 7 && pos[4] != 0 && pos[2] == 0) {moves[2] = true;}
    if (hole == 7 && pos[8] != 0 && pos[9] == 0) {moves[9] = true;}
    if (hole == 8 && pos[5] != 0 && pos[3] == 0) {moves[3] = true;}
    if (hole == 8 && pos[9] != 0 && pos[10] == 0) {moves[10] = true;}
    if (hole == 9 && pos[8] != 0 && pos[7] == 0) {moves[7] = true;}
    if (hole == 9 && pos[5] != 0 && pos[2] == 0) {moves[2] = true;}
    if (hole == 10 && pos[6] != 0 && pos[3] == 0) {moves[3] = true;}
    if (hole == 10 && pos[9] != 0 && pos[8] == 0) {moves[8] = true;}
    if (hole == 11 && pos[7] != 0 && pos[4] == 0) {moves[4] = true;}
    if (hole == 11 && pos[12] != 0 && pos[13] == 0) {moves[13] = true;}
    if (hole == 12 && pos[8] != 0 && pos[5] == 0) {moves[5] = true;}
    if (hole == 12 && pos[13] != 0 && pos[14] == 0) {moves[14] = true;}
    if (hole == 13 && pos[8] != 0 && pos[4] == 0) {moves[4] = true;}
    if (hole == 13 && pos[9] != 0 && pos[6] == 0) {moves[6] = true;}
    if (hole == 13 && pos[12] != 0 && pos[11] == 0) {moves[11] = true;}
    if (hole == 13 && pos[14] != 0 && pos[15] == 0) {moves[15] = true;}
    if (hole == 14 && pos[13] != 0 && pos[12] == 0) {moves[12] = true;}
    if (hole == 14 && pos[9] != 0 && pos[5] == 0) {moves[5] = true;}
    if (hole == 15 && pos[14] != 0 && pos[13] == 0) {moves[13] = true;}
    if (hole == 15 && pos[10] != 0 && pos[6] == 0) {moves[6] = true;}
    return moves;
  }
  
  public void resetgame(int emptyhole) {
    if (emptyhole == 1) {
      int[] newgamepostop = {0,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
      pos = newgamepostop;
    }
    if (emptyhole == 2) {
      int[] newgameposcenter = {0,4,1,2,3,0,5,6,7,8,9,10,11,12,13,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
      pos = newgameposcenter;
    }
    if (emptyhole == 3) {
      int[] newgameposbottomcenter = {0,4,1,2,3,12,5,6,7,8,9,10,11,0,13,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
      pos = newgameposbottomcenter;
    }
    if (emptyhole == 4) {
      int[] newgameposbottomoffcenter = {0,4,1,2,3,12,5,6,7,8,9,10,0,11,13,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
      pos = newgameposbottomoffcenter;
    }
    repaint();

  }
  
  public void mouseEntered(MouseEvent me) {}
  public void mousePressed(MouseEvent me) {
    pressedx = me.getX();
    pressedy = me.getY();
  }
  public void mouseReleased(MouseEvent me) {repaint();
    releasedx = me.getX();
    releasedy = me.getY();
    
    dragx = Math.abs(releasedx - pressedx);
    dragy = Math.abs(releasedy - pressedy);
    distance = Math.sqrt(((dragx * dragx) + (dragy * dragy)));
    //if (distance <= 10) {mouseClicked(me);}
    repaint();
  }
  public void mouseExited(MouseEvent me) {repaint();}
} //end of class CBPegGame










