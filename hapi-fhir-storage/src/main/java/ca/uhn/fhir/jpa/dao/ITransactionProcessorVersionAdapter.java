package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Date;
import java.util.List;

public interface ITransactionProcessorVersionAdapter<BUNDLE extends IBaseBundle, BUNDLEENTRY extends IBase> {

	void setResponseStatus(BUNDLEENTRY theBundleEntry, String theStatus);

	void setResponseLastModified(BUNDLEENTRY theBundleEntry, Date theLastModified);

	void setResource(BUNDLEENTRY theBundleEntry, IBaseResource theResource);

	IBaseResource getResource(BUNDLEENTRY theBundleEntry);

	String getBundleType(BUNDLE theRequest);

	void populateEntryWithOperationOutcome(BaseServerResponseException theCaughtEx, BUNDLEENTRY theEntry);

	BUNDLE createBundle(String theBundleType);

	List<BUNDLEENTRY> getEntries(BUNDLE theRequest);

	void addEntry(BUNDLE theBundle, BUNDLEENTRY theEntry);

	BUNDLEENTRY addEntry(BUNDLE theBundle);

	String getEntryRequestVerb(FhirContext theContext, BUNDLEENTRY theEntry);

	String getFullUrl(BUNDLEENTRY theEntry);

	void setFullUrl(BUNDLEENTRY theEntry, String theFullUrl);

	String getEntryIfNoneExist(BUNDLEENTRY theEntry);

	String getEntryRequestUrl(BUNDLEENTRY theEntry);

	void setResponseLocation(BUNDLEENTRY theEntry, String theResponseLocation);

	void setResponseETag(BUNDLEENTRY theEntry, String theEtag);

	String getEntryRequestIfMatch(BUNDLEENTRY theEntry);

	String getEntryRequestIfNoneExist(BUNDLEENTRY theEntry);

	String getEntryRequestIfNoneMatch(BUNDLEENTRY theEntry);

	void setResponseOutcome(BUNDLEENTRY theEntry, IBaseOperationOutcome theOperationOutcome);

	void setRequestVerb(BUNDLEENTRY theEntry, String theVerb);

	void setRequestUrl(BUNDLEENTRY theEntry, String theUrl);

}
