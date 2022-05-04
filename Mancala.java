// Jeff Anway
// File: Mancala.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.Font;

public class Mancala extends Applet implements MouseListener, MouseMotionListener {
  Graphics buffergraphics;
  Image offscreen;
  Dimension dim;
  int curx, cury, clickx, clicky, pressedx, pressedy, releasedx, releasedy;
  Boolean player1turn, player2turn; 
  String winner, newgame;
  Color bgcolor, pitcolor; 

  //keeps track of the number of seeds in each pit
  int[] board = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
  
  public void init() {
    addMouseMotionListener(this);
    addMouseListener(this);
    dim = getSize();
    offscreen = createImage(dim.width,dim.height);
    buffergraphics = offscreen.getGraphics();
    resetgame(4);
    newgame = "New Game";
    bgcolor = Color.white; //actually is (252,219,66)
    pitcolor = Color.black; //actually is (255,215,29)
  }
  
  public void paint(Graphics g) {
    buffergraphics.clearRect(0,0,dim.width,dim.height);
    
    drawboard();
    
    //fills in the corners
    buffergraphics.setColor(bgcolor);
    buffergraphics.fillRect(0,0,100,50);
    buffergraphics.fillRect(0,150,100,50);
    buffergraphics.fillRect(700,0,100,50);
    buffergraphics.fillRect(700,150,100,50);
    
    /*
    //draws the options to start a new game
    buffergraphics.setColor(Color.white);
    buffergraphics.setFont(new Font("TimesRoman",Font.BOLD,16));
    buffergraphics.drawString(newgame,10,25);
    buffergraphics.drawString("3 Stones",10,45);
    buffergraphics.drawString(newgame,10,175);
    buffergraphics.drawString("5 Stones",10,195);
    buffergraphics.drawString(newgame,710,25);
    buffergraphics.drawString("4 Stones",710,45);
    buffergraphics.drawString(newgame,710,175);
    buffergraphics.drawString("6 Stones",710,195);
    
    //outlines the new game options on mouseover
    if (curx > 700 && cury < 50) {buffergraphics.drawRect(700,0,100,50);}
    if (curx < 100 && cury < 50) {buffergraphics.drawRect(0,0,100,50);}
    if (curx > 700 && cury >150) {buffergraphics.drawRect(700,150,100,50);}
    if (curx < 100 && cury >150) {buffergraphics.drawRect(0,150,100,50);}
    
    //displays which player's turn it is
    if (winner == "") {
      buffergraphics.setFont(new Font("TimesRoman",Font.PLAIN,14));
      if (player1turn == true) {buffergraphics.drawString("Turn: Player 1",350,106);}
      if (player2turn == true) {buffergraphics.drawString("Turn: Player 2",350,106);}
    }
    buffergraphics.setColor(Color.black);
    
    //announces the winner
    if (winner == "Player 1" || winner == "Player 2") {
      buffergraphics.setFont(new Font("TimesRoman",Font.BOLD,48));
      buffergraphics.drawString(winner+" wins!",250,125);
      buffergraphics.setFont(new Font("TimesRoman",Font.PLAIN,12));
    }
    if (winner == "Draw") {
      buffergraphics.setFont(new Font("TimesRoman",Font.BOLD,48));
      buffergraphics.drawString(winner+".",250,125);
      buffergraphics.setFont(new Font("TimesRoman",Font.PLAIN,12));
    }
    */
    
    g.drawImage(offscreen,0,0,this);
  }
  public void update(Graphics g) {paint(g);}
  
  public void mouseClicked(MouseEvent me) {
    clickx = me.getX();
    clicky = me.getY();
    
    //makes a move if a movable pit is clicked
    if (checkcanbemoved(clickx,clicky) == true) {
      makemove(determinepit(clickx,clicky));
    }
    
    //resets the game if a new game option is clicked
    if (clickx < 100 && clicky < 50) {resetgame(3);}
    if (clickx < 100 && clicky >150) {resetgame(5);}
    if (clickx > 700 && clicky < 50) {resetgame(4);}
    if (clickx > 700 && clicky >150) {resetgame(6);}
    repaint();
  }
  
  //determines the current position of the mouse
  public void mouseMoved(MouseEvent me) {
    curx = me.getX();
    cury = me.getY();
    repaint();
  }
  
