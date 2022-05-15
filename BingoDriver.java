/*
		 Author:		Luis Leon
		 				Marcelo Calle
		 Course:		COP2800 Lab section
		 Date:			4/21/2019 

		 Assigment: 	Final Project - PT3
		 Description:	Bingo Game Program	- BingoDriver class - Main driver class
		 Instructor:	Dr. Sergio Pisano
 */	
//Import classes section
import java.util.ArrayList;
import java.util.List;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BingoDriver extends Application
{
	//Private variables declarations

	//Controls instances and assignations
	private Label labelPlayer1;												//Shows player's name							
	private Label labelPlayer2;												//Shows player's name
	private Label labelScore1 = null;										//Shows player's score
	private Label labelScore2 = null;										//Shows player's score
	private Label labelLastBallNumber = new Label(" ");						//Shows the last ball called for statistis section
	private Label labelTotalBalls = new Label(" ");							//Shows number of balls called
	private Label labelPattern = new Label("The winning pattern is X");
	private Label titleLabel = new Label("Welcome to B I N G O game!");		//Shows the Application title

	private TextField textName1 = new TextField();							//Text controls for user names input		
	private TextField textName2 = new TextField();

	private Button buttonStart = new Button("Start game");					//Application buttons
	private Button buttonPlay = new Button("Next Round!");
	private Button buttonExit = new Button("  Exit game  ");

	private ImageView tempImageBig = null;									//To process images in different process
	private CheckBox chkSound = new CheckBox("No sounds");					//To control the sound settings

	//Vboxes, HBoxes and GridPane layouts
	private VBox vBoxLeft = new VBox();										//To contain all the objects at the left side
	private VBox vBoxRight = new VBox();									//To contain all the objects at the right side
	private HBox hBoxButtons = new HBox();									//To contain the buttons
	private GridPane gridPlayer1 = new GridPane();							//To hold all the balls in the player bingo card
	private GridPane gridPlayer2 = new GridPane();							//To hold all the balls in the player bingo card
	private GridPane gridWheel = new GridPane();							//To hold all the balls were been called
	private GridPane gridStats = new GridPane();							//To hold all the stats labels
	private GridPane grid = new GridPane();									//GridPane Holds ALL the objects

	private BingoWheel bingoWheel = new BingoWheel();						//Instantiate the BingoWheel class to use its methods
	private boolean noSound = false;										//Variable to hold the sound setting
	private boolean stopPlay = false;										//Variable - If a winner is declared stops the playing process
	private int runNumber=0;												//Variable - holds the number of rounds

	private Player player1;				//Player class instances - to use its methods
	private Player player2;				//Player class instances - to use its methods	

	//Audio clip class instance - to play background sound
	private AudioClip backgroundSound = new AudioClip("file:src/sounds/background.mp3");

	public static void main(String[] args) 
	{	
		launch(args);					//Launch the application
	}	

	//Public start method to set up the stage and scene and controls 
	@Override
	public void start(Stage primaryStage) {

		//Sets containers
		vBoxLeft.getChildren().add(new Label("Player # 1 Name: "));
		vBoxLeft.getChildren().add(new Label("Player # 2 Name: ")); 
		vBoxLeft.setSpacing(25);
		vBoxLeft.setAlignment(Pos.BOTTOM_CENTER);
		vBoxLeft.setMinHeight(250);

		textName1.setMaxWidth(300);
		textName2.setMaxWidth(300);
		vBoxRight.getChildren().add(textName1); 
		vBoxRight.getChildren().add(textName2);
		vBoxRight.setSpacing(15);
		vBoxRight.setAlignment(Pos.BOTTOM_LEFT);
		vBoxRight.setMinHeight(250);

		//Register control events
		buttonStart.setOnAction(new ButtonClickHandlerStartGame());					//Set action on start game control
		chkSound.setOnAction(new handleChkChange());								//Set sound settings

		primaryStage.setTitle("Bingo Game!"); 										// Set the window's title.
		//grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.BASELINE_CENTER);
		titleLabel.setId("title");
		grid.add(titleLabel, 1,1);
		GridPane.setColumnSpan(titleLabel,  3);										//Merge cells for title 

		VBox vBoxText = new VBox(new Text("Please provide the players' names or click the button to use the default names."));
		vBoxText.setAlignment(Pos.CENTER_LEFT);

		//Adding objects to the general container
		grid.add(vBoxText, 1, 2);
		grid.add(vBoxLeft, 0, 2);
		grid.add(vBoxRight, 1, 2);
		grid.add(buttonStart, 1, 3 );

		//Insert a separator for layout
		Image separator = new Image("\\balls\\separator.png");			
		ImageView imgSeparator = new ImageView(separator);		
		grid.add(imgSeparator, 1, 4 );

		//Show application credits
		VBox vBoxCredits = new VBox(20, new Text("Bingo Game Program - Developed by Luis Leon and Marcelo Calle"), new Text("Miami Dade College - COP20800 Final Project - Spring 2019"));
		vBoxCredits.setId("credits");
		vBoxCredits.setAlignment(Pos.BOTTOM_LEFT);
		grid.add(vBoxCredits, 1, 5);
		grid.setHgap(10);
		grid.setVgap(10);

		//Sets the application icon
		primaryStage.getIcons().add(new Image("\\balls\\32.jpg"));					

		//Set my scene
		Scene scene = new Scene(grid, 1100, 700); 		
		scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);    

		// Show the window.
		primaryStage.show(); 

		//Start the background sound
		backgroundSound.setCycleCount(50);			//Sets repetition
		backgroundSound.play();						//Plays the sound
	}

	//displayBingoCard  method - receives the bingo card array and the corresponding GridPane to set graphycally the Player's Bingo card
	public static GridPane displayBingoCard(String[][] array, GridPane grid)
	{
		//Clears the previous objects
		grid.getChildren().clear();

		//ImageView Array list - to hold the images process and construct the bingo cards
		List<ImageView> tempImage = new ArrayList<>();

		for(int i = 0; i<5; i++)					//Iterates the arrays by columns
		{
			for(int j = 0; j<5; j++)				//Iterates the array by rows
			{
				if(array[j][i] == " X ")
				{
					//Sets the central image to an X picture
					Image image = new Image("\\balls\\X.jpg");			
					tempImage.add(new ImageView(image));
				}
				else
				{					
					//If the array contains a winning pattern ball that has been called, this comes in the format {ball}
					if(array[i][j].substring(0, 1).contains("{"))
					{
						//If the previous called ball is a number less than 1 digit plus two like : {5}
						if(array[i][j].length() == 3)
						{
							//gets the right image file and it is put into the ImageView control to be process
							Image image = new Image("\\balls\\" + array[i][j].substring(1, 2) + "m.jpg");
							tempImage.add(new ImageView(image));							

							//Add animation to the ball
							ScaleTransition  scale = new ScaleTransition (Duration.millis(1000), tempImage.get(tempImage.size()-1)); //duration in milliseconds of the animation
							scale.setByX(0.2);
							scale.setByY(0.2);
							scale.setCycleCount(8);
							scale.setAutoReverse(true);
							scale.play();							
						}

						//If the previous called ball is a number less than 2 digit plus two like : {25}
						else if(array[i][j].length() == 4)
						{			
							//gets the right image file and it is put into the ImageView control to be process
							Image image = new Image("\\balls\\" + array[i][j].substring(1, 3) + "m.jpg");
							tempImage.add(new ImageView(image));

							//Add animation to the ball
							ScaleTransition  scale = new ScaleTransition (Duration.millis(1000), tempImage.get(tempImage.size()-1)); //duration in milliseconds of the animation
							scale.setByX(0.2);
							scale.setByY(0.2);
							scale.setCycleCount(8);
							scale.setAutoReverse(true);
							scale.play();							
						}
					}
					else
					{
						//If the ball is not into the winning patterns then just prepare it to be added it to the grid
						Image image = new Image("\\balls\\" + array[i][j] + ".jpg");	
						tempImage.add(new ImageView(image));
					}
				}
			}		
			for(int j = 0; j<5; j++)
			{
				//Sets the ImageView size
				tempImage.get(j).setFitHeight(50);
				tempImage.get(j).setFitWidth(50);
				//Add the ImageView control to the grid
				grid.addColumn(i, tempImage.get(j));
			}		
			tempImage.clear();			//Delete the contents the temporary ImageView array						
		}	
		return grid;					//retuns the grid already set up
	}

	//ButtonClickHandlerPlayGame class - to receive the play game button actions
	class ButtonClickHandlerPlayGame implements EventHandler<ActionEvent>  
	{ //Inner class event handler
		@Override
		public void handle(ActionEvent event) 
		{
			if(!noSound)
			{
				//If sound setting is on then play the ball sound
				AudioClip audio = new AudioClip("file:src/sounds/button.wav");
				audio.play();
			}

			if(stopPlay == false)
			{
				String ball = BingoWheel.bingoRandomBall();			//Gets a new random bingo ball
				grid.getChildren().remove(tempImageBig);			//remove bingo decoration image from the scene
				labelPattern.setId("header2");

				player1.checkBall(ball);							//Sends the generated random ball to be check
				player2.checkBall(ball);							//Sends the generated random ball to be check

				labelLastBallNumber.setText(ball);					//Sets the statistic's last ball label

				//Gets the player's Bingo Cards
				String [][] tempArr1 = player1.getBingoCard().getBingoCardArray(); 
				String [][] tempArr2 = player2.getBingoCard().getBingoCardArray();

				//Sends the received Bingo cards arrays and the grids pane to be created
				displayBingoCard(tempArr1, gridPlayer1);
				displayBingoCard(tempArr2, gridPlayer2);

				//Sets the las ball to be show with several animatios at the same time
				Image imageBig = new Image("\\balls\\" + ball.substring(1) + ".jpg");	
				tempImageBig = new ImageView(imageBig);

				tempImageBig.setFitHeight(20);
				tempImageBig.setFitWidth(20);
				grid.add(tempImageBig, 2, 4);		

				//Move the ball through the screen
				TranslateTransition moveBall = new TranslateTransition(Duration.millis(1000), tempImageBig); //duration in milliseconds of the animation
				moveBall.setByX(170); //Animates by moving the object on the x axis by the amount specified
				moveBall.setByY(100);
				moveBall.setCycleCount(1); //An animation can run in a loop by setting cycleCount
				moveBall.play();

				//Scale the ball 
				ScaleTransition  scale = new ScaleTransition (Duration.millis(1000), tempImageBig); //duration in milliseconds of the animation
				scale.setByX(10);
				scale.setByY(10);
				scale.setCycleCount(1);
				scale.play();

				//Rotate the ball
				RotateTransition rotate = new RotateTransition(Duration.millis(300), tempImageBig); //duration in milliseconds of the animation
				rotate.setByAngle(360);
				rotate.setCycleCount(3); //An animation can run in a loop by setting cycleCount
				rotate.play();

				//Sets and prepare the las called ball to be show in the general grid together
				Image image = new Image("\\balls\\" + ball.substring(1) + ".jpg");	
				ImageView tempImage = new ImageView(image);
				tempImage.setFitHeight(42);
				tempImage.setFitWidth(42);

				//Depending of the number of previous called balls this if shows them in 5 rows
				//using the number of rounds already made
				if(runNumber < 15)
				{
					gridWheel.addRow(0, tempImage); //column
				}
				else if(runNumber > 14 && runNumber < 30)
				{
					gridWheel.addRow(1, tempImage); 
				}
				else if(runNumber > 29 && runNumber < 45)
				{
					gridWheel.addRow(2, tempImage); 
				}
				else if(runNumber > 44 && runNumber < 60)
				{
					gridWheel.addRow(3, tempImage); 
				}
				else if(runNumber > 59 && runNumber < 75)
				{
					gridWheel.addRow(4, tempImage); 
				}

				//Cast the player's received scores to the right labels for statistics
				labelScore1.setText(Integer.toString(player1.getScore()));				
				labelScore2.setText(Integer.toString(player2.getScore()));

				//Shows the total rounds already called
				labelTotalBalls.setText(Integer.toString(runNumber+1));

				//Update the actual rounds
				runNumber++;

				//Evaluate if there is a winner
				if(player1.getScore() == 8 || player2.getScore() == 8)
				{		
					//If there are more than one winner, shows a dialogbox and play the sound
					if(!noSound)
					{
						AudioClip audiobingo = new AudioClip("file:src/sounds/bingo.wav");					
						audiobingo.play();
					}		

					Alert dlgWinner = new Alert(AlertType.INFORMATION);	

					dlgWinner.setTitle("We got a Winner!!!");
					dlgWinner.setHeaderText(null);
					stopPlay=true;													//sets the end of the game

					if(player1.getScore() == 8 && player2.getScore() == 8)
					{

						dlgWinner.setContentText("The winners are: " + player1.getName() + " and " + player2.getName());	

					}
					//If there is just one winner, shows a dialogbox and play the sound
					else if(player1.getScore() == 8)
					{
						dlgWinner.setContentText("The winner is: " + player1.getName());

					}
					else if(player2.getScore() == 8)
					{
						dlgWinner.setContentText("The winner is: " + player2.getName());
					}
					
					//Delete the next round button
					hBoxButtons.getChildren().remove(buttonPlay);

					dlgWinner.showAndWait();									//Show dialog box
				}				
			}
		}
	}
	//handleCloseButtonAction class - to receive the exit program button actions
	class handleCloseButtonAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e) 
		{
			//Terminate the program
			((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
		}
	}

	//handleChkChange class - to receive the sound setting checkbox actions
	class handleChkChange implements EventHandler<ActionEvent> 
	{ 
		@Override
		public void handle(ActionEvent e) 
		{ 
			if (chkSound.isSelected()) 
			{
				//Stops the sounds
				backgroundSound.stop();
				backgroundSound = null;
				noSound = true;
			}            	 
			else
			{			
				//Play again the background sound
				backgroundSound = new AudioClip("file:src/sounds/background.mp3");
				backgroundSound.play(50);
				noSound = false;
			} 
		} 
	}; 

	//ButtonClickHandlerStartGame class - to receive the start game button actions
	//to prepare the game
	class ButtonClickHandlerStartGame implements EventHandler<ActionEvent>  
	{ //Inner class event handler
		@Override
		public void handle(ActionEvent event) 
		{		
			//Sets players name if the user input them or assign the defaults ones
			String playerName1 = textName1.getText();
			String playerName2 = textName2.getText();		

			if(playerName1.length() == 0)
			{
				playerName1 = "Player 1";
			}
			else
			{
				playerName1 = textName1.getText();
			}

			if(playerName2.length() == 0)
			{
				playerName2 = "Player 2";
			}
			else
			{
				playerName2 = textName2.getText();				
			}	

			//Sets the Player class objects sending them the players names
			player1 = new Player(playerName1);
			player2 = new Player(playerName2);

			//sets the player's labels names
			labelPlayer1 = new Label(playerName1 + "'s Bingo Card");
			labelPlayer1.setId("header2");
			labelPlayer2 = new Label(playerName2 + "'s Bingo Card");
			labelPlayer2.setId("header2");

			//Retrieves the Bingo Card array for each player
			String [][] tempArr1 = player1.getBingoCard().getBingoCardArray(); 
			String [][] tempArr2 = player2.getBingoCard().getBingoCardArray();

			//Sends the retrieved arrays to the GUI bingo Card's constructor
			displayBingoCard(tempArr1, gridPlayer1);
			displayBingoCard(tempArr2, gridPlayer2);

			//Cleans the scene to show the game
			grid.getChildren().clear();			
			vBoxLeft.getChildren().clear();
			vBoxRight.getChildren().clear();

			//Prepares the new scene
			titleLabel.setText("B i n g o  Game - Java Class - COP2800!");
			titleLabel.setId("title");
			grid.add(titleLabel,0,0);

			//Adding to the grid the bingo cards
			grid.add(labelPlayer1, 0,1);
			grid.add(labelPlayer2, 1,1);
			
			//Setting an horizontal Gap to the grid
			grid.setHgap(40);

			//Shows the actual winning pattern with title format			
			labelPattern.setId("title");

			//Shows the statistic section
			Label labelStats = new Label("Statistics: ");
			labelStats.setId("header2");
			Label labelStaPlayer1 = new Label(labelPlayer1.getText() + "'s score:  ");
			labelScore1 = new Label(Integer.toString(player1.getScore()));
			Label labelStaPlayer2 = new Label(labelPlayer2.getText() + "'s score:  ");
			labelScore2 = new Label(Integer.toString(player2.getScore()));
			Label labelLastBall = new Label("Last ball:");
			Label labelTotalCalled = new Label("Total balls:");

			//Declare the listener actions for the buttons
			buttonPlay.setOnAction(new ButtonClickHandlerPlayGame());
			buttonExit.setOnAction(new handleCloseButtonAction());

			//Full fill the statistic block
			gridStats.setVgap(15);
			gridStats.add(labelStats, 0,0);
			gridStats.add(labelStaPlayer1, 0, 1);	
			gridStats.add(labelScore1, 1, 1);
			gridStats.add(labelStaPlayer2, 0, 2);
			gridStats.add(labelScore2, 1, 2);
			gridStats.add(labelLastBall, 0, 3);
			gridStats.add(labelLastBallNumber, 1, 3);
			gridStats.add(labelTotalCalled, 0, 4);
			gridStats.add(labelTotalBalls, 1, 4);
			gridStats.add(chkSound, 0, 5);

			//sets the IDs to use the CSS styles
			gridPlayer1.setId("cards");
			gridPlayer2.setId("cards");

			//Shows the bingo cards headers BINGO leyend showing an image
			Image imgBingo = new Image("\\balls\\bingo.fw.png");
						
			ImageView tempImageBingo = new ImageView(imgBingo);
			ImageView tempImageBingo2 = new ImageView(imgBingo);

			grid.add(tempImageBingo, 0,2);
			grid.add(tempImageBingo2, 1,2);

			hBoxButtons = new HBox(20, buttonPlay, buttonExit);

			grid.add(hBoxButtons, 2, 2);			
			//Adds the bingo cards to the grid
			grid.add(gridPlayer1, 0, 3);	// 0 column, 2 row
			grid.add(gridPlayer2, 1, 3);

			//Adds the other blocks
			grid.add(gridStats, 2, 3);
			grid.add(labelPattern, 0, 4);
			grid.add(gridWheel, 0, 5);

			//Adds decoration image to teh scene
			Image img = new Image("\\balls\\wheel.gif");
			tempImageBig = new ImageView(img);
			grid.add(tempImageBig, 2, 4);			

			//Merge few cells for layout and sets alignments
			GridPane.setColumnSpan(labelPattern,  3);
			GridPane.setColumnSpan(gridWheel,  3);
			GridPane.setColumnSpan(titleLabel,  3);
			
			//Alignments to let the program look better
			grid.setAlignment(Pos.BASELINE_CENTER);
			GridPane.setHalignment(titleLabel, HPos.CENTER);
			GridPane.setHalignment(labelPlayer1, HPos.CENTER);
			GridPane.setHalignment(labelPlayer2, HPos.CENTER);
			GridPane.setHalignment(gridPlayer1, HPos.CENTER);
			GridPane.setHalignment(gridPlayer2, HPos.CENTER);
			
			//Play the ball sound every time we get a new bingo ball
			AudioClip audio = new AudioClip("file:src/sounds/button.wav");
			audio.play();

			//After finish with the game set the program get ready to start generating random balls for the game
			bingoWheel.setFlagWheelReady(true);
		}   
	}
}