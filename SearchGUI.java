import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;

public class SearchGUI extends JFrame{

	//text field from main frame that shows searches
	private JTextField textField;
	//the current user logged into the system
	private User currentUser;
	
	//initialize and create the GUI for searching in which the user puts in what they want to search
	SearchGUI(HashMap<String, ArrayList<AuctionItem>> dataBase, User currentUser, JTextArea displayArea1, String x) {
		this.currentUser = currentUser;
		this.setBounds(100, 100, 450, 154);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);

		this.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		textField = new JTextField();
		textField.setBounds(63, 29, 324, 26);
		this.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(84, 78, 117, 29);

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String search = textField.getText().replace(" ", "_");
//				System.out.println(search);
				Query query = new Query(dataBase, textField.getText(), displayArea1, currentUser, x);
				if(dataBase.containsKey(search)){
//					System.out.println("1");
					try {
						query.pullFromData();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else if(currentUser.getUserRank() == 1){
					try {
//						System.out.println("2");
						query.searchWithoutStoring();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					try {
//						System.out.println("3");
//						searchDataBase.put(search, null);
						ArrayList<AuctionItem> dummy = new ArrayList<AuctionItem>();
						dataBase.put(search, dummy);
						query.searchWithStoring();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				setVisible(false);
				dispose();
			}

		});

		this.getContentPane().add(btnSearch);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(245, 78, 117, 29);

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}

		});

		this.getContentPane().add(btnCancel);

		this.setVisible(true);
	}

}
