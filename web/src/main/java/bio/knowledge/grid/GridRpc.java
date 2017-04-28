package bio.knowledge.grid;

import com.vaadin.shared.communication.ServerRpc;

public interface GridRpc extends ServerRpc {
	public void scrolled(String gridName);
}
