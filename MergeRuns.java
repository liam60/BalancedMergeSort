//Name: Dalyn Anderson
//ID: 1286946
//Name: Liam Barclay
//ID: 1268723

import java.io.*;
import java.util.*;


class MergeRuns{
	static int passes = 0;
	static String eor;
	public static void main(String [] args){
		if (args.length != 2)
		{
			System.err.println("Usage:  java MergeRuns k filename");
			return;
		}
		int k;
		String runs;

		try
		{
			k = Integer.parseInt(args[0]);
			runs = args[1];
			BufferedReader eorReader = new BufferedReader(new FileReader(runs));
			eor = eorReader.readLine();
			
		}
		catch(Exception ex)
		{
			System.err.println("Usage:  java MergeRuns k filename");
			return;
		}
		FileHeap queue = new FileHeap(k, eor);
		int lastmerge = 0;
		while(lastmerge!=1)
		{
			if (!distribute(k, runs, queue))
			{
				return;
			}
			//merges the runs into a file
			if(passes == 0)
			{
					runs = runs.substring(0, runs.length()-4)+"sorted";
			}
			lastmerge=merge(k, runs, queue);
			passes+=lastmerge;
		}
		System.err.println("Total number of merges (passes): "+passes);

		
	}
	
	//creates temp files and distributes runs to them
	private static boolean distribute(int k, String runs, FileHeap queue)
	{
		try
		{
			//create all the temporary files
			System.out.println("distributing runs");
			for(int i=0; i<k; i++)
			{
				File file = new File("temp_" + i);
				if(file.exists())
				{
					file.delete();
				}
				file.createNewFile();

			}

			BufferedReader br = new BufferedReader(new FileReader(runs));	//create a buffered reader with the file given
			if(passes == 0)
			{
				br.readLine();
			}
			String line=br.readLine();
			int fileIndex = 0;
			File writeFile = new File("temp_"+fileIndex);
			BufferedWriter out = new BufferedWriter(new FileWriter(writeFile, false));
			boolean flag = true;
			
			while(line !=null) //while there's still lines to distribute
			{
				while(writeFile.exists())	//used to start from temp_0 when it reaches temp_k
				{
					out = new BufferedWriter(new FileWriter(writeFile, !flag));
					
					while(line.compareTo(eor)!=0) //while still the same run
					{
						out.write(line);
						out.newLine();
						line=br.readLine();
					}
					
					out.write(line);
					out.newLine();
					out.close();
					if(flag)
						{
							queue.add(writeFile); //add to the priority queue
						}
					line=br.readLine();
					if(line == null)
					{
						break;
					}
					fileIndex++;
					writeFile = new File("temp_"+fileIndex);
				}
				flag = false;
				fileIndex = 0;
				writeFile = new File("temp_"+fileIndex);

			}
			
			return true;
		}

		
		catch(Exception ex)
		{
			System.err.println("failure of distributing runs");
			System.err.println(ex.getMessage());
			return false;
		}
	}
	
	//merges the runs in the temp files into larger runs in a sorted file
	private static int merge(int k, String sortedName, FileHeap queue)
	{
		try
		{
			System.out.println("Merging runs");
			queue.openReaders();
			BufferedWriter mergedWriter = new BufferedWriter(new FileWriter(sortedName));
			int passes = 0;
			
			//while there's still files in the queue ie. while there's still data to merge
			while (queue.size() > 0)
			{
				if(passes!=0)
				{
					mergedWriter.write(eor);
					mergedWriter.newLine();
				}
				//while there's still data in each run to merge
				while (queue.getMax()>0 && queue.size() > 0)
				{
					String value = queue.retrieve();
					if(value.compareTo(eor) != 0)
					{
						mergedWriter.write(value);
						mergedWriter.newLine();
					}
				}
				
				
				queue.resetMax();
				passes++;
			}
			//if passes == 1 then we know that was the last pass and now the data is in order so we wouldn't want to write an end of run line
			if(passes!=1)
			{
				mergedWriter.write(eor);
			}
			mergedWriter.close();
			
			//delete all the temporary files
			for(int i=0; i<k; i++)
			{
				File file = new File("temp_" + i);
				file.delete();

			}
			return passes;
		}

		catch(Exception ex)
		{
			System.err.println("failure of merging runs");
			System.err.println(ex.getMessage());
			return 999999999;
		}
	}
}

//Minheap for files
class FileHeap extends PriorityQueue<FileNode>
{
	int max;
	int origMax;
	String eor;
	public FileHeap(int k, String endOfRun)
	{
	super(k);
	max = k;
	origMax = k;
	eor = endOfRun;
	}
	//get the current 'max'
	public int getMax(){return max;}
	
	//retrieves the value from the node
	public String retrieve()
	{
		FileNode node = super.poll();
		String value = node.poll();
		//System.out.println(value);
		if(value.compareTo(eor) == 0)
		{
			node.setFlag(true);
			max--;
			String temp = node.getValue();
			if(temp != null)
			{
				super.add(node);
			}
		}
		else
		{
			super.add(node);
		}
		return value;
	}
	//adds a new file to the minheap
	public boolean add(File f)
	{
		FileNode node = new FileNode(f);
		super.add(node);
		return true;
	}
	//resets max to the original max resets the flags on all the nodes
	public void resetMax() 
	{
	 	max = origMax;
	 	
	 	Iterator it = super.iterator();
	 	FileNode n;
	 	while(it.hasNext())
	 	{
	 		n = (FileNode)it.next();
	 		n.setFlag(false);
	 	}
	}
	//opens readers in all the nodes in the heap
	public void openReaders()
	{
		Iterator it = super.iterator();
	 	FileNode n;
	 	while(it.hasNext())
	 	{
		 	n = (FileNode)it.next();
	 		n.openReader();
	 	}
	}
}

//nodes that hold a file and the 'topmost' value
class FileNode implements Comparable<FileNode>
{
	private String value;
	private File tempFile;
	boolean flag = false;
	BufferedReader persistRead;
	
	public FileNode(File f)
	{
		try
		{
			tempFile = f;
			BufferedReader read = new BufferedReader(new FileReader(tempFile));
			value = read.readLine();
			read.close();
		}
		catch(Exception ex)
		{
			System.err.println("exception constructing FileNode");
			System.err.println(ex.getMessage());
		}
	}
	
	//sets the flag to the argument given
	public void setFlag(boolean b)
	{
		flag = b;
	}
	
	//used to give the natural ordering to the heap (ordered by flag then value)
	public int compareTo(FileNode other)
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
	
	//returns the value without loading a new one
	public String getValue()
	{
		return value;
	}
	
	//returns the value currently being used for ordering in the heap then loads in a new value
	public String poll()
	{
		try
		{
			//persistRead = new BufferedReader(new FileReader(tempFile));
			String oldValue = value;
			value = persistRead.readLine();
			//persistRead.close();
			return oldValue;
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
			return "Exception when polling FileNode";
		}
	}
	
	//opens a reader for the node so it can keep its place when reading the file
	public void openReader()
	{
		try
		{
			persistRead = new BufferedReader(new FileReader(tempFile));
			persistRead.readLine();	//read the line already in value to prevent double lines
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}
}

