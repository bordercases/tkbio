package bio.knowledge.web.view;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.themes.ValoTheme;

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
import bio.knowledge.web.ui.WikiDetailsHandler;

@SpringView(name = RelationsView.NAME)
public class RelationsView extends NewBaseView {

	public static final String NAME = "relations";
	private static final int ROWS_TO_DISPLAY = 11;
	private static final String STYLE = "results-grid";
	private static final int DATAPAGE_SIZE = 12;
	private static final long TIME_OUT = 60;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	
	private TextField filterField = new TextField();

	@Autowired
	KnowledgeBeaconService kbService;
	
	@Autowired
	protected WikiDetailsHandler wd_handler;

	private BeanItemContainer<Statement> container = new BeanItemContainer<Statement>(Statement.class);
	private GeneratedPropertyContainer gpContainer = new GeneratedPropertyContainer(container);

	private Grid dataTable;
	String curies;
	private int numberOfPages;

	@PostConstruct
	protected void initialize() {
		setSizeFull();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null) {
			String[] parameters = event.getParameters().split("/");
			if(parameters[0] != "") {
				curies = String.join(" ", parameters);
			}
			setupUiComponents(curies);
			refresh(curies);
		}
		
		gpContainer.addContainerFilter(new Filter() {

			@Override
			public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
				Statement statement = (Statement) itemId;
				Concept object = statement.getObject();
				Concept subject = statement.getSubject();
				Predicate relation = statement.getRelation();
				String filter = filterField.getValue();
				if (filter.isEmpty()) {
					return true;
				} else{
					return 
							object.getName().contains(filter) || 
							subject.getName().contains(filter) ||
							relation.getName().contains(filter);
				}
			}

			@Override
			public boolean appliesToProperty(Object propertyId) {
				return true;
			}
			
		});
	}

	private void setupUiComponents(String curies) {
		this.removeAllComponents();

		VerticalLayout vLayout = new VerticalLayout();

		ScrollListener scrollListener = new ScrollListener() {

			@Override
			public void scrolledToBottom() {
				addDataPage(curies);
			}

		};

		dataTable = new Grid(scrollListener);

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

		// we add nodes to the graph through this interface
		Button addToGraphButton = new Button("Add to Map", e -> {
			Collection<Object> items = dataTable.getSelectionModel().getSelectedRows();
			DesktopUI ui = (DesktopUI) UI.getCurrent();
			for (Object item : items) {

				Statement statement = (Statement) item;

				Concept subject = statement.getSubject();
				String predicateLabel = statement.getRelation().getName();
				Concept object = statement.getObject();

				// Unusual case of missing data (mostly in sample data?)
				if (subject == null || object == null)
					continue;

				ui.addNodeToConceptMap(subject);
				ui.addNodeToConceptMap(object);
				ui.addEdgeToConceptMap(subject, object, predicateLabel);

				// just in case, reset the currently active highlighted node(?)
				ui.setHighlightedNode(curies);
			}
			// https://dev.vaadin.com/ticket/16345
			((SelectionModel.Multi) dataTable.getSelectionModel()).deselectAll();
		});

		addToGraphButton.setEnabled(false);
		addToGraphButton.setWidth(100, Unit.PERCENTAGE);

		// add selection listener for the grid
		dataTable.addSelectionListener(selection -> {
			boolean isEmpty = selection.getSelected().isEmpty();
			addToGraphButton.setEnabled(isEmpty ? false : true);
		});

		dataTable.setWidth("100%");
		dataTable.setHeightMode(HeightMode.ROW);
		dataTable.setHeightByRows(ROWS_TO_DISPLAY);
		dataTable.setImmediate(true);
		dataTable.addStyleName(STYLE);
		// dataTable.setCellStyleGenerator(cellRef -> getStyle(cellRef));
		dataTable.setSelectionMode(SelectionMode.MULTI);

		dataTable.setContainerDataSource(gpContainer);

		
		filterField.setWidth("15em");
		filterField.setIcon(FontAwesome.SEARCH);
		filterField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
		vLayout.addComponents(filterField, dataTable, addToGraphButton);
		this.addComponent(vLayout);
	}

	private void refresh(String conceptId) {
		if (container.removeAllItems()) {
			numberOfPages = 0;
			addDataPage(conceptId);
			isAllDataLoaded = false;
		}
	}

	private boolean isAllDataLoaded = false;
	private void addDataPage(String conceptId) {
		if (! isAllDataLoaded) {
			CompletableFuture<List<Statement>> future =
					kbService.getStatements(conceptId, null, null, numberOfPages, DATAPAGE_SIZE);
	
			try {
				List<Statement> statements = future.get(TIME_OUT, TIME_UNIT);
				container.addAll(statements);
				numberOfPages++;
				if (statements.size() < DATAPAGE_SIZE) {
					isAllDataLoaded = true;
				}

			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	}

	private void onConceptDetailsSelection(RendererClickEvent event, ConceptRole role) {
		Statement statement = (Statement) event.getItemId();
		Concept subject = statement.getSubject();
		Predicate predicate = statement.getRelation();
		Concept object = statement.getObject();

		DesktopUI ui = (DesktopUI) UI.getCurrent();

		PopupWindow conceptDetailsWindow = new PopupWindow();
		conceptDetailsWindow.addStyleName("concept-details-window");
		conceptDetailsWindow.center();
		conceptDetailsWindow.setSizeUndefined();
		conceptDetailsWindow.setResizable(true);

		// int x = 100, y = 400 ;

		String predicateLabel;

		Concept selectedConcept;

		if (role.equals(ConceptRole.SUBJECT)) {
			selectedConcept = subject;
		} else if (role.equals(ConceptRole.OBJECT)) {
			selectedConcept = object;
			// x+=400 ;
		} else
			throw new RuntimeException("Unsupported Relationship Concept Role?");

		String conceptName;

		if (selectedConcept != null) {
			conceptName = selectedConcept.getName();
		} else {
			conceptName = "Unknown concept";
		}

		predicateLabel = predicate.getName();

		Button showRelations = new Button("Show Relations");
		showRelations.addClickListener(e -> selectionContext(ui, conceptDetailsWindow, selectedConcept));

		// RMB: 9 September 2016 - deprecating relation table display of
		// WikiData
		/*
		 * Button showData = new Button("Find Data") ;
		 * showData.addClickListener( e -> dataSelectionContext( ui,
		 * conceptDetailsWindow, selectedConcept ) );
		 */

		Button addToMap = new Button("Add to Map");
		addToMap.addClickListener(e -> {

			ui.addNodeToConceptMap(subject);
			ui.addNodeToConceptMap(object);
			ui.addEdgeToConceptMap(subject, object, predicateLabel);

			conceptDetailsWindow.close();
		});

		Button closeButton = new Button("Close");
		closeButton.addClickListener(e -> {
			conceptDetailsWindow.close();
		});

		HorizontalLayout operationsLayout = new HorizontalLayout();
		operationsLayout.addComponent(showRelations);

		// RMB: 9 September 2016 - deprecating relation table display of
		// WikiData
		// operationsLayout.addComponent(showData);

		operationsLayout.addComponent(addToMap);
		operationsLayout.setSpacing(true);

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.addComponents(operationsLayout, closeButton);

		buttonsLayout.setSpacing(true);
		buttonsLayout.setMargin(true);
		buttonsLayout.setWidth("100%");

		buttonsLayout.setComponentAlignment(operationsLayout, Alignment.MIDDLE_LEFT);
		buttonsLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_RIGHT);

		VerticalLayout wd_details = wd_handler.getDetails(curies);
		wd_details.addComponent(buttonsLayout);

		conceptDetailsWindow.setCaption(conceptName);
		conceptDetailsWindow.setId("introPanel");
		conceptDetailsWindow.setContent(wd_details);

		ui.addWindow(conceptDetailsWindow);
	}

	private void selectionContext(DesktopUI ui, PopupWindow conceptDetailsWindow, Concept selectedConcept) {
		ui.queryUpdate(selectedConcept, RelationSearchMode.RELATIONS);
		conceptDetailsWindow.close();
		ui.gotoStatementsTable();
	}

}
