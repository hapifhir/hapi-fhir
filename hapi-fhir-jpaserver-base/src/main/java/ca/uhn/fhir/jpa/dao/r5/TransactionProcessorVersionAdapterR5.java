package ca.uhn.fhir.jpa.dao.r5;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Resource;

import java.util.Date;
import java.util.List;

public class TransactionProcessorVersionAdapterR5 implements ITransactionProcessorVersionAdapter<Bundle, Bundle.BundleEntryComponent> {
	@Override
	public void setResponseStatus(Bundle.BundleEntryComponent theBundleEntry, String theStatus) {
		theBundleEntry.getResponse().setStatus(theStatus);
	}

	@Override
	public void setResponseLastModified(Bundle.BundleEntryComponent theBundleEntry, Date theLastModified) {
		theBundleEntry.getResponse().setLastModified(theLastModified);
	}

	@Override
	public void setResource(Bundle.BundleEntryComponent theBundleEntry, IBaseResource theResource) {
		theBundleEntry.setResource((Resource) theResource);
	}

	@Override
	public IBaseResource getResource(Bundle.BundleEntryComponent theBundleEntry) {
		return theBundleEntry.getResource();
	}

	@Override
	public String getBundleType(Bundle theRequest) {
		if (theRequest.getType() == null) {
			return null;
		}
		return theRequest.getTypeElement().getValue().toCode();
	}

	@Override
	public void populateEntryWithOperationOutcome(BaseServerResponseException theCaughtEx, Bundle.BundleEntryComponent theEntry) {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue()
			.setSeverity(OperationOutcome.IssueSeverity.ERROR)
			.setDiagnostics(theCaughtEx.getMessage())
			.setCode(OperationOutcome.IssueType.EXCEPTION);
		theEntry.getResponse().setOutcome(oo);
	}

	@Override
	public Bundle createBundle(String theBundleType) {
		Bundle resp = new Bundle();
		try {
			resp.setType(Bundle.BundleType.fromCode(theBundleType));
		} catch (FHIRException theE) {
			throw new InternalErrorException(Msg.code(1125) + "Unknown bundle type: " + theBundleType);
		}
		return resp;
	}

	@Override
	public List<Bundle.BundleEntryComponent> getEntries(Bundle theRequest) {
		return theRequest.getEntry();
	}

	@Override
	public void addEntry(Bundle theBundle, Bundle.BundleEntryComponent theEntry) {
		theBundle.addEntry(theEntry);
	}

	@Override
	public Bundle.BundleEntryComponent addEntry(Bundle theBundle) {
		return theBundle.addEntry();
	}

	@Override
	public String getEntryRequestVerb(FhirContext theContext, Bundle.BundleEntryComponent theEntry) {
		String retVal = null;
		Bundle.HTTPVerb value = theEntry.getRequest().getMethodElement().getValue();
		if (value != null) {
			retVal = value.toCode();
		}
		return retVal;
	}

	@Override
	public String getFullUrl(Bundle.BundleEntryComponent theEntry) {
		return theEntry.getFullUrl();
	}


	@Override
	public void setFullUrl(Bundle.BundleEntryComponent theEntry, String theFullUrl) {
		theEntry.setFullUrl(theFullUrl);
	}

	@Override
	public String getEntryIfNoneExist(Bundle.BundleEntryComponent theEntry) {
		return theEntry.getRequest().getIfNoneExist();
	}

	@Override
	public String getEntryRequestUrl(Bundle.BundleEntryComponent theEntry) {
		return theEntry.getRequest().getUrl();
	}

	@Override
	public void setResponseLocation(Bundle.BundleEntryComponent theEntry, String theResponseLocation) {
		theEntry.getResponse().setLocation(theResponseLocation);
	}

	@Override
	public void setResponseETag(Bundle.BundleEntryComponent theEntry, String theEtag) {
		theEntry.getResponse().setEtag(theEtag);
	}

	@Override
	public String getEntryRequestIfMatch(Bundle.BundleEntryComponent theEntry) {
		return theEntry.getRequest().getIfMatch();
	}

	@Override
	public String getEntryRequestIfNoneExist(Bundle.BundleEntryComponent theEntry) {
		return theEntry.getRequest().getIfNoneExist();
	}

	@Override
	public String getEntryRequestIfNoneMatch(Bundle.BundleEntryComponent theEntry) {
		return theEntry.getRequest().getIfNoneMatch();
	}

	@Override
	public void setResponseOutcome(Bundle.BundleEntryComponent theEntry, IBaseOperationOutcome theOperationOutcome) {
		theEntry.getResponse().setOutcome((Resource) theOperationOutcome);
	}

	@Override
	public void setRequestVerb(Bundle.BundleEntryComponent theEntry, String theVerb) {
		theEntry.getRequest().setMethod(Bundle.HTTPVerb.fromCode(theVerb));
	}

	@Override
	public void setRequestUrl(Bundle.BundleEntryComponent theEntry, String theUrl) {
		theEntry.getRequest().setUrl(theUrl);
	}

}
