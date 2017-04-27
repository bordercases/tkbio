package bio.knowledge.web.view.concept;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
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
		relationsGrid.setHeightMode(HeightMode.ROW);
		relationsGrid.setHeightByRows(10d);
		
		
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
		LazyQueryContainer lqc = new LazyQueryContainer(new LazyQueryView(rlqd, rqf));
		lqc.addContainerProperty("id", String.class, "", true, true);
		lqc.addContainerProperty("name", String.class, "", true, true);

		GridScrollDetector gsd = new GridScrollDetector() {
			@Override
			public void endHasBeenReached() {
				System.out.println("Reached Bottom");
			}	
		};
		gsd.extend(relationsGrid);
		
		getMore.addClickListener(e -> {
			// get grid lazy container
			LazyQueryContainer currentRQC = (LazyQueryContainer) relationsGrid.getContainerDataSource();
			
			// get definition of the container's query view
			RelationsQueryDefinition currentRQD = (RelationsQueryDefinition) currentRQC.getQueryView().getQueryDefinition();
			
			// change the definition to include an increment
			// will be converted to a page
			currentRQD.setStartCount(currentRQD.getStartCount() + currentRQD.getBatchSize());
			
			// reload the data
			currentRQC.refresh();
		});
		
		relationsGrid.setContainerDataSource(lqc);
		
		vLayout.addComponents(relationsGrid, getMore);
		relationsPane.setContent(vLayout);
		addComponent(relationsPane);
		
	}
}
