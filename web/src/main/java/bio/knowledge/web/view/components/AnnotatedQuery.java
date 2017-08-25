package bio.knowledge.web.view.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gwt.thirdparty.guava.common.collect.Range;
import com.google.gwt.thirdparty.guava.common.collect.RangeSet;
import com.google.gwt.thirdparty.guava.common.collect.TreeRangeSet;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;

import bio.knowledge.model.lang.Entity;

public class AnnotatedQuery extends CustomLayout {
	
	private List<ComboBox> dropdowns = new ArrayList<>();
	private Map<Range<Integer>, List<Entity>> optionLists = new HashMap<>();

	public AnnotatedQuery(String text, List<Entity> entities) {
		
		if (isNullOrEmpty(text)) {
			setTemplateContents("");
			setVisible(false);
			return;
		}
						
		RangeSet<Integer> spans = TreeRangeSet.create();
		List<Range<Integer>> ranges = map(this::createRange, entities);
		ranges.forEach(e -> spans.add(e));
				
		RangeSet<Integer> antispans = TreeRangeSet.create();
		antispans.add(createRange(text));
		antispans.removeAll(spans);
		
		List<Range<Integer>> spanList = new ArrayList<>(spans.asRanges());
		spanList.forEach(r -> optionLists.put(r, new ArrayList<>()));
		
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			Range<Integer> range = ranges.get(i);
			
			for (int j = 0; j < spanList.size(); j++) {
				Range<Integer> span = spanList.get(j);
				
				if (span.encloses(range)) {
					optionLists.get(span).add(entity);
				}
			}
		}
		
		optionLists.forEach((span, options) -> options.sort((o1, o2) -> -Integer.compare(o1.getLength(), o2.getLength())));

		List<String> clickable = map(r -> makeClickable(substring(text, r), r), spans.asRanges());
		List<String> regular = map(r -> substring(text, r), antispans.asRanges());
		
		List<String> ordered = spans.contains(0)? interleave(clickable, regular) : interleave(regular, clickable);
		String annotatedText = String.join("", ordered);
		
		setTemplateContents(annotatedText);
	}
	
	public void addSelectionListener() {
		//getcom
	}
	
	public List<Entity> getDefaultEntities() { // todo: remove + have outside give default choice, not this class
		List<Entity> entities = map(o -> o.get(0), optionLists.values());
		return entities;
	}
	
	public List<Entity> getSelectedEntities() {
		List<Entity> entities = map(d -> (Entity) d.getValue(), dropdowns);
		return entities;
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
	
	private String makeClickable(String s, Range<Integer> r) {
		
		List<Entity> options = optionLists.get(r);
		ComboBox dropdown = new ComboBox();
		dropdown.addItems(options);
		dropdown.setNullSelectionItemId(options.get(0)); // todo: allow actual empty selection in case not a noun?
		
		addComponent(dropdown, r.toString());
		dropdowns.add(dropdown);
		
		return "<span location='" + r + "'></span>";
	}
	
	private String substring(String s, Range<Integer> r) {
		return s.substring(r.lowerEndpoint(), r.upperEndpoint());
	}
	
	private Range<Integer> createRange(Entity e) {
		return Range.closedOpen(e.getStart(), e.getEnd());
	}
	
	private Range<Integer> createRange(String s) {
		return Range.closedOpen(0, s.length());
	}
	
	private <T, R> List<R> map(Function<T, R> f, Collection<T> c) {
		return c.stream().map(f).collect(Collectors.toList());
	}
	
	private boolean isNullOrEmpty(String s) {
		return s == null || s.trim().equals("");
	}

}
