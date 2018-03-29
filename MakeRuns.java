import java.util.PriorityQueue;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.io.*;

public class MakeRuns
{
	public static void main(String [] args)
	{
		if(args.length != 2)
		{
	      	System.err.println("Usage:  java MakeRuns <integer> <filename>");
	      	return;
    	}
	    try
		{
	    	int maxSize = Integer.parseInt(args[0]);
	    	String filename = args[1];
			MinHeap heap = new MinHeap(maxSize);

			BufferedReader br = new BufferedReader(new FileReader(filename));
	   		String s=br.readLine();
	   		String delims = ".,;:!\" \t\n";	 
	   		
	   		BufferedWriter writer;
	   		  		
	      	while(!heap.full() && s!=null)
			{
				//StringTokenizer st = new StringTokenizer(s,delims);
				//while(st.hasMoreTokens())
				heap.add(s);
				s=br.readLine();
	      	}
	      	
	      	File file = new File("output.runs");
	      	writer = new BufferedWriter(new FileWriter(file));
	      	
	      	System.out.println(heap.size());
	      	while(true)
	      	{
	      		if(s==null)
	      		{
	      			while(heap.size() != 0)
	      			{
	      				String out = heap.poll();
			      		writer.write(out);
			      		writer.newLine();
			      	  	System.out.println(out);
	      			}
	      			break;
	      		}
	      		
	      		String out = heap.poll();
	      		writer.write(out);
	      		writer.newLine();
	      	  	System.out.println(out);
	      	  	heap.add(s);
	      	  	s=br.readLine();
	      	  	if(heap.maxSize() == 0)
	      	  	{
	      	  		System.out.println("End of run");
	      	  		
	      	  		writer.write("End of run");
	      	  		writer.newLine();
	      	  		heap.resetMax();
	      	  	} 		
	      	}
	      	System.out.println("End of run");
	      	writer.write("End of run");
	      	writer.newLine();
	      	writer.close();
		}
	    catch(Exception e)
	    {
	    	System.err.println(e.getMessage());
	    }	
	}
}


class MinHeap extends PriorityQueue<Data>
{
	int originalMax;
	int max;
	String prev;
	String[] storage;
	int storageIndex;
	
	public MinHeap(int i)
	{
		originalMax = i;
		max = i;
		prev = null;
		storage = new String[max];
		storageIndex = 0;
	}

	public boolean full()
	{
		if(this.size() < max) return false;
		else return true;
	}
	
	public void resetMax() 
	{
	 	max = originalMax;
	 	prev = null;
	 	for(int i = 0; i < max; i++)
	 	{
	 		if(storage[i] != null)
	 		{
	 			this.add(storage[i]);
	 		}
	 		
	 	}
	 	storageIndex = 0;
	}
	public int maxSize()	{ return max; }
	
	//Retreives and removes the head of the heap
	public String poll()
	{
		prev = super.poll();
		return prev;
	}
	
	public boolean add(String s)
	{
		
		if(prev!=null) //Is there a better way than comparing to null every time?
		{
			if(s.compareTo(prev) < 0)
	  		{
	  			storage[storageIndex] = s;
	  			storageIndex++;
	 			max--;
	 			return false;
	 		}
	 		
	  	}
	  	//put in heap
	 	super.add(s);
	 	return true;
	}
}
/*
class Data implements Comparable<Data> {
  private final String message;
  private final int priority;

  public Data(String message, int priority) {
    this.message = message;
    this.priority = priority;
  }

  @Override
  int compareTo(Data other) {
    return Integer.valueOf(priority).compareTo(other.priority);
  }

  // also implement equals() and hashCode()
}
*/




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

