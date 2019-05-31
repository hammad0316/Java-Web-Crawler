import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginGUI extends JFrame{

	private JTextField textField;
	private JPasswordField textField_1;
	private JLabel lblUsername;
	private JLabel lblPassword;
	HashMap<String, User> userDB;
	private HashMap<String, ArrayList<AuctionItem>> dataBase;
	private String outFile = "";

	/**
	 * Initialize, create and launch frame if the database was already populated before.
	 */
	LoginGUI(HashMap<String, User> userDB, HashMap<String, ArrayList<AuctionItem>> dataBase) {
		this.userDB = userDB;
		this.dataBase = dataBase;
		initialize();
	}

	//initial creation of the project that also prepopulates database with files from system
	LoginGUI(String fileName, String outFile) throws IOException{
		this.outFile = outFile;
		populateDB(fileName);
		initialize();
	}

	//create and show the login screen
	private void initialize(){
		this.setBounds(100, 100, 412, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		this.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		textField = new JTextField();
		textField.setBounds(200, 67, 130, 26);
		this.getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JPasswordField();
		textField_1.setBounds(200, 133, 130, 26);
		this.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		lblUsername = new JLabel("Username");
		lblUsername.setBounds(81, 72, 76, 16);
		this.getContentPane().add(lblUsername);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(81, 138, 61, 16);
		this.getContentPane().add(lblPassword);

		JButton btnSignIn = new JButton("Sign In");
		btnSignIn.setBounds(25, 195, 117, 29);

		btnSignIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					try {
						int userRank = 3;
						User currentUser = userDB.get(textField.getText());
						MainFrame frame = new MainFrame(outFile, userDB, currentUser, dataBase);
						setVisible(false);
						dispose();
					} catch (IOException e1) {
					}
				} else {
					JOptionPane.showMessageDialog(null,"The credentials you entered are invalid.");
				}
			}

		});

		this.getContentPane().add(btnSignIn);

		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(150, 195, 117, 29);

		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterGUI register = new RegisterGUI(userDB, dataBase);
				setVisible(false);
				dispose();
			}

		});

		this.getContentPane().add(btnRegister);

		JButton btnGuest = new JButton("Guest Sign In");
		btnGuest.setBounds(274, 195, 117, 29);

		btnGuest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int userRank = 1;
					User guestUser = userDB.get("Guest");
					MainFrame frame = new MainFrame(outFile, userDB, guestUser, dataBase);
					setVisible(false);
					dispose();
				} catch (IOException e1) {
				}
			}

		});

		this.getContentPane().add(btnGuest);

		this.setVisible(true);
	}

	//check the database for the user account and see if both usernames and passwords match 
	private boolean validation(){
		return userDB.containsKey(textField.getText()) && (Arrays.equals(textField_1.getPassword(), userDB.get(textField.getText()).getPassword().toCharArray()));
	}

	//fill up database with data from the files on the system
	private void populateDB(String inFile) throws IOException{
		this.userDB = new HashMap<String, User>();
		this.dataBase = new HashMap<String, ArrayList<AuctionItem>>();

		String username, password;
		int rank;
		ArrayList<String> searches = new ArrayList<String>();
		int line = 0, length = 0;

		Scanner userInput = new Scanner(new File("userDB.txt"));
		while(userInput.hasNextLine() && userInput.hasNext()){
			length = getLineLength(++line, "userDB.txt");
			//			System.out.println(length);
			username = userInput.next();
			password = userInput.next();
			rank = Integer.parseInt(userInput.next());
			//			System.out.println();
			if(length > (username.length() + password.length() + 3) && userInput.hasNext()){
				int i = (username.length() + password.length() + 4);
				while(i< length){
					i++;
					String current = userInput.next();
					searches.add(current);
					logFileUpdate(current);
					//					System.out.println(current);
					if(!dataBase.containsKey(current)){
						dataBase.put(current, new ArrayList<AuctionItem>());
					}
					i += current.length() + 1;
					//					System.out.println(i + " " + searchList);
				}
			}
			User newUser = new User(username, password, rank, searches);
			searches = new ArrayList<String>();
			userDB.put(username, newUser);
			length = 0;
		}
		userInput.close();
		User guestUser = new User("Guest");
		userDB.put("Guest", guestUser);

		String url = null, imageURL = null, search = null, name = null, price = null, condition = null, currentLine = null;
		Scanner itemInput = new Scanner(new File("itemDB.txt"));
		//		int currentLength = 0, counter = 0;
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
//			System.out.println("should print");
			//			System.out.println(dataBase.toString());
			//			System.out.println(dataBase.get(search.replace("+", "_")).toString());
		}
		itemInput.close();
		//		System.out.println(dataBase.toString());

		if(inFile != ""){
			Scanner itemInput1 = new Scanner(new File(inFile));
			//			int currentLength = 0, counter = 0;
			while(itemInput1.hasNextLine() && itemInput1.hasNext()){
				search = itemInput1.nextLine().replace(" ", "_");
				url = itemInput1.nextLine();
				imageURL = itemInput1.nextLine();
				name = itemInput1.nextLine();
				price = itemInput1.nextLine();
				condition = itemInput1.nextLine();
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
				if(outFile != ""){
					writeToFile(newItem, outFile);
				}
				writeToFile(newItem, "itemDB.txt");
				//				System.out.println(dataBase.toString());
				//				System.out.println(dataBase.get(search.replace("+", "_")).toString());
			}
			itemInput1.close();
		}
	}

	//get the length of the current line in the file
	int getLineLength(int line, String fileName) throws FileNotFoundException{
		Scanner inFile = new Scanner(new File(fileName));
		String current = null;
		int length = 0;
		for(int i = 0; i < line; i++){
			current = inFile.nextLine();
		}
		length = current.length();
		inFile.close();
		return length;
	}

	//write the fields of the item into outFile2
	private void writeToFile(AuctionItem newItem, String outFile2) throws IOException{
		//		current = dataBase.get(search.replace("+", "_"));
		BufferedWriter file = new BufferedWriter(new FileWriter(outFile2, true));
		file.append(newItem.getQuery().replace("_", " ") + "\n");
		file.append(newItem.getAuctionURL() + "\n");
		file.append(newItem.getImageURL() + "\n");
		file.append(newItem.getTitle() + "\n");
		file.append(newItem.getPrice() + "\n");
		file.append(newItem.getCondition() + "\n");
		file.close();
	}
	
	//log whenever a search is made
	void logFileUpdate(String search) throws IOException{
		//DELETED Item ID: 2112; Name: sad; Time Logged: 2019.03.11.11.14.07
		BufferedWriter file = new BufferedWriter(new FileWriter("logFile.txt", true));
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		file.append("INSERT Search query: " + search.replace("+", " ") + "; Added by: SYSTEM; Time logged: " + timeStamp + "\n");
		file.close();
	}

}
