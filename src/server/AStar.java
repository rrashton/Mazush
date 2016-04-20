package server;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import model.IMazeGenerator;
import model.IMazeSolveStrategy;
import model.Point;

public class AStar implements IMazeSolveStrategy
{
	
		 
	private int getHeuristicPriority(ISolveable board, Point p)
	{
		int distance = Math.abs(p.getX() - board.getEndPoint().getX());
		distance += Math.abs(p.getY() - board.getEndPoint().getY());
		return distance;
	}

	@Override
	public Point solve(ISolveable board, Point start) {
		Comparator<PriorityNode> comp = new Comparator<PriorityNode>() 
		{
			public int compare(PriorityNode lhs, PriorityNode rhs) {
				return (int) (lhs.heuristicPriority - rhs.heuristicPriority);
			}
		};

		final PriorityQueue<PriorityNode> pQueue = new PriorityQueue<PriorityNode>(comp);
		// Given an odd warning when changed to int
		final HashMap<PriorityNode, Double> trailLen = new HashMap<PriorityNode, Double>();
		PriorityNode startNode = new PriorityNode(start, 0);
		trailLen.put(startNode, 0.0);

		pQueue.add(startNode);
		PriorityNode current = startNode;
		while(pQueue.size() > 0)
		{
            current = pQueue.poll();
			if (current.location.equals(board.getEndPoint())) {
				return current.location;
			}
			LinkedList<Point> nextPathOpts = board.getAvailableDirections(current.location);

			for(Point p : nextPathOpts) { 
				// Get current length and raise it by one (since we moved once more)
				double len = trailLen.get(current) + 1;
				PriorityNode n = new PriorityNode(p, getHeuristicPriority(board, p));
				// If the trail does not contain the key or it contains it as a longer path, update it
				if ((!trailLen.containsKey(n) || len < trailLen.get(n))) {
					trailLen.put(n, len);
            		n.heuristicPriority += len;
            		pQueue.add(n);
				}
			}
		}
		
		return null;
	}

}


