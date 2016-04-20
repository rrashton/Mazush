package model;

import server.ISolveable;

/**
 * Strategy interface for maze-solving
 * @author Roy Rashti
 *
 */
public interface IMazeSolveStrategy {

	/**
	 * Solve the maze
	 * @param board		ISolveable object that contains the question that needs answer
	 * @param start		Beginning of the board
	 * @return			Point represents a chain of linked nodes that are the solution
	 */
	public Point solve(ISolveable board, Point start);
}
