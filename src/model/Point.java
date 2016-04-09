/**
 * Represents a single 'chart' point (logically represents an (x,y) location) 
 *
 */
package model;
public class Point {
	private int m_x;
	private int m_y;
	private Point m_parent;
	
	 public Point(int x, int y, Point parent) {
	     m_x = x;
	     m_y = y;
	     m_parent = parent;
	 }
	
	 public Point getParent() {
	     return m_parent;
	 }
	 
	 public void setX(int x) {m_x = x;}
	 public void setY(int y) {m_y = y;}
	 public void addX(int x) {m_x += x;}
	 public void addY(int y) {m_y += y;}
	 public int getX() {return m_x;}
	 public int getY() {return m_y;}
}
