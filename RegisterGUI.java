import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class RegisterGUI extends JFrame{

	private JTextField textField;
	private JPasswordField textField_1;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JPasswordField textField_2;
	HashMap<String, User> userDB;
	HashMap<String, ArrayList<AuctionItem>> dataBase;

	/**
	 * Initialize, create, and launch the frame.
	 * @param userDB 
	 * @param dataBase 
	 */
	RegisterGUI(HashMap<String, User> userDB, HashMap<String, ArrayList<AuctionItem>> dataBase) {
		this.userDB = userDB;
		this.dataBase = dataBase;
		this.setBounds(100, 100, 412, 311);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		this.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		textField = new JTextField();
		textField.setBounds(200, 50, 130, 26);
		this.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JPasswordField();
		textField_1.setBounds(200, 110, 130, 26);
		this.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		lblUsername = new JLabel("Username");
		lblUsername.setBounds(81, 55, 76, 16);
		this.getContentPane().add(lblUsername);
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(81, 115, 61, 16);
		this.getContentPane().add(lblPassword);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(60, 227, 117, 29);
		
		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validation()){
					try {
						submit();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					setVisible(false);
					dispose();
					LoginGUI login = new LoginGUI(userDB, dataBase);
				} else {
					JOptionPane.showMessageDialog(null,"The credentials you put are either invalid, or taken.");
				}
			}

		});
		
		this.getContentPane().add(btnRegister);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(230, 227, 117, 29);
		
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginGUI login = new LoginGUI(userDB, dataBase);
				setVisible(false);
				dispose();
			}

		});
		
		this.getContentPane().add(btnCancel);
		
		textField_2 = new JPasswordField();
		textField_2.setBounds(200, 170, 130, 26);
		this.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblPasswordConfirmation = new JLabel("Password confirmation");
		lblPasswordConfirmation.setBounds(44, 175, 144, 16);
		this.getContentPane().add(lblPasswordConfirmation);
		
		this.setVisible(true);
	}
	
	private boolean validation(){
		return Arrays.equals(textField_1.getPassword(), textField_2.getPassword()) && textField.getText().length() > 3 && textField_1.getPassword().length > 3;
	}
	
	private void submit() throws IOException{
		ArrayList<String> temp = new ArrayList<String>();
		User newUser = new User(textField.getText(), new String(textField_1.getPassword()), 2, temp);
//		System.out.println(new String(textField_1.getPassword()));
		userDB.put(textField.getText(), newUser);
//		System.out.println(userDB.toString());
		BufferedWriter file = new BufferedWriter(new FileWriter("userDB.txt", true));
		file.append("\n" + newUser.toString());
		file.close();
	}
	
}
