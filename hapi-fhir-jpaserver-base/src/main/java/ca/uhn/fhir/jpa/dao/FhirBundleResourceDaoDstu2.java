package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class FhirBundleResourceDaoDstu2 extends FhirResourceDaoDstu2<Bundle> {

	@Override
	protected void preProcessResourceForStorage(Bundle theResource) {
		super.preProcessResourceForStorage(theResource);

		if (theResource.getTypeElement().getValueAsEnum() != BundleTypeEnum.DOCUMENT) {
			String message = "Unable to store a Bundle resource on this server with a Bundle.type value other than '" + BundleTypeEnum.DOCUMENT.getCode() + "' - Value was: " + (theResource.getTypeElement().getValueAsEnum() != null ? theResource.getTypeElement().getValueAsEnum().getCode() : "(missing)");
			throw new UnprocessableEntityException(message);
		}
		
		theResource.setBase((UriDt)null);
	}



}
