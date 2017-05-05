package bio.knowledge.web.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.VerticalLayout;

public abstract class NewBaseView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -7575227266311702352L;
	
	protected static final String COL_ID_NAME      = "name";
	protected static final String COL_ID_SUPPORTING_TEXT = "supportingText";
	protected static final String COL_ID_EVIDENCE  = "evidence";
	protected static final String COL_ID_RELATION  = "relation";
	protected static final String COL_ID_OBJECT    = "object";
	protected static final String COL_ID_SUBJECT   = "subject";
	protected static final String COL_ID_REFERENCE = "reference";
	protected static final String COL_ID_PUBLICATION_DATE = "publicationDate";
	
	protected String getStyle(CellReference cellRef) {
		return getStyle((String) cellRef.getPropertyId());
	}
	
	private String getStyle(String cellPropertyId) {
		String styleName = "";
		switch (cellPropertyId) {

		// Concept-by-Text table
		case "name":
			styleName = "name-cell";
			break;

		case "type":
			styleName = "type-cell";
			break;

		case "description":
			styleName = "description-cell";
			break;

		// Relation table
		case COL_ID_SUBJECT:
		case COL_ID_OBJECT:
			styleName = "concept-cell";
			break;

		case COL_ID_RELATION:
			styleName = "relation-cell" ;
			break ;
			
		case COL_ID_EVIDENCE:
			styleName = "evidence-cell" ;
			break ;
			
		case "library":
		case "parents":
			styleName = "library-cell" ;
			break ;
			
		case COL_ID_PUBLICATION_DATE:
			styleName = "publication-date-cell" ;
			break ;

		case COL_ID_REFERENCE:
			styleName = "reference-cell" ;
			break ;

		// Otherwise ignore?
		default:
			styleName = null;
			break;
		}
		return styleName;
	}

}
