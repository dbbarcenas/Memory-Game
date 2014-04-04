package memoryGame;
/*
 * Author: Durwin Barcenas
 * Author: Jimmy jingXian Han
 * Date: December 6, 2013
 */
import java.util.Timer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.util.Random;
import java.util.TimerTask;



public class Card implements ActionListener {
	
	//AWT 
	private JPanel gamePanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel scorePanel = new JPanel();
	//SWING
	private JFrame frame = new JFrame("Memory Game");
	private static final int WINDOW_WIDTH = 500; // pixels
    private static final int WINDOW_HEIGHT = 500; // pixels
    private JButton exitButton, replayButton;
    private JButton[] gameButton = new JButton[16];
    //sets a default imageIcon 
    public ImageIcon facedown = new ImageIcon(getClass().getResource("images//cardback.png"));
	public int selectedCard1 = -1;
	public int selectedCard2 = -1;
	//Miss i made this array simpler because I had trouble generating 52 random cards and matching them up. 
	//So i have made an array that matches instead. To make it more simpler.
    private final String[] arrayCardName = {
			"card_1d.png", "card_1d.png",  
		   "card_7s.png", "card_7s.png",
		   "card_8d.png", "card_8d.png",
		   "card_9s.png", "card_9s.png",
		   "card_10d.png", "card_10d.png",
		   "card_11s.png", "card_11s.png",
		   "card_12d.png", "card_12h.png",
		   "card_13s.png", "card_13s.png",
	};
  
