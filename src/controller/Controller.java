package controller;


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import model.IMazeGenerator;
import view.IMazeView;
import view.UI;
import model.Player;
import model.Point;
import server.Server;

public class Controller {

	private IMazeView m_ui;
	private Player p;
	private IMazeGenerator m_matrix;

	public Controller(IMazeGenerator generator)
	{
		m_matrix = generator;
		m_ui = new UI(m_matrix, getUISize());
		p = initializePlayer();
	}

	private int getUISize()
	{
		String sizeInPX = JOptionPane.showInputDialog(
				"Enter Size In PIXELS. Invalid value would lead to default value of 400");
		int num = 400;
		try {
			num = Integer.parseInt(sizeInPX);
		} catch(NumberFormatException exc) {
		}

		return num;
	}

	private void drawSolution(GC gc, Player p, int limit, Point path)
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

	private void movePlayer(GC gc, Player p, IMazeGenerator.DIR dir)
	{
		m_ui.drawPlayer(gc, p, SWT.COLOR_WHITE);
		switch(dir.value())
		{
		case 4:
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.LEFT.value()) != 0)
			{
				p.x -= p.widthStepSize;
				p.actualLocation.addX(-1);
			}

			break;

		case 2:
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.RIGHT.value()) != 0) {
				p.x = p.x + p.widthStepSize;
				p.actualLocation.addX(1);

			}
			break;

		case 8:
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.UP.value()) != 0) {
				p.y = (p.y + p.heightStepSize);
				p.actualLocation.addY(1);
			}
			break;

		case 16:
			if ((m_matrix.getMaze()[p.actualLocation.getX()][p.actualLocation.getY()] & IMazeGenerator.DIR.DOWN.value()) != 0) 
			{
				p.y = p.y - p.heightStepSize;
				p.actualLocation.addY(-1);
			}
			break;

		default:
			break;
		}
		m_ui.drawPlayer(gc, p, SWT.COLOR_BLUE);
		if (p.actualLocation.equals(m_matrix.getEndPoint())) {
			JOptionPane.showMessageDialog(null, "You solved the maze!");
		}
	}

	public void initMaze()
	{
		PaintListener pa = new PaintListener(){ 
			public void paintControl(PaintEvent e){
				m_ui.drawMaze(e.gc);
				m_ui.drawPlayer(e.gc, p ,SWT.COLOR_BLUE);
				m_ui.paintCell(e.gc, SWT.COLOR_YELLOW, m_matrix.getEndPoint());
			}
		};

		m_ui.registerPainter(pa);
		m_ui.openView();
	}

	public void waitToDispose()
	{
		m_ui.waitToDispose();
	}

	public void registerInputListener()
	{
		Listener l = new Listener(){

			@Override
			public void handleEvent(Event event) {
				switch(event.keyCode) { 
				case SWT.ARROW_DOWN: {
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, IMazeGenerator.DIR.UP);
						}
					};
					m_ui.paint(pa);
					break; 
				}
				case SWT.ARROW_LEFT: {
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, IMazeGenerator.DIR.LEFT);
						}
					};
					m_ui.paint(pa);
					break; 
				}
				case SWT.ARROW_RIGHT: {	
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, IMazeGenerator.DIR.RIGHT);
						}
					};
					m_ui.paint(pa);
					break; 
				}
				case SWT.ARROW_UP: {	
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, IMazeGenerator.DIR.DOWN);
						}
					};
					m_ui.paint(pa);
					break; 
				} 

				case 'b':
				case 'B':
				case 'A':
				case 'a': {
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
							Runnable r = new Runnable() {
								
								@Override
								public void run() {
									Point trail = remoteSolveMaze((char)event.keyCode);
									Runnable innerRunnableDisplay = new Runnable() {
										
										@Override
										public void run() {
											drawSolution(e.gc, p, steps, trail);
											m_ui.drawPlayer(e.gc, p, SWT.COLOR_BLUE);											
										}
									};
									m_ui.runAsyncDisplay(innerRunnableDisplay);
									//innerRunnableDisplay.run();
											
								}
							};
							Thread t = new Thread(r);
							t.start();
						}
					};
					Runnable r = new Runnable() 
					{
						public void run() {
							m_ui.paint(pa);
						}
					};
					m_ui.runAsyncDisplay(r);

					break; 
				} 

				default:
					break;
				}

			} 

		};

		m_ui.registerKeyPressListener(l);
	}

	/**
	 * Gets the solution of the maze from the server
	 * @return The solution as a linked list of Points
	 */
	private Point remoteSolveMaze(char algorithm)
	{
		try{
			Socket s = new Socket("127.0.0.1", Server.PORT);
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(m_matrix);
			oos.writeObject(p.actualLocation);
			oos.writeObject(algorithm);
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);

			model.Point solvedPath = null;
			while (solvedPath == null)
			{
				solvedPath = (model.Point)ois.readObject();
			}
			oos.close();		
			s.close();

			return solvedPath;

		}
		catch(Exception e){
			System.out.println(e);}
		return null;
	}

	/**
	 * Initializes the player object and returns it
	 * @return A player initialized with the actual location and the relative location on the layout
	 */
	private Player initializePlayer()
	{
		int widthLineSize = (m_ui.getSize() - 100) / m_matrix.width();
		int heightLineSize = (m_ui.getSize()- 100) / m_matrix.height();

		return new Player(50 + m_matrix.getStartX() * widthLineSize + widthLineSize / 4,
				50 + m_matrix.getStartY() * heightLineSize + heightLineSize / 4, 
				widthLineSize / 2, 
				heightLineSize / 2, 
				widthLineSize, 
				heightLineSize,
				new Point(m_matrix.getStartX(), m_matrix.getStartY(), null));
	}

}
