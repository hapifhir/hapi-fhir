package ca.uhn.fhir.jpa.bulk.api;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;
import java.util.Set;

public class GroupBulkDataExportOptions extends BulkDataExportOptions {
	private final IIdType myGroupId;
	private final boolean myMdm;

	public GroupBulkDataExportOptions(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters, IIdType theGroupId, boolean theMdm) {
		super(theOutputFormat, theResourceTypes, theSince, theFilters);
		myGroupId = theGroupId;
		myMdm = theMdm;
	}

	public IIdType getGroupId() {
		return myGroupId;
	}

	public boolean isMdm() {
		return myMdm;
	}
}
