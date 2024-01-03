/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IWarningProcessor;

public class ReindexWarningProcessor implements IWarningProcessor {

	private String myRecoveredWarning;

	@Override
	public void recoverWarningMessage(String theErrorMessage) {
		// save non-fatal error as warning, current only support unique search param reindexing error on existing
		// duplicates
		if (theErrorMessage.contains("Can not create resource")
				&& theErrorMessage.contains("it would create a duplicate unique index matching query")) {
			String searchParamName =
					theErrorMessage.substring(theErrorMessage.indexOf("SearchParameter"), theErrorMessage.length() - 1);
			myRecoveredWarning = "Failed to reindex resource because unique search parameter " + searchParamName
					+ " could not be enforced.";
		}
	}

	@Override
	public String getRecoveredWarningMessage() {
		return myRecoveredWarning;
	}
}
