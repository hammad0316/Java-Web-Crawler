//object class for the item being auctioned
public class AuctionItem {

	private String query;
	private String auctionURL;
	private String imageURL;
	private String title;
	private String price;
	private String condition;
	
	public AuctionItem(){
		this.query = "";
		this.auctionURL = "";
		this.imageURL = "";
		this.title = "";
		this.price = "";
		this.condition = "";
	}
	
	public AuctionItem(String query, String auctionURL, String imageURL, String title, String price2, String condition) {
		super();
		this.query = query;
		this.auctionURL = auctionURL;
		this.imageURL = imageURL;
		this.title = title;
		this.price = price2;
		this.condition = condition;
	}
	
	public String getAuctionURL() {
		return auctionURL;
	}
	
	public void setAuctionURL(String auctionURL) {
		this.auctionURL = auctionURL;
	}
	
	public String getImageURL() {
		return imageURL;
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}


	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	@Override
	public String toString() {
		return "Query: " + query + "\n URL: " + auctionURL + "\n ImageURL: " + imageURL + "\n Name: "
				+ title + "\n Price: " + price + "\n Condition: " + condition + "\n";
	}
}
