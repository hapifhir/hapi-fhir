package ca.uhn.fhir.jpa.model.util;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransactionDetails {

	private final Date myTransactionDate;
	private Map<IIdType, ResourcePersistentId> myPreResolvedResourceIds = Collections.emptyMap();

	/**
	 * Constructor
	 */
	public TransactionDetails() {
		myTransactionDate = new Date();
	}

	/**
	 * Constructor
	 */
	public TransactionDetails(Date theTransactionDate) {
		myTransactionDate = theTransactionDate;
	}

	public Map<IIdType, ResourcePersistentId> getPreResolvedResourceIds() {
		return myPreResolvedResourceIds;
	}

	public void addPreResolvedResourceId(IIdType theResourceId, ResourcePersistentId thePersistentId) {
		assert theResourceId != null;
		assert thePersistentId != null;

		if (myPreResolvedResourceIds.isEmpty()) {
			myPreResolvedResourceIds = new HashMap<>();
		}
		myPreResolvedResourceIds.put(theResourceId, thePersistentId);
	}

	public Date getTransactionDate() {
		return myTransactionDate;
	}

}
