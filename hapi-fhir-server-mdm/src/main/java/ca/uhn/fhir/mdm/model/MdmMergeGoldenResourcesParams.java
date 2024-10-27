/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class MdmMergeGoldenResourcesParams {
	private String myFromGoldenResourceId;
	private IAnyResource myFromGoldenResource;
	private String myToGoldenResourceId;

	private IAnyResource myManuallyMergedResource;

	private IAnyResource myToGoldenResource;

	private MdmTransactionContext myMdmTransactionContext;

	private RequestDetails myRequestDetails;

	public String getFromGoldenResourceId() {
		return myFromGoldenResourceId;
	}

	public void setFromGoldenResourceId(String theTheFromGoldenResourceId) {
		myFromGoldenResourceId = theTheFromGoldenResourceId;
	}

	public String getToGoldenResourceId() {
		return myToGoldenResourceId;
	}

	public void setToGoldenResourceId(String theTheToGoldenResourceId) {
		myToGoldenResourceId = theTheToGoldenResourceId;
	}

	public IAnyResource getFromGoldenResource() {
		return myFromGoldenResource;
	}

	public void setFromGoldenResource(IAnyResource theFromGoldenResource) {
		myFromGoldenResource = theFromGoldenResource;
	}

	public IAnyResource getManuallyMergedResource() {
		return myManuallyMergedResource;
	}

	public void setManuallyMergedResource(IAnyResource theManuallyMergedResource) {
		myManuallyMergedResource = theManuallyMergedResource;
	}

	public IAnyResource getToGoldenResource() {
		return myToGoldenResource;
	}

	public void setToGoldenResource(IAnyResource theToGoldenResource) {
		myToGoldenResource = theToGoldenResource;
	}

	public MdmTransactionContext getMdmTransactionContext() {
		return myMdmTransactionContext;
	}

	public void setMdmTransactionContext(MdmTransactionContext theMdmTransactionContext) {
		myMdmTransactionContext = theMdmTransactionContext;
	}

	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		myRequestDetails = theRequestDetails;
	}
}
