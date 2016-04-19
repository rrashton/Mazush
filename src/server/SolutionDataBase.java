package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import model.IMazeSolveStrategy;
import model.MazeMatrix;
import model.Point;

/**
 * Represents a database for maze solutions
 * @author Roy Rashti
 *
 */
public class SolutionDataBase {

	// Filename of the database
	private static String dbName = "solutions.db";
	private HashMap<Pair<MazeMatrix, Point>, Point> m_dict;
	
	/**
	 * Serializes the current database that's kept on the RAM to the HDD
	 */
	private void serializeToFile()
	{
		ObjectOutputStream objectOStream = null;
		FileOutputStream fileOStream = null;
		try {
		fileOStream = new FileOutputStream(dbName, false);
		objectOStream = new ObjectOutputStream(fileOStream);
		objectOStream.writeObject(m_dict);
		} catch (Exception e) {
			
		}
		 try {
			 if (objectOStream != null)
			 {
				 objectOStream.close();
			 }
		 } catch (Exception e ) {}
		 
		 try {
			 if (fileOStream != null)
			 {
				 fileOStream.close();
			 }
		 } catch (Exception e ) {}
	}
	
	/**
	 * A static function that deserializes a database from the HDD
	 * @return
	 */
	public static SolutionDataBase fromFile()
	{
		FileInputStream streamIn = null;
		ObjectInputStream objectinputstream = null;
		try {
		 streamIn = new FileInputStream(dbName);
		    objectinputstream = new ObjectInputStream(streamIn);
		    @SuppressWarnings("unchecked")
			HashMap<Pair<MazeMatrix, model.Point>, model.Point> sol = (HashMap<Pair<MazeMatrix, model.Point>, model.Point>) objectinputstream.readObject();
		    streamIn.close();
		    objectinputstream.close();
		return new SolutionDataBase(sol);
		}
		catch (Exception e)
		{
			try {
			if (streamIn != null)
				streamIn.close();
			if (objectinputstream != null)
				objectinputstream.close();
			}
			catch (Exception a) {}
			return new SolutionDataBase();
		}
	}
	
	/**
	 * Default c'tor
	 */
	public SolutionDataBase()
	{
		m_dict = new HashMap<Pair<MazeMatrix, model.Point>, model.Point>(); 	
	}
	
	/**
	 * Constructs the object and assigns an existing hashmap of solutions
	 * @param solutions The existing solutions in an hashmap
	 */
	public SolutionDataBase(HashMap<Pair<MazeMatrix, model.Point>, model.Point> solutions)
	{
		m_dict = solutions;
	}
	
	/**
	 * Returns the solution of a maze. If exists in the database - returns it,
	 * otherwise solves it and adds it to the database
	 * @param matrix		The matrix that needs to be solved
	 * @param startingPoint	The starting point within the matrix
	 * @return	Solution for the maze and starting point
	 */
	public Point getSolution(MazeMatrix matrix, Point startingPoint, char solverAlgorithm)
	{
		Pair<MazeMatrix, Point> p = new Pair<MazeMatrix, Point>(matrix, startingPoint);
		Point solution = null;
		try {
			solution = m_dict.get(p);
		} catch (Exception e)
		{
			System.out.println(e);
		}
		
		if (null != solution) {
			System.out.println("Cached solution has been found");
			return solution;
		}
		
		IMazeSolveStrategy solver = (solverAlgorithm == 'b' || solverAlgorithm == 'B') ? new BFS() : new AStar();
		solution = solver.solve(matrix, startingPoint);
		m_dict.put(p, solution);
		serializeToFile();	
		return solution;
		
	}
	

}
