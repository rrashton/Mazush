package server;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.MazeMatrix;
import model.Point;
import java.io.*;


/**
 * Represents the server entity which holds a threadpool for the connections
 * @author Roy Rashti
 *
 */
public class Server{
	// The listening port
	public static final int PORT = 5525;

	// Maximum threadpool size
	private static final int THREAD_POOL_SIZE = 50;

	public static SolutionDataBase db;

	/**
	 * The main function that accepts connection and initializes sessions between the server and the client
	 */
	public static void listen() {

		
		System.out.println("Listening on port " + PORT);
		db = SolutionDataBase.fromFile();
		// Initialize threadpool
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			ServerSocket ss = new ServerSocket(PORT);
			Socket s = null;

			// Always keep accepting connections
			while (true)
			{
				s = ss.accept();
				Runnable work = new WorkerThread(s);
				executor.execute(work);
			}
		}
		catch(Exception e) {
			System.out.println(e);}
	}

	public static void main(String[] args)
	{
		Server.listen();
	}
}


/**
 * Represents a single worker thread that communicates with the client and solves the maze
 * @author Roy Rashti
 *
 */
class WorkerThread implements Runnable {

	private final char INVALID_ALGORITHM_VALUE = '~';
	private Socket m_sock;  

	public WorkerThread(Socket s){  
		this.m_sock = s;  
	}  
	public void run() {  
		try {

			System.out.println("Accepted a connection");
			InputStream is = m_sock.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			MazeMatrix matrix = null;
			Point actualLocation = null;
			char solverAlgorithm = INVALID_ALGORITHM_VALUE;
			while (null == matrix)
			{
				matrix = (model.MazeMatrix)ois.readObject();
			}
			while (null == actualLocation)
			{
				actualLocation = (model.Point)ois.readObject();
			}

			while (INVALID_ALGORITHM_VALUE == solverAlgorithm)
			{
				solverAlgorithm = (char)ois.readObject();
			}
			model.Point ret;
			if (matrix ==null || actualLocation == null)
			{
				ret = new Point(0, 0, null);
			}
			else {
				ret = Server.db.getSolution(matrix, actualLocation, solverAlgorithm);
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