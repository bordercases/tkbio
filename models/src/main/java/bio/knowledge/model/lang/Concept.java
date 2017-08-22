package bio.knowledge.model.lang;

public class Concept {
	
	private String id;
	private String text;
	
	public Concept(String id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public String getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}

}
