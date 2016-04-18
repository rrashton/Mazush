package view;

import java.io.Serializable;

import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Listener;

import model.Player;
import model.Point;

/**
 * UI Interface
 * @author Roy Rashti
 *
 */
public interface IMazeView extends Serializable{

	/**
	 * Paints a single cell corresponds to the given point
	 * @param gc	The gc that's used to draw
	 * @param color	The color in which the cell should be painted with
	 * @param p		The point coherent to the cell that needs to be painted
	 */
	public void paintCell(GC gc, int color, Point p);

	/**
	 * Paints the player in a given location
	 * @param gc	The gc that's used to draw
	 * @param p		The player object that holds the actual location
	 * @param color	The color in which the cell should be painted with
	 */
	public void drawPlayer(GC gc, Player p, int color);

	/**
	 * Draw the maze
	 * @param gc	The gc that's used to draw
	 */
	public void drawMaze(GC gc);
	
	/**
	 * Registers a paintlistener for the object which would be called when the view is opened or refreshed
	 * @param p	
	 */
	public void registerPainter(PaintListener p);
	
	/**
	 * Gets the callback that's being held in a paint listener to be called immediately
	 */
	public void paint(PaintListener p);
	
	/**
	 * Refreshes the view and calls the all of the paintlisteners callback
	 */
	public void refresh();
	
	/**
	 * Opens the view and makes it vibisle
	 */
	public void openView();
	
	/**
	 * Loop that keeps the ui alive until it disposes
	 */
	public void waitToDispose();

	/**
	 * Register a listener for the view that holds callback for keystokes event
	 * @param l	Listener that holds the callback
	 */
	public void registerKeyPressListener(Listener l);
	
	/**
	 * Runs an async operation on the display
	 * @param r	Runnable that should be exeuted
	 */
	public void runAsyncDisplay(Runnable r);
	/**
	 *	Retrieves the size of the view
	 *	@return Size in pixels 
	 */
	public int getSize();
}
