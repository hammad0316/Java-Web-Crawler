import java.io.IOException;

public class Phase3 {

	public static void main(String[] args) throws IOException{
		
		int i = 0, j;
		String arg;
		char flag;
		boolean vflag = false;
		String outFile = "";
		String inFile = "";

		//read the arg files for flags 
		while (i < args.length && args[i].startsWith("-")) {

			arg = args[i++];

			if(arg.equals("-i")) {
				if(i < args.length)
					inFile = args[i++];
				if (vflag)
					System.out.println("output file = " + inFile);
			}
			else if(arg.equals("-o")) {
				if(i < args.length) 
					outFile = args[i++];
				else
					System.err.println("-o requires a filename");
				if (vflag)
					System.out.println("output file = " + outFile);	
			}
		}
		
		//start up the programs sign in screen
		LoginGUI signIn = new LoginGUI(inFile, outFile);
		
	}
	
}
