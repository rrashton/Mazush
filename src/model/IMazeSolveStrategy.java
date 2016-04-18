package model;

/**
 * Strategy interface for maze-solving
 * @author Roy Rashti
 *
 */
public interface IMazeSolveStrategy {

	/**
	 * Solve the maze
	 * @param matrix	Mazegenerator algorithm to retrieve the maze
	 * @param start		Beginning of the maze
	 * @return			Point represents a chain of linked nodes that are the solution of the maze
	 */
	public Point solve(IMazeGenerator matrix, Point start);
}
