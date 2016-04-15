package main;

import javax.swing.JOptionPane;

import controller.Controller;
import model.MazeMatrix;

public class main {
	public static void main(String[] args)
	{
		int size = getMatrixSize();
		MazeMatrix m = new MazeMatrix(size, size); 
		Controller c = new Controller(m);
		c.initMaze();
		c.registerInputListener();
		c.waitToDispose();
	}
	
	private static int getMatrixSize()
	{
		String sizeInPX = JOptionPane.showInputDialog(
				"Enter matrix size, invalid value would lead to default value 20");
		int num = 20;
		try {
			num = Integer.parseInt(sizeInPX);
		} catch(NumberFormatException exc) {
		}
		
		return num;
	}
}