    public int[] cardDeck = new int[16]; //array card deck 
    public int score = 0; 	
    public Timer gameTimer = new Timer();
    public int startTime;
    private JTextField ScoreLabel, TimerLabel, Label ;
    
    
    //Method Constructor 
	public Card() {
		gui();
		panels();
        frame.setTitle("MemoryGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setVisible(true);
        
	}
	
	/*
	 * Gui method that sets the buttons and textFields
	 */
	public void gui()
	{
		//grid layout of the buttons
		for (int i = 0; i < gameButton.length; i++)
		{
			//sets a default imageIcon for the buttons
			gameButton[i] = new JButton(facedown); 
			
			gameButton[i].setName(String.valueOf(i));
			gameButton[i].addActionListener(this);
		}// end of grid layout button
		//GUI TextFields
		ScoreLabel = new JTextField("Score: " + ScoreLabel);
		TimerLabel = new JTextField("Time: 0:" + TimerLabel); 
		Label = new JTextField("Pick a card, any card.");
		//GUI buttons
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		replayButton = new JButton("Replay");
		replayButton.addActionListener(this); 
		//called shuffleDeck method 
		shuffleDeck();
		//gameTimer(); 
	}
	
	/*
	 * This method shuffles the deck 
	 */
	public void shuffleDeck()
	{
		int counter;
		for (counter=0; counter<16; counter++) {
			cardDeck[counter] = counter;
		}
		Random rnd = new Random();
		for (counter=0; counter<16; counter++) {
			int index = rnd.nextInt(16);
			int temp = cardDeck[counter];
			cardDeck[counter] = cardDeck[index];
			cardDeck[index] = temp;
		}

		//Sets the time to 60 
		startTime = 60;
		//sets the score to 0
		score = 0; 
		//New gameTimer
		gameTimer = new Timer();
		gameTimer.scheduleAtFixedRate(new TimerTask() {  
			//delays the updateTimer method 
		    @Override
		    public void run() {
		    	updateTimer();       
		    }
		}, 1000, 1000);
		//updateScore panel called to update
		updateScorePanel();
	}
	
	/*
	 * this method panel lays out the grids of the buttons and labels
	 */
	public void panels(){
		//GridLayout of the buttons 
		gamePanel.setLayout(new GridLayout(4, 4));
		for (int i = 0; i < gameButton.length; i++){
			gamePanel.add(gameButton[i]); 
		}
		//adding button panels
		buttonPanel.add(replayButton); 
		buttonPanel.add(exitButton);
		
		scorePanel.add(Label);
		scorePanel.add(ScoreLabel);
		scorePanel.add(TimerLabel); 
		
		scorePanel.setLayout(new GridLayout(1,0));
		buttonPanel.setLayout(new GridLayout(1,0));
		//this positions the panels 
		frame.add(scorePanel, BorderLayout.NORTH);
		frame.add(gamePanel, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	/*
	 * This method checks the deck
	 */
	public boolean checkFinished() 
	{
		for (int c=0; c<16; c++) { //for loop
			if (cardDeck[c] != -1)  //if carddeck array is not -1 returns false
				return false;
		}//end for loop
		return true; 
	}
	
	/*
	 * This method calls the checkFinished method to check if the game is finished
	 */
	public void completeCheck()
	{
		if (checkFinished()) { 
			completeGameLabel(); //calls this method to change the TextField Label
			gameTimer.cancel(); //stops the timer 
			JOptionPane.showMessageDialog(null, "Congrats! You have succeded in completing the game"); // dialog that pops up when game is completed.
			
		}//end if statement
	}//end of completeCheck method
	
	/*
	 * This method updates the buttons 
	 */
	public void updateAllCards()
	{
		int counter;
		for (counter=0; counter<16; counter++) {
			JButton button = gameButton[counter]; //initializes the gameButton array to button
			int cardindex = cardDeck[counter]; // initializes the cardDeck array to cardindex
			if (cardindex != -1) { // if card index is not equal -1 do the if statement bellow
				if (counter == selectedCard1 || counter == selectedCard2) {  //if counter equals selectedcard1 or counter == selectedcard2
					int cardIndex = cardDeck[counter];	//sets the cardDeck into cardIndex 
	            	//flips the cardbutton to faceup 
	            	ImageIcon faceup = new ImageIcon(getClass().getResource("images//" + arrayCardName[cardIndex]));
	            	button.setIcon(faceup);
				}//end inner if
				else { //else stay facedown 
					button.setIcon(facedown);
				}//end else
			}//end outer if
		}//end for loop
		
		if (selectedCard2 != -1) {
			
			int card1 = cardDeck[selectedCard1];
			int card2 = cardDeck[selectedCard2];
			
			if ((card1/2) == (card2/2)) {
				
				cardDeck[selectedCard1] = -1;
				cardDeck[selectedCard2] = -1;
				score++; 
				
				completeCheck();
				updateScorePanel(); 
				
				Label.setText("You have chosen pairs!");
			} else { //end else
				new Timer().schedule(new TimerTask() {          
				    @Override
				    public void run() {
				    	updateAllCards();       
				    }
				}, 1000);
			} //end else
			
			selectedCard1 = -1;
	    	selectedCard2 = -1;
		}//end outer if statement
	}//end of updateAllCards method
	
	/*
	 * This method when clicked will replay the whole application
	 */
	public void replay() 
	{
		selectedCard1 = -1;
    	selectedCard2 = -1;
    	gameTimer.cancel(); //cancels the timer 
    	shuffleDeck(); // called method to shuffle the deck again
    	updateAllCards(); // updates the all cards
    	score = 0; 	// sets the score back to 0 
	}//end of replay method
	
	/*
	 * This method actionPerformed is an button listener when click activate the button's functions
	 */
	public void actionPerformed(ActionEvent e)
    {
			// this statement will exit the application
            if (exitButton == e.getSource())
            {
                System.exit(0);
            } else if (replayButton == e.getSource())//statement will call the replay method that will reset the game
            {
              	replay();
              
            } else {	// else statement game card buttons functions 
            	
            	JButton clickedButton = (JButton)e.getSource();
            	int buttonID = Integer.valueOf(clickedButton.getName());
            
            	if (selectedCard1 == -1) {
            		selectedCard1 = buttonID;
            		Label.setText("Now pick another card.");
            		
            	} else {
            		if (selectedCard1 == buttonID) {
            			selectedCard1 = selectedCard2;
            			selectedCard2 = -1;
            			
            		} else {
	            		if (selectedCard2 == -1) {
	            			selectedCard2 = buttonID;
	            		} else {
	            			selectedCard1 = -1;
	            			selectedCard2 = -1;
	            		}
            		}
            	}
            	updateAllCards();
            }
    }//end of actionPerformed

	/*
	 * This method will update the timer 
	 */
	public void updateTimer()
	{
		TimerLabel.setText("Time: "+ startTime+" sec");
		startTime--; 
		if (startTime < 0) { // if statement is zero then outputs the dialog down bellow 
			JOptionPane.showMessageDialog(null, "You Failed in solving the Memory Game!");
			//cancels the timer
			gameTimer.cancel();
		}
	}//ends the updateTimer
	
	/*
	 * This method updates the score panel when called 
	 */
	public void updateScorePanel(){
		ScoreLabel.setText("Score: "+ score);
		
	}//end of updateScorePanel
	
	/*
	 * This method updates the Label when completed. 
	 */
	public void completeGameLabel(){
		Label.setText("Completed Game!");
	}//end of completeGameLabel

}//end of card class
