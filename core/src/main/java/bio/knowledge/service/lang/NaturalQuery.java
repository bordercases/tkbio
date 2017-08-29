package bio.knowledge.service.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import bio.knowledge.model.Statement;
import bio.knowledge.model.lang.Concept;
import bio.knowledge.model.lang.Entity;
import bio.knowledge.model.lang.Relationship;
import bio.knowledge.model.lang.Token;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class NaturalQuery {
	
	private KnowledgeBeaconService kbService;
	private Map<String, Concept> concepts = new HashMap<>();
	private List<Relationship> relationships = new ArrayList<>();
	
	public NaturalQuery(List<Entity> entities) {
		entities.forEach(e -> addConcept(e.getToken()));
	}
	
	public void addConcept(Token token) {
		
		String conceptId = token.getId();
		List<String> terms = token.getTerms();
		String text = terms.size() > 0? terms.get(0) : "";
		
		Concept concept = new Concept(conceptId, text);
		concepts.put(conceptId, concept);
	}
	
//	public void addRelationship(String subjectId, String objectId, String text) {
//		
//		Concept subject = concepts.get(subjectId);
//		Concept object = concepts.get(objectId);
//		if (subject == null || object == null) throw new IllegalArgumentException("Subject and Object must be part of query");
//		
//		Relationship relationship = new Relationship(subject, object, text);
//		relationships.add(relationship);
//	}
		
	public List<Statement> getDataPage(KnowledgeBeaconService kbService, int pageNumber, int pageSize) {
		this.kbService = kbService;
		List<Concept> c = new ArrayList<>(concepts.values());
		return getConnections(c, new ArrayList<>(), pageNumber, pageSize);
	}
	
	private List<Statement> getConnections(List<Concept> unknowns, List<Concept> knowns, int pageNumber, int pageSize) {
		
		// look for connections between unknowns
		// (in addition to connections from unknowns to knowns)
		// (and prepend so loop index doesn't double-search each pair of unknowns)
		knowns.addAll(0, unknowns);
		
		List<CompletableFuture<List<Statement>>> futures = new ArrayList<>();
		List<String> objects = new ArrayList<>();
		List<Statement> statements = new ArrayList<>();
		
		for (int i = 0; i < unknowns.size(); i++) {
			for (int j = i; j < knowns.size(); j++) {
				Concept subject = unknowns.get(i);
				Concept object = knowns.get(j);

				String filter = (subject == object)? "subclass" : object.getText();
				
				CompletableFuture<List<Statement>> future = kbService.getStatements(subject.getId(), filter, "", pageNumber, 5);

				futures.add(future);
				objects.add(subject == object? null : object.getText());
			}
		}
		
		for (	int i = 0, t = 20;
				i < futures.size();
				i++, t /= 2
			) {
			
			CompletableFuture<List<Statement>> future = futures.get(i);
			String object = objects.get(i);
			
			List<Statement> response = waitFor(future, t);
			if (response == null) continue;
//			List<Statement> relevant = filter(response, object); // uncomment to filter results by keyword
			statements.addAll(response);
						
			List<Statement> interesting = filter(response, "subclass");
			// TODO: interesting statements can be recursively searched for
			// (outside of this loop, and added to statements)
			// eg. getConnections(interesting, knowns, pageNumber, pageSize) 

		}
		
		return statements;
	}
	
	private List<Statement> filter(List<Statement> statements, String keywords) {
		if (keywords == null || keywords.equals("")) return new ArrayList<>();
		return statements.stream()
				.filter(s -> (
						s.getSubject().getName()
						+ s.getRelation().getName()
						+ s.getObject().getName()
					).contains(keywords))
				.collect(Collectors.toList());
	}
	
//	private List<String> wordsIn(Statement s) {
//		return Arrays.asList(
//			s.getSubject().getName().split(" "),
//			s.getRelation().getName().split(" "),
//			s.getObject().getName().split(" ")
//		).stream()
//			.flatMap(Arrays::stream)
//			.collect(Collectors.toList());
//	}
	
	private <T> T waitFor(CompletableFuture<T> future, int timeout) {
		try {
			return future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

}
