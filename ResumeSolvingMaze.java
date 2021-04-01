/*
In this program, we will read in the binary file that has the saved game and continue solving myMaze
*/

import java.util.*;
import java.lang.Math.*;
import java.io.*;

public class ResumeSolvingMaze implements Serializable   //we have to implement Serializable to be able to save our game
{
	public static void main (String [] args)
	{
		//create a new Scanner
		Scanner kb = new Scanner(System.in);

		//ask the user for the location of the binary file
		System.out.println("Please enter the serialized binary file name: ");

		//store in the user response
		String fileName2 = kb.next();


		//PART 2: Read the file "ResumeSolvingMaze" to resume the game
		try
		{
			ObjectInputStream oisVar = new ObjectInputStream (new FileInputStream (fileName2));

			//typecast differentMaze as MazeSolver
			MazeSolver differentMaze = (MazeSolver)oisVar.readObject();

			//tell the typecasted instance to .solve()
			differentMaze.solve();

			//make sure to close, so the data could be saved
			oisVar.close();
		}
		//catches everything through Throwable
		catch(Throwable exx)
		{
			System.out.println(exx); //prints the exception
		}
	}
}