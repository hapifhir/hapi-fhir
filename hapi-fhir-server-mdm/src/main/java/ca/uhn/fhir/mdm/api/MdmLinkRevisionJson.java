package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MdmLinkRevisionJson implements IModelJson {
	@JsonProperty(value = "mdmLink", required = true)
	MdmLinkJson myMdmLink;

	@JsonProperty(value = "revisionNumber", required = true)
	Integer myRevisionNumber;

	@JsonProperty(value = "revisionTimestamp", required = true)
	LocalDateTime myRevisionTimestamp;

	public MdmLinkRevisionJson(MdmLinkJson theMdmLink, Integer theRevisionNumber, LocalDateTime theRevisionTimestamp) {
		myMdmLink = theMdmLink;
		myRevisionNumber = theRevisionNumber;
		myRevisionTimestamp = theRevisionTimestamp;
	}

	public MdmLinkJson getMdmLink() {
		return myMdmLink;
	}

	public Integer getRevisionNumber() {
		return myRevisionNumber;
	}

	public LocalDateTime getRevisionTimestamp() {
		return myRevisionTimestamp;
	}
}
