import java.util.PriorityQueue;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.io.*;
import java.util.*;

//Name and ID numbers
//Liam Barclay 		126 8723
//Dalyn Anderson 	128 6946

/*
The MakeRuns class, this class is used to make the file with the runs inside
The end of run character is random, and is printed at the top of the file
This is so the mergeruns program can read the end of run character and save it, before moving
on to the data
*/

public class MakeRuns
{
	public static void main(String [] args)
	{
		if(args.length != 2)
		{
	      	System.err.println("Usage:  java MakeRuns <integer-k> <filename>");
	      	return;
    	}
	    try
		{
		
			String uniqueID = UUID.randomUUID().toString();
			int runsCount = 0;
	    	int maxSize = Integer.parseInt(args[0]);
	    	//if the k has been set to 0 or below, throw an error and stop.
	    	if(maxSize < 1)
	    	{
	    		System.err.println("Error: k cannot be less than 1");
	    		System.err.println("Usage:  java MakeRuns <integer-k> <filename>");
	    		return;
	    	}
	    	
	    	//Creates the required files, heaps, readers and writers for the application
	    	String filename = args[1];
			MinHeap heap = new MinHeap(maxSize);
			BufferedReader br = new BufferedReader(new FileReader(filename));
	   		String s=br.readLine();	   		
	   		BufferedWriter writer;
	   		  		
	   		//While there is more data to read in and the heap is not full yet
	      	while(!heap.full() && s!=null)
			{
				//Add the lines to the min heap.
				heap.add(s);
				s=br.readLine();
	      	}
	      	
	      	//Creates a new file and opens a writer to this file
	      	//This file will contain the final runs.
	      	File file = new File(args[1]+".runs");
	      	writer = new BufferedWriter(new FileWriter(file));
	      	writer.write(uniqueID);
	      	writer.newLine();
	      	while(true)
	      	{
	      		if(s==null)
	      		{
	      			//If s is null, all our remaining data is in the heap
	      			for(int i = 0; i < heap.maxSize(); i++)
	      			{	
	      				//For the size of the heap, take the items out in priority order.
	      				String out = heap.retreive();
			      		writer.write(out);
			      		writer.newLine();
	      			}
	      		
	      			if(heap.size() != 0)
	      			{
	      				//If there are any items left in the heap that have been 'cutoff'
	      	  			writer.write(uniqueID);
	      	  			writer.newLine();
	      	  			heap.resetMax();
	      	  			runsCount++;
	      	  			
	      	  			while(heap.size() != 0)
	      				{
	      					//Print these out two in their respective order as a new run.
	      					String out = heap.retreive();
			      			writer.write(out);
			      			writer.newLine();
	      				}
	      			}	      	  		
	      			break;
	      		}
	      		
	      		//Write the next lowest priority data to the file
	      		writer.write(heap.retreive());
	      		writer.newLine();
	      	  	heap.add(s);
	      	  	s=br.readLine();
	      	  	//If the abstract heap size is 0, we have no more data that could fit into this run.
	      	  	if(heap.maxSize() == 0)
	      	  	{
	      	  		writer.write(uniqueID);
	      	  		runsCount++;
	      	  		writer.newLine();
	      	  		heap.resetMax();

	      	  	} 		
	      	}
	      	//There is no more data to print to file
	      	//Signal end of the last run.
	      	writer.write(uniqueID);
	      	runsCount++;
	      	writer.newLine();
	      	//Close the writer and print to standard error the number of runs.
	      	writer.close();
	      	System.err.println("Total number of runs: "+ runsCount);
		}
	    catch(Exception e)
	    {
	    	System.err.println(e.getMessage());
	    }	
	}
}
	

/*
This class is for the minheap priority queue.

It extends the built in priority queue class, but with an extension
to add custom features.
*/
class MinHeap extends PriorityQueue<StringNode>
{
	private int originalMax;
	private int max;
	private String prev;
	
	//Creates a new minheap with i as the abstract size
	public MinHeap(int i)
	{
		//Creates a new priorityQueue with size i
		super(i);
		originalMax = i;
		max = i;
		prev = null;
	}

	//Returns true if the queue is 'full', false if there are less items than maximum
	public boolean full()
	{
		if(this.size() < max) return false;
		else return true;
	}
	
	//Resets the max number of items allowed in the queue, this essentially restores the queue.
	public void resetMax() 
	{
	 	max = originalMax;
	 	Iterator it = super.iterator();
	 	StringNode n;
	 	//Iterate over all items in the queue and reset their flags to false so they can be 'seen'
	 	while(it.hasNext())
	 	{
	 		n = (StringNode)it.next();
	 		n.setFlag(false);
	 	}
	}
	
	//Returns the maxSize of the queue.
	public int maxSize()	{ return max; }
	
	//Retreives and removes the head of the heap
	public String retreive()
	{
		//Saves the item about to be removed from the queue as the prev
		prev = super.poll().getValue();
		return prev;
	}
	
	//Add the given string into the priority queue.
	public boolean add(String s)
	{
		StringNode sn = new StringNode(s);	
		//Checks to see if this is the first filling of the queue
		if(prev!=null)
		{
			//if s is less than the item that just left the queue, store it at the bottom
			//of the queue, and reduce the visible size of the queue.
			if(s.compareTo(prev) < 0)
	  		{
	  			sn.setFlag(true);
	  			super.offer(sn);
	 			max--;
	 			return false;
	 		}
	 		
	  	}
	  	//put the node in the heap.
	 	super.offer(sn);
	 	return true;
	}
}


/*
The class for StringNode, this string node store the value, a string.  As well
as a flag which indicates whether the node is currently visible in the 
priority queue.

This Stringnode extends itself from the comparable datatype.
*/
class StringNode implements Comparable<StringNode>
{
	private String value;
	private boolean flag;
	
	//Creates a new StringNode with s as the value of the node.
	public StringNode(String s)
	{
		value = s;
		flag = false;
	}
	
	//The compare method for comparing one string node to another.
	public int compareTo(StringNode other)
	{
		//If the flag is true (item to be 'invisible') and the item comparing to is also 'invisible'
		//Keep in heap order by store the higher value lower in heap order.
		if(other.flag == true && flag == true)
		{
			return value.compareTo(other.value);
		}
		//if only this items flag is true, store below the 'visible item' regardless
		if(flag == true)
		{
			return 1;
		}
		//if this item is 'visible' but not the other item, store this item above regardless
		if(other.flag == true)
		{
			return -1;
		}
		//Otherwise both items are visible, so store in heap order.
		return value.compareTo(other.value);
	}
	
	//Returns the value of the node
	public String getValue()
	{
		return value;
	}
	
	//Sets the flag of the node to true 'invisible' or false 'visible'
	public void setFlag(boolean f)
	{
		flag = f;
	}
}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

