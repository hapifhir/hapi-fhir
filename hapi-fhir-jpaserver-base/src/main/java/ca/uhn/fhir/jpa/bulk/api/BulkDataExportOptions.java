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

import java.util.Date;
import java.util.Set;

public class BulkDataExportOptions {
	private final String myOutputFormat;
	private final Set<String> myResourceTypes;
	private final Date mySince;
	private final Set<String> myFilters;

	public BulkDataExportOptions(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters) {
		myOutputFormat = theOutputFormat;
		myResourceTypes = theResourceTypes;
		mySince = theSince;
		myFilters = theFilters;
	}

	public String getOutputFormat() {
		return myOutputFormat;
	}

	public Set<String> getResourceTypes() {
		return myResourceTypes;
	}

	public Date getSince() {
		return mySince;
	}

	public Set<String> getFilters() {
		return myFilters;
	}
}
