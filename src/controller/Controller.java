package controller;


import java.util.ArrayList;

import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import model.BFS;
import model.IMazeGenerator;
import view.UI;
import model.Player;
import model.Point;

public class Controller {

	private UI m_ui;
	private Player p;
	private IMazeGenerator m_matrix;

	public Controller(IMazeGenerator generator)
	{
		m_matrix = generator;
		m_ui = new UI(m_matrix);
		p = initializePlayer();
	}

	public void drawSolution(GC gc, Player p, int limit, Point path)
	{
		limit += 1; //The current position is also printed as a marked junction
		int counter = 0;
		ArrayList<Point> trail = new ArrayList<Point>(); 
		while(path != null) {
			trail.add(path);
			path = path.getParent();
		}

		for(int i = trail.size(); (i > 0) && (limit != counter); --i, ++counter)
		{
			m_ui.paintCell(gc, SWT.COLOR_GREEN, trail.get(i - 1));	
		}

	}

	void movePlayer(GC gc, Player p, IMazeGenerator.DIR dir)
	{
		m_ui.drawPlayer(gc, p, SWT.COLOR_WHITE);
		System.out.println("("+p.actualLocation.getX()+","+p.actualLocation.getY()+") = " + m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()]);
		switch(dir.value())
		{
		case 4:
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.LEFT.value()) != 0)
			{
				p.x -= p.widthStepSize;
				p.actualLocation.addX(-1);
				System.out.println("LEFT x from " + p.x + " to " + (p.x - p.widthStepSize));
			}

			break;

		case 2:
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.RIGHT.value()) != 0) {
				p.x = p.x + p.widthStepSize;
				p.actualLocation.addX(1);
				System.out.println("RIGHT x from " + p.x + " to " + (p.x + p.widthStepSize));
			}
			break;

		case 8:
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.UP.value()) != 0) {
				System.out.println("Up Y from " + p.y + " to " + (p.y + p.heightStepSize));
				p.y = (p.y + p.heightStepSize);
				p.actualLocation.addY(1);
			}
			break;

		case 16:
			System.out.println("Checking down");
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.DOWN.value()) != 0) 
			{
				System.out.println("Down Y from " + p.y + " to " + (p.y - p.heightStepSize));
				p.y = p.y - p.heightStepSize;
				p.actualLocation.addY(-1);
			}
			break;

		default:
			break;
		}
		m_ui.drawPlayer(gc, p, SWT.COLOR_BLUE);

	}

	public void initMaze()
	{
		PaintListener pa = new PaintListener(){ 
			public void paintControl(PaintEvent e){
				m_ui.drawMaze(e.gc);
				m_ui.drawPlayer(e.gc, p ,SWT.COLOR_BLUE);
			}
		};
		m_ui.shell.addPaintListener(pa);

		m_ui.shell.pack();
		m_ui.shell.open ();
	}

	public void waitToDispose()
	{
		while (!m_ui.shell.isDisposed ()) {
			if (!m_ui.display.readAndDispatch ()) 
				m_ui.display.sleep ();
		}
		m_ui.display.dispose ();
	}
	public void registerInputListener()
	{
		Shell shell = m_ui.shell;

		m_ui.display.addFilter(SWT.KeyDown, new Listener(){

			@Override
			public void handleEvent(Event event) {
				switch(event.keyCode) { 
				case SWT.ARROW_DOWN: {
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, IMazeGenerator.DIR.UP);
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
							movePlayer(e.gc, p, IMazeGenerator.DIR.LEFT);
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
							movePlayer(e.gc, p, IMazeGenerator.DIR.RIGHT);
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
							movePlayer(e.gc, p, IMazeGenerator.DIR.DOWN);
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
					String numberOfSteps = JOptionPane.showInputDialog(
							"Enter amount of steps you wish to see. Illegal input will result a full solution");
					//
					int num = Integer.MAX_VALUE;
					try {
						num = Integer.parseInt(numberOfSteps);
					} catch(NumberFormatException exc) {
					}
					final int steps = num > 0 ? num : Integer.MAX_VALUE;
					PaintListener pa = new PaintListener(){

						public void paintControl(PaintEvent e){
							BFS b = new BFS();
							Point trail = b.solve(m_matrix, p.actualLocation);
							drawSolution(e.gc, p, steps, trail);
							m_ui.drawPlayer(e.gc, p, SWT.COLOR_BLUE);
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


		while (!shell.isDisposed ()) {
			if (!m_ui.display.readAndDispatch ()) {
				m_ui.display.sleep ();
			}
		}
		m_ui.display.dispose ();

	}

	private Player initializePlayer()
	{
		//TODO: Fix the m_size to a func
		int widthLineSize = (m_ui.m_size - 100) / m_matrix.width();
		int heightLineSize = (m_ui.m_size- 100) / m_matrix.height();

		return new Player(50 + m_matrix.getStartX() * widthLineSize + widthLineSize / 4,
				50 + m_matrix.getStartY() * heightLineSize + heightLineSize / 4, 
				widthLineSize / 2, 
				heightLineSize / 2, 
				widthLineSize, 
				heightLineSize,
				new Point(m_matrix.getStartX(), m_matrix.getStartY(), null));
	}

}
