package bio.knowledge.web.view.concept;

import com.vaadin.client.widgets.Grid;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
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
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.core.TableSorter;
import bio.knowledge.web.view.BaseView;
import bio.knowledge.web.view.ListView;
import bio.knowledge.web.view.components.DataTableBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryView;

@SpringView(name = RelationsView.NAME)
public class RelationsView extends BaseView {

	public static final String NAME = "concept";
	String searchInput = "diabetes";
	
	@Override
	public void enter(ViewChangeEvent event) {
		removeAllComponents();
		Panel relationsPane = new Panel();
		
		//Grid dataTable = DataTableBuilder.build(null, null);
		//conceptWindow.setContent(dataTable);
		
		VerticalLayout vLayout = new VerticalLayout();
		Grid relationsGrid = new Grid();
		relationsGrid.setHeightMode(HeightMode.ROW);
		relationsGrid.setHeightByRows(10d);
		GridScrollDetector gsd = new GridScrollDetector() {
			@Override
			public void endHasBeenReached() {
				Notification.show("Reached Bottom");
			}	
		};
		gsd.extend(relationsGrid);
		
//		BeanQueryFactory<ConceptBeanQuery> queryFactory = new 
//				BeanQueryFactory<ConceptBeanQuery>(ConceptBeanQuery.class);
//		queryFactory.setQueryConfiguration(serviceDirectory);
		
		// TODO:
		// DONE: boolean compositeItems, DONE: int batchSize, DONE: java.lang.Object idPropertyId
		// Check
		RelationsQueryDefinition rlqd = new RelationsQueryDefinition(searchInput, false, 10, null);
		
		// TODO:
		Object[] sortPropertyIds = new Object[0];
		boolean[] sortPropertyAscendingStates = new boolean[0];

		rlqd.setSortState(sortPropertyIds, sortPropertyAscendingStates);
		
		RelationsQueryFactory rqf = new RelationsQueryFactory(rlqd, conceptService);
		LazyQueryContainer lqc = new LazyQueryContainer(rlqd, rqf);
		lqc.addContainerProperty("id", String.class, "", true, true);
		lqc.addContainerProperty("name", String.class, "", true, true);

//		List<Concept> testList = new ArrayList<Concept>() {{
//			add(new Neo4jConcept());
//		}}; 
		relationsGrid.setContainerDataSource(lqc);
		
		vLayout.addComponent(relationsGrid);
		relationsPane.setContent(vLayout);
		addComponent(relationsPane);
		
	}
}
