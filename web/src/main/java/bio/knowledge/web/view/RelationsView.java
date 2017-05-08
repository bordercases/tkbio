package bio.knowledge.web.view;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.apache.jena.sparql.pfunction.library.container;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.grid.Grid;
import bio.knowledge.grid.Grid.ScrollListener;
import bio.knowledge.model.Concept;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.Statement;
import bio.knowledge.renderer.ButtonRenderer;
import bio.knowledge.service.KBQuery.RelationSearchMode;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.ui.PopupWindow;
import bio.knowledge.web.view.ListView.ConceptRole;

@SpringView(name = RelationsView.NAME)
public class RelationsView extends NewBaseView {

	public static final String NAME = "relations";
	private static final int ROWS_TO_DISPLAY = 11;
	private static final String STYLE = "results-grid";
	private static final int DATAPAGE_SIZE = 12;
	private static final long TIME_OUT = 60;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	
	@Autowired
	KnowledgeBeaconService kbService;

	private BeanItemContainer<Statement> container = new BeanItemContainer<Statement>(Statement.class);
	private GeneratedPropertyContainer gpContainer = new GeneratedPropertyContainer(container);

	private Grid dataTable;

	int numberOfPages = 1;

	@PostConstruct
	protected void initialize() {
		setSizeFull();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null) {
			String[] parameters = event.getParameters().split("/");
			for (String parameter : parameters) {
				System.out.println(parameter);
			}
		}
		
		removeAllComponents();
		numberOfPages = 1;

		dataTable = new Grid(new ScrollListener() {

			@Override
			public void scrolledToBottom() {
//				loadDataPage(numberOfPages);
			}

		});

		String[] columns = new String[] { COL_ID_SUBJECT, COL_ID_RELATION, COL_ID_OBJECT, COL_ID_EVIDENCE };
		for (String column : columns) {
			dataTable.addColumn(column);
		}

		dataTable.getColumn(COL_ID_EVIDENCE).setRenderer(new ButtonRenderer(clicked -> {
			DesktopUI ui = (DesktopUI) UI.getCurrent();
			Statement selectedStatement = (Statement) clicked.getItemId();
			ui.displayEvidence(selectedStatement);
		}));

		// dataTable.getColumn(COL_ID_SUBJECT).setRenderer(new ButtonRenderer(
		// clicked -> onConceptDetailsSelection(clicked, ConceptRole.SUBJECT)));
		//
		// dataTable.getColumn(COL_ID_OBJECT).setRenderer(new ButtonRenderer(
		// clicked -> onConceptDetailsSelection(clicked, ConceptRole.OBJECT)));
		
		dataTable.setWidth("100%");
		dataTable.setHeightMode(HeightMode.ROW);
		dataTable.setHeightByRows(ROWS_TO_DISPLAY);
		dataTable.setImmediate(true);
		dataTable.addStyleName(STYLE);
		// dataTable.setCellStyleGenerator(cellRef -> getStyle(cellRef));
		dataTable.setSelectionMode(SelectionMode.MULTI);

		dataTable.setContainerDataSource(gpContainer);

		this.addComponent(dataTable);

//		loadDataPage(0);

	}

//	private void loadDataPage(int pageNumber) {
//
//		Optional<Concept> currentStatementOpt = query.getCurrentQueryConcept();
//		try {
//			Concept currentStatement = currentStatementOpt.get();
//			CompletableFuture<List<Statement>> future = kbService.getStatements(currentStatementOpt.get().getId(), null,
//					null, pageNumber, DATAPAGE_SIZE);
//
//			List<Statement> statements = future.get(TIME_OUT, TIME_UNIT);
//			container.addAll(statements);
//			numberOfPages++;
//		} catch (InterruptedException | ExecutionException | TimeoutException e) {
//			e.printStackTrace();
//		}
//	}

