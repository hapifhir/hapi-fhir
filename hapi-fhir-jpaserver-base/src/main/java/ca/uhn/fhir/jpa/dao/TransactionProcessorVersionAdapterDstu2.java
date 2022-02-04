package ca.uhn.fhir.jpa.dao;

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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Date;
import java.util.List;

public class TransactionProcessorVersionAdapterDstu2 implements ITransactionProcessorVersionAdapter<Bundle, Bundle.Entry> {
	@Override
	public void setResponseStatus(Bundle.Entry theBundleEntry, String theStatus) {
		theBundleEntry.getResponse().setStatus(theStatus);
	}

	@Override
	public void setResponseLastModified(Bundle.Entry theBundleEntry, Date theLastModified) {
		theBundleEntry.getResponse().setLastModified(theLastModified, TemporalPrecisionEnum.MILLI);
	}

	@Override
	public void setResource(Bundle.Entry theBundleEntry, IBaseResource theResource) {
		theBundleEntry.setResource((IResource) theResource);
	}

	@Override
	public IBaseResource getResource(Bundle.Entry theBundleEntry) {
		return theBundleEntry.getResource();
	}

	@Override
	public String getBundleType(Bundle theRequest) {
		if (theRequest.getType() == null) {
			return null;
		}
		return theRequest.getTypeElement().getValue();
	}

	@Override
	public void populateEntryWithOperationOutcome(BaseServerResponseException theCaughtEx, Bundle.Entry theEntry) {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue()
			.setSeverity(IssueSeverityEnum.ERROR)
			.setDiagnostics(theCaughtEx.getMessage())
			.setCode(IssueTypeEnum.EXCEPTION);
		theEntry.setResource(oo);
	}

	@Override
	public Bundle createBundle(String theBundleType) {
		Bundle resp = new Bundle();
		try {
			resp.setType(BundleTypeEnum.forCode(theBundleType));
		} catch (FHIRException theE) {
			throw new InternalErrorException(Msg.code(936) + "Unknown bundle type: " + theBundleType);
		}
		return resp;
	}

	@Override
	public List<Bundle.Entry> getEntries(Bundle theRequest) {
		return theRequest.getEntry();
	}

	@Override
	public void addEntry(Bundle theBundle, Bundle.Entry theEntry) {
		theBundle.addEntry(theEntry);
	}

	@Override
	public Bundle.Entry addEntry(Bundle theBundle) {
		return theBundle.addEntry();
	}

	@Override
	public String getEntryRequestVerb(FhirContext theContext, Bundle.Entry theEntry) {
		String retVal = null;
		HTTPVerbEnum value = theEntry.getRequest().getMethodElement().getValueAsEnum();
		if (value != null) {
			retVal = value.getCode();
		}
		return retVal;
	}

	@Override
	public String getFullUrl(Bundle.Entry theEntry) {
		return theEntry.getFullUrl();
	}

	@Override
	public void setFullUrl(Bundle.Entry theEntry, String theFullUrl) {
		theEntry.setFullUrl(theFullUrl);
	}

	@Override
	public String getEntryIfNoneExist(Bundle.Entry theEntry) {
		return theEntry.getRequest().getIfNoneExist();
	}

	@Override
	public String getEntryRequestUrl(Bundle.Entry theEntry) {
		return theEntry.getRequest().getUrl();
	}

	@Override
	public void setResponseLocation(Bundle.Entry theEntry, String theResponseLocation) {
		theEntry.getResponse().setLocation(theResponseLocation);
	}

	@Override
	public void setResponseETag(Bundle.Entry theEntry, String theEtag) {
		theEntry.getResponse().setEtag(theEtag);
	}

	@Override
	public String getEntryRequestIfMatch(Bundle.Entry theEntry) {
		return theEntry.getRequest().getIfMatch();
	}

	@Override
	public String getEntryRequestIfNoneExist(Bundle.Entry theEntry) {
		return theEntry.getRequest().getIfNoneExist();
	}

	@Override
	public String getEntryRequestIfNoneMatch(Bundle.Entry theEntry) {
		return theEntry.getRequest().getIfNoneMatch();
	}

	@Override
	public void setResponseOutcome(Bundle.Entry theEntry, IBaseOperationOutcome theOperationOutcome) {
		theEntry.setResource((IResource) theOperationOutcome);
	}

	@Override
	public void setRequestVerb(Bundle.Entry theEntry, String theVerb) {
		theEntry.getRequest().setMethod(HTTPVerbEnum.forCode(theVerb));
	}

	@Override
	public void setRequestUrl(Bundle.Entry theEntry, String theUrl) {
		theEntry.getRequest().setUrl(theUrl);
	}

}
