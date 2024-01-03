/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
