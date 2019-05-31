import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

//displays user history if nonadmin, all history if admin

public class HistoryGUI extends JFrame{

	private static Dimension dim;
	HashMap<String, ArrayList<AuctionItem>> dataBase;
	HashMap<String, User> userDB;
	User currentUser;
	JTextArea textArea;

	public HistoryGUI(User currentUser, HashMap<String, User> userDB, HashMap<String, ArrayList<AuctionItem>> dataBase){
		this.currentUser = currentUser;
		this.userDB = userDB;
		this.dataBase = dataBase;

		setTitle("Search History");
		setResizable(false);
		dim = Toolkit.getDefaultToolkit().getScreenSize();

		setBounds(100, 100, 428, 438);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 416, 355);
		getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		getContentPane().add(scrollPane);

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(226, 372, 117, 29);
		getContentPane().add(btnNewButton_1);

		btnNewButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}

		});

		JButton btnNewButton = new JButton("Delete");
		btnNewButton.setBounds(73, 372, 138, 29);
		getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String entry = JOptionPane.showInputDialog(null,"Please enter the search query of the history you want to delete").replace(" ", "_");
					while(!dataBase.containsKey(entry)) {
						entry = JOptionPane.showInputDialog(null,"Doesn't exist, try again");
					}
					int dialogResult = JOptionPane.showConfirmDialog (null, "Deleting it will permanently remove it from the inventory, are you sure?","Warning",JOptionPane.YES_NO_OPTION);
					while(dialogResult == JOptionPane.NO_OPTION) {
						entry = JOptionPane.showInputDialog(null,"Please enter the ID of the item you want to edit");
						dialogResult = JOptionPane.showConfirmDialog (null, "Deleting it will permanently remove it from the inventory, are you sure?","Warning",JOptionPane.YES_NO_OPTION);
					}
					if(dialogResult == JOptionPane.YES_OPTION){
						dataBase.remove(entry);
						logFileUpdate(entry);
						textArea.setText(null);
						updateUI();
					}
				} catch (Exception f) {
					f.printStackTrace();
				}
			}

		});

		updateUI();

		setVisible(true);
	}

	void updateUI(){
		if(currentUser.getUserRank() == 2){
			for(int i=0; i<currentUser.getSearches().size(); i++){
				ArrayList<AuctionItem> current = dataBase.get(currentUser.getSearches().get(i));
				textArea.append(currentUser.getSearches().get(i).toString().replace("_", " ") + "\n");
				for(int j = 0; j < current.size(); j++){
					textArea.append("          " + current.get(j).getAuctionURL().replace("\"", "") + "\n");
					textArea.append("          " + current.get(j).getImageURL().replace("\"", "") + "\n");
					textArea.append("          " + current.get(j).getTitle().replace("\"", "") + "\n");
					textArea.append("          " + current.get(j).getPrice().replace("\"", "") + "\n");
					textArea.append("          " + current.get(j).getCondition().replace("\"", "") + "\n");
				}
			}
		} else {
			dataBase.forEach((n,k) -> {
				if(!k.isEmpty()){
				textArea.append("Seach query: " + k.get(0).getQuery().replace("_", " ") + "\n");
				k.forEach((f) -> {
					textArea.append("        Name: " + f.getTitle() + "\n");
					textArea.append("        Price: " + f.getPrice() + "\n");
					textArea.append("        Condition: " + f.getCondition() + "\n");
					textArea.append("\n");
//					System.out.println(f.getTitle());
				});
			}
			});
		}
	}
	
	void logFileUpdate(String search) throws IOException{
		//DELETED Item ID: 2112; Name: sad; Time Logged: 2019.03.11.11.14.07
		BufferedWriter file = new BufferedWriter(new FileWriter("logFile.txt", true));
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		file.append("DELETE Search query: " + search.replace("+", " ") + "; Added by: SYSTEM; Time logged: " + timeStamp + "\n");
		file.close();
	}

}
