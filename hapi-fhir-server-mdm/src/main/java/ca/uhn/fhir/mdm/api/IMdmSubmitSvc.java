package ca.uhn.fhir.mdm.api;

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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nullable;

public interface IMdmSubmitSvc {

	/**
	 * Submit all eligible resources for MDM processing.
	 *
	 * @param theCriteria The FHIR search critieria for filtering the resources to be submitted for MDM processing.
	 *                    NOTE:
	 *                    When using this function, the criteria supplied must be valid for all MDM types. e.g. , if you
	 *                    run this with the criteria birthDate=1990-06-28, it will fail, as Practitioners do not have a birthday.
	 *                    Use with caution.
	 *
	 * @return
	 */
	long submitAllSourceTypesToMdm(@Nullable String theCriteria, RequestDetails theRequestDetails);

	/**
	 * Given a type and a search criteria, submit all found resources for MDM processing.
	 *
	 * @param theSourceResourceType the resource type that you wish to execute a search over for submission to MDM.
	 * @param theCriteria The FHIR search critieria for filtering the resources to be submitted for MDM processing..
	 * @return the number of resources submitted for MDM processing.
	 */
	long submitSourceResourceTypeToMdm(String theSourceResourceType, String theCriteria, RequestDetails theRequestDetails);

	/**
	 * Convenience method that calls {@link #submitSourceResourceTypeToMdm(String, String)} with the type pre-populated.
	 *
	 * @param theCriteria The FHIR search critieria for filtering the resources to be submitted for MDM processing.
	 * @return the number of resources submitted for MDM processing.
	 */
	long submitPractitionerTypeToMdm(String theCriteria, RequestDetails theRequestDetails);

	/**
	 * Convenience method that calls {@link #submitSourceResourceTypeToMdm(String, String)} with the type pre-populated.
	 *
	 * @param theCriteria The FHIR search critieria for filtering the resources to be submitted for MDM processing.
	 * @return the number of resources submitted for MDM processing.
	 */
	long submitPatientTypeToMdm(String theCriteria, RequestDetails theRequestDetails);

	/**
	 * Given an ID and a source resource type valid for MDM, manually submit the given ID for MDM processing.
	 *
	 * @param theId the ID of the resource to process for MDM.
	 * @return the constant `1`, as if this function returns successfully, it will have processed one resource for MDM.
	 */
	long submitSourceResourceToMdm(IIdType theId, RequestDetails theRequestDetails);

	/**
	 * This setter exists to allow imported modules to override settings.
	 *
	 * @param theMdmSettings Settings to set
	 */
	void setMdmSettings(IMdmSettings theMdmSettings);

	/**
	 * Buffer size for fetching results to add to MDM queue.
	 *
	 * @param theBufferSize
	 */
	public void setBufferSize(int theBufferSize);

}
