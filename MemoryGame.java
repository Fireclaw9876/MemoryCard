package SpringFinal2023;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.Timer;

import Spring2023.Card;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
	
public class MemoryGame {
	// setting up image variables
	Image startScreenImage, backgroundImage, loseImage, winImage, timerImg, movesImg;
	
	// screen information
	final int topPanelSize = 35;
	final int W_SIZE = 600, textHeight = 35;
	final int textSize = 60, textY = topPanelSize, timerTextX = 5, movesTextX = 400;
	
	// variables depend on user input
	int cardsPerRow;
	int NUMCARDS, numMatch;
	
	// variable to count number of matches made 
	int totalMatchcards;
	
	// array list to store the cards
	private ArrayList <MemoryCard> cards = new ArrayList <>();
	private int [] cardPic;
	
	// variables to determine locations of cards
	int initialX, initialY, cardSize;
	
	// array list to store the selected cards information
    private ArrayList <MemoryCard> selectedCards = new ArrayList <> ();
    
    boolean checkForMatch = false;
    
	// timer information before the cards flips over
	private int TIMEGAP;
	Timer timer;
	
	// timer information for the overall card game
	private int timerCount, secondTime, timeAlloted, moveCount;
	Timer bigTimer;
	
	// booleans to control screens
	boolean thacherOn, celebrityOn, thacherScreenOn, celebrityScreenOn;
	boolean startScreenOn, menuScreenOn, endScreenOn;
	boolean win, lose;
	
	// image variables for cards
	Image frontImage, backImage, correctImage;
	// variables for the name of the cards
	String frontImgName, backImgName, correctImgName;
	
	// no magic number, for aesthetic purposes and sizing
	int levelButtonWidth = 50, levelButtonHeight = 30;
	int easyX = (int) W_SIZE/3 - 25, mediumX = easyX + 100, difficultX = mediumX + 100;
	int levelsY = W_SIZE - 200;
	int startTextLength = 50;
	
	// button variables 
	JButton thacherButton, celebrity;

    public void setup() {
    	// initializes with impossible numbers to ensure while statement works
    	NUMCARDS = -1;
    	numMatch = -1;
    	
    	// Check if the bigTimer is running, and stop it if necessary
    	if (bigTimer != null && bigTimer.isRunning()) {
    	    bigTimer.stop();
    	}
    	
    	// start information
    	JOptionPane.showMessageDialog(null, "Welcome to Memory Game.\nMake sure to input WHOLE NUMBERS.");
    	
    	// will exit code if input is not an integer
    	// makes sure input is a square
    	while (!validateSquare(NUMCARDS)) {
    		NUMCARDS = Integer.parseInt( JOptionPane.showInputDialog("Input Number of Cards Desired: (Perfect Squares Only)"));		
    	}
    	
    	// will exit code if input is not an integer
    	// makes sure input allows for limited 8 image options through boolean
    	while (!validateMatchInput(numMatch, NUMCARDS)) {
    		numMatch = Integer.parseInt( JOptionPane.showInputDialog("Input Number of Matches Desired:\nMust be a factor of card number\nMust be greater than or equal to the number of cards divided by 8"));
    	}
    	
    	// start screen 
    	startScreenOn = true;
    	cardsPerRow = (int) Math.sqrt(NUMCARDS);
    	
    	cardPic = new int [NUMCARDS];
    	
    	// clears the arraylists
    	selectedCards.clear();
    	cards.clear();
    	
    	// resets the array to 0
    	for (int i = 0; i < cardPic.length; i++) {
    		cardPic[i] = 0;
    	}
    	
    	// every other screen should be off
    	thacherOn = false;
    	celebrityOn = false;
    	thacherScreenOn = false;
    	celebrityScreenOn = false;
    	endScreenOn = false;
    	menuScreenOn = false;
    	win = false;
    	lose = false;
    	
    	// setting the numbers in the game to zero
    	secondTime = 0;
    	moveCount = 0;
    	totalMatchcards = 0;
    	
    	// initializing timer variables
    	timerCount = 1000;
    	
    	// loading the images into variable
    	try {
            startScreenImage = ImageIO.read(new File("CardImages/startScreenImg.png"));
            backgroundImage = ImageIO.read(new File("CardImages/springBackground.png"));
    		loseImage = ImageIO.read(new File ("CardImages/lostImage.png"));
    		winImage = ImageIO.read(new File ("CardImages/winImage.png"));
    		timerImg = ImageIO.read(new File ("CardImages/timer.png"));
    		movesImg = ImageIO.read(new File ("CardImages/moves.png"));
          } catch (IOException e) {
              e.printStackTrace();
          }
    }
    
