//-------------------------------------------------------------------------
// Assignment #4 - BattleShip Game
// Written by: Gabriel Giangi 40174314
// For COMP-248-EC - Fall 2020
//
// This program is intended to simulate the classic game of BattleShip, 
// whereas the player chooses to place 6 ships and 4 grenades on the map 
// The computer then chooses his own placement of 6 ships and 4 grenades,
// of which isn't visible to the user. The user and computer then takes 
// turns "shooting" a rocket at a specific tile. If the rocket hits nothing,
// nothing happens and their turn is over. If the rocket hits an opposing
// player's grenade, the user loses their next turn. If a rocket hits an 
// opposing player's ship, then that ship sinks. The game continues until
// all of one player's ships have been sunk. 
//-------------------------------------------------------------------------
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {

	public static void main(String[] args) 
	{
		//Strings for coordinates
		String coordinate, column, row;
		//Integer values of the columns and rows
		int rowInt, columnInt;
		
		//Welcome banner
		System.out.println("\t --------------------------------------------------------------------------");
		System.out.println("\t+                         Welcome to Battleship!                         +"); 
		System.out.println("\t+                        ------------------------                        +"); 
		System.out.println("\t+            Start by selecting coordinates for your 6 ships!            +"); 
		System.out.println("\t+           Then choose 4 grenade locations... if the enemy hits         +");
		System.out.println("\t+          one of yours, they lose their next turn! The enemy will       +"); 
		System.out.println("\t+          then choose their own ship and grenade coordinates. After     +"); 
		System.out.println("\t+           the locations have been determined, you will each take       +");
		System.out.println("\t+           turns by guessing where the other player's ships are...      +");
		System.out.println("\t+          FIRST PLAYER TO GUESS WHERE ALL THE OTHER SHIPS ARE WINS!     +");
		System.out.println("\t--------------------------------------------------------------------------");
		System.out.println();
		
		//Declaring new object of Grid class
		Grid map = new Grid();
		
		//Print the game map so the user can see
		map.printGameMap();
		
		//Allows for user input
		Scanner input = new Scanner(System.in);
		
		//Ask for each of the user's ship coordinates
		for(int ships = 1; ships <= 6; ships++)
		{
			System.out.print("Enter the coordinates of your ship #" + ships + ": ");
			coordinate = input.next();
			
			//Split the input into the respective column and row value
			column = coordinate.substring(0, 1);
			row = coordinate.substring(1, 2);
			
			//Change the values to integers
			columnInt = Integer.parseInt(row) - 1;
			rowInt = map.toInt(column);
			
			//Check if the tile is valid, if it isn't, then the program will ask until the coordinate is valid
			while(map.checkTile(columnInt, rowInt))
			{
				System.out.print("Tile out of range or already occupied, please enter another coordinate: ");
				coordinate = input.next();
				
				column = coordinate.substring(0, 1);
				row = coordinate.substring(1, 2);
				
				columnInt = Integer.parseInt(row) - 1;
				rowInt = map.toInt(column);
			}
			
			//Once the coordinate is valid, place player ship there
			map.setPlayerShip(columnInt, rowInt);
			
		}
		
		//Same algorithm as setting the player's ships is used for placing the user's grenades
		for(int grenades = 1; grenades <= 4; grenades++)
		{
			System.out.print("Enter the coordinates of your grenade #" + grenades + ": ");
			coordinate = input.next();
			
			column = coordinate.substring(0, 1);
			row = coordinate.substring(1, 2);
			
			columnInt = Integer.parseInt(row) - 1;
			rowInt = map.toInt(column);
			
			while(map.checkTile(columnInt, rowInt))
				{
					System.out.print("Tile out of range or already occupied, please enter another coordinate: ");
					coordinate = input.next();
					
					column = coordinate.substring(0, 1);
					row = coordinate.substring(1, 2);
					
					rowInt = map.toInt(column);
					columnInt = Integer.parseInt(row) - 1;
				}
			
			map.setPlayerGrenade(columnInt, rowInt);
			
		}
		
		System.out.println();
		//Added a feature to delay time between user and computer's turn, to give the illusion that the AI is "thinking"
		System.out.println("Waiting for AI User to place its ships and grenades...");
		map.wait(7);
		
		//Calling the method that makes the AI choose it's own locations
		map.aiPlacement();
		System.out.println();
		System.out.println("Game starting...");
		System.out.println();
		System.out.println();
		System.out.println();
		
		//Game starts here
		//Continues to play until one player's ships have all been hit
		do
		{
			//If the AI hits one of your grenades, you have access to this loop, which allows you an extra turn
			if(map.playerTurn)
			{
				System.out.print("Type the position of your rocket: ");
				coordinate = input.next();
				
				//Similarly to placing the user's ships and grenades, the coordinate must be split, turned into integers, and checked if the rocket cooridnate is valid or has hit anything
				column = coordinate.substring(0, 1);
				row = coordinate.substring(1, 2);
				
				columnInt = Integer.parseInt(row) - 1;
				rowInt = map.toInt(column);
				
				//Check if the rocket coordinate is valid
				map.checkPlayerRocket(columnInt, rowInt);
				
				//If playing is false, the user has destroyed all the opponents ships and the game is over.
				if(map.playing == false)
				{
					if(map.userWin)
					{
						//If user wins
						System.out.println("Congratulations, all enemy ships have been destroyed!");
					}
					else
					{
						//If user hits their own ship and loses the game
						System.out.println("All your ships have been destroyed... you lose!");
					}
					
					map.printMapGrid();
					break;
				}
				else
				{
					map.printGameMap();
				}
				
				//Set the AI turn to true, so you can escape the loop and continue the game
				map.playerTurn = false;
			}
			
			//Ask for the user rocket coordinate
			System.out.print("Type the position of your rocket: ");
			coordinate = input.next();
			
			//Split into column and row
			column = coordinate.substring(0, 1);
			row = coordinate.substring(1, 2);
			
			//Get integer value of row and column
			columnInt = Integer.parseInt(row) - 1;
			rowInt = map.toInt(column);
			
			//Check the player rocket to determine if it has hit anything
			map.checkPlayerRocket(columnInt, rowInt);
			
			//If playing is false, all opponents ships have been destroyed and the game is over.
			if(map.playing == false)
			{
				if(map.userWin)
				{
					//If user wins
					System.out.println("Congratulations, all enemy ships have been destroyed!");
				}
				else
				{
					//If user hits their own ship and loses the game
					System.out.println("All your ships have been destroyed... you lose!");
				}
				map.printMapGrid();
				break;
			}
			else
			{
				map.printGameMap();
			}
			
			if(map.aiTurn)
			{
				System.out.println();
				System.out.print("AI User's turn...");
				System.out.println();
				
				//Added a feature to delay time between user and computer's turn, to give the illusion that the AI is "thinking"
				// this feature also gives the user a chance to look at the game board before the computer takes his turn
				map.wait(7);
				
				//Method calls for the AI to shoot its own rocket
				map.AiRocket();
				//If all user ships have been destroyed, game is over
				if(map.playing == false)
				{
					System.out.println("All your ships have been destroyed... you lose!");
					map.printMapGrid();
				}
				else
				{
					map.printGameMap();
				}
				
				map.aiTurn = false;
			}
			System.out.println();
			System.out.print("AI User's turn...");
			System.out.println();
			
			//Added a feature to delay time between user and computer's turn, to give the illusion that the AI is "thinking"
			// this feature also gives the user a chance to look at the game board before the computer takes his turn
			map.wait(7);
			
			//Method calls for the AI to shoot its own rocket
			map.AiRocket();
			//If all user ships have been destroyed, game is over
			if(map.playing == false)
			{
				System.out.println("All your ships have been destroyed... you lose!");
				map.printMapGrid();
			}
			else
			{
				map.printGameMap();
			}
			
		}
		while(map.playing);
		
		input.close();


	}
	

}
