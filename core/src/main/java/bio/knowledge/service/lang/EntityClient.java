package bio.knowledge.service.lang;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import bio.knowledge.model.lang.Entity;

public class EntityClient {
	
	private final String BASE_PATH = "https://scigraph-data.monarchinitiative.org/scigraph";
	private final String SETTINGS = "minLength=1&longestOnly=false&includeAbbrev=true&includeAcronym=true&includeNumbers=true";
	private final String ENTITY_LIST = BASE_PATH + "/annotations/entities?content={text}&" + SETTINGS;
	private final String ANNOTATED_TEXT = BASE_PATH + "/annotations?content={text}&" + SETTINGS;
	
	private RestTemplate rest;
	private HttpHeaders headers;
	
	public EntityClient() {
		rest = new RestTemplate();
		headers = new HttpHeaders();
		//headers.setAccept(MediaType.APPLICATION_JSON);
	}
	
	public List<Entity> getAnnotationList(String text) {
		List<Entity> entities = rest.getForObject(ENTITY_LIST, (Class<? extends List<Entity>>) List.class, text);
		return entities;
	}

}
