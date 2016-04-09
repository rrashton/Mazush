package model;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class BFS { 


	
	/**
	 * Checks if the point is on the edge of the maze
	 * @param p				The point we wish to check
	 * @param arrWidth		Width of the array (range of x)
	 * @param arrHeight		Height of the array (range of y)
	 * @return				True if the point is located on the edge, false otherwise
	 */
	private static boolean isOnEdge(Point p, int arrWidth, int arrHeight)
	{
		int x = p.getX();
		int y = p.getY();
		return (0 == x || (arrWidth -1) == x || 0 == y || (arrHeight - 1) == y);
	}
	 
	private static boolean isEqual(Point p, Point other)
	{
		return (p.getX() == other.getX()) && (p.getY() == other.getY());
	}
	
	
	/**
	 * Finds a path from the entrance of the maze to it's exit.
	 * The function works by Breadth Search First (BFS) algorithm
	 * @param matrix	The matrix object on which we wish to run the algorithm on
	 * @return	A point describing the found exit, which is linked to the entire path of the trail.
	 * 			null if not found
	 */
    public static Point getPathBFS (MazeMatrix matrix) {

    	// Initialize the run. Insert the root node to the queue
    	Queue<Point> visitQueue = new LinkedList<Point>();
    	int[][] arr = matrix.getMaze();
    	Point startPoint = new Point(matrix.getStartX(), matrix.getStartY(), null);
    	
        visitQueue.add(startPoint);

        // Iterate the queue (Each level at a time, if we look at the maze's different paths as a binary tree)
        while(!visitQueue.isEmpty()) {
            Point p = visitQueue.remove();
            int currentX = p.getX();
            int currentY = p.getY();
            
            // If we've arrived at an edge which is not our starting point - it's the exit
            if (isOnEdge(p, matrix.width(), matrix.height()) && !isEqual(p, startPoint)) {
                return p;
            }
            
            
          
            // Add all of the available directions to the queue
            if(isVisitable(p, MazeMatrix.DIR.UP, arr)) {
                Point nextP = new Point(currentX, currentY + 1, p);
                visitQueue.add(nextP);
            }

             if(isVisitable(p, MazeMatrix.DIR.DOWN, arr)) {
                Point nextP = new Point(currentX, currentY - 1, p);
                visitQueue.add(nextP);
            }

            if(isVisitable(p, MazeMatrix.DIR.RIGHT, arr)) {
                Point nextP = new Point(currentX + 1 ,currentY, p);
                visitQueue.add(nextP);
            }

            if(isVisitable(p, MazeMatrix.DIR.LEFT, arr)) {
                Point nextP = new Point(currentX - 1, currentY, p);
                visitQueue.add(nextP);
            }
            arr[currentX][currentY] = 0;
        }
        return null;
    }


    private static boolean isVisitable(Point current, MazeMatrix.DIR dir, int[][] arr) {
        return 0 != (arr[current.getX()][current.getY()] & dir.value());
    }
}