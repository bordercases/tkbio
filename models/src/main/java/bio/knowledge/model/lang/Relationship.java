package bio.knowledge.model.lang;

public class Relationship {
	
	private Concept subject;
	private Concept object;
	private String text;
	
	public Relationship() {}
	
	public Relationship(Concept subject, Concept object, String text) {
		this.subject = subject;
		this.object = object;
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setSubject(Concept subject) {
		this.subject = subject;
	}
	
	public Concept getSubject() {
		return subject;
	}

	public void setObject(Concept object) {
		this.object = object;
	}
	
	public Concept getObject() {
		return object;
	}

}
