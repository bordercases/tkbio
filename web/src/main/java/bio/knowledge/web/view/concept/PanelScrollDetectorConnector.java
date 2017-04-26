package bio.knowledge.web.view.concept;

import bio.knowledge.web.view.concept.PanelScrollDetector;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.shared.ui.Connect;

@Connect(PanelScrollDetector.class)
public class PanelScrollDetectorConnector extends AbstractExtensionConnector implements ScrollHandler {
	private VPanel target;
	private HandlerRegistration scrollHandler;
	private int latestScrollTop;
	private int latestScrollLeft;

	@Override
	protected void extend(ServerConnector target) {
		this.target = ((PanelConnector) target).getWidget();
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
				int newscrollTop = DOM.getElementPropertyInt(target.contentNode, "scrollTop");
				int newscrollLeft = DOM.getElementPropertyInt(target.contentNode, "scrollLeft");
				if (newscrollTop != latestScrollTop || newscrollLeft != latestScrollLeft) {
					int panelHeight = target.contentNode.getOffsetHeight();
					int contentHeight = target.contentNode.getFirstChildElement().getOffsetHeight();
					int maxScrollPoint = contentHeight - panelHeight;
					latestScrollTop = newscrollTop;
					latestScrollLeft = newscrollLeft;
					if (maxScrollPoint == latestScrollTop) {
						getRpcProxy(PanelScrollDetectorRpc.class).thresholdReached();
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