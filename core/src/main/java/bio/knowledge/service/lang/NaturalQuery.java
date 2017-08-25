package bio.knowledge.service.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import bio.knowledge.model.Statement;
import bio.knowledge.model.lang.Concept;
import bio.knowledge.model.lang.Entity;
import bio.knowledge.model.lang.Relationship;
import bio.knowledge.model.lang.Token;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class NaturalQuery {
	
	// todo: add ability to question?
	// todo: find paths and make sure relevant
	// todo: relationships (or delete)
	
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
		
	public List<Statement> getDataPage(KnowledgeBeaconService kbService, int pageNo, int pageSize, String filter) { // todo: use filter or not
		
		int n = concepts.size();
		int pairs = n * (n - 1) / 2; // see: Triangular Number Sequence
		// todo: handle 0 or less pairs
		
		List<CompletableFuture<List<Statement>>> futures = new ArrayList<>();
		List<Statement> statements = new ArrayList<>();
		return statements;
		
//		for (Concept subject : concepts.values()) {
//			for (Concept object : concepts.values()) {
//				if (subject != object) {
//					
//					CompletableFuture<List<Statement>> future = kbService.getStatements(subject.getId(), object.getText(), "", pageNo, 5);
//					future = future.thenApply(list -> {
//						Predicate<Statement> isRelevant = s -> (s.getSubject().getName() + s.getRelation().getName() + s.getObject().getName()).contains(object.getText());
//						return list.stream()
//							.filter(isRelevant)
//							.collect(Collectors.toList());
//					});
//					futures.add(future);
//				}
//			}
//		}
//		
//		for (int i = 0, t = 20; i < futures.size(); i++, t /= 2) {
//			try {
//				statements.addAll(futures.get(i).get(t, TimeUnit.SECONDS));
//			} catch (InterruptedException | ExecutionException | TimeoutException e) {
//				// todo: error log?
//			}
//		}
//		
//		return statements;
	}
}
