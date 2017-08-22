package bio.knowledge.model.lang;

public class Entity {

	private Token token;
	private int start;
	private int end;
	
	public void setToken(Token token) {
		this.token = token;
	}
	
	public Token getToken() {
		return token;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	public int getStart() {
		return start;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public int getEnd() {
		return end;
	}
	
}