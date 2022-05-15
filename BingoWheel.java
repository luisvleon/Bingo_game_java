/*
		 Author:		Luis Leon
		 				Marcelo Calle
		 Course:		COP2800 Lab section
		 Date:			4/11/2019 

		 Assigment: 	Final Project - PT2
		 Description:	Bingo Game Program - BingoWheel class - represents the bingo wheel-bowl that generates random bingo balls 
		 Instructor:	Dr. Sergio Pisano
 */	
import java.util.*;		//Imports Scanner, random class
public class BingoWheel 
{
	//Holds the values of called bingo balls
	private static List<String> calledBingoBalls =new ArrayList<>();
	//Class field variables
	private static String[][] allBalls = new String [75][2];			//Holds all the possible bingo balls and their status
	private static boolean flagWheelReady = false;						//Initialization flag variable, true after the game tables were being generated
	private static int calledBalls;										//Accumulator variable - counts how many bingo balls have been generated

	//Class constructor
	public BingoWheel()
	{
		for(int i = 0; i < 75; i++)
		{
			allBalls[i][0] = getLetter(i + 1) + (i + 1);		//Populates the array with all the bingo balls
			allBalls[i][1] = "0";								//populate the status of every ball with zeroes for initialization
		}
	}

	//setCalledBalls method - sets the value for the static field variable ballsList
	public static void setCalledBingoBalls(List<String> list)
	{
		calledBingoBalls = list;
	}

	//getCalledBalls method - gets the value for the class field variable ballsList
	public static List<String> getCalledBingoBalls()
	{
		return calledBingoBalls;
	}

	//setAllBalls method - sets the field variable calledBingoBalls
	public static void setAllBalls(String[][] called)
	{
		allBalls = called.clone();
	}
	//getAllBalls method - get the value from the field variable calledBingoBalls
	public static String[][] getAllBalls()
	{
		return allBalls.clone();
	}

	//setFlagWheelReady method - sets the value to the field variable FlagWheelReady
	public void setFlagWheelReady(boolean flag)
	{
		flagWheelReady = flag;
	}
	//getFlagWheelReady method - gets the status of the field variable FlagWheelReady
	public static boolean getFlagWheelReady()
	{
		return flagWheelReady;
	}

	//setCalledBalls method - sets the value for the static field variable calledBalls
	public static void setCalledBalls()
	{
		calledBalls =calledBalls + 1;
	}

	//getCalledBalls method - gets the value for the class field variable calledBalls
	public static int getCalledBalls()
	{
		return calledBalls;
	}

	//bingoRandomBall method - This method simulates the spinning of a bingo wheel-bowl and returns a random bingo ball in String value format
	public static String bingoRandomBall()
	{
		int num;							//Temporary variable to hold the generated number
		String ball="!";					//Variable to store the random bingo ball 
		int ballNumber;						//Temporary ball number variable
		Random rand = new Random();			//Create an instance of the Random class		

		if(getFlagWheelReady() == true)		//if the bingo tables are already created we can start recording further calls
		{
			String[][] tempBingoBalls = allBalls;					//Gets the field variable array with the whole set of bingo balls to work with
			List<String> leftBallsList = new ArrayList<>();			//Temporary Array List variable to hold only the bingo balls have not been call yet
			int pendingBalls;										//Variable to hold the number of pending bingo balls to be call

			//Copy all the bingo balls that have not been called before to the temporary array to work with
			for(int i = 0; i < tempBingoBalls.length; i++)
			{
				//Copy every ball that has a 0 status into the temporary array
				if(tempBingoBalls[i][1].equals("0"))
				{
					leftBallsList.add(tempBingoBalls[i][0]);
				}
			}

			pendingBalls = leftBallsList.size();						//Store the number of pending bingo balls not been called yet
			if(pendingBalls > 0)
			{
				num = rand.nextInt(pendingBalls);						//Generate the random number between 1 and the number of bingo balls left
				ball = leftBallsList.get(num);							//Assign the integer to the String variable
				ballNumber = Integer.parseInt(ball.substring(1));		//Gets the only the ball number

				System.out.print("\n\tRandom Bingo Ball: " + ball + "\n\n");

				//With the ball number minus 1, sets the ball as called already setting the status value to 1 and enclosing the bingo ball inside { }
				tempBingoBalls[ballNumber-1][0] = "{" + tempBingoBalls[ballNumber-1][0] + "}";
				tempBingoBalls[ballNumber-1][1] = "1";

				calledBingoBalls.add(ball);

				//Output to console the called Bingo Balls as the project guidelines requires from the calledBingoBalls array
				System.out.println("Called Bingo Balls: ");
				if(getCalledBingoBalls().size() < 37)
				{
					for(int i = 0; i < getCalledBingoBalls().size(); i++)
					{
						System.out.print(" " + getCalledBingoBalls().get(i));
					}
					System.out.print("\n");
				}
				else
				{
					//make two lines of output if there are more than 37 bingo balls called
					for(int i = 0; i < 37; i++)
					{
						System.out.print(" " + getCalledBingoBalls().get(i));
					}
					System.out.print("\n");
					for(int i = 37; i < getCalledBingoBalls().size(); i++)
					{
						System.out.print(" " + getCalledBingoBalls().get(i));
					}
					System.out.print("\n");
				}

				//System.out.println("\n\tWin Pattern: X");				//Show win pattern message

				setAllBalls(tempBingoBalls);							//update the calledBingoBalls array

				setCalledBalls();										//Update the accumulator value
			}
		}
		else
		{
			num = rand.nextInt(74) + 1;					//Generate the random number between 1 to 75
			ball = getLetter(num) + num;				//Assign the integer to the String variable	
		}
		return ball;								//Return the value
	}
	//getLetter method - returns a letter depending on the passed number range
	public static String getLetter(int ind)
	{
		String letter = "X";

		if(ind < 16)
		{
			letter = "B";
		}
		else if(ind > 15 && ind < 31)
		{
			letter = "I";
		}
		else if(ind > 30 && ind < 46)
		{
			letter = "N";
		}
		else if(ind > 45 && ind < 61)
		{
			letter = "G";
		}
		else if(ind > 60)
		{
			letter = "O";
		}
		return letter;
	}
}