    public boolean validateSquare (int input) {
    	// finding the square root of given number 
    	double sq = Math.sqrt(input); 

    	// Math.floor() returns closest integer value
    	return ((sq - Math.floor(sq)) == 0);
    }
    
    // makes sure matchInput is a factor and is greater than number of cards divided by 8 (8 because my limited image choices)
    public boolean validateMatchInput (int matchInput, int cardInput) {
    	if ((cardInput * cardInput) % matchInput == 0 && cardInput/matchInput > 0 && cardInput/matchInput <= 8) 
    		return true;
    	
    	return false;
    }
    
    public void setUpCards () {
    	// intial values to draw the card
    	initialX = 100;
        initialY = 100;
        
        // determining the size based on the window size for aesthetics
        cardSize = (W_SIZE - (2 * initialX)) / cardsPerRow;
        int xPicLocation = initialX;
        
        // initializing this boolean to false 
        checkForMatch = false;
    	
        // setting each card picture an integer based on the the numMatch variable
        for (int i = 0; i < NUMCARDS; i++) {
            cardPic[i] = i / numMatch + 1;
        }
        
        // calls on method to randomize the card image locations
        randomizeCard();
    	
        // inputs the card picture number into a string 
        for (int i = 0; i < NUMCARDS; i++) {
            if (thacherOn) {
                frontImgName = thacherEdition() + "card" + cardPic[i] + ".png";
                backImgName = thacherEdition() + "back.png";
                correctImgName = thacherEdition() + "correctImg.png";
            } else {
                frontImgName = celebrityEdition() + "image" + cardPic[i] + ".png";
                backImgName = celebrityEdition() + "imageBack.png";
                correctImgName = celebrityEdition() + "correctImage.png";
            }
            
            try {
                frontImage = ImageIO.read(new File(frontImgName));
                backImage = ImageIO.read(new File(backImgName));
                correctImage = ImageIO.read(new File(correctImgName));
                
                // call on memory card constructor to build a card
                MemoryCard card = new MemoryCard(xPicLocation, initialY, cardSize, cardSize, frontImgName, backImgName, correctImgName);    
                // adds the card to the array list
                cards.add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // every card, moves right a bit
            xPicLocation += cardSize;

            // new row
            if (cards.size() % cardsPerRow == 0) {
                initialY += cardSize;
                xPicLocation = initialX;
            }
        }
        
    }
    
    public void prepareCardGame() {
    	// turns the levels screen off
    	menuScreenOn = false;
    	
    	// calls on the method to prepare cards
    	setUpCards();
    	
    	// draws the screen based on the edition the user chose
    	if (thacherOn) {
    		thacherScreenOn = true;
    	}
    	else if (celebrityOn) {
    		celebrityScreenOn = true;
    	}
    	
    	// sets a big timer counting by second
		bigTimer = new Timer(timerCount, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	cardTimerAction();
            }
        });
		
