package main;

import controller.Controller;
import model.MazeMatrix;

public class main {
	public static void main(String[] args)
	{
		MazeMatrix m = new MazeMatrix(20, 20); 
		Controller c = new Controller(m);
		c.initMaze();
		c.registerInputListener();
		c.waitToDispose();
	}
}