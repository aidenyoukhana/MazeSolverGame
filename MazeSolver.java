/*
In this program, we will create a MazeSolver that has the appropriate logic to solve and serialize a given maze.
*/

import java.util.*;
import java.io.*;
import java.lang.Math.*;

public class MazeSolver implements Serializable  //we have to implement Serializable to be able to save our game
{
	//Data
	protected Maze myMaze;
	protected Stack<Direction> myStack;
	protected boolean [][] visited;

	//Constructor
	//which will be parameterized and will receive (numRows, numCols)
	public MazeSolver(int numRows, int numCols)
	{
		//create a new instance of maze with the user choice
		myMaze = new Maze(numRows, numCols);

		//set the animation delay speed
		myMaze.buildMaze(0);

		//set the moving speed
		myMaze.setSolveAnimationDelay(0);

		//create the other data structure
		myStack = new Stack<Direction>();
		visited = new boolean [numRows][numCols];
	}

	//Methods
	//.solve() has the actual logic that will solve and serialize the maze through a do-while loop
	//solve() Method
	public void solve()
	{
		//declare the variables
		String userChoice;
		String fileName;

		//create a new instance of MazeDisplay
		MazeDisplay myMazeDisplay = new MazeDisplay(myMaze);

		//mark the current cell as visited
		visited [myMaze.getCurrentRow()][myMaze.getCurrentCol()]=true;

		//create a new instance of a scanner 0000
		Scanner kb = new Scanner(System.in);

		//prompt the user to enter Q to quit, S to save, or ENTER to move
		System.out.println("Please enter Q to Quit, S to Save to a file, or ENTER to move: ");

		//use a do-while loop to convert the userChoice into cases
		do
		{
			//ask the user again for the input
			userChoice = kb.nextLine();

			//the magic starts here, we have to mark our current location as visited
			visited [myMaze.getCurrentRow()][myMaze.getCurrentCol()]=true;

			//if userChoice is "S," serialize it to a file
			if (userChoice.equalsIgnoreCase("s"))
			{
				//ask the user which file it should be serialized to
				System.out.println("what file should we serialize to? " );
				//capture the file name
				fileName = kb.nextLine();

				//PART 1: Serialize it to a file
				//java require us to catch the exceptions
				try
				{
					//create a new instance of ObjectOutputStream using the same file name provided by the user
					ObjectOutputStream oosVar = new ObjectOutputStream(new FileOutputStream(fileName));
					//tell oosVar to write the collected information into a binary file
					oosVar.writeObject(this);
					//close oosVar as a final step to save the binary file
					oosVar.close();
				}
				catch (Throwable ex)   //Throwable catches everything
				{
					System.out.println(ex);  //print the exception
				}
			}

			//if userChoice is "ENTER," tell myMaze to move itself to the appropriate cell
			if (userChoice.equals(""))
			{
				//below is the actual code that is used to solve the myMaze
				//case1: if we are open in the Direction.UP
				if (myMaze.isOpen(Direction.UP) && !visited [myMaze.getCurrentRow()-1][myMaze.getCurrentCol()])
				{
						myStack.push(Direction.UP);
						myMaze.move(Direction.UP);

				}
				//case2: if we are open in the Direction.DOWN
				else if (myMaze.isOpen(Direction.DOWN) && !visited [myMaze.getCurrentRow()+1][myMaze.getCurrentCol()])
				{
						myStack.push(Direction.DOWN);
						myMaze.move(Direction.DOWN);

				}
				//case3: if we are open in the Direction.LEFT
				else if (myMaze.isOpen(Direction.LEFT) && !visited [myMaze.getCurrentRow()][myMaze.getCurrentCol()-1])
				{
						myStack.push(Direction.LEFT);
						myMaze.move(Direction.LEFT);

				}
				//case4: if we are open in the Direction.RIGHT
				else if (myMaze.isOpen(Direction.RIGHT) && !visited [myMaze.getCurrentRow()][myMaze.getCurrentCol()+1])
				{
						myStack.push(Direction.RIGHT);
						myMaze.move(Direction.RIGHT);

				}
				//case5: if we are here, that means that we have reached a dead end. What we have to do next is extremely important
				//we will pop the stack and we will tell myMaze to move on an opposite Direction temp
				else
				{
					//store the Direction in temp
					Direction temp = myStack.pop();

					if (temp ==  Direction.UP)
					{
						myMaze.move(Direction.DOWN);
					}
					else if (temp == Direction.DOWN)
					{
						myMaze.move(Direction.UP);
					}
					else if (temp == Direction.RIGHT)
					{
						myMaze.move(Direction.LEFT);
					}
					else if (temp == Direction.LEFT)
					{
						myMaze.move(Direction.RIGHT);
					}

				}
			}
		}while ((!myMaze.goalReached()) && (!userChoice.equalsIgnoreCase("Q")) ); //keep running until the user enter "Q","q", or we have reached our goal
	}

}//end of class











// ****************************BELOW IS A DEBUGGER**********************
/*System.out.println("Current Row is: "+ myMaze.getCurrentRow());
System.out.println("Current Col is: "+ myMaze.getCurrentCol());
for (int row =0; row<visited.length;row++)
{
	System.out.println();
	for (int col =0; col<visited[row].length;col++)
	{
		System.out.print(visited [row][col]+"   ");
	}
}
System.out.println();*/
// ****************************ABOVE IS A DEBUGGER**********************
