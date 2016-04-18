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
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import model.BFS;
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
					//TODO: Throw this code out to a function
					PaintListener pa = new PaintListener(){ 
						public void paintControl(PaintEvent e){
							movePlayer(e.gc, p, IMazeGenerator.DIR.LEFT);
						}
					};
					m_ui.paint(pa);
					break; 
				}
				case SWT.ARROW_RIGHT: {	
					System.out.println("right");
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
							
							Point trail = remoteSolveMaze();
							drawSolution(e.gc, p, steps, trail);
							m_ui.drawPlayer(e.gc, p, SWT.COLOR_BLUE);
						}
					};
					m_ui.paint(pa);
					break; 
				} 

				default:
					break;
				}

			} 

		};

		m_ui.registerKeyPressListener(l);
	}


	private Point remoteSolveMaze()
	{
		try{
			System.out.println("Attempting connection");
			Socket s = new Socket("127.0.0.1", Server.PORT);
			System.out.println("Connected");
			OutputStream os = s.getOutputStream();
			System.out.println("Sending request");
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(m_matrix);
			oos.writeObject(p.actualLocation);
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			

			System.out.println("Trying to read answer");
			model.Point solvedPath = null;
			while (solvedPath == null)
			{
				solvedPath = (model.Point)ois.readObject();
			}
			System.out.println("Read answer! it is (" + solvedPath.getX() +"," + solvedPath.getY()+")");
			
			
			oos.close();		
			s.close();
			
			return solvedPath;
			
			}
		catch(Exception e){
			System.out.println(e);}
		return null;
	}
	
	private Player initializePlayer()
	{
		//TODO: Fix the m_size to a func
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
