package ca.uhn.fhir.batch2.jobs.imprt;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NdJsonFileJson implements IModelJson {

	@JsonProperty("ndJsonText")
	private String myNdJsonText;
	@JsonProperty("sourceName")
	private String mySourceName;

	public String getNdJsonText() {
		return myNdJsonText;
	}

	public NdJsonFileJson setNdJsonText(String theNdJsonText) {
		myNdJsonText = theNdJsonText;
		return this;
	}

	public String getSourceName() {
		return mySourceName;
	}

	public void setSourceName(String theSourceName) {
		mySourceName = theSourceName;
	}
}
