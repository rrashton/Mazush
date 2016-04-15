package model;

public interface IMazeGenerator {
	public int[][] getMaze();

	public int getStartY();

	public int getStartX();
	
	public int height();
	
	public int width();
	
	public Point getEndPoint();
	
	public static enum DIR {
		RIGHT(2), LEFT(4), UP(8), DOWN(16);

		private final int dir;
		DIR(int dir) { this.dir = dir; }
		public int value() { return dir; }
	}

}
