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
	      	for(int i = 0; i<maxSize; i++)
			{
				//StringTokenizer st = new StringTokenizer(s,delims);
				//while(st.hasMoreTokens())
				heap.add(s);
				s=br.readLine();
	      	}
	      	  	
	      	
	      	System.out.println(heap.size());
	      	while(heap.Size != heap.maxSize())
	      	{
	      	  	System.out.println(heap.poll());

	      	  	System.out.println("");
	      	}
		}
	    catch(Exception e)
	    {
	    	System.err.println(e.getMessage());
	    }	
	}
	
	public void make(int i,String filename)
	{
		
	}
}


class MinHeap 
{
	PriorityQueue<String> heap;
	int max;
	String prev;
	String[] storage;
	int storageIndex;
	
	public MinHeap(int i)
	{
		max = i;
		heap = new PriorityQueue<String>();
		prev = null;
		storage = new String[max];
		storageIndex = 0;
	}
	
	public int size()
	{
		return heap.size();
	}
	
	public int maxSize()
	{
		return max;
	}
	
	
	//Retreives and removes the head of the heap
	public String poll()
	{
		prev = heap.poll();
		return prev;
	}
	
	public void add(String s)
	{
		//item read in < prev and size < max
		if(prev!=null || s.compareTo(prev) > 0)
	  	{
	  		storage[storageIndex].add(s);
	  		storageIndex++;
	 		max--;
	  	}
	  	else
	  	{
	 		//put in heap
	 		heap.add(s);
	  	}
		
	}
	
	public int getMax()
	{
		return max;
	}
}



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

