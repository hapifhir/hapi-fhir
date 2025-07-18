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
package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesJobParameters;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MergeJobParameters extends ReplaceReferencesJobParameters {
	@JsonProperty("deleteSource")
	private boolean myDeleteSource;

	@JsonProperty("resultResource")
	private String myResultResource;

	@JsonProperty("originalInputParameters")
	private String myOriginalInputParameters;

	/**
	 * @deprecated we have the whole original input parameters object now,
	 * which contains this as well, look up resultsResource from there
	 */
	@Deprecated(since = "8.3")
	public void setResultResource(String theResultResource) {
		myResultResource = theResultResource;
	}

	/**
	 * @deprecated we have the whole original input parameters object now,
	 * which contains this as well, use getMyOriginalInputParameters and look up resultsResource from there
	 */
	@Deprecated(since = "8.3")
	public String getResultResource() {
		return myResultResource;
	}

	/**
	 * @deprecated we have the whole original input parameters object now,
	 * which contains this as well, use getMyOriginalInputParameters and look up deleteSource from there
	 */
	@Deprecated(since = "8.3")
	public boolean getDeleteSource() {
		return myDeleteSource;
	}

	/**
	 * @deprecated we have the whole original input parameters object now, which contains this as well use setMyOriginalInputParameters
	 */
	@Deprecated(since = "8.3")
	public void setDeleteSource(boolean theDeleteSource) {
		this.myDeleteSource = theDeleteSource;
	}

	public String getOriginalInputParameters() {
		return myOriginalInputParameters;
	}

	public void setOriginalInputParameters(String myOriginalInputParameters) {
		this.myOriginalInputParameters = myOriginalInputParameters;
	}
}
