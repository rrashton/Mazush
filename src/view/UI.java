package view;

import model.Player;
import model.IMazeGenerator;
import model.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class UI {

	public int m_size;
	private IMazeGenerator m_maze;
	public Shell shell;
	public Display display;
	public GridLayout layout;
	public UI(IMazeGenerator maze) {

		m_size = 400;
		layout = new GridLayout();
		layout.marginWidth = 200;
		layout.marginHeight = 200;
		layout.numColumns = 3;
	    display = new Display();
		m_maze = maze;
		m_maze.getMaze();
		shell = new Shell(display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setLayout(layout);
		shell.setText("Maze");

	}
	public void drawPlayer(GC gc, Player p, int color)
	{
		//Display display = Display.getCurrent();
		gc.setBackground(display.getSystemColor(color));
		gc.fillOval(p.x, p.y, p.xSize, p.ySize);
	}

	
	public void paintCell(GC gc, int color, Point p)
	{
		int widthLineSize = (m_size - 100) / m_maze.width();
		int heightLineSize = (m_size - 100) / m_maze.height();
		int xStartPoint = 50 + widthLineSize * p.getX();
		int yStartPoint = 50 + heightLineSize * p.getY();
		Display display = Display.getCurrent();
		gc.setBackground(display.getSystemColor(color));
		gc.fillRectangle(xStartPoint + 2, yStartPoint + 2, widthLineSize - 4, heightLineSize - 4);
	}
	
	public void drawMaze(GC gc)
	{   
		int[][] mazeArr = m_maze.getMaze();

		int widthLineSize = (m_size - 100) / m_maze.width();
		int heightLineSize = (m_size - 100) / m_maze.height();
		for (int i = 0; i < m_maze.width(); ++i)
		{
			for(int j = 0; j < m_maze.height(); ++j)
			{
				int widthStart = 50 + (i) * widthLineSize;
				int heightStart = 50 + (j) * heightLineSize;

				// If blocked to the right
				if ((m_maze.width() - 1) == i || 
						(0 == (mazeArr[i][j] & IMazeGenerator.DIR.RIGHT.value()) && 0 == (mazeArr[i + 1][j] & IMazeGenerator.DIR.LEFT.value()))) {
					gc.drawLine(widthStart + widthLineSize, heightStart, widthStart + widthLineSize,heightStart + heightLineSize);
				}

				// If blocked to the left
				if (0 == i || 
						(0 == (mazeArr[i][j] & IMazeGenerator.DIR.LEFT.value()) && 0 == (mazeArr[i - 1][j] & IMazeGenerator.DIR.RIGHT.value()))) {
					gc.drawLine(widthStart, heightStart, widthStart ,heightStart + heightLineSize);
				}

				// If blocked from down

				if (0 == j ||
						(0 == (mazeArr[i][j] & IMazeGenerator.DIR.DOWN.value()) && 0 == (mazeArr[i][j - 1] & IMazeGenerator.DIR.UP.value()))) {
					gc.drawLine(widthStart, heightStart, widthStart + widthLineSize, heightStart);
				}

				// If blocked from up
				if ((m_maze.height() - 1) == j || 
						(0 == (mazeArr[i][j] & IMazeGenerator.DIR.UP.value()) && 0 == (mazeArr[i][j + 1] & IMazeGenerator.DIR.DOWN.value()))) {
					gc.drawLine(widthStart, heightStart + heightLineSize, widthStart + widthLineSize, heightStart + heightLineSize);
				}

			}
		}
	}
}