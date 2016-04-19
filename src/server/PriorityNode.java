package server;

import model.Point;

public class PriorityNode
{
	public Point location;
	public int heuristicPriority;
	public PriorityNode(Point location, int heuristicPriority) {
		this.location = location;
		this.heuristicPriority = heuristicPriority;
	}
	
	@Override
	  public int hashCode() {
		 return location.hashCode();
	 }
	 
	 /*
	   * Override for equals(inherited from Object)
	   */
	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof PriorityNode)) {
	    	return false;
	    }
	    PriorityNode pairo = (PriorityNode) o;
	    return  this.location.equals(pairo.location);
	  }
}	