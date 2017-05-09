package bio.knowledge.web.view;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;

import bio.knowledge.grid.Grid;
import bio.knowledge.grid.Grid.ScrollListener;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.Concept;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.Statement;
import bio.knowledge.renderer.ButtonRenderer;
import bio.knowledge.service.KBQuery.RelationSearchMode;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.ui.PopupWindow;
import bio.knowledge.web.ui.WikiDetailsHandler;

@SpringView(name = EvidenceView.NAME)
public class EvidenceView extends NewBaseView {
	
	public static final String NAME = "evidence";
	private static final int ROWS_TO_DISPLAY = 11;
	private static final String STYLE = "results-grid";
	private static final int DATAPAGE_SIZE = 12;
	private static final long TIME_OUT = 60;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	@Autowired
	KnowledgeBeaconService kbService;

	private BeanItemContainer<Annotation> container = new BeanItemContainer<Annotation>(Annotation.class);
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
			setupDataTable(curies);
			refresh(curies);
		}
	}

	private void setupDataTable(String curies) {
		this.removeAllComponents();
		ScrollListener scrollListener = new ScrollListener() {

			@Override
			public void scrolledToBottom() {
				addDataPage(curies);
			}

		};

		dataTable = new Grid(scrollListener);

		String[] columns = new String[] { "publicationDate", "supportingText" };
		for (String column : columns) {
			dataTable.addColumn(column);
		}
//
//		dataTable.getColumn(COL_ID_EVIDENCE).setRenderer(new ButtonRenderer(clicked -> {
//			DesktopUI ui = (DesktopUI) UI.getCurrent();
//			Statement selectedStatement = (Statement) clicked.getItemId();
//			ui.displayEvidence(selectedStatement);
//		}));

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
	}

	private void refresh(String statementId) {
		if (container.removeAllItems()) {
			numberOfPages = 0;
			addDataPage(statementId);
			isAllDataLoaded = false;
		}
	}

	private boolean isAllDataLoaded = false;

	private void addDataPage(String statementId) {
		if (!isAllDataLoaded ) {
			CompletableFuture<List<Annotation>> future = kbService.getEvidences(statementId, "", numberOfPages,
					DATAPAGE_SIZE);

			try {
				List<Annotation> evidences = future.get(TIME_OUT, TIME_UNIT);
				container.addAll(evidences);
				numberOfPages++;
				if (evidences.size() < DATAPAGE_SIZE) {
					isAllDataLoaded = true;
				}

			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	}

}
