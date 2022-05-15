/*
		 Author:		Luis Leon
		 				Marcelo Calle
		 Course:		COP2800 Lab section
		 Date:			4/11/2019 

		 Assigment: 	Final Project - PT2
		 Description:	Player class
		 Instructor:	Dr. Sergio Pisano
 */	

public class Player 
{
	private String name;
	private int score;
	
	//Sets an instance object of BingoCard class
	private BingoCard bingoCard = new BingoCard();
	
	//Player class constructor
	public Player(String nam)
	{
		this.setName(nam);			//sets the field variable name
		this.setScore(0);			//Initialize the score for each player to 0
	}

	//setName method - sets the field variable name
	public void setName(String nam)
	{
		this.name = nam;
	}

	//getName method - gets the value from the field variable name
	public String getName()
	{
		return this.name;
	}
	//setscore method - sets the field variable score
	public void setScore(int sco)
	{
		this.score = sco;
	}
	//getScore method - gets the value from the field variable score
	public int getScore()
	{
		return this.score;
	}	
	//getBingoVard method - gets the object from the field object bingoCard
	public BingoCard getBingoCard()
	{
		return this.bingoCard;
	}
	
	//setBingoCard method - Sets field object
	public void setBingoCard(BingoCard bingo)
	{
		this.bingoCard = bingo;
	}
	
	//checkBall method - calls the checkWinPattern and sets the player's scores
	public void checkBall(String ball)
	{
		//Sends the random ball to verify if there is a match
		if(this.bingoCard.checkWinPattern(ball) == true)
		{
			//If the match is inside the winning pattern then rise 1 to the score field variable
			int tempScore = this.getScore() + 1;			
			this.setScore(tempScore);
		}
	}	
}