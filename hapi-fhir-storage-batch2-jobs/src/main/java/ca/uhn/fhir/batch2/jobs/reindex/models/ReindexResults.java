/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.reindex.models;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class ReindexResults implements IModelJson {

	/**
	 * A map of resource type : whether or not the reindex is completed;
	 * true = more work needed. false (or omitted) = reindex is done
	 */
	@JsonProperty("resource2NeedsWork")
	private HashMap<String, Boolean> myResourceToHasWorkToComplete;

	public ReindexResults() {}

	public HashMap<String, Boolean> getResourceToHasWorkToComplete() {
		if (myResourceToHasWorkToComplete == null) {
			myResourceToHasWorkToComplete = new HashMap<>();
		}
		return myResourceToHasWorkToComplete;
	}

	public void addResourceTypeToCompletionStatus(String theResourceType, boolean theRequiresMoreWork) {
		getResourceToHasWorkToComplete().put(theResourceType, theRequiresMoreWork);
	}
}
