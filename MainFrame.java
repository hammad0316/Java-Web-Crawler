import javax.swing.*;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static Dimension dim;
	//	private static Hashing hashMap;
	//	private static Item item;
	private File f;
	private static String x;
	static HashMap<String, ArrayList<AuctionItem>> dataBase;
	HashMap<String, User> userDB;
	User currentUser;

	/**
	 * Create the main GUI of the program
	 * @param inFile Name of file to be read
	 * @param currentUser 
	 * @param dataBase 
	 * @param userDB2 
	 * @param displayArea Display area for ID
	 * @param displayArea1 Display area for name
	 * @param displayArea2 Display area for price
	 * @param displayArea3 Display area for quantity
	 * @param displayArea4 Display area for time added
	 * @param displayArea5 Display area for threshold 
	 * @throws IOException In case file isn't found
	 */
	MainFrame(String outFile, HashMap<String, User> userDB, User currentUser, HashMap<String, ArrayList<AuctionItem>> dataBase) throws IOException{
		this.currentUser = currentUser;
		this.userDB = userDB;
		this.dataBase = dataBase;
		//key is search query
//		dataBase = new HashMap<String, ArrayList<AuctionItem>>();

//		searchDataBase.put(searchDataBase.size()+1, "hello");
//		searchDataBase.put(searchDataBase.size()+1, "test");

		//		System.out.println(searchDataBase.values());
		//		System.out.println(searchDataBase.size());
		//		System.out.println(getKeyFromValue(searchDataBase,"hello"));

		if(outFile != "") 
			x = outFile;
		else
			x = "outFile.txt";

		JTextArea displayArea = new JTextArea(20,21);
		JTextArea displayArea1 = new JTextArea(20,42);

		displayArea.setBackground(new Color(238,238,238,238));

		//Creating the Frame
		setTitle("Search Results");
		setResizable(false);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 420);
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

		//Creating the top menu and adding components
		JMenuBar topMenu = new JMenuBar();
		JMenu m1 = new JMenu("File");
		topMenu.add(m1);
		JMenuItem m11 = new JMenuItem("Open");
		JMenuItem m22 = new JMenuItem("Quit");
		m1.add(m11);
		m1.add(m22);

		// Action to be performed if Open was clicked in top menu
		m11.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String menuName = e.getActionCommand();
				if (menuName.equals("Open")) {
					//when user clicks cancel in file chooser, null pointer exception is caught and not printed to console
					try {
						try {
							ChoosingFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					catch (NullPointerException x){ 

					}
				}
				else if (menuName.equals("Exit"))
					System.exit(0);
			}

		});

		// Action to be performed if exit was click in top menu
		m22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String menuName = e.getActionCommand();
				if (menuName.equals("Quit")) {
					System.exit(0);
				}
			}
		});

		//Creating the panel at bottom and adding components
		JPanel bottomPanel = new JPanel();
		JButton search = new JButton("Search");
		JButton history = new JButton("History");
		JButton logOut = new JButton("Log Out");

		// Action to be performed if add button is clicked
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SearchGUI search = new SearchGUI(dataBase, currentUser, displayArea1, x);
			}

		});

		// Action to be performed if edit button was clicked
		history.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentUser.getUserRank() == 1){
					JOptionPane.showMessageDialog(null,"Guest accounts don't have access to this feature.");
				} else {
					HistoryGUI history = new HistoryGUI(currentUser, userDB, dataBase);
				}

			}

		});

		// Action to be performed if delete button was clicked
		logOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				LoginGUI login = new LoginGUI(userDB, dataBase);
			}

		});

		bottomPanel.add(search);
		bottomPanel.add(history);
		bottomPanel.add(logOut);

		JPanel midPanel = new JPanel();

		//Area that displays inventory
		JScrollPane scrollPane = new JScrollPane(displayArea);
		displayArea.setEditable(false);
		scrollPane.setBorder(null);

		JScrollPane scrollPane1 = new JScrollPane(displayArea1);
		displayArea1.setEditable(false);
		displayArea1.setToolTipText("Your search results");

		midPanel.add(scrollPane);
		midPanel.add(scrollPane1);

		//Adding everything to the frame.
		getContentPane().add(BorderLayout.SOUTH, bottomPanel);
		getContentPane().add(BorderLayout.NORTH, topMenu);
		getContentPane().add(BorderLayout.CENTER, midPanel);
		
		setVisible(true);
		
		displayArea.append("\n \n \n Username: " + currentUser.getUsername());
		if(currentUser.getUserRank() == 1){
			displayArea.append("\n \n \n Type of Account: Basic guest account");
		} else if(currentUser.getUserRank() == 2){
			displayArea.append("\n \n \n Type of Account: Basic user account");
		} else {
			displayArea.append("\n \n \n Type of Account: Admin account");
		}
	}

	// Open up JFileChooser and send the chosen file into inputFromFile
	public void ChoosingFile() throws IOException {
		JFileChooser fd = new JFileChooser();
		fd.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //let them look through both directories and files
		fd.showOpenDialog(null);
		f = fd.getSelectedFile(); //set selected file f which will be read
		inputFromFile(f); //send in file, unsorted and sorted wordlist 
	} // method ChooseFile

	// Open File f, read it and input all valid lines into hash map
	private static void inputFromFile(File f) throws IOException{
		String url = null, imageURL = null, search = null, name = null, price = null, condition = null, currentLine = null;
		Scanner itemInput = new Scanner(f);
		int currentLength = 0, counter = 0;
		while(itemInput.hasNextLine() && itemInput.hasNext()){
			search = itemInput.nextLine().replace(" ", "_");
			url = itemInput.nextLine();
			imageURL = itemInput.nextLine();
			name = itemInput.nextLine();
			price = itemInput.nextLine();
			condition = itemInput.nextLine();
			AuctionItem newItem = new AuctionItem(search.replace(" ", "_"), url, imageURL, name, price, condition);
			ArrayList<AuctionItem> current;
			if(dataBase.containsKey(search)  ){ 
				current = dataBase.remove(search);
			} else {
				current = new ArrayList<AuctionItem>();
			}
			current.add(newItem);
			dataBase.remove(search.replace(" ", "_"));
			dataBase.put(search.replace(" ", "_"), current);
//			System.out.println(dataBase.toString());
//			System.out.println(dataBase.get(search.replace("+", "_")).toString());
		}
		itemInput.close();
	}  // method inputFromFile

}
