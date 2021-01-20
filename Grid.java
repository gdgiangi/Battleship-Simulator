import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Grid 
{
	//This map is the map that will be shown throughout the game
	private String[][] gameMap = new String[8][8];
	//This map is hidden but will contain all of the positions of grenades and ships
	private String[][] mapGrid = new String[8][8];
	//Player lives, as well as temporary integers for the AI to choose it's own coordinates
	private int randomTempCol, randomTempRow, playerLives = 6, AiLives = 6;
	//Boolean to determine if game is still playing, the winner, and if there's another turn to be allocated due to grenade hit
	public boolean playing = true, playerTurn = false, aiTurn = false, userWin = false;
	
	//CONSTRUCTOR
	//Initiliazes both gameMap and mapGrid when object of this class is created
	public Grid()
	{
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				mapGrid[i][j] = "___ ";
			}
		}
		
		//Copy the values of the mapGrid to the actual playing map
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				gameMap[i][j] = mapGrid[i][j];
			}
		}
	}
	
	//Set player ship at specific coordinate
	public void setPlayerShip(int column, int row)
	{
		mapGrid[column][row] = "_s_ ";
	}
	
	//Set AI ship at specific coordinate
	private void setAiShip(int column, int row)
	{
		mapGrid[column][row] = "_S_ ";
	}
	
	//Set player grenade at specific coordinate
	public void setPlayerGrenade(int column, int row)
	{
		mapGrid[column][row] = "_g_ ";
	}
	
	//Set AI grenade at specific coordinate
	public void setAiGrenade(int column, int row)
	{
		mapGrid[column][row] = "_G_ ";
	}
	
	//Check the validity of a coordinate
	public boolean checkTile(int column, int row)
	{
		//Check if the coordinate is out of range
		if(column >= 8 || row >= 8 || column < 0 || row < 0)
		{
			return true;
		}
		//Check if the coordinate is already occupied
		else if(mapGrid[column][row] == "_s_ " || mapGrid[column][row] == "_g_ " || mapGrid[column][row] == "_G_ " || mapGrid[column][row] == "_S_ ")
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Method to make the AI smart, it will avoid shooting at its own grenades and ships, and won't shoot at a tile that has already been shot at
	private boolean aiRocketLogic(int column, int row)
	{
		//Check to see if the space is occupied by their own ship or grenade
		if(mapGrid[column][row] == "_G_ " || mapGrid[column][row] == "_S_ ")
		{
			return true;
		}
		//Check to see if the tile has already been revealed
		if(gameMap[column][row] == "_s_ " || gameMap[column][row] == "_*_ " || gameMap[column][row] == "_g_ ")
		{
			return true;
		}
		//If it gets to this, it means the tile is blank and hasn't been revealed yet
		else
		{
			return false;
		}
	}
	
	//Method to change coordinate value to its corresponding column location in the array
	public int toInt(String column)
	{
		switch(column)
		{
			case "A", "a":
				return 0;
			case "B", "b":
				return 1;
			case "C", "c":
				return 2;
			case "D", "d":
				return 3;
			case "E", "e":
				return 4;
			case "F", "f":
				return 5;
			case "G", "g":
				return 6;
			case "H", "h":
				return 7;
			default:
				return 8;
		}
	}
	
	//Method for the AI to place its ships and grenades
	public void aiPlacement()
	{
		//Call random class in order to create random integers
		Random randomNb = new Random();
		
		for(int i = 0; i < 6; i++)
		{
			//Randomly generate a coordinate
			randomTempCol = randomNb.nextInt(8);
			randomTempRow = randomNb.nextInt(8);
			
			//Check if the coordinates are valid, and that there are no user ships or grenades placed there
			while(checkTile(randomTempCol, randomTempRow))
			{
				randomTempCol = randomNb.nextInt(8);
				randomTempRow = randomNb.nextInt(8);
			}
			
			//Set the AI ship at the confirmed coordinate
			setAiShip(randomTempCol, randomTempRow);
			
		}
		
		//Exact same algorithm for the AI grenade placement
		for(int j = 0; j < 4; j++)
		{
			randomTempCol = randomNb.nextInt(8);
			randomTempRow = randomNb.nextInt(8);
			
			while(checkTile(randomTempCol, randomTempRow))
			{
				randomTempCol = randomNb.nextInt(8);
				randomTempRow = randomNb.nextInt(8);
			}
			
			//Place the AI grenade if the coordinate is valid
			setAiGrenade(randomTempCol, randomTempRow);
		}
	}
	
	//Method to shoot and check the result of user rocket
	public void checkPlayerRocket(int column, int row)
	{
		//If the tile has already been revealed
		if(gameMap[column][row] == "_*_ " || gameMap[column][row] == "_G_ " || gameMap[column][row] == "_S_ " || gameMap[column][row] == "_s_ " || gameMap[column][row] == "_g_ ")
		{
			System.out.println("Position unavailable, coordinate has already been revealed!");
			System.out.println();
		}
		//If the tile contains an enemy ship
		else if(mapGrid[column][row] == "_S_ ")
		{
			System.out.println("Enemy ship has been hit!");
			System.out.println();
			//Display the ship on the gamemap
			gameMap[column][row] = "_S_ ";
			//Remove one life from AI
			AiLives -= 1;
			//Check if game is over
			if(AiLives == 0)
			{
				playing = false;
				userWin = true;
			}
		}
		//If the tile contains an enemy grenade
		else if(mapGrid[column][row] == "_G_ ")
		{
			System.out.println("BOOM! Enemy grenade has been hit!");
			System.out.println();
			//Display on the gamemap
			gameMap[column][row] = "_G_ ";
			//Give the AI another turn
			aiTurn = true;
		}
		//If the tile contains your own ship
		else if(mapGrid[column][row] == "_s_ ")
		{
			System.out.println("What are you doing Captain!? You just sunk your own ship!");
			System.out.println();
			//Display on the gamemap
			gameMap[column][row] = "_s_ ";
			//Remove one of user's lives
			playerLives -= 1;
			//Check if game over
			if(playerLives == 0)
			{
				playing = false;
			}
		}
		//If the tile contains your own grenade
		else if(mapGrid[column][row] == "_g_ ")
		{
			System.out.println("What are you doing Captain!? You just hit your own grenade!");
			System.out.println();
			//Display on the gamemap
			gameMap[column][row] = "_g_ ";
		}
		//If the tile is empty
		else
		{
			System.out.println("Rocket missed!");
			System.out.println();
			//Display on the gamemap
			gameMap[column][row] = "_*_ ";
		}
	}

	//Method to shoot and check result of AI rocket 
	public void checkAiRocket(int column, int row)
	{
		//The tile contains one of the user ships
		if(mapGrid[column][row] == "_s_ ")
		{
			System.out.println("Your ship has been hit!");
			System.out.println();
			//Display on the gamemap
			gameMap[column][row] = "_s_ ";
			//Remove player life
			playerLives -= 1;
			//Check if game over
			if(playerLives == 0)
			{
				playing = false;
			}
		}
		//The tile contains one of the user grenades
		else if(mapGrid[column][row] == "_g_ ")
		{
			System.out.println("BOOM! The enemy hit one of your grenades!");
			System.out.println();
			//Display on the gamemap
			gameMap[column][row] = "_g_ ";
			//Give the user another turn
			playerTurn = true;
		}
		//The tile is empty
		else
		{
			System.out.println("Enemy missed!");
			System.out.println();
			//Display on the gamemap
			gameMap[column][row] = "_*_ ";
		}
	}
	
	//Method for the AI to randomly generate rocket coordinate
	public void AiRocket()
	{
		Random randomNb = new Random();
		
		//Randomly generate column and row values
		randomTempCol = randomNb.nextInt(8);
		randomTempRow = randomNb.nextInt(8);
		
		//Check the validity of the coordinates until valid
		while(aiRocketLogic(randomTempCol, randomTempRow))
		{
			randomTempCol = randomNb.nextInt(8);
			randomTempRow = randomNb.nextInt(8);
		}
		
		//Shoot rocket at valid coordinate
		checkAiRocket(randomTempCol, randomTempRow);
	}
	
	//Print the gamemape on the console
	public void printGameMap()
	{
		System.out.print("\t ");
		//prints the headers from A to H (so it's more user friendly and readable)
		for (char columns = 'A'; columns <= 'H'; columns++)
		{
			System.out.print("\t" + columns + "  ");
		}
		System.out.println();
		
		//Print the tiles and row numbers
		for(int row = 0; row < 8; row++)
		{
			//Print the row numbers
			   System.out.print("\t" + (row + 1) + " ");
			   
			   //Print the tiles
			   for (int column = 0; column < 8; column++)
			   {
			       System.out.print("\t" + gameMap[row][column]);
			   }
			   System.out.println();
		}
		
		System.out.println();
	}
	
	//Print the mapGrid which contains all the ship and grenade locations
	public void printMapGrid()
	{
		System.out.print("\t ");
		//prints the headers from A to H
		for (char columns = 'A'; columns <= 'H'; columns++)
		{
			System.out.print("\t" + columns + "  ");
		}
		System.out.println();
		
		//Print the tiles
		for(int row = 0; row < 8; row++)
		{
			//Print the row numbers
			   System.out.print("\t" + (row + 1) + " ");
			   
			   //Print the tiles
			   for (int column = 0; column < 8; column++)
			   {
			       System.out.print("\t" + mapGrid[row][column]);
			   }
			   System.out.println();
		}
		
		System.out.println();
	}
	
	//Added a feature to delay time between user and computer's turn, to give the illusion that the AI is "thinking"
	public void wait(int seconds)
	{
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
