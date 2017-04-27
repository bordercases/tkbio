package bio.knowledge.web.view.concept;

import bio.knowledge.web.view.concept.GridScrollDetectorRpc;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Panel;

public abstract class GridScrollDetector extends AbstractExtension {

	public GridScrollDetector() {
		registerRpc(new GridScrollDetectorRpc() {

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