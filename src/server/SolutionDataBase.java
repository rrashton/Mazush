package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import model.BFS;
import model.MazeMatrix;
import model.Point;


public class SolutionDataBase {

	private static String dbName = "solutions.db";
	
	private void serializeToFile()
	{
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try {
		fout = new FileOutputStream(dbName, false);
		oos = new ObjectOutputStream(fout);
		oos.writeObject(m_dict);
		} catch (Exception e) {
			
		}
		 try {
			 if (oos != null)
			 {
				 oos.close();
			 }
		 } catch (Exception e ) {}
		 
		 try {
			 if (fout != null)
			 {
				 fout.close();
			 }
		 } catch (Exception e ) {}
	}
	
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
	
	public SolutionDataBase()
	{
		m_dict = new HashMap<Pair<MazeMatrix, model.Point>, model.Point>(); 	
	}
	
	public SolutionDataBase(HashMap<Pair<MazeMatrix, model.Point>, model.Point> solutions)
	{
		m_dict = solutions;
	}
	
	public Point getSolution(MazeMatrix matrix, Point startingPoint)
	{
		System.out.println("Trying to solve");
		Pair<MazeMatrix, Point> p = new Pair<MazeMatrix, Point>(matrix, startingPoint);
		Point solution = null;
		try {
			solution = m_dict.get(p);
		} catch (Exception e)
		{
			System.out.println(e);
		}
		
		if (null != solution) {
			System.out.println("Already existed! Cache is great!!");
			return solution;
		}
		
		BFS b = new BFS();
		solution = b.solve(matrix, startingPoint);
		m_dict.put(p, solution);
		serializeToFile();
		return solution;
		
	}
	private HashMap<Pair<MazeMatrix, model.Point>, model.Point> m_dict;
}
