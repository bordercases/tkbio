package bio.knowledge.model.lang;

import java.util.List;

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
	
	public int getLength() {
		return end - start;
	}
	
	public int getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		
		List<String> terms = token.getTerms();
		String name = terms.size() > 0? terms.get(0) + " " : "";
		name += "(" + token.getId() + ")";
		
		return name;
	}
	
}