package bio.knowledge.web.view.concept;

import bio.knowledge.web.view.concept.PanelScrollDetectorRpc;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Panel;

public abstract class PanelScrollDetector extends AbstractExtension {

	public PanelScrollDetector() {
		registerRpc(new PanelScrollDetectorRpc() {

			@Override
			public void thresholdReached() {
				endHasBeenReached();
			}
		});
	}

	protected void extend(Panel target) {
		super.extend(target);
	}

	public abstract void endHasBeenReached();
}