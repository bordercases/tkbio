package bio.knowledge.web.view.concept;

import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

public class RelationsQueryFactory implements QueryFactory {
    
	private QueryDefinition definition;
    private Map<String, Object> serviceDirectory; 
	
	public RelationsQueryFactory(QueryDefinition definition, Map<String, Object> serviceDirectory) {
		this.definition = definition;
		this.serviceDirectory = serviceDirectory;
	}
	
    public void setQueryDefinition(QueryDefinition definition) {
            this.definition=definition;
    }
	
	@Override
	public Query constructQuery(QueryDefinition definition) {
		return new RelationsItemQuery(definition, serviceDirectory);
	}

}
