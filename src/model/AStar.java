//package model;
//
//public class AStar implements IMazeSolveStrategy
//{
//
//	public int compare(Node node1, Node node2) {
//    	return (int) (node1.priority - node2.priority);
//    }
//	
//	@Override
//	public Point solve(IMazeGenerator matrix, Point start) {
//		//    final PriorityQueue<Node> pQueue = new PriorityQueue<Node>(10, new Comparator<Node>() {
//        
//    });
//    
//    Node startNode = searchDomain.getInitialNode(start);
//    final Map<Node, Double> costSoFar = new HashMap<Node, Double>();
//    costSoFar.put(startNode, 0.0);
//    
//    Node current = startNode;
//    pQueue.add(current);
//    int stepsCounter = -1;
//    while (pQueue.size() != 0) {
//
//        current = pQueue.poll();
//        stepsCounter++;
//        
//        boolean reachedStepsAmount = steps > 0 && stepsCounter == steps;
//
//        if (reachedStepsAmount || searchDomain.isTarget(current)) {
//        	return current;
//        }
//        
//        final Node currentCopy = current;
//        
//        searchDomain.foreachSuccessor(current, new SuccessorLogic() {
//        	@Override
//        	public void checkSuccessor(Node successor, double distanceToEnd) {
//        		double newCost = costSoFar.get(currentCopy) + 1;
//        		
//        		if((!costSoFar.containsKey(successor) || newCost < costSoFar.get(successor))) {
//            		costSoFar.put(successor, newCost);
//            		successor.priority = newCost + distanceToEnd;
//            		successor.parent = currentCopy;
//                    pQueue.add(successor);
//            	}
//        	}
//        });
//    }
//    
//    return null;
//    }
//
//}       
//	}
//
//}
