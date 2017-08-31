package bio.knowledge.service.lang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.gwt.thirdparty.guava.common.collect.Range;
import com.google.gwt.thirdparty.guava.common.collect.RangeSet;

import bio.knowledge.model.lang.Concept;
import bio.knowledge.model.lang.Entity;
import bio.knowledge.model.lang.Relationship;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.parser.chunking.Parser;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

/**
 * Given some potential entities in sentence,
 * handles extraction of valid entities from sentence.
 * 
 * @author Meera Godden
 *
 */
@Service
public class ParserService {
	
	private static final List<String> TOKEN = Arrays.asList("TK");
	private static final List<String> NOUN = Arrays.asList("NN", "NNS", "NNP", "NNPS");
	private static final List<String> NOUN_PHRASE = Arrays.asList("NP", "NX");
	private static final List<String> VERB = Arrays.asList("VB", "VBD", "VBG", "VBN", "VBP", "VBZ");
	private static final List<String> VERB_PHRASE = Arrays.asList("VP");
	private static final List<String> ADJECTIVE = Arrays.asList("JJ", "JJR", "JJS");
	private static final List<String> CLAUSE = Arrays.asList("S", "SBAR", "SBARQ", "SINV", "SQ");

	private Parser parser;
	
	public ParserService() {
				
		try {
			File file = new File(getClass().getClassLoader().getResource("en-parser-chunking.bin").getFile());
			ParserModel model = new ParserModel(file);
			parser = new Parser(model);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns only the entities that contain content words (e.g. nouns and not conjunctions).
	 * 
	 * @param text
	 * @param entities
	 * @return
	 */
	public List<Entity> filterLexical(String text, List<Entity> entities) {
		Parse[] parses = ParserTool.parseLine(text, parser, 1);
		parses[0].show();
		entities = new ArrayList<Entity>(entities);
		removeNonLexical(parses[0], entities);
		return entities;
	}
	
	private void removeNonLexical(Parse parse, List<Entity> entities) {
		if (parse.getChildCount() == 1 && hasType(parse.getChildren()[0], TOKEN)) {
			if (! (hasType(parse, NOUN) || hasType(parse, ADJECTIVE))
				) {
				entities.removeIf(e ->
					parse.getSpan().getStart() <= e.getStart()
					&& e.getEnd() <= parse.getSpan().getEnd());
			}
		} else {
			for (Parse child : parse.getChildren()) {
				removeNonLexical(child, entities);
			}
		}
	}
	
//	public List<Relationship> parse(String text, RangeSet<Integer> spans, RangeSet<Integer> antispans) {
//		Parse[] parses = ParserTool.parseLine(text, parser, makeTokenizer(spans, antispans), 20);
//		List<Relationship> relations = new ArrayList<>();
//		for (Parse parse : parses) {
//			relations = extractRelations(parse);
//			System.out.println();
//			parse.show();
//			relations.forEach(r -> {
//				System.out.println(
//						r == null?
//							"NO RELATION"
//						:
//							(r.getSubject() == null? "NONE" : r.getSubject().getText())
//							+ " -["
//							+ r.getText()
//							+ "]-> "
//							+ (r.getObject() == null? "NONE" : r.getObject().getText())
//				);
//			});
//			
//		}
//		return relations;
//	}
//	
	private List<Relationship> extractRelations(Parse phrase) {
		
		List<Relationship> relations = new ArrayList<>();
		
		if (hasType(phrase, CLAUSE)) {
			relations.add(makeRelation(phrase));
		}
		
		for (Parse child : phrase.getChildren()) {
			relations.addAll(extractRelations(child));
		}
		
		return relations;

	}
	
	private Relationship makeRelation(Parse clause) {
		
		Relationship relation = new Relationship();
		for (Parse child : clause.getChildren()) {
			
			if (hasType(child, VERB_PHRASE)) {
				for (Parse grandchild : child.getChildren()) {
					
					if (hasType(grandchild, VERB)) {
						relation.setText(grandchild.getCoveredText());
					}
					if (hasType(grandchild, NOUN_PHRASE)) {
						relation.setObject(makeConcept(grandchild));
					}
					
				}				
			}
			
			if (hasType(child, NOUN_PHRASE)) {
				relation.setSubject(makeConcept(child));
			}
		}
		
		return relation;
	}
	
	private Tokenizer makeTokenizer(RangeSet<Integer> spans, RangeSet<Integer> antispans) {
		return new Tokenizer() {
			
			@Override
			public Span[] tokenizePos(String s) {
				return null;
			}
			
			@Override
			public String[] tokenize(String s) {
				
				List<String[]> entities = map(r -> {String[] sub = {substring(s, r)}; return sub;}, spans.asRanges());
				List<String[]> regular = map(r -> SimpleTokenizer.INSTANCE.tokenize(substring(s, r)), antispans.asRanges());
				List<String[]> ordered = spans.contains(0)? interleave(entities, regular) : interleave(regular, entities);
				
				return ordered.stream().flatMap(Arrays::stream).toArray(String[]::new);
			}
		};
	}
	
	private Concept makeConcept(Parse phrase) {
		Concept concept = new Concept();
		concept.setText(phrase.getCoveredText());
		return concept;
	}

	private boolean hasType(Parse phrase, List<String> types) {
		return types.contains(phrase.getType());
	}
	
	private String substring(String s, Range<Integer> r) {
		return s.substring(r.lowerEndpoint(), r.upperEndpoint());
	}
	
	private <T> List<T> interleave(List<T> l1, List<T> l2) {
		
		Iterator<T> it1 = l1.iterator();
		Iterator<T> it2 = l2.iterator();
		List<T> list = new ArrayList<>();
		
		while (it1.hasNext() || it2.hasNext()) {
			if (it1.hasNext()) list.add(it1.next());
			if (it2.hasNext()) list.add(it2.next());
		}
		
		return list;
	}
	
	private <T, R> List<R> map(Function<T, R> f, Collection<T> c) {
		return c.stream().map(f).collect(Collectors.toList());
	}
	
}
