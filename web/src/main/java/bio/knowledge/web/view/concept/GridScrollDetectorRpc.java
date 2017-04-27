package bio.knowledge.web.view.concept;

import com.vaadin.shared.communication.ServerRpc;

public interface GridScrollDetectorRpc extends ServerRpc {

public void thresholdReached();

}