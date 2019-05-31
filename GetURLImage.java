// Code taken from class notes

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

public class GetURLImage {

	URL url = null;
	File outputImageFile = null;
	public static BufferedImage image = null;

	public static void fetchImageFromURL (URL url) {
		try {
			// Read from a URL
			image = ImageIO.read(url);
		} catch (IOException e) {
		} // catch

	} // fetchImageFromURL

	// Create a URL from the specified address, open a connection to it,
	// and then display information about the URL.
	public GetURLImage(String link) 
			throws MalformedURLException, IOException    {

		URL url = new URL(link);
		String fileName = link.substring(link.lastIndexOf('/')+1, link.length());
		String fileEnd = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());
		FileOutputStream imageFile = new FileOutputStream(fileName);
		fetchImageFromURL(url);
		ImageIO.write(image, fileEnd, imageFile);
		imageFile.close();

	} // GetURLImage

} // GetURLImage
