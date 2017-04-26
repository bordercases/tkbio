package bio.knowledge.web.view.concept;

import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import bio.knowledge.service.ConceptService;

public class RelationsQueryFactory implements QueryFactory {
    
	private RelationsQueryDefinition definition;
    private ConceptService conceptService; 
	
	public RelationsQueryFactory(RelationsQueryDefinition definition, ConceptService conceptService) {
		this.definition = definition;
		this.conceptService = conceptService;
	}
	
    public void setQueryDefinition(RelationsQueryDefinition definition) {
            this.definition=definition;
    }
	
	@Override
	public Query constructQuery(QueryDefinition definition) {
		return new RelationsItemQuery(((RelationsQueryDefinition) definition), conceptService);
	}

}
