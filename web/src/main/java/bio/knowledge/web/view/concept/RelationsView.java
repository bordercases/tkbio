package bio.knowledge.web.view.concept;

import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryView;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import bio.knowledge.grid.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.web.view.BaseView;

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
