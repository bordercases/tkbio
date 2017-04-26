package bio.knowledge.web.view.concept;

import com.vaadin.shared.communication.ServerRpc;

public interface PanelScrollDetectorRpc extends ServerRpc {

public void thresholdReached();

}