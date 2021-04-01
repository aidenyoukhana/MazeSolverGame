/* This program implements a Maze.  Internally, walls and data are stored
    in a single 2D array; the entries whose row/col are even-odd or odd-even
    are the walls and the entries whose row/col are odd-odd are the cells
    themselves.  Therefore, a 4x5 maze will be stored in a 9x11 array so the
    walls can be stored as well as the cells.  In the code, the variable names
    would be made clear so it can work with (realRow vs arrayRow).
 */

public class Maze implements java.io.Serializable
{
	//-------data
	private int[ ][ ] mazeArray;
	private int currentArrayRow;
	private int currentArrayCol;
	private int goalArrayRow;
	private int goalArrayCol;
	private int buildAnimationDelay;
	private int solveAnimationDelay;
	private boolean alreadyBuilt;

	//-------constructors
	public Maze(int numRealRows, int numRealCols)
	{
		//since the maze is being created, initialize alreadyBuilt to false
		alreadyBuilt = false;

		//set the buildAnimationDelay and solveAnimationDelay to 0;  they can be reset with methods
		buildAnimationDelay = 0;
		solveAnimationDelay = 0;

		//make sure that the numRealRows and numRealCols are both > 1 (start cannot == goal)
		if (numRealRows < 2)
			throw new IllegalArgumentException("number of rows must be > 1");
		if (numRealCols < 2)
			throw new IllegalArgumentException("number of columns must be > 1");

		//create the 2D array to hold the maze (its even rows/cols hold the walls, odd rows/cols hold the paint
		mazeArray = new int[2*numRealRows+1][2*numRealCols+1];

		//since the even values are the walls, set anything with an even component to 1 (wall exists to start)
		for (int row=0; row<mazeArray.length; row++)
			for (int col=0; col<mazeArray[row].length; col++)
				if (row%2==0 || col%2==0)   //if either dimension is even...
					mazeArray[row][col] = 1;		//its a wall, so set value to 1

		//initialize the currentArrayRow and currentArrayCol to the upper left corner
		currentArrayRow = 1;
		currentArrayCol= 1;
	}

	// **************** methods *******************************************

	//equals - returns true if it equals what is received
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		Maze objMaze = (Maze)obj;

		//now do the real check - do the arrays have the exact same elements?
		//build a single String out of all the elements in this Maze, row major
		String myElementsStr = "";
		for (int row=0; row<this.mazeArray.length; row++)
			for (int col=0; col<this.mazeArray[row].length; col++)
				myElementsStr = myElementsStr + this.mazeArray + " ";

		//build a single String out of all the elements in argument Maze, row major
		String objElementsStr = "";
		java.util.ArrayList objElementsAL = new java.util.ArrayList();
		for (int row=0; row<objMaze.mazeArray.length; row++)
			for (int col=0; col<objMaze.mazeArray[row].length; col++)
				objElementsStr = objElementsStr + objMaze.mazeArray[row][col] + " ";

