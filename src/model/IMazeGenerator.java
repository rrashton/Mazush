package model;

import java.io.Serializable;

import server.ISolveable;

/**
 * Maze Generator interface
 * @author Roy Rashti
 */
public interface IMazeGenerator extends Serializable, ISolveable{
	
	/**
	 * Returns the maze, represented by a two dimensional array.
	 * Each cell is being used as an entrance to the nearby cells.
	 * The if the value of the cell & Direction value is not zero, it means 
	 * that one can access a nearby cell at that direction from the current cell
	 */
	public int[][] getMaze();
	
	/**
	 * Returns the start point's Y value
	 */
	public int getStartY();

	/**
	 * Returns the start point's X value
	 */
	public int getStartX();
	
	/**
	 * Returns the height of the maze (amount of cells)
	 */
	public int height();
	
	/**
	 * Returns the width of the maze (amount of cells)
	 */
	public int width();
	
	/**
	 * Returns the location of the end of the maze
	 */
	public Point getEndPoint();
	
	public static enum DIR {
		RIGHT(2), LEFT(4), UP(8), DOWN(16);

		private final int dir;
		DIR(int dir) { this.dir = dir; }
		public int value() { return dir; }
	}
}
