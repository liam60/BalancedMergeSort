import java.util.PriorityQueue;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.io.*;
import java.util.*;

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
	      				String out = heap.retreive();
			      		writer.write(out);
			      		writer.newLine();
			      	  	System.out.println(out);
	      			}
	      			break;
	      		}
	      		

	      		String out = heap.retreive();

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


class MinHeap extends PriorityQueue<StringNode>
{
	private int originalMax;
	private int max;
	private String prev;
	private String[] storage;
	private int storageIndex;
	
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
	 	Iterator it = super.iterator();
	 	StringNode n;
	 	while(it.hasNext())
	 	{
	 		n = (StringNode)it.next();
	 		n.setFlag(false);
	 	}
	 	
	 	storageIndex = 0;
	}
	public int maxSize()	{ return max; }
	
	//Retreives and removes the head of the heap
	public String retreive()
	{

		prev = super.poll().getValue();

		return prev;
	}
	
	public boolean add(String s)
	{
		StringNode sn = new StringNode(s);	
		if(prev!=null)
		{
			if(s.compareTo(prev) < 0)
	  		{
	  			sn.setFlag(true);
	  			super.add(sn);
	 			max--;
	 			return false;
	 		}
	 		
	  	}
	  	//put in heap
	 	super.add(sn);
	 	return true;
	}
}

class StringNode implements Comparable<StringNode>
{
	private String value;
	private boolean flag;
	
	public StringNode(String s)
	{
		value = s;
		flag = false;
	}
	
	public int compareTo(StringNode other)
	{
		if(other.flag == true && flag == true)
		{
			return value.compareTo(other.value);
		}
		if(flag == true)
		{
			return 1;
		}
		if(other.flag == true)
		{
			return -1;
		}

		return value.compareTo(other.value);
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setFlag(boolean f)
	{
		flag = f;
	}
}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

