import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * Represents a maze.
 * Creates a random two dimensional int array describing a maze,
 * whereas 1 represents a 'train' 0 represents a wall.
 *
 */
public class MazeMatrix {

	final static int MIN_EXIT_DISTANCE_FROM_ENTRY = 20;
	
	private int m_startX;
	private int m_startY;
	private int m_height;
	private int m_width;
	private int[][] m_maze;
	private boolean m_isMazeInitialized;


	/**
	 * C'tor, initializes necessary members
	 * @param height	The height of the maze
	 * @param width		The width of the maze
	 */
	public MazeMatrix(int height, int width)
	{
		m_height = height;
		m_width = width;	
		m_isMazeInitialized = false;
	}


	public int getStartX() { return m_startX;}
	public int getStartY() { return m_startY;}	
	public int height() {return m_height;}
	public void setHeight (int height) {m_height = height;}
	public int width() {return m_width;}
	public void setWidth (int width) {m_width = width;}

	/**
	 * Returns the maze
	 * @return
	 */
	public int[][] getMaze()
	{
		if (!m_isMazeInitialized) {
			generateMaze();
			m_isMazeInitialized = true;
			return m_maze;
		}
		return m_maze;
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
	 * Iterate through the maze inner area, add random '1' to cells in the maze
	 * @return void
	 */
	private void randomizeMaze()
	{
		Random r = new Random();
		for(int i = 1; i < m_width - 1; ++i)
			for (int j = 1; j < m_height - 1; ++j)
			{
				m_maze[i][j] = m_maze[i][j] | ((0 == r.nextInt(3)) ? 1 : 0);
			}
	}

	/**
	 * Generate a maze with size of height * length. 
	 * Would be instantiated at a random access and exit point.
	 * @note 0 stands for a 'wall', 1 stands for a clear way.
	 */
	private void generateMaze() {
		m_maze = new int[m_height][m_width];
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

		// Start the path generation. Keep marking a path until we've reached an edge.
		do
		{
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
						&& ((Math.abs(m_startX - x) + Math.abs(m_startY - (y - 1)) < MIN_EXIT_DISTANCE_FROM_ENTRY))))
					{
						moved = true;
						y -= 1;
					}
					break;

				case 2: // Right
					if (x + 1 == m_width)
						continue;
					if (!((0 == y || y == (m_height - 1) ||(m_width - 2) == x)
							&& ((Math.abs(m_startX - (x + 1)) + Math.abs(m_startY - y) < MIN_EXIT_DISTANCE_FROM_ENTRY))))
					{
						moved = true;
						x += 1;
					}
					break;

				case 3: // Up
					if (y + 1 == m_height)
						continue;
					if (!(((m_height - 2) == y || 0 == x || (m_width - 1) == x)
							&& ((Math.abs(m_startX - x) + Math.abs(m_startY - (y + 1)) < MIN_EXIT_DISTANCE_FROM_ENTRY))))
					{
						moved = true;
						y += 1;
					}
					break;

				case 4: // Left
					if (x - 1 < 0)
						continue;
					if (!((0 == y || m_height - 1 == y || 1 == x )
							&& ((Math.abs(m_startX - (x - 1)) + Math.abs(m_startY - y) < MIN_EXIT_DISTANCE_FROM_ENTRY))))
					{
						moved = true;
						x -= 1;
					}

					break;

				default:
					break;
				}
			}

			m_maze[x][y] = 1;
		}
		while (0 != x && (m_width - 1) != x && (m_height -1) != y && 0 != y);
		// Add random paths to the maze and return it 
		randomizeMaze();
	}

	/*public static void main(String[] args)
	{
		MazeMatrix m = new MazeMatrix(20,20);
		int[][] arr = m.getMaze();
		for (int i = 0; i < m.m_height; ++i) {
			for (int j = 0; j < m.m_width; ++j) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		Point p = BFS.getPathBFS(m);

        while(p.getParent() != null) {
            System.out.println("("+p.getY() + ","+p.getX() + ")");
            p = p.getParent();
        }
        System.out.println("("+p.getY() + ","+p.getX() + ")");
	}
*/}