package bio.knowledge.web.view.concept;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.web.view.concept.GridScrollDetector;
import elemental.json.JsonObject;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.widgets.Grid;
import com.vaadin.client.connectors.GridConnector;
import com.vaadin.shared.ui.Connect;

@Connect(GridScrollDetector.class)
public class GridScrollDetectorConnector extends AbstractExtensionConnector implements ScrollHandler {
	private Grid<JsonObject> target;
	private HandlerRegistration scrollHandler;
	private int latestScrollTop;
	private int latestScrollLeft;

	@Override
	protected void extend(ServerConnector target) {
		this.target = ((GridConnector) target).getWidget();
		scrollHandler = this.target.addHandler(this, ScrollEvent.getType());
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void onScroll(ScrollEvent event) {

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				int newscrollTop = (int) target.getScrollTop();
				int newscrollLeft = (int) target.getScrollLeft();
				if (newscrollTop != latestScrollTop || newscrollLeft != latestScrollLeft) {
					int gridHeight = target.getOffsetHeight();
					latestScrollTop = newscrollTop;
					latestScrollLeft = newscrollLeft;
					if (gridHeight == latestScrollTop) {
						getRpcProxy(GridScrollDetectorRpc.class).thresholdReached();
					}
				}
			}
		});
	}

	@Override
	public void onUnregister() {
		super.onUnregister();
		if (scrollHandler != null) {
			scrollHandler.removeHandler();
		}
	}
}