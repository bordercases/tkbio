package bio.knowledge.model.lang;

import java.util.List;

public class Token {
	
	private String id;
	private List<String> categories;
	private List<String> terms;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public List<String> getCategories() {
		return categories;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	
	public List<String> getTerms() {
		return terms;
	}

}