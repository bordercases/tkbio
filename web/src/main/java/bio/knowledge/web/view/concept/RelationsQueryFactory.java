package bio.knowledge.web.view.concept;

import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import bio.knowledge.service.ConceptService;
import bio.knowledge.service.KBQuery;

public class RelationsQueryFactory implements QueryFactory {
    
	private RelationsQueryDefinition definition;
    private ConceptService conceptService;
	private KBQuery query; 
	
	public RelationsQueryFactory(RelationsQueryDefinition definition, KBQuery query, ConceptService conceptService) {
		this.definition = definition;
		this.query = query;
		this.conceptService = conceptService;
	}
	
    public void setQueryDefinition(RelationsQueryDefinition definition) {
            this.definition=definition;
    }
	
	@Override
	public Query constructQuery(QueryDefinition definition) {
		return new RelationsItemQuery(((RelationsQueryDefinition) definition), query, conceptService);
	}

}
