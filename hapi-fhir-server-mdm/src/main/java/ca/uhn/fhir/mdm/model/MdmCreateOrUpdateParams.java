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

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class MdmCreateOrUpdateParams {
	/**
	 * The golden resource id
	 */
	private String myGoldenResourceId;

	/**
	 * The golden resource to update for the link.
	 */
	private IAnyResource myGoldenResource;

	/**
	 * The source id.
	 */
	private String myResourceId;

	/**
	 * The source resource linked to the golden resource
	 */
	private IAnyResource mySourceResource;
	/**
	 * Match result to update the link to.
	 */
	private MdmMatchResultEnum myMatchResult;
	/**
	 * The context.
	 */
	private MdmTransactionContext myMdmContext;

	/**
	 * The request details.
	 */
	private RequestDetails myRequestDetails;

	public String getGoldenResourceId() {
		return myGoldenResourceId;
	}

	public void setGoldenResourceId(String theGoldenResourceId) {
		myGoldenResourceId = theGoldenResourceId;
	}

	public String getResourceId() {
		return myResourceId;
	}

	public void setResourceId(String theResourceId) {
		myResourceId = theResourceId;
	}

	public IAnyResource getGoldenResource() {
		return myGoldenResource;
	}

	public void setGoldenResource(IAnyResource theGoldenResource) {
		myGoldenResource = theGoldenResource;
	}

	public IAnyResource getSourceResource() {
		return mySourceResource;
	}

	public void setSourceResource(IAnyResource theSourceResource) {
		mySourceResource = theSourceResource;
	}

	public MdmMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public void setMatchResult(MdmMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
	}

	public MdmTransactionContext getMdmContext() {
		return myMdmContext;
	}

	public void setMdmContext(MdmTransactionContext theMdmContext) {
		myMdmContext = theMdmContext;
	}

	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		myRequestDetails = theRequestDetails;
	}
}
