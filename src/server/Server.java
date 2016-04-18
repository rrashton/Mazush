package server;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.plaf.ActionMapUIResource;

import controller.Controller;
import model.BFS;
import model.MazeMatrix;
import model.Point;

import java.io.*;

class WorkerThread implements Runnable {
	private SolutionDataBase db = SolutionDataBase.fromFile();
	private Socket m_sock;  
	
	public WorkerThread(Socket s){  
		this.m_sock = s;  
	}  
	public void run() {  
		try {
			System.out.println("Accepted a connection");
			InputStream is = m_sock.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			model.MazeMatrix matrix = null;
			model.Point actualLocation = null;
			System.out.println("Waiting for it");
			while (matrix == null)
			{
				matrix = (model.MazeMatrix)ois.readObject();
			}
			System.out.println("Got 1");
			while (null == actualLocation)
			{
				actualLocation = (model.Point)ois.readObject();
			}
			System.out.println("Got 2");
			model.Point ret;
			if (matrix ==null || actualLocation == null)
			{
				System.out.println("Error fuck it");
				ret = new Point(0, 0, null);
			}
			else {
			ret = db.getSolution(matrix, actualLocation);
			}

			OutputStream os = m_sock.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			oos.writeObject(ret);

			is.close();
			m_sock.close();
		}
		catch (Exception e)
		{

		}

	}    
}  

public class Server{
	public static final int PORT = 5525;
	private static final int THREAD_POOL_SIZE = 50;
	
	
	public static void listen() {

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			ServerSocket ss = new ServerSocket(PORT);
			System.out.println("Server is listening");

			Socket s = null;
			while (true)
			{
				s = ss.accept();
				Runnable work = new WorkerThread(s);
				executor.execute(work);
			}
		}
		catch(Exception e){System.out.println(e);}
	}

	public static void main(String[] args)
	{
		Server.listen();
	}
}

