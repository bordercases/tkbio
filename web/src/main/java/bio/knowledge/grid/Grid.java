package bio.knowledge.grid;

import com.vaadin.ui.Notification;

import bio.knowledge.grid.client.grid.GridRpc;

public class Grid extends com.vaadin.ui.Grid {
	private static final long serialVersionUID = 6340543125776370643L;
	private GridRpc rpc = new GridRpc() {
                 				public void scrolled(String message) {
             						Notification.show(message);
                 				};
							};
		
	public Grid() {
		super();
        registerRpc(rpc);
	}
}
