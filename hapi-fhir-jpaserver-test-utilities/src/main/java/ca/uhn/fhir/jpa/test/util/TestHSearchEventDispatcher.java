package ca.uhn.fhir.jpa.test.util;

import ca.uhn.fhir.jpa.dao.IHSearchEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TestHSearchEventDispatcher implements IHSearchEventListener {
	private final Logger ourLog = LoggerFactory.getLogger(TestHSearchEventDispatcher.class);

	private final List<IHSearchEventListener> listeners = new ArrayList<>();


	public void register(IHSearchEventListener theListener) {
		if ( theListener.equals(this) ) {
			ourLog.error("Dispatcher is not supposed to register itself as a listener. Ignored.");
			return;
		}

		listeners.add(theListener);
	}


	/**
	 * Dispatch event to registered listeners
	 */
	@Override
	public void hsearchEvent(HSearchEventType theEventType) {
		listeners.forEach( l -> l.hsearchEvent(theEventType) );
	}
}
