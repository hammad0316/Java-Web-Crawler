import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

// sends link into webpagereaderwithagent, then reads through file made and creates auction objects, displays in mainframe

public class Query {

	//Search that was queued 
	private String search;
	//the datebase that stores all the items 
	private HashMap<String, ArrayList<AuctionItem>> dataBase;
	//text area that displays the searches
	JTextArea displayArea1;
	//the current user log into the system
	private User currentUser;
	//the outfile 
	private String outFile = "";
	//all the fields needed for the auction items
	String name ="", condition="", url="", imageURL="", price ="";

	//constructor to instantiate all the variables 
	Query(HashMap<String, ArrayList<AuctionItem>> dataBase, String search, JTextArea displayArea1, User currentUser, String x){
		this.currentUser = currentUser;
		this.search = search.replace(" ", "+");
		this.dataBase = dataBase;
		this.displayArea1 = displayArea1;
		this.outFile = x;
	}

	//iterate through database since it already exists in the database and querying isn't required 
	public void pullFromData() throws IOException{
		//if already exists
		ArrayList<AuctionItem> current = dataBase.get(search.replace("+", "_"));
//		System.out.println(search.replace("+", "_") + " " + current.size());
//		System.out.println(dataBase.toString());
		//		System.out.println(current);
		displayArea1.append("Seach query: " + search.replace("+", " ") + "\n");
		updateUI();
		if(currentUser.getUserRank()>1 && !currentUser.getSearches().contains(search.replace("+", "_"))){
//			System.out.println("shit");
			ArrayList<String> temp = currentUser.getSearches();
			temp.add(search.replace("+", "_"));
			currentUser.setSearches(temp);
			updateDB();
		}
		//		updateUI();
	}

	//query the search for all accounts but the guest account, and store/log them
	public void searchWithStoring() throws Exception{
		//search and store
		//		System.out.println(search);
		WebpageReaderWithAgent query = new WebpageReaderWithAgent("https://www.webstore.com/search.php?option=homepage_search&zip=&q=" + search + "&parent_id=0");
		//		System.out.println("test");
		ArrayList<String> current = currentUser.getSearches();
		current.add(search.replace("+", "_"));
		currentUser.setSearches(current);
		updateDB();
		displayArea1.append("Seach query: " + search.replace("+", " ") + "\n");
		queryThroughFile(true);
		updateUI();
		writeToFile("itemDB.txt");
		writeToFile(outFile);
		logFileUpdate();
		if(!dataBase.get(search.replace(" ", "_")).isEmpty()){
			imageDownload(dataBase.get(search.replace(" ", "_")).get(0).getImageURL().replace("\"", ""));
		}
	}

	//guery the search for the guest account without storing 
	public void searchWithoutStoring() throws Exception{
		//search without storing
		WebpageReaderWithAgent query = new WebpageReaderWithAgent("https://www.webstore.com/search.php?option=homepage_search&zip=&q=" + search + "&parent_id=0");
		displayArea1.append("Seach query: " + search.replace("+", " ") + "\n");
		queryThroughFile(false);
	}

	public void queryThroughFile(boolean flag) throws FileNotFoundException{
		Scanner inFile = new Scanner(new File("Result.txt"));
		String line = "";

		//search for line in the html file that contains the code for the auction items
		while(inFile.hasNextLine() && !line.startsWith("<tr class=\"contentfont br_row")){
			line = inFile.nextLine();	
			//			System.out.println(line);
		}

		//regex to find the price
		String priceRegex = "\\$[0-9]*\\.[0-9]{2}";
		//regex to find the name
		String nameRegex = ".+?(?= condition)";
		//regex to find the condition of the item
		String conditionRegex = "(?<= condition:\\s)(\\w+)";
		//regex to find the links to the auction itself
		String linksRegex = "(\"https:\\/\\/(.*?)\")";
		//split the long line into an array for each item 
		String[] lines = line.split("<td align=\"center\">");
		//iterate through each array and find all the needed information
		for(int i=1; i<lines.length; i++){
			//			System.out.println("shit");
			this.url = regexLinkSearch(linksRegex, lines[i]);
			this.imageURL = regexIMGSearch(linksRegex, lines[i]);
			lines[i] = lines[i].replaceAll("<[^>]*>", " ").trim();
			this.name = regexSearch(nameRegex, lines[i], "Name:");
			this.price = regexSearch(priceRegex, lines[i], "Price:");
			this.condition = regexSearch(conditionRegex, lines[i], "Condition:");
//			System.out.println("queryThroughFile: " + url + " " + imageURL + " " + name + " " + price + " " + condition);
			//only do if flag is true, meaning anyone other than the guest account has queried the search
			if(flag){
				addAuctionItem();
			} else if(!flag) {
				updateUI2();
			}
		}

		inFile.close();
	}

