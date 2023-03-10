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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

public class MdmHistorySearchParameters {
	// TODO:  mdmLinkId
	// TODO:  goldenResourceId

	public static final String MDM_LINK_ID = "myMdmLinkId";

	private List<String> myMdmGoldenResourceIds;
	private List<String> myMdmTargetResourceIds;

	// TODO:  other constructors?
	public MdmHistorySearchParameters() {}

	public List<String> getMdmGoldenResourceIds() {
		return myMdmGoldenResourceIds;
	}

	public List<String> getMdmTargetResourceIds() {
		return myMdmTargetResourceIds;
	}

	public MdmHistorySearchParameters setMdmGoldenResourceIds(List<String> theMdmGoldenResourceIds) {
		myMdmGoldenResourceIds = theMdmGoldenResourceIds;
		return this;
	}

	public MdmHistorySearchParameters setMdmTargetResourceIds(List<String> theMdmTargetResourceIds) {
		myMdmTargetResourceIds = theMdmTargetResourceIds;
		return this;
	}
}
