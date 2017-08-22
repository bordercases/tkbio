package bio.knowledge.service.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import bio.knowledge.model.Statement;
import bio.knowledge.model.lang.Concept;
import bio.knowledge.model.lang.Relationship;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.service.core.ListTablePager;
import bio.knowledge.service.core.TableSorter;

public class Query implements ListTablePager<Statement> {
	
	// todo: add ability to question?
	
	private Map<String, Concept> concepts = new HashMap<>();
	private List<Relationship> relationships = new ArrayList<>();
	private KnowledgeBeaconService kbService;
	
	public Query(KnowledgeBeaconService kbService) {
		this.kbService = kbService;
	}
	
	public void addConcept(String conceptId, String text) {
		Concept concept = new Concept(conceptId, text);
		concepts.put(conceptId, concept);
	}
	
	// todo: delete?
//	public void addRelationship(String subjectId, String objectId, String text) {
//		
//		Concept subject = concepts.get(subjectId);
//		Concept object = concepts.get(objectId);
//		if (subject == null || object == null) throw new IllegalArgumentException("Subject and Object must be part of query");
//		
//		Relationship relationship = new Relationship(subject, object, text);
//		relationships.add(relationship);
//	}
	
	@Override
	public List<Statement> getDataPage(int pageNo, int pageSize, String filter, TableSorter sorter, boolean direction) {
		
		int n = concepts.size();
		int pairs = n * (n - 1) / 2; // see: Triangular Number Sequence
		// todo: handle 0 or less pairs
		
		List<CompletableFuture<List<Statement>>> futures = new ArrayList<>();
		List<Statement> statements = new ArrayList<>();
		
		for (Concept subject : concepts.values()) {
			for (Concept object : concepts.values()) {
				if (subject != object) {
					CompletableFuture<List<Statement>> future = kbService.getStatements(subject.getId(), object.getText(), "", pageNo, 5);
				}
			}
		}
		
		for (int i = 0, t = 8; i < futures.size(); i++, t /= 2) {
			try {
				statements.addAll(futures.get(i).get(t, TimeUnit.SECONDS));
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				// todo: error log?
			}
		}
		
		return null;
	}
}
