package bio.knowledge.model.lang;

public class Relationship {
	
	private Concept subject;
	private Concept object;
	private String text;
	
	public Relationship(Concept subject, Concept object, String text) {
		this.subject = subject;
		this.object = object;
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

}
