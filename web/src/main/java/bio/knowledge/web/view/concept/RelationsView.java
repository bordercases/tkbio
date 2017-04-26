package bio.knowledge.web.view.concept;

import com.vaadin.data.Item;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Panel;

import bio.knowledge.service.ConceptService;
import bio.knowledge.web.view.BaseView;
import bio.knowledge.web.view.ListView;
import bio.knowledge.web.view.components.DataTableBuilder;

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
	String userInput = "";
	
	@Override
	public void enter(ViewChangeEvent event) {
		removeAllComponents();
		Panel relationsWindow = new Panel();
		//Grid dataTable = DataTableBuilder.build(null, null);
		//conceptWindow.setContent(dataTable);
		
		VerticalLayout vLayout = new VerticalLayout();
		Grid relationGrid = new Grid();
				
//		BeanQueryFactory<ConceptBeanQuery> queryFactory = new 
//				BeanQueryFactory<ConceptBeanQuery>(ConceptBeanQuery.class);
//		queryFactory.setQueryConfiguration(serviceDirectory);
		
		// TODO:
		// DONE: boolean compositeItems, DONE: int batchSize, DONE: java.lang.Object idPropertyId
		// Check
		RelationsQueryDefinition rlqd = new RelationsQueryDefinition(userInput, false, 10, null);
		
		// TODO:
		Object[] sortPropertyIds = new Object[0];
		boolean[] sortPropertyAscendingStates = new boolean[0];
		rlqd.setSortState(sortPropertyIds, sortPropertyAscendingStates);
		
		RelationsQueryFactory rqf = new RelationsQueryFactory(rlqd, serviceDirectory);
		relationGrid.setContainerDataSource(new LazyQueryContainer(rlqd, rqf));
		
		vLayout.addComponent(relationGrid);
		relationsWindow.setContent(vLayout);
		addComponent(relationsWindow);
		
	}
}
