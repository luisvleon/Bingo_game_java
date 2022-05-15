/*
		 Author:		Luis Leon
		 				Marcelo Calle
		 Course:		COP2800 Lab section
		 Date:			3/30/2019 

		 Assigment: 	Final Project - PT2
		 Description:	Winnable - interface class
		 Instructor:	Dr. Sergio Pisano
 */	
public interface Winnable {
	//checkWinPattern interface method - Receives the random Bingo Balls
	//to check if it matches with the player's bingo card
	public boolean checkWinPattern(String ball);	
}