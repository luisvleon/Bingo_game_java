/*
		 Author:		Luis Leon
		 				Marcelo Calle
		 Course:		COP2800 Lab section
		 Date:			4/6/2019 

		 Assigment: 	Final Project - PT2
		 Description:	Bingo Game Program - Bingo card class - represents a game card for each player
		 Instructor:	Dr. Sergio Pisano
 */	
public class BingoCard implements Winnable
{
	//Class field - bingoCardArray holds the player table numbers
	private String[][] bingoCardArray = new String [5][5];	
	
	//BingoCard class constructor generates a Bingo Card for each player
	public BingoCard()
	{
		String tempBall;								//temporary variable to hold the generated number								
		String[][] tempArray = new String [5][5];		//Temporary player's game table while is constructed
		
		//Array variable with the number ranges for each column to construct every player's game table
		//Ex: column B needs numbers from 1 to 15, etc
		int[][] rangeArray = new int[][] {{1,15},{16,30},{31,45},{46,60},{61,75}};	

		boolean flag = false;							//flag variable in case the generated number is already into the array

		for(int i = 0; i < 5; i++)						//Populate columns			
		{			
			for(int j = 0; j < 5; j++)					//Populate rows
			{		
				do
				{
					flag=false;										//Sets flag variable to false to reset it
					tempBall = BingoWheel.bingoRandomBall();		//Calls the bingoRandomBall method to get a new bingo ball

					//Checks if the incoming random ball number is in the right column is being generated, if not, ask for a new ball
					if(Integer.parseInt(tempBall.substring(1)) >= rangeArray[i][0] && Integer.parseInt(tempBall.substring(1)) <= rangeArray[i][1])
					{
						for(int g=0; g <5 ; g++)		//iterates the whole column to check if the ball number already was selected
						{
							//Check if the generated number was already selected against every element in the temporary array
							if(tempBall.substring(1).equals(tempArray[i][g]))	
							{
								flag= true;		//Sets the flag to true if the number is already in the player's game table
							}
						}
					}
					else
					{
						flag = true;	//sets the flag variable to true if the random ball is out of the range of the actual column 
					}

				}while(flag==true);		//Keeps asking for a new random ball until the flag variable is sets to false

				if (flag==false) 
				{										//If the random number was not selected then...
					if(i == 2 && j==2)					//If the table array index is the center of the table assign only a X letter
					{
						tempArray[i][j] = " X ";
					}
					else								
					{
						//If the table array index is not the center assign the random number to that index
						tempArray[i][j] = tempBall.substring(1);
					}				
				}	
			}	
		}
		this.setBingoCardArray(tempArray);		//Returns and sets the new player's Bingo Card into the field variable
	}

	//setBingoCardArray method - set the value for the class field
	public void setBingoCardArray(String[][] bingoCard)
	{
		this.bingoCardArray = bingoCard.clone();		
	}

	//getBingoCardArray method - gets the value from the class field
	public String[][] getBingoCardArray()
	{
		return this.bingoCardArray.clone();		
	}	

	//checkWinPattern method - Receive a bingo random ball letter and number and search into the player's game tables for a match 
	public boolean checkWinPattern(String ball)
	{
		boolean result = false;
		String ballLetter;			//Variable to hold only the ball letter
		String ballNumber;			//Variable to hold only the ball number
		
		//Winning pattern X group # 1 included into checkWinPattern method
		int[][] winPattern = new int[][] {{0,0},{1,1},{3,3},{4,4},{0,4},{1,3},{3,1},{4,0}};	
		
		//Gets the field variable BingoCardArray to make comparisons
		String [][] tempCard = this.getBingoCardArray();

		ballLetter = ball.substring(0, 1);		//Gets the random ball letter
		ballNumber = ball.substring(1);			//Gets te random ball number
		int j=10;

		//Depending on the letter number sets the column to work with
		switch(ballLetter)	
		{
		case "B":
			j = 0;
			break;
		case "I":
			j = 1;
			break;
		case "N":
			j = 2;
			break;
		case "G":
			j = 3;
			break;
		case "O":
			j = 4;
			break;
		}

		for(int i = 0; i < 5; i++)
		{
			//Checks if the random number is present in the player's table game
			if(tempCard[j][i].equals(ballNumber))					
			{				
				//Check if the match is inside the winning pattern iterating the array
				for(int g = 0; g < winPattern.length; g++)
				{			
					//Compare columns and rows to verify if the match is into the winning pattern
					if(winPattern[g][1] == j && winPattern[g][0] == i)
					{
						//Mark the ball into the players table with {  }
						tempCard[j][i] = "{" + tempCard[j][i] + "}";
						
						//Returns true if the match coincides with the winning pattern
						result = true;								
					}
				}
			}
		}		

		return result;	//Returns the result true if there is a match and that match is into the winning pattern
	}
}