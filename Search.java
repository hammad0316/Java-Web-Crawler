
public class Search {
	
	private String search;
	private int Id;
	
	public Search(String search, int id) {
		super();
		this.search = search;
		Id = id;
	}
	
	public String getSearch() {
		return search;
	}
	
	public void setSearch(String search) {
		this.search = search;
	}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
}
