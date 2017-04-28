package bio.knowledge.grid.client.grid;

import com.vaadin.client.widget.grid.events.ScrollEvent;
import com.vaadin.client.widget.grid.events.ScrollHandler;
import com.vaadin.shared.ui.Connect;

//@Connect(bio.knowledge.renderer.ButtonRenderer.class)
@Connect(bio.knowledge.grid.Grid.class)
public class GridConnector extends com.vaadin.client.connectors.GridConnector {

	private static final long serialVersionUID = 7130352625723507638L;

	@Override
	public GridWidget getWidget() {
		return (GridWidget) super.getWidget();
	}

	public GridConnector() {
		getWidget().addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent event) {
				
				if (getWidget().getScrollTop() == (getWidget().getScrollHeight() - 10)) {
					getWidget().setTitle("HELLOOO");
				} else {
					getWidget().setTitle("GOODBYE");
				}
				GridRpc rpc = getRpcProxy(GridRpc.class);
				String message = "Yep";
				rpc.scrolled(message);
			}
			
		});
 	}
	
}
