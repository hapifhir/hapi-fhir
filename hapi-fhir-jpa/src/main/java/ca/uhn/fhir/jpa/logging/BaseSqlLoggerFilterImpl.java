/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.logging;

import com.google.common.annotations.VisibleForTesting;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseSqlLoggerFilterImpl implements ISqlLoggerFilter {
	protected final List<String> myFilterDefinitions = new LinkedList<>();

	@Override
	public boolean evaluateFilterLine(String theFilterLine) {
		boolean matched = theFilterLine.startsWith(getPrefix());
		if (matched) {
			myFilterDefinitions.add(
					theFilterLine.substring(getPrefix().length()).trim());
		}
		return matched;
	}

	@Override
	public void clearDefinitions() {
		myFilterDefinitions.clear();
	}

	@Override
	public Object getLockingObject() {
		return myFilterDefinitions;
	}

	@VisibleForTesting
	public void setFilterDefinitions(List<String> theFilterDefinitions) {
		synchronized (myFilterDefinitions) {
			myFilterDefinitions.clear();
			myFilterDefinitions.addAll(theFilterDefinitions);
		}
	}
}
