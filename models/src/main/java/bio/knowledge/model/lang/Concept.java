package bio.knowledge.model.lang;

/**
 * Concept with just an id and name.
 *
 */
public class Concept {
	
	private String id;
	private String text;
	
	public Concept() {}
	
	public Concept(String id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

}
