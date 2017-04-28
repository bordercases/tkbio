package bio.knowledge.web.view.concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.core.TableSorter;
import bio.knowledge.model.Concept;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.service.ConceptService;

public class RelationsItemQuery implements Query {
	Map<String, Object> serviceDirectory;
	private RelationsQueryDefinition definition;
	private Object[] sortPropertyIds;
	private boolean[] sortStates;
	private ConceptService conceptService;

	public RelationsItemQuery(RelationsQueryDefinition definition,
							ConceptService conceptService) {
		this.definition = definition;
		this.conceptService = conceptService;
		this.sortPropertyIds = definition.getSortPropertyIds();
		this.sortStates = definition.getSortPropertyAscendingStates();
	}

	@Override
    public Item constructItem() {
            return new BeanItem<Concept>(new Neo4jConcept());
    }
	
	
	@Override
	public int size() {
		if(this.definition.getStartCount() >= 50) {
			this.definition.setStartCount(50);
		}
		return this.definition.getStartCount();
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		// spoofing the start count
		int page = (int) Math.ceil(this.definition.getStartCount() / count);
		List<Concept> concepts = conceptService.findAllFiltered(definition.getSearchInput(), new PageRequest(page, count)).getContent();
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
	
 

