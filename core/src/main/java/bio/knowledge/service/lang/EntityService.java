package bio.knowledge.service.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import bio.knowledge.model.lang.Entity;
import bio.knowledge.model.lang.PosTag;

@Service // todo: service?
public class EntityService {
	
	private final String BASE_PATH = "https://scigraph-data.monarchinitiative.org/scigraph";
	private final String SETTINGS = "minLength=1&longestOnly=false&includeAbbrev=true&includeAcronym=true&includeNumbers=true";
	
	private final String ENTITIES = BASE_PATH + "/annotations/entities?content={text}&" + SETTINGS;
	private final String ANNOTATED_TEXT = BASE_PATH + "/annotations?content={text}&" + SETTINGS;
	private final String POS_TAGS = BASE_PATH + "/lexical/pos?text={text}";
	
	private RestTemplate rest = new RestTemplate();;
	
	public List<Entity> getEntities(String text) {
		Entity[] array = rest.getForObject(ENTITIES, Entity[].class, text);
		return Arrays.asList(array);
	}
	
	public List<PosTag> getPartsOfSpeech(String text) {
		PosTag[] tags = rest.getForObject(POS_TAGS, PosTag[].class, text);
		return Arrays.asList(tags);
	}

}
