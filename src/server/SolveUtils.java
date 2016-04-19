package server;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.IMazeGenerator;
import model.Point;
import model.IMazeGenerator.DIR;
import model.MazeMatrix;

public class SolveUtils {


	public static LinkedList<Point> getAvailableDirections(IMazeGenerator matrix, Point location)
	{
		int[][] arr = matrix.getMaze();
		LinkedList<Point> avilableDirections = new LinkedList<Point>();
		// Add all of the available directions to the queue
        if(isVisitable(location, MazeMatrix.DIR.UP, arr)) {
            Point nextP = new Point(location.getX(), location.getY() + 1, location);
            avilableDirections.add(nextP);
        }

         if(isVisitable(location, MazeMatrix.DIR.DOWN, arr)) {
            Point nextP = new Point(location.getX(), location.getY() - 1, location);
            avilableDirections.add(nextP);
        }

        if(isVisitable(location, MazeMatrix.DIR.RIGHT, arr)) {
            Point nextP = new Point(location.getX() + 1 ,location.getY(), location);
            avilableDirections.add(nextP);
        }

        if(isVisitable(location, MazeMatrix.DIR.LEFT, arr)) {
            Point nextP = new Point(location.getX() - 1, location.getY(), location);
            avilableDirections.add(nextP);
        }
        
        return avilableDirections;
	}
	
	private static boolean isVisitable(Point current, MazeMatrix.DIR dir, int[][] arr) {
        return 0 != (arr[current.getX()][current.getY()] & dir.value());
    }
}
