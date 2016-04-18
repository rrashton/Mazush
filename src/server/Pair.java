package server;

import java.io.Serializable;

/*
 * Represents a pair, which is an object constructed with two sub-objects
 * @author Roy Rashti
 */
public class Pair<F,S> implements Serializable{

  private final F first;
  private final S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  /*
   * F getter
   */
  public F getfirst() { return first; }
  
  /*
   * S getter
   */
  public S getRight() { return second; }

  /*
   * Override for hashcode (inherited from Object)
   */
  @Override
  public int hashCode() { return first.hashCode() ^ second.hashCode(); }

  /*
   * Override for equals(inherited from Object)
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) {
    	return false;
    }
    Pair pairo = (Pair) o;
    return this.first.equals(pairo.getfirst()) &&
           this.second.equals(pairo.getRight());
  }

}