		//if the 2 Strings are equal, then the original arrays contained the same elements
		return myElementsStr.equals(objElementsStr);
	}

	//------- setSolveAnimationDelay - sets the delay (milliseconds) for the maze being solved (in case its animated)
	public void setSolveAnimationDelay(int theDelay)
	{
		solveAnimationDelay = theDelay;
	}

	//------- buildMaze - builds the Maze; calls other buildMaze method to set buildAnimationDelay to 0
	public void buildMaze()
	{
		buildMaze(0);
	}

	//------- buildMaze - builds the Maze; receives a delay to slow it down (in case its displayed)
	public void buildMaze(int buildAnimationDelay)
	{
		//if this maze has already been built and it trying to be built again, throw an exception
		if (alreadyBuilt)
			throw new IllegalStateException("cannot build maze - it has already been built");
		else
			alreadyBuilt = true;   //because we are NOW building it

		System.out.println("\nbeginning to build the maze with " + mazeArray.length/2 + " rows, " + mazeArray[0].length/2 + " cols");

		//create a Stack to hold the cells we are visiting as it is built (they will be stored as Points)
		//and an ArrayList to hold the "neighbors" in the code below
		java.util.Stack<java.awt.Point> cellStack = new java.util.Stack<java.awt.Point>();
		java.util.ArrayList<java.awt.Point> neighborAL;

		//in the mazeArray, the even rows/cols are the walls, the odd rows/cols are the cells
		int numRealRows = mazeArray.length/2;
		int numRealCols = mazeArray[0].length/2;

		//calculate the total number of (real) cells to visit
		int totalCells = numRealRows * numRealCols;  //rows x cols

		//the odd rows/cols store the actual cells.  Choose a random cell to start.
		java.util.Random gen = new java.util.Random();

		int currentArrayRow = gen.nextInt(numRealRows)*2 + 1;	//ex: if 3 real rows, this is a random from 1,3,5
		currentArrayCol = gen.nextInt(numRealCols)*2 + 1;		//same for cols...
		int lastArrayRow = currentArrayRow;
		int lastArrayCol = currentArrayCol;

		mazeArray[currentArrayRow][currentArrayCol] = 2;
		int numVisitedCells = 1;

		//while all cells have not been visited...
		while(numVisitedCells < totalCells)
		{
			//go to sleep to slow down animation (based on its speed)
			try{ Thread.sleep(buildAnimationDelay); }
			catch(Exception ex) {}

			//find all neighbors of currentCell with all walls intact
			neighborAL = new java.util.ArrayList<java.awt.Point>();
			//try cell above it
			if (inMaze(currentArrayRow-2, currentArrayCol) && allWallsIntact(currentArrayRow-2, currentArrayCol))
				neighborAL.add(new java.awt.Point(currentArrayRow-2, currentArrayCol));
			//try cell below it
			if (inMaze(currentArrayRow+2, currentArrayCol) && allWallsIntact(currentArrayRow+2, currentArrayCol))
				neighborAL.add(new java.awt.Point(currentArrayRow+2, currentArrayCol));
			//try cell to the left of it
			if (inMaze(currentArrayRow, currentArrayCol-2) && allWallsIntact(currentArrayRow, currentArrayCol-2))
				neighborAL.add(new java.awt.Point(currentArrayRow, currentArrayCol-2));
			//try cell to the right of it
			if (inMaze(currentArrayRow, currentArrayCol+2) && allWallsIntact(currentArrayRow, currentArrayCol+2))
				neighborAL.add(new java.awt.Point(currentArrayRow, currentArrayCol+2));

			//if neighbors with intact walls exist...
			if (neighborAL.size() > 0)
			{
				//choose a neighbor at random
				int randomInt = gen.nextInt(neighborAL.size());
				java.awt.Point theNeighbor = neighborAL.get(randomInt);
				int neighborRow = (int)theNeighbor.getX();
				int neighborCol = (int)theNeighbor.getY();

				//knock down the wall in between
				if (currentArrayRow != neighborRow)    //neighbor chosen was above or below
					mazeArray[(currentArrayRow+neighborRow)/2][currentArrayCol] = 0;   //knock down wall in between
				else if (currentArrayCol != neighborCol)   //neighbor chosen was to the left or right
					mazeArray[currentArrayRow][(currentArrayCol+neighborCol)/2] = 0;    //knock down wall in between

				//push the current cell onto the cellStack
				cellStack.push(new java.awt.Point(currentArrayRow, currentArrayCol));

				//clear the current cell
				mazeArray[currentArrayRow][currentArrayCol] = 0;

				//make the new cell the current cell
				currentArrayRow = neighborRow;
				currentArrayCol = neighborCol;
				mazeArray[currentArrayRow][currentArrayCol] = 2;

				//add 1 to visitedCells
				numVisitedCells++;
			}
			else
			{
				//clear the current cell
				mazeArray[currentArrayRow][currentArrayCol] = 0;

				//pop the most recent entry off of cellStack and make it the current cell
				java.awt.Point popped = cellStack.pop();
				currentArrayRow = (int)popped.getX();
				currentArrayCol = (int)popped.getY();
				mazeArray[currentArrayRow][currentArrayCol] = 2;
			}
		}   //end while

		//clear the cell that ended up as the current Cell
		mazeArray[currentArrayRow][currentArrayCol] = 0;

		//set the current cell to the upper left corner
		currentArrayRow = 1;
		currentArrayCol = 1;
		mazeArray[currentArrayRow][currentArrayCol] = 2;   //current

		//set the goal to the lower right corner
		goalArrayRow = numRealRows*2-1;
		goalArrayCol = numRealCols*2-1;
		mazeArray[goalArrayRow][goalArrayCol] = 3;   //goal

		System.out.println("finished building the maze\n");
	}

	//-------- getNumRows - returns the number of rows in the maze (from user's perspective)
	private int getNumRows()
	{
		return mazeArray.length/2;    // divide by 2 for real answer
	}

	//-------- getNumCols - returns the number of columns in the maze (from user's perspective)
	private int getNumCols()
	{
		return mazeArray[0].length/2;    // number of columns in row0 (divide by 2 for real answer)
	}


	//-------- allWallsIntact - returns true if the cell at [aRow][aCol] has all walls around it intact
	private boolean allWallsIntact(int aRow, int aCol)
	{
		return   (mazeArray[aRow-1][aCol] == 1   &&	//wall above it exists
						mazeArray[aRow+1][aCol] == 1  &&	//wall below it exists
						mazeArray[aRow][aCol-1] == 1   &&	//wall to the left exists
						mazeArray[aRow][aCol+1] == 1);			//wall to the right exists
	}

	//-------- inMaze - returns true if the cell at [aRow][aCol] is in the maze
	private boolean inMaze(int aRow, int aCol)
	{
		return   (aRow > 0   &&   aRow < mazeArray.length-1   &&
						aCol > 0     &&   aCol   < mazeArray[0].length-1);
	}

	//-------- getCurrentRow - returns the current (real) row
	public int getCurrentRow()
	{
		return currentArrayRow/2;
	}

	//-------- getCurrentCol - returns the current (real) col
	public int getCurrentCol()
	{
		return currentArrayCol/2;
	}


	//-------- isOpen - returns true if there is no wall in the direction that is passed in
	public boolean isOpen(Direction direction)
	{
		boolean result = false;
		if (direction == Direction.UP && mazeArray[currentArrayRow-1][currentArrayCol]==0)
			result = true;
		else if (direction == Direction.DOWN && mazeArray[currentArrayRow+1][currentArrayCol]==0)
			result = true;
		else if (direction == Direction.LEFT && mazeArray[currentArrayRow][currentArrayCol-1]==0)
			result = true;
		else if (direction == Direction.RIGHT && mazeArray[currentArrayRow][currentArrayCol+1]==0)
			result = true;

		return result;
	}

	//-------- isOpenTo - returns true if the current cell is openTo (no wall) the one passed in
	private boolean isOpenTo(int aRow, int aCol)
	{
		boolean result;

		if (!isAdjacentTo(aRow, aCol))
			result = false;
		else if (currentArrayRow-aRow == 2)  //IS adjacent, figure which direction and call other method
			result = mazeArray[currentArrayRow-1][currentArrayCol]==0;   //UP
		else if (currentArrayRow-aRow == -2)
			result = mazeArray[currentArrayRow+1][currentArrayCol]==0;   //DOWN
		else if (currentArrayCol-aCol == 2)
			result = mazeArray[currentArrayRow][currentArrayCol-1]==0;   //LEFT
		else if (currentArrayCol-aCol == -2)
			result = mazeArray[currentArrayRow][currentArrayCol+1]==0;   //RIGHT
		else
			result = false;

		return result;
	}

	//-------- adjacentTo - returns true if the current cell is adjacentTo (above/below/left/right) current cell
	public boolean isAdjacentTo(int aRow, int aCol)
	{
		//calculate how far the move is (hopefully row OR col is just +-2)
		int arrayRowChange = currentArrayRow - aRow;
		int arrayColChange = currentArrayCol - aCol;

		//it is adjacent if EITHER the rows or the cols differ by 2
		return Math.abs(arrayRowChange)==2 ^ Math.abs(arrayColChange)==2;  //checking row xor col
	}

	// -------- move - receives a Direction and moves there if OK.  Calls the other
	//                 arrayMove to do this
	public boolean move(Direction direction)
	{
		boolean success = false;

		if (direction == Direction.UP)
			success = arrayMove(currentArrayRow-2, currentArrayCol);
		else if (direction == Direction.DOWN)
			success = arrayMove(currentArrayRow+2, currentArrayCol);
		else if (direction == Direction.LEFT)
			success = arrayMove(currentArrayRow, currentArrayCol-2);
		else if (direction == Direction.RIGHT)
			success = arrayMove(currentArrayRow, currentArrayCol+2);

		return success;
	}

	//-------- move - receives the literal (not array) row/col to move to.  Calls the
	//                other ArrayMove to do this.
	private boolean move(int realRow, int realCol)
	{
		return arrayMove(2*realRow+1, 2*realCol+1);
	}

	//-------- arrayMove - moves using the maze array (moves a distance of 2 to get to next cell)
	//                     first checks to see if move is legal.  Returns true if successful.
	private boolean arrayMove(int newArrayRow, int newArrayCol)
	{
		boolean success;

		//go to sleep to slow down animation (based on its speed)
		try{ Thread.sleep(solveAnimationDelay); }
		catch(Exception ex) {}

		//make sure the new row/col is still in the maze
		if (!inMaze(newArrayRow, newArrayCol))
			throw new IllegalMazeMoveException("trying to move to cell <" + newArrayRow/2 + ", " +
					newArrayCol/2 + "> which is outside the maze");

		//make sure the new row/col is adjacent
		else if (!isAdjacentTo(newArrayRow, newArrayCol))
			throw new IllegalMazeMoveException("trying to move from cell <" +  currentArrayRow/2 + ", " + currentArrayCol/2 +
					"> to non-adjacent cell <" + newArrayRow/2 + ", " + newArrayCol/2 + ">");

		//make sure there is not a wall in between
		else if (!isOpenTo(newArrayRow, newArrayCol))
			throw new IllegalMazeMoveException("trying to move from cell <" +  currentArrayRow/2 + ", " + currentArrayCol/2 +
					"> to cell <" + newArrayRow/2 + ", " + newArrayCol/2 + "> and there is a wall in between");

		//if OK, move the current cell
		else
		{
			//if new ArrayRow is already in the path, then we are retreating from current location so
			//clear current location
			if (mazeArray[newArrayRow][newArrayCol] == 2)
				mazeArray[currentArrayRow][currentArrayCol] = 0;

			currentArrayRow = newArrayRow;
			currentArrayCol = newArrayCol;						//move current cell
			mazeArray[currentArrayRow][currentArrayCol] = 2;   //and show it as part of path
			success = true;

		}

		//return
		return success;
	}

	//-------- goalReached - returns true if the maze is solved (current location == goal)
	public boolean goalReached()
	{
		return (currentArrayRow == goalArrayRow && currentArrayCol == goalArrayCol);
	}

	//-------- getMazeArray - returns the mazeArray
	public int[][] getMazeArray()
	{
		return mazeArray;
	}

	//***********************************************************************

	//This is Maze's enumerated data type: moves can be UP, DOWN, LEFT, RIGHT
//	public enum Direction implements java.io.Serializable
//	{
//		UP, DOWN, LEFT, RIGHT
//	}

	//***********************************************************************

	//IllegalMazeMoveException will be thrown when an illegal move is requested in the maze
	public class IllegalMazeMoveException extends IllegalArgumentException implements java.io.Serializable
	{
		//no data is needed - all inherited...

		//constructor - just do what the parent class would do if it received the String
		public IllegalMazeMoveException(String str)
		{
			super(str);
		}

		//no methods are needed - all inherited...
	}

}



