package bio.knowledge.web.view.concept;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;

import bio.knowledge.model.Concept;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.scrollgrid.GridScrollDetector;
import bio.knowledge.scrollgrid.ScrollGrid;
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.core.TableSorter;
import bio.knowledge.web.view.BaseView;
import bio.knowledge.web.view.ListView;
import bio.knowledge.web.view.components.DataTableBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.LazyQueryView;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryView;

@SpringView(name = RelationsView.NAME)
public class RelationsView extends BaseView {

	public static final String NAME = "concept";
	String searchInput = "diabetes";
	private int pageIncrement = 0;
	private final int batchSize = 10;

	@Override
	public void enter(ViewChangeEvent event) {
		removeAllComponents();
		Panel relationsPane = new Panel();
		
		//Grid dataTable = DataTableBuilder.build(null, null);
		//conceptWindow.setContent(dataTable);
		
		VerticalLayout vLayout = new VerticalLayout();
		Button getMore = new Button();
		getMore.setCaption("Get more options");

		Grid relationsGrid = new Grid();
		IndexedContainer container = new IndexedContainer();
		relationsGrid.setContainerDataSource(container);
		getMore.addClickListener(e -> {
			int querySize = conceptService.size();
			if(pageIncrement / batchSize < querySize) {
				pageIncrement = pageIncrement + 1;
				List<Concept> concepts = conceptService.findAllFiltered(query.getCurrentQueryText(), new PageRequest(pageIncrement, batchSize)).getContent();
				for(Concept concept : concepts) {
					relationsGrid.getContainerDataSource().addItem(concept);
				}
			}
		});
				
		vLayout.addComponents(relationsGrid, getMore);
		relationsPane.setContent(vLayout);
		addComponent(relationsPane);
		
	}
}
