package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Represents a maze.
 * Creates a random two dimensional int array describing a maze,
 * whereas 1 represents a 'train' 0 represents a wall.
 *
 */
public class MazeMatrix implements IMazeGenerator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int m_minimumTrailLength;
	private int m_startX;
	private int m_startY;
	private int m_height;
	private int m_width;
	private int[][] m_maze;
	private boolean m_isMazeInitialized;
	private Point m_end;
	
	/**
	 * C'tor, initializes necessary members
	 * @param height	The height of the maze
	 * @param width		The width of the maze
	 */
	public MazeMatrix(int height, int width)
	{
		m_end = null;
		m_height = height;
		m_width = width;	
		m_isMazeInitialized = false;
		m_minimumTrailLength = (height + width) / 2;
	}

	@Override
	public int getStartX() { return m_startX;}
	
	@Override
	public int getStartY() { return m_startY;}
	
	@Override
	public int height() {return m_height;}
	public void setHeight (int height) {m_height = height;}
	
	@Override
	public int width() {return m_width;}
	public void setWidth (int width) {m_width = width;}

	/**
	 * Returns the maze
	 * @return
	 */
	@Override
	public int[][] getMaze()
	{
		if (!m_isMazeInitialized) {
			generateMaze();
			m_isMazeInitialized = true;
		}


		final int[][] result = new int[m_maze.length][m_maze[0].length];

		for (int i = 0; i < result.length; ++i)
			result[i] = Arrays.copyOf(m_maze[i], m_maze[i].length);


		return result;
	}

	/**
	 * Generate an array with random directions 1-4
	 * @return Array containing 4 directions in random order
	 */
	private Integer[] generateRandomDirections() {
		ArrayList<Integer> rand = new ArrayList<Integer>();
		for (int n = 0; n < 4; ++n)
			rand.add(n + 1);
		Collections.shuffle(rand);

		return rand.toArray(new Integer[4]);
	}

	/**
	 * Generate a maze with size of height * length. 
	 * Would be instantiated at a random access and exit point.
	 * @note 0 stands for a 'wall', 1 stands for a clear way.
	 */
	private void generateMaze() {
		m_maze = new int[m_width][m_height];
		// Initialize the two-dimensional array's cells to zeros 
		for (int i = 0; i < m_height; ++i)
			for (int j = 0; j < m_width; ++j)
				m_maze[i][j] = 0;

		boolean moved = false;
		Random r = new Random();

		// Find starting x and starting y that should be on the edges of the maze
		// First, decide whether the location would be on the vertical or horizontal side edges - 
		int x;
		int y;
		// Decide whether the entry point would be on the vertical edges
		boolean is_right_or_left = 1 == r.nextInt(2);
		if (is_right_or_left) {
			y = 1 + r.nextInt(m_height - 2);
			x = 0 == r.nextInt(2) ? 0 : (m_width - 1);
		}
		else {
			x = 1 + r.nextInt(m_width - 2);
			y = 0 == r.nextInt(2) ? 0 : (m_height - 1); 
		}

		// Mark the maze's entry point
		m_startX = x;
		m_startY = y;
		m_maze[x][y] = 1;

		boolean isDeadlock = false;
		int lastVisitedX = -1;
		int lastVisitedY = -1;
		// Start the path generation. Keep marking a path until we've reached an edge.
		do
		{
			isDeadlock = (lastVisitedX == x && lastVisitedY == y);
			// Get the random directions
			Integer[] randDirs = generateRandomDirections();

			// Pick the first one that could be applied
			moved = false;
			for (int i = 0; i < randDirs.length; ++i) {
				if (moved)
					break;

				switch(randDirs[i]){
				case 1: // Down
					if (y - 1 < 0)
						continue;

					if (!(((1 == y || y - 1 == m_height - 1) || (0 == x || m_width - 1 == x))
							&&((Math.abs(m_startX - x) + Math.abs(m_startY - (y - 1)) < m_minimumTrailLength))))
					{ 
						if (isDeadlock || (m_maze[x][y - 1] & DIR.UP.value()) == 0 || m_maze[x][y] >= 30)
						{
							m_maze[x][y] |= DIR.DOWN.value();
							m_maze[x][y-1] |= DIR.UP.value();
							moved = true;
							y -= 1;
						}
					}
					break;

				case 2: // Right
					if (x + 1 == m_width)
						continue;
					if (!((0 == y || y == (m_height - 1) ||(m_width - 2) == x)
							&& ((Math.abs(m_startX - (x + 1)) + Math.abs(m_startY - y) < m_minimumTrailLength))))
					{
						if (isDeadlock || (m_maze[x + 1][y] & DIR.LEFT.value()) == 0 || m_maze[x][y] >= 30) {
							m_maze[x][y] |= DIR.RIGHT.value();
							m_maze[x+1][y] |= DIR.LEFT.value();
							moved = true;
							x += 1;
						}
					}
					break;

				case 3: // Up
					if (y + 1 == m_height)
						continue;
					if (!(((m_height - 2) == y || 0 == x || (m_width - 1) == x)
							&&  ((Math.abs(m_startX - x) + Math.abs(m_startY - (y + 1)) < m_minimumTrailLength))))
					{
						if (isDeadlock || (m_maze[x][y + 1] & DIR.DOWN.value()) == 0 || m_maze[x][y] >= 30) {
							m_maze[x][y] |= DIR.UP.value();
							m_maze[x][y+1] |= DIR.DOWN.value();
							moved = true;
							y += 1;
						}
					}
					break;

				case 4: // Left
					if (x - 1 < 0)
						continue;
					if (!((0 == y || m_height - 1 == y || 1 == x )
							&& ((Math.abs(m_startX - (x - 1)) + Math.abs(m_startY - y) < m_minimumTrailLength))))
					{
						if (isDeadlock || (m_maze[x-x][y] & DIR.RIGHT.value()) == 0 || m_maze[x][y] >= 30) {
							m_maze[x][y] |= DIR.LEFT.value();
							m_maze[x-1][y] |= DIR.RIGHT.value();
							moved = true;
							x -= 1;
						}
					}

					break;

				default:
					break;
				}
			}

			m_maze[x][y] |= 1;
			lastVisitedX = x;
			lastVisitedY = y;
		}
		while (0 != x && (m_width - 1) != x && (m_height -1) != y && 0 != y);
		m_end = new Point(x, y, null);
	}

	 @Override
	  public boolean equals(Object o)
	  {
		 MazeMatrix other = (MazeMatrix)o;
		 if (m_height != other.height()) {
			 return false;
		 }
		 if (m_width != other.width()) {
			 return false;
		 }
		 for(int i = 0; i < m_maze.length; ++i)
		 {
			 for (int j = 0; i < m_maze[0].length; ++j)
			 {
				 if (m_maze[i][j] != other.getMaze()[i][j]) {
					 return false;
				 }
			 }
		 }
		 
		 if (m_startX != other.getStartX()) {
			 return false;
		 }
		 return m_startY == other.getStartY(); 
	  }
	 
	@Override
	public Point getEndPoint() {
		return m_end;
	}
}