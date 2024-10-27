/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the reason a specific service suggestion was overridden
 */
public class CdsServiceOverrideReasonJson implements IModelJson {
	@JsonProperty("reason")
	CdsServiceResponseCodingJson myReason;

	@JsonProperty("userComment")
	String myUserComment;

	public CdsServiceResponseCodingJson getReason() {
		return myReason;
	}

	public CdsServiceOverrideReasonJson setReason(CdsServiceResponseCodingJson theReason) {
		myReason = theReason;
		return this;
	}

	public String getUserComment() {
		return myUserComment;
	}

	public CdsServiceOverrideReasonJson setUserComment(String theUserComment) {
		myUserComment = theUserComment;
		return this;
	}
}
