11//In this program, we will create and solve a maze using myMaze method .solve()
//Also, myMaze size is declared by the user
//

import java.util.*;
import java.io.*;
import java.lang.Math.*;

public class StartSolvingMaze implements Serializable   //we have to implement Serializable to be able to save our game
{
	public static void main (String [] args)
	{
		//declare the variables
		int numRows;
		int numCols;

		//create a new instance of a scanner
		Scanner kb = new Scanner(System.in);

		//ask the uer for the number of rows
		System.out.println("Please enter the number of rows followed by the number of columns: ");

		//read in the user respond
		numRows = kb.nextInt();
		numCols = kb.nextInt();

		//create a new instance of MazeSolver
		MazeSolver myMazeSolver = new MazeSolver(numRows,numCols);

		//call myMaze.solve() method
		myMazeSolver.solve();


	}//end of main
}//end of class