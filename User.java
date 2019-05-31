import java.util.ArrayList;

public class User {

	private String username;
	private String password;
	private int userRank;
	private ArrayList searches;
	
	public User(String username){
		super();
		this.username = "Guest";
		this.password = "password";
		this.userRank = 1;
	}
	
	public User(String username, String password, int userRank, ArrayList searches) {
		super();
		this.username = username;
		this.password = password;
		this.userRank = userRank;
		this.setSearches(searches);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserRank() {
		return userRank;
	}


	public void setUserRank(int userRank) {
		this.userRank = userRank;
	}
	
	@Override
	public String toString() {
		String results = "";
		for(int i = 0; i< searches.size(); i++){
			if(i >0) results += " " + searches.get(i);
			else results += searches.get(i);
		}
		return username + " " + password + " " + userRank + " " + results;
	}

	public ArrayList getSearches() {
		return searches;
	}

	public void setSearches(ArrayList searches) {
		this.searches = null;
		this.searches = searches;
	}
}
