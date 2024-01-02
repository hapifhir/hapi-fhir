/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class ReindexOutcome {

	private List<String> myWarnings;

	public List<String> getWarnings() {
		return defaultIfNull(myWarnings, Collections.emptyList());
	}

	public void addWarning(String theWarning) {
		if (myWarnings == null) {
			myWarnings = new ArrayList<>();
		}
		myWarnings.add(theWarning);
	}
}