//	private void onConceptDetailsSelection(RendererClickEvent event, ConceptRole role) {
//		Statement statement = (Statement) event.getItemId();
//		Concept subject = statement.getSubject();
//		Predicate predicate = statement.getRelation();
//		Concept object = statement.getObject();
//
//		RelationSearchMode searchMode = query.getRelationSearchMode();
//		if (searchMode.equals(RelationSearchMode.WIKIDATA) && role.equals(ConceptRole.OBJECT)) {
//
//			// This is a WikiData item property value...
//			wd_handler.displayDataPage(predicate.getId(), object.getName());
//
//		} else {
//
//			DesktopUI ui = (DesktopUI) UI.getCurrent();
//
//			PopupWindow conceptDetailsWindow = new PopupWindow();
//			conceptDetailsWindow.addStyleName("concept-details-window");
//			conceptDetailsWindow.center();
//			conceptDetailsWindow.setSizeUndefined();
//			conceptDetailsWindow.setResizable(true);
//
//			// int x = 100, y = 400 ;
//
//			String predicateLabel;
//
//			Concept selectedConcept;
//
//			if (role.equals(ConceptRole.SUBJECT)) {
//				selectedConcept = subject;
//			} else if (role.equals(ConceptRole.OBJECT)) {
//				selectedConcept = object;
//				// x+=400 ;
//			} else
//				throw new RuntimeException("Unsupported Relationship Concept Role?");
//
//			String conceptName;
//
//			if (selectedConcept != null) {
//				conceptName = selectedConcept.getName();
//			} else {
//				conceptName = "Unknown concept";
//			}
//
//			predicateLabel = predicate.getName();
//
//			Button showRelations = new Button("Show Relations");
//			showRelations.addClickListener(e -> selectionContext(ui, conceptDetailsWindow, selectedConcept));
//
//			// RMB: 9 September 2016 - deprecating relation table display of
//			// WikiData
//			/*
//			 * Button showData = new Button("Find Data") ;
//			 * showData.addClickListener( e -> dataSelectionContext( ui,
//			 * conceptDetailsWindow, selectedConcept ) );
//			 */
//
//			Button addToMap = new Button("Add to Map");
//			addToMap.addClickListener(e -> {
//
//				ui.addNodeToConceptMap(subject);
//				ui.addNodeToConceptMap(object);
//				ui.addEdgeToConceptMap(subject, object, predicateLabel);
//
//				conceptDetailsWindow.close();
//			});
//
//			Button closeButton = new Button("Close");
//			closeButton.addClickListener(e -> {
//				conceptDetailsWindow.close();
//			});
//
//			HorizontalLayout operationsLayout = new HorizontalLayout();
//			operationsLayout.addComponent(showRelations);
//
//			// RMB: 9 September 2016 - deprecating relation table display of
//			// WikiData
//			// operationsLayout.addComponent(showData);
//
//			operationsLayout.addComponent(addToMap);
//			operationsLayout.setSpacing(true);
//
//			HorizontalLayout buttonsLayout = new HorizontalLayout();
//			buttonsLayout.addComponents(operationsLayout, closeButton);
//
//			buttonsLayout.setSpacing(true);
//			buttonsLayout.setMargin(true);
//			buttonsLayout.setWidth("100%");
//
//			buttonsLayout.setComponentAlignment(operationsLayout, Alignment.MIDDLE_LEFT);
//			buttonsLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_RIGHT);
//
//			VerticalLayout wd_details = wd_handler.getDetails(selectedConcept);
//			wd_details.addComponent(buttonsLayout);
//
//			conceptDetailsWindow.setCaption(conceptName);
//			conceptDetailsWindow.setId("introPanel");
//			conceptDetailsWindow.setContent(wd_details);
//
//			ui.addWindow(conceptDetailsWindow);
//		}
//	}

	private void selectionContext(DesktopUI ui, PopupWindow conceptDetailsWindow, Concept selectedConcept) {
		ui.queryUpdate(selectedConcept, RelationSearchMode.RELATIONS);
		conceptDetailsWindow.close();
		ui.gotoStatementsTable();
	}

}
