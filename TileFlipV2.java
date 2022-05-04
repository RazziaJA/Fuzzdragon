import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;

public class TileFlipV2 extends Applet implements MouseMotionListener, MouseListener {
  Graphics buffergraphics;
  Image offscreen;
  Dimension dim;
  int pressedx,pressedy,releasedx,releasedy,draggedx,draggedy,curx,cury;
  int squarelength,redcount,size;
  double dragdistance;
  int[] xcoords, ycoords;
  int[][] grid;
  Random gen = new Random();
  
  public void init() {
    addMouseMotionListener(this);
    addMouseListener(this);
    dim = getSize();
    offscreen = createImage(dim.width,dim.height);
    buffergraphics = offscreen.getGraphics();
    resetgame(10);
  }
  
  public void paint(Graphics g) {
    buffergraphics.setColor(new Color(128,128,128));
    buffergraphics.fillRect(0,0,dim.width,dim.height);
    buffergraphics.setColor(Color.black);
    drawboard();
    drawmenu();
    g.drawImage(offscreen,0,0,this);
  }
  public void update(Graphics g) {paint(g);}
  
  public void mouseMoved(MouseEvent me) {
    curx = me.getX();
    cury = me.getY();
    repaint();
  }
  
  public void mouseDragged(MouseEvent me) {}
  public void mouseClicked(MouseEvent me) {}
  public void mouseEntered(MouseEvent me) {}
  public void mousePressed(MouseEvent me) {
    pressedx = me.getX();
    pressedy = me.getY();
  }
  
  public void mouseReleased(MouseEvent me) {
    releasedx = me.getX();
    releasedy = me.getY();
    draggedx = Math.abs(releasedx - pressedx);
    draggedy = Math.abs(releasedy - pressedy);
    dragdistance = Math.sqrt(draggedx*draggedx + draggedy*draggedy); //checks for clicks with small movement
    
    if (dragdistance <= 10) {
      if (checkcolumn(pressedx,pressedy) != 0 && checkrow(pressedx,pressedy) != 0) {
        makemove(checkcolumn(pressedx,pressedy),checkrow(pressedx,pressedy));
      }
      for (int j=5;j<=10;j++) {
        Rectangle rect = new Rectangle(10,10+50*j-250,75,20);
        if (rect.contains(pressedx,pressedy)) {resetgame(j);}
      }
    }
    repaint();
  }
  public void mouseExited(MouseEvent me) {}
  
  public void drawmenu() {
    for (int j=5;j<=10;j++) {
      buffergraphics.drawRect(10,10+50*j-250,75,20);
      buffergraphics.drawString(""+j+"x"+j+" Game",14,10+50*j-234);
    }
  }
  
  public void drawboard() { //fills in the squares with their respective colors
    for (int j=1;j<=size;j++) {
      for (int i=1;i<=size;i++) {
        if (grid[j][i] == 0) {buffergraphics.setColor(Color.blue);} 
        if (grid[j][i] == 1) {buffergraphics.setColor(Color.red);}
        buffergraphics.fillRect(xcoords[j],ycoords[i],squarelength,squarelength);
        buffergraphics.setColor(Color.black);
        buffergraphics.drawRect(xcoords[j],ycoords[i],squarelength,squarelength);
      }
    }
    buffergraphics.setColor(Color.black);
  }
  
  public int checkrow(int x, int y) {
    int row = 0;
    for (int j = 1;j<=size;j++) {
      if (y >= ycoords[j]) {row = j;}
    }
    return row;
  }
  public int checkcolumn(int x, int y) {
    int column = 0;
    for (int j=1;j<=size;j++) {
      if (x >= xcoords[j]) {column = j;}
    }
    return column;
  }
  
  public void makemove(int row, int column) {
    if (grid[row][column] == 0) {grid[row][column] = 1;} //flips clicked tile
    else if (grid[row][column] == 1) {grid[row][column] = 0;}
    
    if (row != 1) { //flips top tile
      if (grid[row-1][column] == 0) {grid[row-1][column] = 1;} 
      else if (grid[row-1][column] == 1) {grid[row-1][column] = 0;}
    }
    
    if (row != size) { //flips bottom tile
      if (grid[row+1][column] == 0) {grid[row+1][column] = 1;} 
      else if (grid[row+1][column] == 1) {grid[row+1][column] = 0;}
    }
    
    if (column != 1) { //flips left tile
      if (grid[row][column-1] == 0) {grid[row][column-1] = 1;} 
      else if (grid[row][column-1] == 1) {grid[row][column-1] = 0;}
    }
    
    if (column != size) { //flips right tile
      if (grid[row][column+1] == 0) {grid[row][column+1] = 1;} 
      else if (grid[row][column+1] == 1) {grid[row][column+1] = 0;}
    }
  }
  
  public void resetgame(int sizee) {
    size = sizee;
    for (int j=5;j<=10;j++) {
      if (size == j) {squarelength = (int)300/size;} //sets square dimensions
    }
    xcoords = new int[size+1];
    ycoords = new int[size+1];
    for (int j=1;j<xcoords.length;j++) {
      xcoords[j] = 100 + squarelength*j - squarelength; //sets coordinates for columns
      ycoords[j] = squarelength*j - squarelength; //sets coordinates for rows
    }
    grid = new int[sizee+1][size+1];
    for (int j=1;j<=size;j++) {
      for (int i=1;i<=size;i++) {
        grid[j][i] = 0; //sets the board to all blue 
      }
    }
    while (redcount <= size*size/2) { //randomly moves until 50-50 red/blue
      redcount = 0;
      makemove(gen.nextInt(size-1)+1,gen.nextInt(size-1)+1);
      for (int j=1;j<=size;j++) {
        for (int i=1;i<=size;i++) {
          if (grid[j][i] == 1) {redcount++;}
        }
      }
    }
    redcount = 0;
  }
} //end of class TileFlipV2