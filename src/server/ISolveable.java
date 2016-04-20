package server;

import java.util.LinkedList;

import model.Point;

public interface ISolveable {
	
	public Point getEndPoint();
	
	public LinkedList<Point> getAvailableDirections(Point location);
}
