import java.util.PriorityQueue;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;

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
	      	while(!heap.full() && s!=null)
			{
				//StringTokenizer st = new StringTokenizer(s,delims);
				//while(st.hasMoreTokens())
				heap.add(s);
				s=br.readLine();
	      	}
	      	  	
	      	
	      	System.out.println(heap.size());
	      	/*while(heap.size() != heap.maxSize())
	      	{
	      	  	System.out.println(heap.poll());

	      	  	System.out.println("");
	      	}*/
		}
	    catch(Exception e)
	    {
	    	System.err.println(e.getMessage());
	    }	
	}
}


class MinHeap extends PriorityQueue<String>
{
	int max;
	String prev;
	String[] storage;
	int storageIndex;
	
	public MinHeap(int i)
	{
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
			if(s.compareTo(prev) > 0)
	  		{
	  			storage[storageIndex] = s;
	  			storageIndex++;
	 			max--;
	 		}	
	 		return false;
	  	}
 		//put in heap
	 	super.add(s);
	 	return true;
	  	
	}
}



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

