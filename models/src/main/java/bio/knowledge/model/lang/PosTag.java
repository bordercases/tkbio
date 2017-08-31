package bio.knowledge.model.lang;

/**
 * Represents a Part-Of-Speech tag on a section of text.
 * Can be used to deserialize POS tags from the Monarch Scigraph service.
 *
 */
public class PosTag {
	
	private String token;
	private String pos;
	private String start;
	private String end;
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}
	
	public String getPos() {
		return pos;
	}

	public void setStart(String start) {
		this.start = start;
	}
	
	public String getStart() {
		return start;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public String getEnd() {
		return end;
	}
	
}