  //finds the number of seeds in each pit and displays the corresponding picture
  public void drawboard() {
      buffergraphics.drawImage(drawpit(board[1]),100,100,this);
      buffergraphics.drawImage(drawpit(board[2]),200,100,this);
      buffergraphics.drawImage(drawpit(board[3]),300,100,this);
      buffergraphics.drawImage(drawpit(board[4]),400,100,this);
      buffergraphics.drawImage(drawpit(board[5]),500,100,this);
      buffergraphics.drawImage(drawpit(board[6]),600,100,this);
      buffergraphics.drawImage(drawpit(board[7]),700,50,this);
      buffergraphics.drawImage(drawpit(board[8]),600,0,this);
      buffergraphics.drawImage(drawpit(board[9]),500,0,this);
      buffergraphics.drawImage(drawpit(board[10]),400,0,this);
      buffergraphics.drawImage(drawpit(board[11]),300,0,this);
      buffergraphics.drawImage(drawpit(board[12]),200,0,this);
      buffergraphics.drawImage(drawpit(board[13]),100,0,this);
      buffergraphics.drawImage(drawpit(board[14]),0,50,this);
  }
  
  //determines whether a clicked pit can be moved; checks the player's turn and the pit's contents
  public Boolean checkcanbemoved(int x, int y){
    Boolean pitcanbemoved = false;
    if (determinepit(x,y) == 7 || determinepit(x,y) == 14) {pitcanbemoved = false;}
    if (player1turn == true) {
      for (int j=1;j<7;j++) {
        if (determinepit(x,y) == j && board[j] > 0) {pitcanbemoved = true;}
      }
    }
    if (player2turn == true) {
      for (int i=8;i<14;i++) {
        if (determinepit(x,y) == i && board[i] > 0) {pitcanbemoved = true;}
      }
    }
    return pitcanbemoved;
  }
  
  //uses the coordinates of a click or mouseover and determines which pit the cursor is over
  public int determinepit(int x, int y) {
    int pit = 0;
      if (x<100) {pit = 14;}
      if (x>100 && x<200) {
        if (y<100) {pit = 13;}
        if (y>100 && y<200) {pit = 1;} 
      }
      if (x>200 && x<300) {
        if (y<100) {pit = 12;}
        if (y>100 && y<200) {pit = 2;}
      }
      if (x>300 && x<400) {
        if (y<100) {pit = 11;}
        if (y>100 && y<200) {pit = 3;}
      }
      if (x>400 && x<500) {
        if (y<100) {pit = 10;}
        if (y>100 && y<200) {pit = 4;}
      }
      if (x>500 && x<600) {
        if (y<100) {pit = 9;}
        if (y>100 && y<200) {pit = 5;}
      }
      if (x>600 && x<700) {
        if (y<100) {pit = 8;}
        if (y>100 && y<200) {pit = 6;}
      }
      if (x>700) {pit = 7;}
    return pit;
  }
  
  public void makemove(int pit) {
    int curpit = pit;
    int stonesinhand = board[pit];
    board[pit] = 0;
    int endpit = 0;

    //adds seeds until all seeds from the original pit have been sown
    while (stonesinhand > 0) {
      curpit = curpit%14;
      curpit++;
      if (player1turn == true && curpit == 14) {curpit = 1;}
      if (player2turn == true && curpit == 7) {curpit = 8;}
      board[curpit]++;
      stonesinhand--;
    } 
    endpit = curpit;
    
    //makes a capture if necessary
    if (endpit != 14 && endpit != 7) {
      if (board[endpit] == 1) {
        if (player1turn == true && endpit >= 1 && endpit <= 6 && board[14-endpit] > 0) {
          board[7]++;
          board[endpit] = 0;
          board[7] = board[7] + board[14-endpit];
          board[14-endpit] = 0;
        }
        if (player2turn == true && endpit >= 8 && endpit <= 13 && board[14-endpit] > 0) {
          board[14]++;
          board[endpit] = 0;
          board[14] = board[14] + board[14-endpit];
          board[14-endpit] = 0;
        }
      }
    }
    
    //determines whose turn it is next, checks for extra moves
    if ((player1turn == true && endpit != 7) || (player2turn == true && endpit != 14)) {
      if (player1turn == true) {player1turn = false; player2turn = true;}
      else {player2turn = false; player1turn = true;}
    }

    //checks for gameover, if either side has no seeds remaining
    Boolean player1sideempty = true;
    Boolean player2sideempty = true;
    for (int k = 1; k < 7; k++) {if (board[k] > 0) {player1sideempty = false;}}
    if (player1sideempty == true) {
      for (int j = 8; j < 14; j++) {
        board[14] = board[14] + board[j];
        board[j] = 0;
      }
    }
    for (int k = 8; k < 14; k++) {if (board[k] > 0) {player2sideempty = false;}}
    if (player2sideempty == true) {
      for (int j = 1; j < 7; j++) {
        board[7] = board[7] + board[j];
        board[j] = 0;
      }
    }
    
    //ends the game and decides the winner, if the gameover check is positive
    if (player1sideempty == true || player2sideempty == true) {
      player1turn = false; player2turn = false;
      if (board[7] > board[14]) {winner = "Player 1";}
      if (board[14] > board[7]) {winner = "Player 2";}
      if (board[7] == board[14]) {winner = "Draw";}
    }
  }
  