        // starts the timer
		bigTimer.start();
    }
    
    // what happens when timer runs out for the card game
    public void cardTimerAction() {
       	// the seconds number is displayed through this variable 
    	secondTime++;
    	
    	// counting the number of total matches to determine win or loss
    	if (totalMatchcards == NUMCARDS/numMatch) {
    		win = true;
    		
    		thacherScreenOn = false;
    		celebrityScreenOn = false;
    		
        	bigTimer.stop();
    	}
    	
    	//  if user takes too long, lose the game
    	if (secondTime == timeAlloted) {
    		lose = true;
    		thacherScreenOn = false;
    		celebrityScreenOn = false;
    		
        	bigTimer.stop();
    	}
    }
    
    // changes the big game timer to two minutes and 0.5 seconds for card to flip
    public void easyMode() {
    	timeAlloted = 120;
    	TIMEGAP = 500;
    	prepareCardGame(); 
    }
    
    // changes the big game timer to 1 minutes and 0.4 seconds for card to flip 
    public void mediumMode() {
    	timeAlloted = 60;
    	TIMEGAP = 400;
    	prepareCardGame(); 
    }
    
    // changes the big game timer to 30 seconds and 0.2 seconds for card to flip 
    public void difficultMode() {
    	timeAlloted = 30;
    	TIMEGAP = 200;
    	prepareCardGame(); 
    }
	
	public void randomizeCard() {
		for (int i = 0; i < NUMCARDS; i++) {
			// randomize the location of image information in the array
			Random random = new Random();
			int x = random.nextInt(NUMCARDS);
			int temp = cardPic[i];
			
			cardPic[i] = cardPic[x];
			cardPic[x] = temp;
		}
	}
	
	public void reset() {
		// runs set up method to reset
		setup();
	}
	
	public String thacherEdition() {
		return "CardImages/";
	}
	
	public String celebrityEdition() {
		return "CelebrityImages/";
	}
	
	// what you want to happen at the moment when the mouse is first pressed down.
	public void mousePressed(int mouseX, int mouseY) {
		// only happens when the card game is being played
		if (thacherScreenOn || celebrityScreenOn) {
	        if (checkForMatch)
	            return; // Wait for previous cards to be checked
	        
	        for (MemoryCard card : cards) {
	        	// flips the card by adding it to the arraylist
	            if (card.contains(mouseX, mouseY) && !card.isFlipped() && !card.isMatched()) {
	                selectedCards.add(card);
	                card.setFlipped(true);
	                
	                // only allows the flipping of those cards
	                if (selectedCards.size() == numMatch) {
	                    checkForMatch = true;
	                    
	                    // starts timer before it flips back 
	                    timer = new Timer(TIMEGAP, new ActionListener() {
	                        @Override
	                        public void actionPerformed(ActionEvent e) {
	                            checkMatch();
	                            timer.stop();
	                        }
	                    });
	                    
	                    timer.start();
	                }
	            }
	        }
		}
		
		// determining levels by the buttons on the level menu screen 
		int mousePressX = mouseX;
		int mousePressY = mouseY;
		
		if (menuScreenOn) {
		    // what you want to happen when the mouse is clicked
		    if ((mousePressX >= easyX) && (mousePressX <= easyX + levelButtonWidth) && (mousePressY >= levelsY) && (mousePressY <= levelsY + levelButtonHeight)) {
		    	easyMode();
		    }
		    else if ((mousePressX >= mediumX) && (mousePressX <= mediumX + levelButtonWidth) && (mousePressY >= levelsY) && (mousePressY <= levelsY + levelButtonHeight)) {
		   		mediumMode();
		   	}
		   	else if ((mousePressX >= difficultX) && (mousePressX <= difficultX + levelButtonWidth) && (mousePressY >= levelsY) && (mousePressY <= levelsY + levelButtonHeight)) {
		   		difficultMode();
	    	}
		}
	}

	// method checks if the image is the same for all of the selected cards
	public void checkMatch() {	
		int matchCount = 0;
		
		for (int i = 0; i < selectedCards.size(); i++) {
			if (selectedCards.get(i).getFrontImgName().equals(selectedCards.get(0).getFrontImgName())) {	
				matchCount++;
			}
			
			// only matched if all cards are the same
			if (matchCount == numMatch) {	
				// need an embedded for loop to go through
				for (int j = 0; j < numMatch; j++) {
					selectedCards.get(j).setMatched(true);
				}
				
				totalMatchcards++;
			}
			// flips back 
			else {
				selectedCards.get(i).setFlipped(false);
			}
		}	
		
		// clears the arraylist, resets boolean, adds to move count
		selectedCards.clear();
		checkForMatch = false;
		moveCount++;
	}

	
    // draws everything
    public void draw(Graphics g) {
    	if (startScreenOn) {
    		g.drawImage(startScreenImage, 0, 0, W_SIZE, W_SIZE, null);
    	}
    	
    	if (menuScreenOn) {
    		drawStartMenu(g);
    	}
    	
    	if (lose) {
    		g.drawImage(loseImage, 0, 0, W_SIZE, W_SIZE, null);
    	}
    	
    	if (win) {
    		g.drawImage(winImage, 0, 0, W_SIZE, W_SIZE, null);
    	}
    	
    	if (thacherScreenOn || celebrityScreenOn) {
        	// draws background
    		g.drawImage(backgroundImage, 0, 0, W_SIZE, W_SIZE, null);
    		
    		// draws timer text 
    		g.drawImage(timerImg, timerTextX, textY, textSize, textSize, null);
    		
    		g.setColor(Color.white);
    		g.setFont(new Font("Courier New", Font.BOLD, textSize));
    		g.drawString(String.valueOf(secondTime), timerTextX + textSize, textY + 40);
    		
    		// draws moves text
    		g.drawImage(movesImg, movesTextX, textY, textSize, textSize, null);
    		
    		g.setColor(Color.white);
    		g.setFont(new Font("Courier New", Font.BOLD, textSize));
    		g.drawString(String.valueOf(moveCount), movesTextX + textSize, textY + 40);
    		
    		// draws cards
    		for (MemoryCard card : cards) {
                card.draw(g);
            }
    	}
    }
    
	public void drawStartMenu(Graphics g) {
		// draws background
		g.drawImage(backgroundImage, 0, 0, W_SIZE, W_SIZE, null);
		
		// draws the level text
		g.setColor(Color.white);
		g.drawString("Level: ", W_SIZE/2 - startTextLength/2, levelsY - 2 * startTextLength);

		// Rectangles for Buttons
		g.setColor(Color.white);
		g.fillRect(easyX, levelsY, levelButtonWidth, levelButtonHeight);
		g.drawString("Easy", easyX, levelsY - startTextLength/2);
		
		g.setColor(Color.white);
		g.fillRect(mediumX, levelsY, levelButtonWidth, levelButtonHeight);
		g.drawString("Medium", mediumX, levelsY - startTextLength/2);
		
		g.setColor(Color.white);
		g.fillRect(difficultX, levelsY, levelButtonWidth, levelButtonHeight);
		g.drawString("Difficult", difficultX, levelsY - startTextLength/2);
	}

    
    // filler code from angry birds and button help from friedman
	public MemoryGame() {
		setup();
		
		JFrame frame = new JFrame();
		
		JPanel container = new JPanel();
		
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(W_SIZE, topPanelSize));
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				reset();
			}
			
		});
		
		thacherButton = new JButton("Thacher Edition");
		thacherButton.addActionListener(new ActionListener() {
			// what happens when button is pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// makes sure no other button is already pressed
				if (!thacherOn && !celebrityOn) {
					thacherOn = true;
					startScreenOn = false;
					menuScreenOn = true;
				}
			}
			
		});
		
		celebrity = new JButton("Celebrity Edition");
		celebrity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// makes sure no other button is already pressed
				if (!thacherOn && !celebrityOn) {
					celebrityOn = true;
					startScreenOn = false;
					menuScreenOn = true;
				}
			}
			
		});
		
		// adds the buttons onto the top panel
		topPanel.add(resetButton);
		topPanel.add(thacherButton);
		topPanel.add(celebrity);
		
		frame.setSize(W_SIZE, W_SIZE + topPanelSize);
		JPanel canvas = new JPanel() {
			public void paint(Graphics g) {
				draw(g);
			}
		};
		canvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				MemoryGame.this.mousePressed(e.getX(),e.getY());	
			}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		canvas.setPreferredSize(new Dimension(W_SIZE, W_SIZE));
		
		container.add(topPanel);
		container.add(canvas);
		
		frame.add(container);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while (true) {
			canvas.repaint();
			
			try {Thread.sleep(20);} 
			catch (InterruptedException e) {}
			
		}
	}
	
	public static void main(String[] args) {
		new MemoryGame();
	}
}