// This class implements a JFrame which will display a Maze (written to combine walls and data)

public class MazeDisplay extends javax.swing.JFrame implements Runnable, java.io.Serializable
{
	//------------ constants
	private final int START_WIDTH = 700;
	private final int START_HEIGHT = 500;
	private final int ANIMATIONDELAY = 0;	//Animation display rate (in milliseconds), so 20fps

	//------------ data
	private int cellDim;
	private int[ ][ ] mazeArray;
	private int numArrayRows;
	private int numArrayCols;

	private java.awt.Graphics g;
	private Thread animationThread;
	private java.awt.Insets insets;

	//-----------  constructor(s)
	// Parameterized constructor which receives an int that is the landscapeID
	public MazeDisplay(Maze aMaze)
	{
		//if aMaze is null, throw an exception
		if (aMaze == null)
			throw new IllegalArgumentException("trying to create a MazeDisplay with a null Maze");

		//store the reference to aMaze's mazeArray to be used in the display
		mazeArray = aMaze.getMazeArray();

		//isolate the number of rows and the number of columns in the mazeArray
		numArrayRows = mazeArray.length;
		numArrayCols = mazeArray[0].length;

		//get the number of "real" rows and cols tht the Maze has
		int numRealRows = numArrayRows/2;
		int numRealCols= numArrayCols/2;

		//calculate the optimum size of one cell
		int calcWidth = START_WIDTH/numRealCols;
		int calcHeight = START_HEIGHT/numRealRows;
		cellDim = Math.min(calcWidth, calcHeight);

		//but if the cellDim is < 2 pixels, then it is too small to draw
		if (cellDim < 2)
			throw new IllegalArgumentException("maze has too many rows/cols to draw");

		//set the JFrame attributes
		 setTitle("THE MAZE SOLVER");
		setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE);
		setSize(numRealCols*cellDim+7,numRealRows*cellDim+1+28); //add one for last border, subtract 38 for title bar, borders
		center();
		setResizable(false);
		setAlwaysOnTop(true);
		setVisible(true);

		// use those values to determine the greeny and the orangey
		insets = getInsets();

		// add a WindowListener
		addWindowListener(new java.awt.event.WindowAdapter()
						  {public void windowClosing(java.awt.event.WindowEvent e)
						  { System.exit(0);}});

		// get the Graphics object that will be used to write to this JFrame;
		g = getGraphics();

		// Anonymous inner class window listener to terminate the program.
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				System.exit(0);
			}
		}
		);

		// Create and start animation thread
		animationThread = new Thread(this);
		animationThread.start();

	}

	//-----------  methods(s)

	// run will actually run this Frame
	public void run()
	{
		//Loop, sleep, and update sprite positions once each ANIMATIONDELAY milliseconds
		long time = System.currentTimeMillis();
		while (true) //infinite loop
		{
			paint(g);
			try
			{
				time += ANIMATIONDELAY;
				Thread.sleep(Math.max(0,time - System.currentTimeMillis()));
			}
			catch (InterruptedException e)
			{
				System.out.println(e);
			}//end catch
		}//end while loop
	}//end run method

	// center - will set the x and y of this Frame to the center of the screen
	private void center()
	{
		java.awt.Dimension ScreenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension FrameSize = this.getSize();
		int x = (ScreenSize.width - FrameSize.width)/2;
		int y = (ScreenSize.height - FrameSize.height)/2;
		this.setLocation(x, y);
	}

	//------------------------------------------------
	public void windowGainedFocus(java.awt.event.WindowEvent e)
	{
		repaint();
	}

	//------------------------------------------------
	public void windowLostFocus(java.awt.event.WindowEvent e)
	{
		repaint();
	}

	//------------------------------------------------
	public void componentResized(java.awt.event.ComponentEvent e)
	{
		repaint();
	}

	//------------------------------------------------
	public void componentMoved(java.awt.event.ComponentEvent e)
	{
		repaint();
	}

	//------------------------------------------------
	public void componentShown(java.awt.event.ComponentEvent e)
	{
		repaint();
	}

	//------------------------------------------------
	public void componentHidden(java.awt.event.ComponentEvent e)
	{
		//repaint();
	}

	//------------------------------------------------
	public void update(java.awt.Graphics g)
	{
		repaint();
	}



	//------------------------------------------------
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
	}

	// paint - repaints the whole Maze.  Uses "double-buffering" to eliminate flickering.
	public void paint(java.awt.Graphics g)
	{
		// create an image - we will "double buffer" (draw to that image first and then
		// draw the image) to eliminate flickering
		java.awt.Image image = createImage(getWidth(), getHeight());
    	java.awt.Graphics graphicsBuffer = image.getGraphics();

    	// fill the image with the background color
    	//graphicsBuffer.setColor(Color.WHITE);
       	//graphicsBuffer.fillRect(12, 12, getWidth()-24, getHeight()-24);


		graphicsBuffer.setColor(java.awt.Color.BLACK);

		for (int row=0; row<numArrayRows; row++)
			for (int col=0; col<numArrayCols;col++)
			{
				if (row%2==1 && col%2==1 && mazeArray[row][col] == 3)    //odd rows, odd cols are the actual cells
				{
					graphicsBuffer.setColor(java.awt.Color.MAGENTA);   //goal
					int startx = getInsets().left + col/2 * cellDim;
					int starty = getInsets().top + row/2 * cellDim;
					graphicsBuffer.fillRect(startx, starty, cellDim, cellDim);
				}
				if (row%2==1 && col%2==1 && mazeArray[row][col] == 2)    //odd rows, odd cols are the actual cells
				{
					graphicsBuffer.setColor(java.awt.Color.YELLOW);   //current
					int startx = getInsets().left + col/2 * cellDim;
					int starty = getInsets().top + row/2 * cellDim;
					graphicsBuffer.fillRect(startx, starty, cellDim, cellDim);
				}
				else if (row%2==1 && col%2==1 && mazeArray[row][col] == 0)    //odd rows, odd cols are the actual cells
				{
					graphicsBuffer.setColor(java.awt.Color.WHITE);   //current
					int startx = getInsets().left + col/2 * cellDim;
					int starty = getInsets().top + row/2 * cellDim;
					graphicsBuffer.fillRect(startx, starty, cellDim, cellDim);
				}
			}

		graphicsBuffer.setColor(java.awt.Color.BLACK);

		for (int row=0; row<numArrayRows; row++)
			for (int col=0; col<numArrayCols;col++)
			{
				if (row%2==0 && col%2==1 && mazeArray[row][col] == 1)    //even rows, odd cols are the horizontal walls
				{
					int startx = getInsets().left + col/2 * cellDim;
					int endx = getInsets().left + col/2 * cellDim + cellDim;
					int starty = getInsets().top + row/2 * cellDim;
					int endy = getInsets().top + row/2 * cellDim;
					graphicsBuffer.drawLine(startx, starty, endx, endy);
				}

				else if (row%2==1 && col%2==0 && mazeArray[row][col] == 1)    //odd rows, even cols are the vertical walls
				{
					int startx = getInsets().left + col/2 * cellDim;
					int endx = getInsets().left + col/2 * cellDim;
					int starty = getInsets().top + row/2 * cellDim;
					int endy = getInsets().top + row/2 * cellDim + cellDim;
					graphicsBuffer.drawLine(startx, starty, endx, endy);
				}
			}

		// copy the image to the actual Frame
		g.drawImage(image, 0, 0, java.awt.Color.WHITE, null);

		repaint();


	}

}



