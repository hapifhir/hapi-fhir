/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.util;

import org.apache.commons.lang3.Validate;
import org.hibernate.ScrollableResults;

import java.io.Closeable;
import java.util.Iterator;

public class ScrollableResultsIterator<T extends Object> extends BaseIterator<T> implements Iterator<T>, Closeable {
	private boolean hasNext;
	private T myNext;
	private ScrollableResults myScroll;

	public ScrollableResultsIterator(ScrollableResults theScroll) {
		myScroll = theScroll;
	}

	@SuppressWarnings("unchecked")
	private void ensureHaveNext() {
		if (myNext == null) {
			if (myScroll.next()) {
				hasNext = true;
				myNext = (T) myScroll.get();
			} else {
				hasNext = false;
			}
		}
	}

	@Override
	public boolean hasNext() {
		ensureHaveNext();
		return hasNext;
	}

	@Override
	public T next() {
		ensureHaveNext();
		Validate.isTrue(hasNext);
		T next = myNext;
		myNext = null;
		return next;
	}

	@Override
	public void close() {
		if (myScroll != null) {
			myScroll.close();
			myScroll = null;
		}
	}
}
