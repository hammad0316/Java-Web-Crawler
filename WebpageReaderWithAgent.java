// This is code from the class notes. Modified to fit usage of Phase 2

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class WebpageReaderWithAgent {

	public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";

	public static InputStream getURLInputStream(String sURL) throws Exception {
		URLConnection oConnection = (new URL(sURL)).openConnection();
		oConnection.setRequestProperty("User-Agent", USER_AGENT);
		return oConnection.getInputStream();
	} // getURLInputStream

	public static BufferedReader read(String url) throws Exception {
		InputStream content = (InputStream)getURLInputStream(url);
		return new BufferedReader (new InputStreamReader(content));
	} // read

	public WebpageReaderWithAgent(String websiteLink) throws Exception{

		// Save file name from URL to use and create a file on computer with
		BufferedReader reader = read(websiteLink);
		String line = reader.readLine();

		BufferedWriter file = new BufferedWriter(new FileWriter("Result.txt", false));
		// Write to file and also count total number of lines in file
		while (line != null) {
			file.append(line + "\n");
			line = reader.readLine();
		} // while
		file.close();
		// Only do if the URL ends in .jpeg, .jpg, .gif, .png
		//		if(websiteLink.endsWith(".jpeg") || websiteLink.endsWith(".jpg") || websiteLink.endsWith(".gif") || websiteLink.endsWith(".png")){
		//			GetURLImage image = new GetURLImage(websiteLink);
		//		}

	}

} // WebpageReaderWithAgent
