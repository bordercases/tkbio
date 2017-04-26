package bio.knowledge.web.view.concept;

import java.util.List;
import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.github.andrewoma.dexx.collection.ArrayList;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import bio.knowledge.service.core.TableSorter;
import bio.knowledge.model.Concept;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.service.ConceptService;

public class RelationsItemQuery implements Query {
	Map<String, Object> serviceDirectory;
	private RelationsQueryDefinition definition;
	private Object[] sortPropertyIds;
	private boolean[] sortStates;

	public RelationsItemQuery(RelationsQueryDefinition definition,
			Map<String, Object> serviceDirectory) {
		this.definition = definition;
		this.serviceDirectory = serviceDirectory;
		this.sortPropertyIds = definition.getSortPropertyIds();
		this.sortStates = definition.getSortPropertyAscendingStates();
	}

	@Override
    public Item constructItem() {
            return new BeanItem<Concept>(new Neo4jConcept());
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
		
		// doesn't use parts of definition?
		List<Concept> concepts = conceptService.getDataPage(start, count, definition.getSearchInput(), TableSorter.SUBJECT, true);
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
	
 