	//function to search for pattern in the line
	public static String regexSearch(String regex, String matcher, String title){
		Pattern search = Pattern.compile(regex);
		Matcher match = search.matcher(matcher);
		while(match.find()){
			return match.group().trim();
		}
		return "";
	}

	//function to search for pattern in the line
	public static String regexLinkSearch(String regex, String matcher){
		Pattern search = Pattern.compile(regex);
		Matcher match = search.matcher(matcher);
		while(match.find()){
			return match.group().trim();
		}
		return "";
	}

	//function to search for pattern in the line
	public static String regexIMGSearch(String regex, String matcher){
		Pattern search = Pattern.compile(regex);
		Matcher match = search.matcher(matcher);
		int counter = 0;
		while(match.find()){
			if(counter > 0)
				return match.group().trim();

			counter++;
		}
		return "";
	}

	//add item into the database
	private void addAuctionItem(){
		//create auction object and add to data
		AuctionItem newItem = new AuctionItem(search.replace("+", "_"), this.url, this.imageURL, this.name, this.price, this.condition);
		ArrayList<AuctionItem> current;
		if(dataBase.containsKey(search.replace("+", "_"))  ){ 
			current = dataBase.remove(search.replace("+", "_"));
		} else {
			current = new ArrayList<AuctionItem>();
		}
		current.add(newItem);
		dataBase.remove(search.replace("+", "_"));
		dataBase.put(search.replace("+", "_"), current);
		//		System.out.println(dataBase.toString());
//		System.out.println(dataBase.get(search.replace("+", "_")).toString());
	}

	//update the mainFrame's textArea
	private void updateUI(){
		ArrayList<AuctionItem> current = dataBase.get(search.replace("+", "_"));
//		System.out.println(dataBase.containsKey(search.replace("+", "_")));
		for(int i = 0; i < current.size(); i++){
			displayArea1.append("        Name: " + current.get(i).getTitle() + "\n");
			displayArea1.append("        Price: " + current.get(i).getPrice() + "\n");
			displayArea1.append("        Condition: " + current.get(i).getCondition() + "\n");
			displayArea1.append("\n");
		}
	}
	
	//method to print only for the non storage method, ie; guest account
	private void updateUI2(){
//		System.out.println("queryThroughFile: " + url + " " + imageURL + " " + name + " " + price + " " + condition);
		displayArea1.append("        Name: " + name + "\n");
		displayArea1.append("        Price: " + price + "\n");
		displayArea1.append("        Condition: " + condition + "\n");
		displayArea1.append("\n");
	}

	//log everything in outFile2
	private void writeToFile(String outFile2) throws IOException{
		ArrayList<AuctionItem> current = dataBase.get(search.replace("+", "_"));
		BufferedWriter file = new BufferedWriter(new FileWriter(outFile2, true));
		for(int i = 0; i < current.size(); i++){
			file.append(search.replace("+", " ") + "\n");
			file.append(current.get(i).getAuctionURL() + "\n");
			file.append(current.get(i).getImageURL() + "\n");
			file.append(current.get(i).getTitle() + "\n");
			file.append(current.get(i).getPrice() + "\n");
			file.append(current.get(i).getCondition() + "\n");
			//			file.append(current.toString());
		}
		file.close();
	}

	//find the line in the file in which the user's searches exist to update them 
	void updateDB() throws IOException{
		File inputFile = new File("userDB.txt");
		File tempFile = new File("myTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String currentLine;

		while((currentLine = reader.readLine()) != null) {
			// trim newline when comparing with lineToRemove
			String trimmedLine = currentLine.trim();
			if(trimmedLine.startsWith(currentUser.getUsername())) continue;
			writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.write(currentUser.toString());
		writer.close(); 
		reader.close(); 
		tempFile.renameTo(inputFile);
	}
	
	//log everytime someone searches something
	void logFileUpdate() throws IOException{
		//DELETED Item ID: 2112; Name: sad; Time Logged: 2019.03.11.11.14.07
		BufferedWriter file = new BufferedWriter(new FileWriter("logFile.txt", true));
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		file.append("INSERT Search query: " + search.replace("+", " ") + "; Username: " + currentUser.getUsername() + "; Time logged: " + timeStamp + "\n");
		file.close();
	}
	
	//ask user if they want the image of the first search item to show up to be downloaded
	private void imageDownload(String string) {
		// TODO Auto-generated method stub
		try {
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to download an image of the first item?", null ,JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				GetURLImage asd = new GetURLImage(string);
				JOptionPane.showMessageDialog(null, "It's downloaded in your files.");
			}
		} catch (Exception f) {
			f.printStackTrace();
		}
	}
}
