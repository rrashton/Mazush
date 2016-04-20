package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import model.IMazeGenerator;
import model.IMazeSolveStrategy;
import model.Point;

public class BFS implements IMazeSolveStrategy{ 
	/**
	 * Checks if the point is on the edge of the maze
	 * @param p				The point we wish to check
	 * @param arrWidth		Width of the array (range of x)
	 * @param arrHeight		Height of the array (range of y)
	 * @return				True if the point is located on the edge, false otherwise
	 */
	
	/**
	 * Finds a path from the entrance of the maze to it's exit.
	 * The function works by Best Search First (BFS) algorithm
	 * @param board	The matrix object on which we wish to run the algorithm on
	 * @return	A point describing the found exit, which is linked to the entire path of the trail.
	 * 			null if not found
	 */
	@Override
	public Point solve(ISolveable board, Point start) {
		final Queue<Point> optional = new LinkedList<Point>();
        final ArrayList<Point> nonOptionalPaths = new ArrayList<Point>();
		optional.add(start);
		Point current;
		while (optional.size() > 0) 
		{
			current = optional.poll();
			if (current.equals(board.getEndPoint())) {
				return current;
			}
			nonOptionalPaths.add(current);
			LinkedList<Point> nextPathOptions = board.getAvailableDirections(current);

			for(Point p : nextPathOptions) { 
				if (!optional.contains(current) && nonOptionalPaths.contains(current))
				{
					optional.add(p);
				}
			}
		}
        return null;
    }
}