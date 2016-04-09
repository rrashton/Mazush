package view;
import java.io.Console;

import model.MazeMatrix;
import model.BFS;
import model.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class UI {

	private class Player
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

		private int widthStepSize;
		private int heightStepSize;
		public Point actualLocation;
		public int x;
		public int y;
		public int xSize;
		public int ySize;
	}
	private int m_size;
	private MazeMatrix m_maze;

	public UI(MazeMatrix maze) {

		m_size = 400;
		GridLayout layout = new GridLayout();
		layout.marginWidth = 200;
		layout.marginHeight = 200;
		layout.numColumns = 3;
		Display display = new Display();
		m_maze = maze;
		m_maze.getMaze();
		Shell shell = new Shell(display);
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setLayout(layout);
		shell.setText("Maze");
		Player p = initializePlayer();

		
		display.addFilter(SWT.KeyDown, new Listener(){

			@Override
			public void handleEvent(Event event) {
				switch(event.keyCode) { 
				case SWT.ARROW_DOWN: {
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, MazeMatrix.DIR.UP);
						}
					};
					shell.addPaintListener(pa);
					shell.redraw();
					shell.update();
					shell.removePaintListener(pa);
					break; 
				}
				case SWT.ARROW_LEFT: {
					//TODO: Throw this code out to a function
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, MazeMatrix.DIR.LEFT);
						}
					};
					shell.addPaintListener(pa);
					shell.redraw();
					shell.update();
					shell.removePaintListener(pa);
					break; 
				}
				case SWT.ARROW_RIGHT: {	
					System.out.println("right");
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, MazeMatrix.DIR.RIGHT);
						}
					};
					shell.addPaintListener(pa);
					shell.redraw();
					shell.update();
					shell.removePaintListener(pa);
					break; 
				}
				case SWT.ARROW_UP: {	
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, MazeMatrix.DIR.DOWN);
						}
					};
					shell.addPaintListener(pa);
					shell.redraw();
					shell.update();
					shell.removePaintListener(pa);
					break; 
				} 
				
				case 'F':
				case 'f': {	
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							drawSolution(e.gc);
							drawPlayer(e.gc, p, SWT.COLOR_BLUE);
						}
					};
					shell.addPaintListener(pa);
					shell.redraw();
					shell.update();
					shell.removePaintListener(pa);
					break; 
				} 
				
				default:
					break;
				}
				
			} 

		});
		shell.addPaintListener(new PaintListener(){ 
			public void paintControl(PaintEvent e){
				drawMaze(e.gc);
				drawPlayer(e.gc, p ,SWT.COLOR_BLUE);
			}
		});

		shell.pack();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();

	}

	private Player initializePlayer()
	{
		int widthLineSize = (m_size - 100) / m_maze.width();
		int heightLineSize = (m_size - 100) / m_maze.height();

		return new Player(50 + m_maze.getStartX() * widthLineSize + widthLineSize / 4,
				50 + m_maze.getStartY() * heightLineSize + heightLineSize / 4, 
				widthLineSize / 2, 
				heightLineSize / 2, 
				widthLineSize, 
				heightLineSize,
				new Point(m_maze.getStartX(), m_maze.getStartY(), null));
	}

	void movePlayer(GC gc, Player p, MazeMatrix.DIR dir)
	{
		
		drawPlayer(gc, p, SWT.COLOR_WHITE);
		System.out.println("("+p.actualLocation.getX()+","+p.actualLocation.getY()+") = " + m_maze.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()]);
		switch(dir.value())
		{
		case 4:
			if ((m_maze.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & MazeMatrix.DIR.LEFT.value()) != 0)
			{
				p.x -= p.widthStepSize;
				p.actualLocation.addX(-1);
				System.out.println("LEFT x from " + p.x + " to " + (p.x - p.widthStepSize));
			}

		break;

		case 2:
			if ((m_maze.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & MazeMatrix.DIR.RIGHT.value()) != 0) {
				p.x = p.x + p.widthStepSize;
				p.actualLocation.addX(1);
				System.out.println("RIGHT x from " + p.x + " to " + (p.x + p.widthStepSize));
			}
		break;

		case 8:
			if ((m_maze.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & MazeMatrix.DIR.UP.value()) != 0) {
				System.out.println("Up Y from " + p.y + " to " + (p.y + p.heightStepSize));
				p.y = (p.y + p.heightStepSize);
				p.actualLocation.addY(1);
			}
		break;

		case 16:
			System.out.println("Checking down");
			if ((m_maze.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & MazeMatrix.DIR.DOWN.value()) != 0) 
			{
			System.out.println("Down Y from " + p.y + " to " + (p.y - p.heightStepSize));
			p.y = p.y - p.heightStepSize;
			p.actualLocation.addY(-1);
			}
		break;
		
		default:
			break;
		}
		drawPlayer(gc, p, SWT.COLOR_BLUE);

	}

	private void drawPlayer(GC gc, Player p, int color)
	{
		Display display = Display.getCurrent();
		gc.setBackground(display.getSystemColor(color));
		gc.fillOval(p.x, p.y, p.xSize, p.ySize);
	}

	private void drawSolution(GC gc)
	{
		Point p = BFS.getPathBFS(m_maze);
		while(p != null) {
			paintCell(gc, SWT.COLOR_GREEN, p);
			p = p.getParent();
		}
	}
	private void paintCell(GC gc, int color, Point p)
	{
		int widthLineSize = (m_size - 100) / m_maze.width();
		int heightLineSize = (m_size - 100) / m_maze.height();
		int xStartPoint = 50 + widthLineSize * p.getX();
		int yStartPoint = 50 + heightLineSize * p.getY();
		Display display = Display.getCurrent();
		gc.setBackground(display.getSystemColor(color));
		gc.fillRectangle(xStartPoint + 2, yStartPoint + 2, widthLineSize - 4, heightLineSize - 4);
	}
	
	private void drawMaze(GC gc)
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
						(0 == (mazeArr[i][j] & MazeMatrix.DIR.RIGHT.value()) && 0 == (mazeArr[i + 1][j] & MazeMatrix.DIR.LEFT.value()))) {
					gc.drawLine(widthStart + widthLineSize, heightStart, widthStart + widthLineSize,heightStart + heightLineSize);
				}

				// If blocked to the left
				if (0 == i || 
						(0 == (mazeArr[i][j] & MazeMatrix.DIR.LEFT.value()) && 0 == (mazeArr[i - 1][j] & MazeMatrix.DIR.RIGHT.value()))) {
					gc.drawLine(widthStart, heightStart, widthStart ,heightStart + heightLineSize);
				}

				// If blocked from down

				if (0 == j ||
						(0 == (mazeArr[i][j] & MazeMatrix.DIR.DOWN.value()) && 0 == (mazeArr[i][j - 1] & MazeMatrix.DIR.UP.value()))) {
					gc.drawLine(widthStart, heightStart, widthStart + widthLineSize, heightStart);
				}

				// If blocked from up
				if ((m_maze.height() - 1) == j || 
						(0 == (mazeArr[i][j] & MazeMatrix.DIR.UP.value()) && 0 == (mazeArr[i][j + 1] & MazeMatrix.DIR.DOWN.value()))) {
					gc.drawLine(widthStart, heightStart + heightLineSize, widthStart + widthLineSize, heightStart + heightLineSize);
				}

			}
		}
	}
/*	private void centerWindow(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();

		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}
*/
	@SuppressWarnings("unused")
	public static void main(String[] args) {


		MazeMatrix m = new MazeMatrix(17, 17);
		UI ex = new UI(m);

	}
}