package ca.uhn.fhir.rest.server;

import java.util.List;

import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;

public interface IVersionSpecificBundleFactory {

	void addResourcesToBundle(List<IResource> theResult, BundleTypeEnum theBundleType, String theServerBase);

	void addRootPropertiesToBundle(String theAuthor, String theServerBase, String theCompleteUrl, Integer theTotalResults, BundleTypeEnum theBundleType);

	void initializeBundleFromBundleProvider(RestfulServer theServer, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl, boolean thePrettyPrint,
			int theOffset, Integer theLimit, String theSearchId, BundleTypeEnum theBundleType);

	Bundle getDstu1Bundle();

	IBaseResource getResourceBundle();

	void initializeBundleFromResourceList(String theAuthor, List<IResource> theResult, String theServerBase, String theCompleteUrl, int theTotalResults, BundleTypeEnum theBundleType);

	void initializeWithBundleResource(IResource theResource);

	List<IResource> toListOfResources();

}
