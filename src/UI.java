
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class UI {

	private MazeMatrix m_maze;
	
    public UI(Display display, MazeMatrix maze) {
        m_maze = maze;
        Shell shell = new Shell(display);
        drawMaze(shell);
        shell.setText("Maze");
        shell.setSize(500, 500);
        shell.setToolTipText("ASD");
        FillLayout fillLayout = new FillLayout();
        fillLayout.type = SWT.HORIZONTAL;
        shell.setLayout(fillLayout);
        centerWindow(shell);

        shell.open();

        while (!shell.isDisposed()) {
          if (!display.readAndDispatch()) {
            display.sleep();
          }
        }
    }

    private void drawMaze(Shell shell)
    {   
    	int[][] mazeArr = m_maze.getMaze();
    	
    	for (int i = 0; i < m_maze.height(); ++i) {
			for (int j = 0; j < m_maze.width(); ++j) {
				System.out.print(mazeArr[i][j] + " ");
			}
			System.out.print("\n");
		}
    	
    	
    	PaintListener p = new PaintListener(){
    			public void paintControl(PaintEvent e){ 
	              Rectangle clientArea = shell.getClientArea();
	              
	             
	              int widthLineSize = (clientArea.width - 100) / m_maze.width();
	              int heightLineSize = (clientArea.height - 100) / m_maze.height();
	              for (int i = 0; i < m_maze.width(); ++i)
	              {
	            	  for(int j = 0; j < m_maze.height(); ++j)
	            	  {
	            		  int widthStart = 50 + (i) * widthLineSize;
            			  int heightStart = 50 + (j) * heightLineSize;
            			  
	            		  // If blocked to the right
	            		  if ((m_maze.width() - 1) == i) {
	            			  if (0 == mazeArr[i][j]) {
	            				  e.gc.drawLine(widthStart + widthLineSize, heightStart, widthStart + widthLineSize,heightStart + heightLineSize);
	            			  }
	            		  }
	            		  else if(0 == mazeArr[i+1][j] && 0 != mazeArr[i][j])
	            		  {
	            			  e.gc.drawLine(widthStart + widthLineSize, heightStart, widthStart + widthLineSize,heightStart + heightLineSize);
	            		  }
	            		 
	            		  // If blocked to the left
	            		  if (0 == i) {
	            			  if (0 == mazeArr[i][j]) {
	            				  e.gc.drawLine(widthStart, heightStart, widthStart ,heightStart + heightLineSize);
	            			  }
	            		  } else if((0 == mazeArr[i-1][j] && 0 != mazeArr[i][j])) {
	            			  e.gc.drawLine(widthStart, heightStart, widthStart ,heightStart + heightLineSize);
	            		  }
	            		  
	            		  // If blocked from up
	            		  
	            		  if (0 == j) {
	            			  if (0 == mazeArr[i][j]) {
	            				  e.gc.drawLine(widthStart, heightStart, widthStart + widthLineSize, heightStart);
	            			  }
	            		  } else if (0 == mazeArr[i][j - 1] && 0 != mazeArr[i][j]) {
	            			  e.gc.drawLine(widthStart, heightStart, widthStart + widthLineSize, heightStart);
	            		  }
	            		  
	            		  // If blocked from down
	            		  if ((m_maze.height() - 1) == j) {
	            			  if (0 == mazeArr[i][j]) {
	            				  e.gc.drawLine(widthStart, heightStart + heightLineSize, widthStart + widthLineSize, heightStart + heightLineSize);
	            			  }
	            		  } else if (0 == mazeArr[i][j + 1] && 0 != mazeArr[i][j]) { 
	            			  e.gc.drawLine(widthStart, heightStart + heightLineSize, widthStart + widthLineSize, heightStart + heightLineSize);
	            		  }

	            	  }
	              }	         
	          }
    	};
    	 shell.addPaintListener(p); 
    	
    }
    private void centerWindow(Shell shell) {

        Rectangle bds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
    
        Display display = new Display();
        MazeMatrix m = new MazeMatrix(20, 20);
        UI ex = new UI(display,m);
        display.dispose();
    }
}