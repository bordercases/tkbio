package bio.knowledge.model.core;

public class Curie {
	private String rawCurie;

	public Curie(String rawCurie) {
		this.setRawCurie(rawCurie);
	}

	public String getRawCurie() {
		return rawCurie;
	};

	public String setRawCurie(String rawCurie) {
		return rawCurie;
	};

	// these don't get fields so that they're deterministically returned by 
	public String getSourceTag() {
		return getRawCurie().replace("[", "").replace("]", "").split(":")[0];
	};

	public int getId() {
		return Integer.parseInt(getRawCurie().replace("[", "").replace("]", "").split(":")[1]);
	};

}