  //draws the pictures. varied seed arrangements for counts under 6, then the same arrangement for 7+, with a number drawn over 
  public Image drawpit(int stones) {
    Image pic = createImage(100,100);
    Graphics pitgraphics = pic.getGraphics();
    pitgraphics.setColor(bgcolor);
    pitgraphics.fillRect(0,0,100,100);
    pitgraphics.setColor(pitcolor);
    pitgraphics.drawArc(7,7,86,86,0,360); //actually is fillArc
      pitgraphics.setColor(Color.black); //actually is white
    if (stones == 1) {pitgraphics.fillArc(43,43,14,14,0,360);}
    if (stones == 2) {
      pitgraphics.fillArc(29,43,14,14,0,360); 
      pitgraphics.fillArc(57,43,14,14,0,360); }
    if (stones == 3) {
      pitgraphics.fillArc(43,31,14,14,0,360);
      pitgraphics.fillArc(29,50,14,14,0,360);
      pitgraphics.fillArc(64,50,14,14,0,360); }
    if (stones == 4) {
      pitgraphics.fillArc(29,29,14,14,0,360);
      pitgraphics.fillArc(29,57,14,14,0,360);
      pitgraphics.fillArc(57,57,14,14,0,360);
      pitgraphics.fillArc(57,29,14,14,0,360); }
    if (stones == 5) {
      pitgraphics.fillArc(43,43,14,14,0,360);
      pitgraphics.fillArc(29,29,14,14,0,360);
      pitgraphics.fillArc(29,57,14,14,0,360);
      pitgraphics.fillArc(57,57,14,14,0,360);
      pitgraphics.fillArc(57,29,14,14,0,360); }
    if (stones == 6) {
      pitgraphics.fillArc(23,33,14,14,0,360);
      pitgraphics.fillArc(63,33,14,14,0,360);
      pitgraphics.fillArc(23,50,14,14,0,360);
      pitgraphics.fillArc(63,50,14,14,0,360);
      pitgraphics.fillArc(43,23,14,14,0,360);
      pitgraphics.fillArc(43,63,14,14,0,360); }
    if (stones >= 7) {
      pitgraphics.fillArc(23,33,14,14,0,360);
      pitgraphics.fillArc(63,33,14,14,0,360);
      pitgraphics.fillArc(23,50,14,14,0,360);
      pitgraphics.fillArc(63,50,14,14,0,360);
      pitgraphics.fillArc(43,23,14,14,0,360);
      pitgraphics.fillArc(43,63,14,14,0,360);
      pitgraphics.fillArc(43,43,14,14,0,360);
      pitgraphics.setColor(Color.black);
      pitgraphics.setFont(new Font("TimesRoman",Font.BOLD,48));
      if (stones > 7 && stones < 10) {pitgraphics.drawString(""+stones,40,64);}
      if (stones >= 10 && stones < 100) {pitgraphics.drawString(""+stones,30,64);}
      if (stones >= 100 && stones < 1000) {pitgraphics.drawString(""+stones,20,64);}
    }
    return pic;
  }
  
  //starts a new game with a number of seeds in each pit
  public void resetgame(int stones) {
    for (int j = 1; j<15;j++) {
      board[j] = stones;
      board[7] = 0;
      board[14] = 0;
    }
    player1turn = true;
    player2turn = false;
    winner = "";
  }
  
  //gives a 5-pixel tolerance to clicks to allow for slight movements while clicking
  public void mousePressed(MouseEvent me) {
    pressedx = me.getX();
    pressedy = me.getY();
  }
  public void mouseReleased(MouseEvent me) {
    releasedx = me.getX();
    releasedy = me.getY();
    int vertdragdistance = releasedy - pressedy;
    int horidragdistance = releasedx - pressedx;
    double dragdistance = Math.sqrt(vertdragdistance^2 + horidragdistance^2);
    if (dragdistance <= 5 && dragdistance != 0) { 
      mouseClicked(me);
    }
  }
  
  public void mouseEntered(MouseEvent me) {}
  public void mouseExited(MouseEvent me) {}
  public void mouseDragged(MouseEvent me) {}
}