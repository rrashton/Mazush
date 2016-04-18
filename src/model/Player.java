package model;

/**
 * Represents a player in the maze
 * @author Roy Rashti
 *
 */
public class Player
{
	public Player(int x, int y, int xSize, int ySize, int widthStepSize, int heightStepSize, Point p)
	{
		actualLocation = p;
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
		this.widthStepSize = widthStepSize;
		this.heightStepSize = heightStepSize;
	}

	public int widthStepSize;
	public int heightStepSize;
	public Point actualLocation;
	public int x;
	public int y;
	public int xSize;
	public int ySize;
}
