package bio.knowledge.scrollgrid;

import com.vaadin.shared.communication.ServerRpc;

public interface GridScrollDetectorRpc extends ServerRpc {

public void thresholdReached();

}