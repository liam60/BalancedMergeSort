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
	      		int i = Integer.parseInt(args[0]);
	      		String filename = args[1];
			PriorityQueue<String> heap = new PriorityQueue<String>(i);

			BufferedReader br = new BufferedReader(new FileReader(filename));
	      		String s=br.readLine();
	      		String delims = ".,;:!\" \t\n";
	      		while(s!=null)
			{
				StringTokenizer st = new StringTokenizer(s,delims);
				while(st.hasMoreTokens())
				heap.add(st.nextToken());
				s=br.readLine();
	      		}
	      		System.out.println(heap.size());
	      		while(heap.size() != 0) System.out.println(heap.poll());
	      	}
	      	catch(Exception e){
	      		System.err.println(e.getMessage());
	      	}
		
	}
	
	public void make(int i,String filename)
	{
		
	}
}
