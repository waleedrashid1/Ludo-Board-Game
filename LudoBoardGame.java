// Waleed Rashid
// June 1, 2022
// Ludo Board Game
// 2 people can play the board game Ludo with one piece each

import hsa.Console;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class LudoBoardGame
  
{
  static Console s; // Console for starting screen
  static Console b; // Console for main board
  static Console c; // Console for user inputs
  
  public static void main (String[] args) throws IOException
  {
    String [] playerNames = new String [2]; // Both players in the game
    String playAgain; // User's decision if they wish to play again\
    Color [] players = new Color [] {Color.blue, Color.green}; // Player colours (blue, green)
    final int PIECESPACING = 29; // The distance between each space
    int [] wins = new int [2]; // Each player's total number of wins
    boolean win = false; // If a win has occured in the current game
    Image ludoStart;  // Starting ludo logo image
    BufferedReader inputScores; // Inputted score .txt file
    PrintWriter outputScores = new PrintWriter (new FileWriter ("LudoScores.txt")); // Outputted score .txt file
    
    
    s = new Console (26,64);
    
    // Try to import the image from the file
    try
    {
      ludoStart = ImageIO.read (new File ("ludo logo.png"));
    }
    
    // Catches the error if the file is not found
    catch (IOException e)
    {
      ludoStart = null;
    }
    
    s.drawImage(ludoStart, 0, 0, null); // Draws ludo logo image
    
    s.setCursor (26,22);
    s.print ("Play (Press Any Key)");
    s.getChar (); // User enters any key to play
    s.close ();
    
    
    c = new Console (28,64);
    b = new Console ();
    
    
    // Try to input the scores .txt file
    try
    {
      inputScores = new BufferedReader (new FileReader ("LudoScores.txt"));
      inputScores.readLine ();
    }
    
    // Catches the error if the file is not found
    catch (FileNotFoundException e)
    {
      outputScores = new PrintWriter (new FileWriter ("LudoScores.txt")); // Creates scores .txt file to output in the end
      
      // Reads scores .txt file
      inputScores = new BufferedReader (new FileReader ("LudoScores.txt"));
      inputScores.readLine ();
    }
    
    
    do
    {
      board (PIECESPACING, win, players); // Draws board
      
      
      // For loop to ask for players' names
      for (int x = 0; x <= playerNames.length - 1; x++)
      {
        c.println ("What is player " + (x + 1) + "'s name? (12 or less characters)");
        playerNames [x] = c.readLine ();
        
        // If the players name is longer than 12 characters
        if (playerNames [x].length () > 12)
        {
          c.println ("That name is too long, please use 12 or less characters.");
          x--;
        }
      }
      
      
      // Displays which player is which colour
      for (int x = 0; x < playerNames.length; x++)
      {
        b.setColor (players [x]);
        b.fillOval (475,200 + (38 * x),25,25);
        b.setCursor (11 + (2 * x),65);
        b.println (playerNames [x]);
      }
      
      diceRoll (playerNames, players, PIECESPACING, win, wins); // Runs dice rolling method
      
      // User can play again after the game is over
      c.println ("Would you like to play again? (Y/N):");
      playAgain = c.readLine ();
      b.clear ();
      c.clear ();
    }
    
    while (playAgain.equalsIgnoreCase ("Y"));
    
    c.clear ();
    b.close ();
    c.println ("Thanks for playing!");
    c.print ("Scores have been recorded in \"LudoScores.txt\".");
    
    // Scores gets outputted to .txt file
    outputScores.println ("Ludo Scores\n");
    for (int x = 0; x < playerNames.length; x++)
    {
      outputScores.println (playerNames [x] + ": " + wins [x]);
    }
    
    outputScores.close ();
  } // niaM
  
  
  // Method to draw board
  public static void board (final int PIECESPACING, boolean win, Color [] players)
  {
    int boardLines = 39; // The horizontally drawn lines on the board that seperate the spaces
    int [] homeX = new int [] {82, 345}; // Both player's X-coordinated position for their home colour
    int [] homeY = new int [] {345, 82}; // Both player's Y-coordinated position for their home colour
    
    
    // Ludo Board
    b.setColor (Color.black);
    b.drawRect (10,10,435,435); // Perimeter of board
    
    // Red square in top-left of board
    b.setColor (Color.red);
    b.fillRect (11,11,174,174);
    
    b.fillRect (40,213,145,30);
    b.fillRect (40,184,29,29);
    
    // Green square in top-right of board
    b.setColor (Color.green);
    b.fillRect (272,11,174,174);
    
    b.fillRect (214,40,29,145);
    b.fillRect (214,40,63,29);
    
    // Blue square in bottom-left of board
    b.setColor (Color.blue);
    b.fillRect (11,272,174,174);
    
    b.fillRect (214,272,29,145);
    b.fillRect (185,388,58,29);
    
    // Yellow square in bottom-right of board
    b.setColor (Color.yellow);
    b.fillRect (272,272,174,174);
    
    b.fillRect (272,213,145,29);
    b.fillRect (388,242,29,29);
    
    // Black outlines for each square on board
    b.setColor (Color.black);
    b.drawRect (10,10,174,174); // Red square outlines
    b.drawRect (271,10,174,174); // Green square outlines
    b.drawRect (10,271,174,174); // Blue square outlines
    b.drawRect (271,271,174,174); // Yellow square outlines
    
    // Black lines seperating spaces on the board
    b.drawLine (213,11,213,445); // Green/blue line 1
    b.drawLine (242,11,242,445); // Green/blue line 2
    b.drawLine (11,213,445,213); // Red/yellow line 1
    b.drawLine (11,242,445,242); // Red/yellow line 2
    
    // Black lines drawn to seperate spaces on the board
    for (int x = 0; x < 15; x++)
    {
      b.drawLine (185, boardLines, 271, boardLines); // Blue/Green horizontal lines
      b.drawLine (boardLines,185,boardLines,271);  // Red/Yellow vertical lines
      boardLines += PIECESPACING;
    }
    
    // White circles for piece's home position
    b.setColor (Color.white);
    b.fillOval (70,70,50,50); // Red's home position
    b.fillOval (332,70,50,50); // Green's home position
    b.fillOval (70,332,50,50); // Blue's home position
    b.fillOval (332,332,50,50); // Yellow's home position
    
    // Outlines for piece's starting home circle
    b.setColor (Color.black);
    b.drawOval (70,70,50,50); // Red's home position
    b.drawOval (332,70,50,50); // Green's home position
    b.drawOval (70,332,50,50); // Blue's home position
    b.drawOval (332,332,50,50); // Yellow's home position
    
    // Square in the middle of the board
    b.setColor (Color.pink);
    b.fillRect (184,184,87,87);
    b.setColor (Color.black);
    b.drawRect (184,184,87,87);
    
    // Draws each player's piece at home position
    for (int x = 0; x <= 1; x++)
    {
      b.setColor (players [x]);
      b.fillOval (homeX [x], homeY [x], 25, 25);
      
      b.setColor (Color.black);
      b.drawOval (homeX [x], homeY [x], 25, 25);
    }
    
  } // End of board method
  
  
  // Method for dice rolling
  public static void diceRoll (String [] playerNames, Color [] players, final int PIECESPACING, boolean win, int [] wins)
  {
    int dice; // User's rolled dice number
    
    int [] positionX = new int [] {82, 345}; // Both player's current X-coordinated position
    int [] positionY = new int [] {345, 82}; // Both player's current Y-coordiinated position
    
    int [] startingX = new int [] {186, 244, 41, 389}; // X-coordinated position for each colour's starting space {blueX, greenX, redX, yellowX}
    int [] startingY = new int [] {389, 41, 186, 244}; // Y-coordinated position for each colour's starting space {blueY, greenY, redY, yellowY}
    
    int [] homeX = new int [] {82, 345}; // Both player's X-coordinated position for their home colour
    int [] homeY = new int [] {345, 82}; // Both player's Y-coordinated position for their home colour
    
    int [] homeCirclesX = new int [] {70, 332}; // Both player's X-coordinated position for home position circles
    int [] homeCirclesY = new int [] {332, 70}; // Both player's Y-coordinated position for home position circles
    
    int winSpaceX = 215;
    int [] winSpaceY = new int [] {244, 186};
    
    // Ludo Dice
    b.setColor (Color.black);
    b.drawRect (475,10,100,100);
    
    // For loop to alternate turns between player 1 and player 2
    for (int playerMoveLooper = 0; playerMoveLooper <= 1; playerMoveLooper++)
    {
      for (int x = 0; x <= 1; x++)
      {
        b.setCursor (7,60);
        b.println (playerNames [x] + "'s turn."); // Displays the current roller
        
        c.println (playerNames [x] + "'s turn. Roll dice (Any key):");
        c.readLine ();
        
        dice = (int)(Math.random () * 6 + 1); // Generates random dice number between 1-6
        
        c.println ("You rolled " + dice + ".");
        
        // Refreshes dice
        b.setColor (Color.white);
        b.fillRect (485,20,90,90);
        b.setColor (Color.black);
        
        // Dice roll = 1
        if (dice == 1)
        {
          b.fillOval (515, 47, 20, 20); // Middle dot
          
          oneSpaceMovement (positionX, positionY, homeX, homeY, dice, startingX, startingY, winSpaceX, winSpaceY, PIECESPACING, players, win, x, playerNames);
        }
        
        // Dice roll = 2
        else if (dice == 2)
        {
          b.fillOval (500,35,20,20); // Top-left dot
          b.fillOval (530,59,20,20); // Bottom-right dot 
          
          // Repeats "oneSpaceMovement" method 2 times
          for (int y = 1; y <= 2; y++)
          {
            oneSpaceMovement (positionX, positionY, homeX, homeY, dice, startingX, startingY, winSpaceX, winSpaceY, PIECESPACING, players, win, x, playerNames);
          }
        }
        
        // Dice roll = 3
        else if (dice == 3)
        {
          b.fillOval (490, 25, 20, 20); // Top-left dot
          b.fillOval (515, 47, 20, 20); // Middle dot
          b.fillOval (540, 69, 20, 20); // Bottom-right dot
          
          // Repeats "oneSpaceMovement" method 3 times
          for (int y = 1; y <= 3; y++)
          {
            oneSpaceMovement (positionX, positionY, homeX, homeY, dice, startingX, startingY, winSpaceX, winSpaceY, PIECESPACING, players, win, x, playerNames);
          }
        }
        
        // Dice roll = 4
        else if (dice == 4)
        {
          b.fillOval (490,25,20,20); // Top-left dot
          b.fillOval (540,25,20,20); // Top-right dot
          b.fillOval (490,75,20,20); // Bottom-left dot
          b.fillOval (540,75,20,20); // Bottom right dot
          
          // Repeats "oneSpaceMovement" method 4 times
          for (int y = 1; y <= 4; y++)
          {
            oneSpaceMovement (positionX, positionY, homeX, homeY, dice, startingX, startingY, winSpaceX, winSpaceY, PIECESPACING, players, win, x, playerNames);
          }
        }
        
        // Dice roll = 5
        else if (dice == 5)
        {
          b.fillOval (490,25,20,20); // Top-left dot
          b.fillOval (540,25,20,20); // Top-right dot
          b.fillOval (515,47,20,20); // Middle dot
          b.fillOval (490,75,20,20); // Bottom-left dot
          b.fillOval (540,75,20,20); // Bottom right dot
          
          // Repeats "oneSpaceMovement" method 5 times
          for (int y = 1; y <= 5; y++)
          {
            oneSpaceMovement (positionX, positionY, homeX, homeY, dice, startingX, startingY, winSpaceX, winSpaceY, PIECESPACING, players, win, x, playerNames);
          }
        }
        
        // Dice roll = 6
        else if (dice == 6)
        {
          b.fillOval (490,25,20,20); // Top-left dot
          b.fillOval (540,25,20,20); // Top-right dot
          b.fillOval (490,49,20,20); // Middle-left dot
          b.fillOval (540,49,20,20); // Middle-right dot
          b.fillOval (490,75,20,20); // Bottom-left dot
          b.fillOval (540,75,20,20); // Bottom right dot
          
          // x's position = x's home position
          if (positionX [x] == homeX [x] && positionY [x] == homeY [x])
          {
            c.println ("You got out of home!");
            
            // Erases piece in home position
            b.setColor (Color.white);
            b.fillOval (homeCirclesX [x],homeCirclesY [x],50,50);
            b.setColor (Color.black);
            b.drawOval (homeCirclesX [x],homeCirclesY [x],50,50);
            
            // Redraws piece in starting position
            b.drawOval (startingX [x], startingY [x], 25, 25);
            
            // Reassigns current position to starting position
            positionX [x] = startingX [x];
            positionY [x] = startingY [x];
            
            c.println ("Roll again!");
            
            // If current turn is player 1, roll again
            if (x == 0)
            {
              x = 1;
            }
            
            // If current turn is player 2, roll again
            else if (x == 1)
            {
              x = 0;
            }
            
          }
          
          // x's position != x's home position
          else 
            // Repeats "oneSpaceMovement" method 6 times
            for (int y = 1; y <= 6; y++)
          {
            oneSpaceMovement (positionX, positionY, homeX, homeY, dice, startingX, startingY, winSpaceX, winSpaceY, PIECESPACING, players, win, x, playerNames);
          }
        }
        
        // x's position = x's winning position, x wins
        if (positionX [x] == 215 && positionY [x] == winSpaceY [x])
        {
          c.println (playerNames [x] + " wins!");
          b.setCursor (12,24);
          b.print (playerNames [x] + " wins!");
          
          // Increments number of wins for x
          wins [x]++;
          x = 3;
          win = true; // Declares the win
        }
      }
      
      // If a win occured, alternating turns loop gets broken
      if (win == true)
      {
        playerMoveLooper = 3;
      }
      
      // If a win has not occured yet, alternating loop gets reset 
      else 
        playerMoveLooper = 0;
    }
    
  } // End of diceRoll method
  
  
  // Method for erasing piece's current position
  public static void pieceRefresh (int [] positionX, int [] positionY, int x)
  {
    b.setColor (Color.white);
    b.fillRect (positionX [x],positionY [x],26,26);
  }
  
  
  // Method for redrawing piece's new position
  public static void pieceRedraw (int [] positionX, int [] positionY, Color [] players, int x)
  {
    b.setColor (players [x]);
    b.fillOval (positionX [x], positionY [x], 25, 25);
    b.setColor (Color.black);
    b.drawOval (positionX [x], positionY [x], 25, 25);
  }
  
  
  // Method for moving piece when up is considered forward
  public static void upMovement (int [] positionX, int [] positionY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space up
    positionY [x] -= PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of upMovement method
  
  
  // Method for moving piece when down is considered forward
  public static void downMovement (int [] positionX, int [] positionY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space down
    positionY [x] += PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of downMovement method
  
  
  // Method for moving piece when left is considered forward
  public static void leftMovement (int [] positionX, int [] positionY, int [] homeX, int [] homeY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space left
    positionX [x] -= PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of leftMovement method
  
  
  // Method for moving piece when right is considered forward
  public static void rightMovement (int [] positionX, int [] positionY, int [] homeX, int [] homeY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space right
    positionX [x] += PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of rightMovement method
  
  
  // Method for turning the corner from the blue area to the red area
  public static void blueToRedCornerMovement (int [] positionX, int [] positionY, int [] homeX, int [] homeY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space after blue-to-red corner
    positionX [x] -= PIECESPACING;
    positionY [x] -= PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of blueToRedMovement method
  
  
  // Method for turning the corner from the red area to the green area
  public static void redToGreenCornerMovement (int [] positionX, int [] positionY, int [] homeX, int [] homeY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space after red-to-green-corner
    positionX [x] += PIECESPACING;
    positionY [x] -= PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of redToGreenMovement method
  
  
  // Method for turning the corner from the green area to the yellow area
  public static void greenToYellowCornerMovement (int [] positionX, int [] positionY, int [] homeX, int [] homeY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space after green-to-yellow-corner
    positionX [x] += PIECESPACING;
    positionY [x] += PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of greenToYellowCornerMovement
  
  
  // Method for turning the corner from the yellow area to the blue area
  public static void yellowToBlueCornerMovement (int [] positionX, int [] positionY, int [] homeX, int [] homeY, final int PIECESPACING, Color [] players, int x)
  {
    pieceRefresh (positionX, positionY, x);
    
    // Reassigns and redraws x's position to one space after yellow-to-blue corner
    positionX [x] -= PIECESPACING;
    positionY [x] += PIECESPACING;
    
    pieceRedraw (positionX, positionY, players, x);
  } // End of yellowToBlueCornerMovement method
  
  
  // Base method for moving one space forward
  public static void oneSpaceMovement (int [] positionX, int [] positionY, int [] homeX, int [] homeY, int dice, int [] startingX, int [] startingY,
                                       int winSpaceX, int [] winSpaceY, final int PIECESPACING, Color [] players,boolean win, int x, String [] playerNames)
  {
    int [] cornerTurnsX = new int [] {186, 12, 157, 244, 418, 273}; // Each main corner turn's X-coordinated position ({blue 1/green 2, red 2, red 1, blue 2/green 1, yellow 2, yellow 1})
    int [] cornerTurnsY = new int [] {273, 244, 186, 12, 157, 418}; // Each main corner turn's X-coordinated position ({blue 1, red 2/yellow 1, red 1/yellow 2, green 2, green 1, blue 2)}
    
    int [] winningAreaTurnX = new int [] {215, 12, 418}; // Each colour's X-coordinated position to enter the winning area {blueX/greenX, redX, yellowX}
    int [] winningAreaTurnY = new int [] {418, 215, 12}; // Each colour's Y-coordinated position to enter the winning area {blueY, redY/yellowY, greenY}
    
    int xTemp; // Temporary variable for x
    
    // ---------------------------------------------------------------- BLUE AREA ----------------------------------------------------------------
    // x's position = yellow-to-blue area corner, piece moves one space up
    if (positionX [x] == cornerTurnsX [3] && positionY [x] >= cornerTurnsY [0] && positionY [x] < cornerTurnsY [5])
    {
      downMovement (positionX, positionY, PIECESPACING, players, x);
    }
    
    // Green's position between blue corner 1 and 2, piece moves one space left
    else  if (positionX [1] <= cornerTurnsX [3] && positionX [1] > cornerTurnsX [0] && positionY [1] == cornerTurnsY [5])
    {
      xTemp = x; // Saves the current turn
      x = 1;
      leftMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
      x = xTemp;
    }
    
    // blue's position between blue corner 1 and blue winning area position, piece moves one space left
    else if (positionX [0] == cornerTurnsX [3] && positionY [0] == cornerTurnsY [5])
    {
      xTemp = x; // Saves the current turn
      x = 0;
      leftMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
      x = xTemp;
    }
    
    // blue's position = blue winning area position, piece moves one space up
    else if (positionX [0] == winningAreaTurnX [0] && positionY [0] == winningAreaTurnY [0])
    {
      xTemp = x; // Saves the current turn
      x = 0;
      upMovement (positionX, positionY, PIECESPACING, players, x);
      x = xTemp;
    }
    
    // blue's position after blue winning area position, piece moves one space up
    else if (positionX [x] == winningAreaTurnX [0] && positionY [x] <= startingY [0] && positionY [x] > winSpaceY [0])
    {
      upMovement (positionX, positionY, PIECESPACING, players, x);
      
      // Fills in blue's winning area colour
      b.setColor (Color.blue);
      b.fillRect (positionX [x],positionY [x] + PIECESPACING, 27, 27);
    }
    
    // x's position between blue corner 2 and blue-to-red corner, piece moves one space up 
    else if (positionX [x] == cornerTurnsX [0] && positionY [x] > cornerTurnsY [0] && positionY [x] <= cornerTurnsY [5])
    {
      // x's position = blue starting position
      if (positionX [x] == startingX [0] && positionY [x] == startingY [0])
      {
        upMovement (positionX, positionY, PIECESPACING, players, x);
        
        // Fills in blue's starting position colour
        b.setColor (Color.blue);
        b.fillRect (positionX [x],positionY [x] + PIECESPACING, 27, 27);
      }
      
      // x's position != blue, starting position
      else
        upMovement (positionX, positionY, PIECESPACING, players, x);
    }
    
    // x's position = blue-to-red corner, piece turns corner
    else if (positionX [x] == cornerTurnsX [0] && positionY [x] == cornerTurnsY [0])
    {
      blueToRedCornerMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    // ---------------------------------------------------------------- RED AREA ----------------------------------------------------------------
    // x's position between blue-to-red area corner & red corner 1, piece moves one space left
    else if (positionX [x] > cornerTurnsX [1] && positionX [x] <= cornerTurnsX [2] && positionY [x] == cornerTurnsY [1])
    {
      leftMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    // x's position between red corner 1 & 2, piece moves one space up
    else if (positionX [x] == cornerTurnsX [1] && positionY [x] <= cornerTurnsY [1] && positionY [x] > cornerTurnsY [2])
    {
      upMovement (positionX, positionY, PIECESPACING, players, x);
    }
    
    // x's position between red corner 2 & red-to-green area corner, piece moves one space right
    else if (positionX [x] >= cornerTurnsX [1] && positionX [x] < cornerTurnsX [2] && positionY [x] == cornerTurnsY [2])
    {
      // x's position = red starting position
      if (positionX [x] == startingX [2] && positionY [x] == startingY [2])
      {
        rightMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
        
        // Fills in red's starting position colour
        b.setColor (Color.red);
        b.fillRect (positionX [x] - PIECESPACING, positionY [x], 27, 27);
      }
      
      // x's position != red starting position
      else
        rightMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    // x's position = red-to-green area corner, piece turns corner
    else if (positionX [x] == cornerTurnsX [2] && positionY [x] == cornerTurnsY [2])
    {
      redToGreenCornerMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    
    // ---------------------------------------------------------------- GREEN AREA ----------------------------------------------------------------
    // x's position between red-to-green area corner and green corner 1, piece moves one space up
    else if (positionX [x] == cornerTurnsX [0] && positionY [x] < cornerTurnsY [2] && positionY [x] > cornerTurnsY [3])
    {
      upMovement (positionX, positionY, PIECESPACING, players, x);
    }
    
    // Blue's position between green corner 1 & 2, piece moves one space right
    else if (positionX [0] >= cornerTurnsX [0] && positionX [0] < cornerTurnsX [3] && positionY [0] == cornerTurnsY [3])
    {
      xTemp = x; // Saves the current turn
      x = 0;
      rightMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
      x = xTemp;
    }
    
    // Green's position between green corner 1 & green winning area position, piece moves one space right
    else if (positionX [1] == cornerTurnsX [0] && positionY [1] == cornerTurnsY [3])
    {
      xTemp = x; // Saves the current turn
      x = 1;
      rightMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
      x = xTemp;
    }
    
    // Green's position = green winning area position, piece moves one space down
    else if (positionX [1] == winningAreaTurnX [0] && positionY [1] == winningAreaTurnY [2])
    {
      xTemp = x; // Saves the current turn
      x = 1;
      downMovement (positionX, positionY, PIECESPACING, players, x);
      x = xTemp;
    }
    
    // Green's position between green winning area position & green winning space, piece moves one space down
    else if (positionX [x] == winningAreaTurnX [0] && positionY [x] >= startingY [1] && positionY [x] < winSpaceY [1])
    {
      downMovement (positionX, positionY, PIECESPACING, players, x);
      
      // Fills in green's winning area colour
      b.setColor (Color.green);
      b.fillRect (positionX [x],positionY [x] - PIECESPACING, 27, 27);
    }
    
    // x's position between green corner 2 & green-to-yellow area corner, piece moves one space down
    else if (positionX [x] == cornerTurnsX [3] && positionY [x] >= cornerTurnsY [3] && positionY [x] < cornerTurnsY [4])
    {
      // x's position = green starting position
      if (positionX [x] == startingX [1] && positionY [x] == startingY [1])
      {
        downMovement (positionX, positionY, PIECESPACING, players, x);
        
        // Fills in green's starting position colour
        b.setColor (Color.green);
        b.fillRect (positionX [x], positionY [x] - PIECESPACING, 27, 27);
      }
      
      // x's position != green starting position
      else
        downMovement (positionX, positionY, PIECESPACING, players, x);
    }
    
    // x's position = green-to-yellow area corner, piece turns corner
    else if (positionX [x] == cornerTurnsX [3] && positionY [x] == cornerTurnsY [4])
    {
      greenToYellowCornerMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    
    // ---------------------------------------------------------------- YELLOW AREA ----------------------------------------------------------------
    // x's position between green-to-yellow area corner & yellow corner 1, piece moves one space right
    else if (positionX [x] >= cornerTurnsX [5] && positionX [x] < cornerTurnsX [4] && positionY [x] == cornerTurnsY [2])
    {
      rightMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    // x's position between yellow corner 1 & 2, piece moves one space down
    else if (positionX [x] == cornerTurnsX [4] && positionY [x] >= cornerTurnsY [2] && positionY [x] < cornerTurnsY [1])
    {
      downMovement (positionX, positionY, PIECESPACING, players, x);
    }
    
    // x's position between yellow corner 2 & yellow-to-blue area corner, piece moves one space left
    else if (positionX [x] <= cornerTurnsX [4] && positionX [x] > cornerTurnsX [5] && positionY [x] == cornerTurnsY [1])
    {
      // x's position = yellow starting position
      if (positionX [x] == startingX [3] && positionY [x] == startingY [3])
      {
        leftMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
        
        // Fills in yellow's starting position colour
        b.setColor (Color.yellow);
        b.fillRect (positionX [x] + PIECESPACING, positionY [x], 27, 27);
      }
      
      // x's position != yellow starting position
      else
        leftMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    // x's position = yellow-to-blue area corner, piece turns corner
    else if (positionX [x] == cornerTurnsX [5] && positionY [x] == cornerTurnsY [1])
    {
      yellowToBlueCornerMovement (positionX, positionY, homeX, homeY, PIECESPACING, players, x);
    }
    
    // Redraws both pieces in case one passes another to ensure it doesn't dissapear
    for (int y = 0; y <= 1; y++)
    {
      // Redraws piece
      b.setColor (players [y]);
      b.fillOval (positionX [y], positionY [y], 25, 25);
      
      // Piece outlines
      b.setColor (Color.black);
      b.drawOval (positionX [y], positionY [y], 25, 25);
    }
    
  } // End of oneSpaceMovement method
  
} // ssalC