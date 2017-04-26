package bio.knowledge.web.view.concept;

import java.util.List;
import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.github.andrewoma.dexx.collection.ArrayList;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import bio.knowledge.model.Concept;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.service.ConceptService;

public class RelationsItemQuery implements Query {
	Map<String, Object> serviceDirectory;
	private QueryDefinition definition;
	private Object[] sortPropertyIds;
	private boolean[] sortStates;

	public RelationsItemQuery(QueryDefinition definition,
			Map<String, Object> serviceDirectory) {
		this.definition = definition;
		this.serviceDirectory = serviceDirectory;
		this.sortPropertyIds = definition.getSortPropertyIds();
		this.sortStates = definition.getDefaultSortPropertyAscendingStates();
	}

	@Override
    public Item constructItem() {
            return new BeanItem<Concept>(new Concept());
    }
	
	
	@Override
	public int size() {
		ConceptService conceptService =
				(ConceptService) serviceDirectory.get("conceptService");		
		return conceptService.size();
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		ConceptService conceptService =
				(ConceptService) serviceDirectory.get("conceptService");
		
		List<Concept> concepts = conceptService.load(start, count, sortPropertyIds, sortStates);
        List<Item> items = (List<Item>) new ArrayList<Item>();
        for(Concept concept : concepts) {
                items.add(new BeanItem<Concept>(concept));
        }
        
	    return items;
	    
	}

	@Override
	public void saveItems(List<Item> arg0, List<Item> arg1, List<Item> arg2) {
		throw new UnsupportedOperationException();		
	}
	
	@Override
	public boolean deleteAllItems() {
		throw new UnsupportedOperationException();		
	}
	
}
	
 
