package ca.uhn.fhir.jpa.dao;

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
