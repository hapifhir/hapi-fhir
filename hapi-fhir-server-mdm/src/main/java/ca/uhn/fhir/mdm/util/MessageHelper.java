package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageHelper {

	@Autowired
	private final IMdmSettings myMdmSettings;

	@Autowired
	private final FhirContext myFhirContext;

	public MessageHelper(IMdmSettings theMdmSettings, FhirContext theFhirContext) {
		myMdmSettings = theMdmSettings;
		myFhirContext = theFhirContext;
	}

	public String getMessageForUnmanagedResource() {
		return String.format(
			"Only MDM managed resources can be merged. MDM managed resources must have the %s tag.",
			MdmConstants.CODE_HAPI_MDM_MANAGED);
	}

	public String getMessageForUnsupportedResource(String theName, IAnyResource theResource) {
		return getMessageForUnsupportedResource(theName, myFhirContext.getResourceType(theResource));
	}

	public String getMessageForUnsupportedResource(String theName, String theResourceType) {
		return String.format("Only %s resources can be merged. The %s points to a %s",
			myMdmSettings.getSupportedMdmTypes(), theName, theResourceType);
	}

	public String getMessageForUnsupportedMatchResult() {
		return "Match Result may only be set to " + MdmMatchResultEnum.NO_MATCH + " or " + MdmMatchResultEnum.MATCH;
	}

	public String getMessageForUnsupportedFirstArgumentTypeInUpdate(String goldenRecordType) {
		return "First argument to " + ProviderConstants.MDM_UPDATE_LINK + " must be a "
			+ myMdmSettings.getSupportedMdmTypes() + ".  Was " + goldenRecordType;
	}

	public String getMessageForUnsupportedSecondArgumentTypeInUpdate(String theGoldenRecordType) {
		return "First argument to " + ProviderConstants.MDM_UPDATE_LINK + " must be a "
			+ myMdmSettings.getSupportedMdmTypes() + ".  Was " + theGoldenRecordType;
	}

	public String getMessageForArgumentTypeMismatchInUpdate(String theGoldenRecordType, String theSourceResourceType) {
		return "Arguments to " + ProviderConstants.MDM_UPDATE_LINK + " must be of the same type. Were " +
			theGoldenRecordType + " and " + theSourceResourceType;
	}

	public String getMessageForUnsupportedSourceResource() {
		return "The source resource is marked with the " + MdmConstants.CODE_NO_MDM_MANAGED
			+ " tag which means it may not be MDM linked.";
	}

	public String getMessageForNoLink(IAnyResource theGoldenRecord, IAnyResource theSourceResource) {
		return getMessageForNoLink(theGoldenRecord.getIdElement().toVersionless().toString(),
			theSourceResource.getIdElement().toVersionless().toString());
	}

	public String getMessageForNoLink(String theGoldenRecord, String theSourceResource) {
		return "No link exists between " + theGoldenRecord + " and " + theSourceResource;
	}

	public String getMessageForPresentLink(IAnyResource theGoldenRecord, IAnyResource theSourceResource) {
		return getMessageForPresentLink(theGoldenRecord.getIdElement().toVersionless().toString(),
			theSourceResource.getIdElement().toVersionless().toString());
	}

	public String getMessageForPresentLink(String theGoldenRecord, String theSourceResource) {
		return "Link already exists between " + theGoldenRecord + " and " + theSourceResource + ". Use $mdm-update-link instead.";
	}

	public String getMessageForMultipleGoldenRecords(IAnyResource theSourceResource) {
		return getMessageForMultipleGoldenRecords(theSourceResource.getIdElement().toVersionless().toString());
	}

	public String getMessageForMultipleGoldenRecords(String theSourceResource) {
		return theSourceResource + " already has matched golden resource. Use $mdm-query-links to see more details.";
	}

	public String getMessageForFailedGoldenResourceLoad(String theParamName, String theGoldenResourceId) {
		return theGoldenResourceId + " used as parameter [" + theParamName + "] could not be loaded as a golden resource, as it appears to be lacking the golden resource meta tags.";
	}

	public String getMessageForMismatchPartition(IAnyResource theGoldenRecord, IAnyResource theSourceResource) {
		return getMessageForMismatchPartition(theGoldenRecord.getIdElement().toVersionless().toString(),
			theSourceResource.getIdElement().toVersionless().toString());
	}

	public String getMessageForMismatchPartition(String theGoldenRecord, String theSourceResource) {
		return theGoldenRecord + " and " + theSourceResource + " are stored in different partitions. This operation is only available for resources on the same partition.";
	}
}
