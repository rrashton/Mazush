package view;

import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Listener;

import model.Player;
import model.Point;

public interface IMazeView {

	public void paintCell(GC gc, int color, Point p);

	public void drawPlayer(GC gc, Player p, int color);

	public void drawMaze(GC gc);
	
	public void registerPainter(PaintListener p);
	
	public void paint(PaintListener p);
	
	public void refresh();
	
	public void openView();
	
	public void waitToDispose();
	
	public void registerKeyPressListener(Listener l);
	
	public int getSize();
}
