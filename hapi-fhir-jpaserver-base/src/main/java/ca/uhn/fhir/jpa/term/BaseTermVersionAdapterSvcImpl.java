package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.r4.model.CodeSystem;

public abstract class BaseTermVersionAdapterSvcImpl implements ITermVersionAdapterSvc {


	protected void validateCodeSystemForStorage(CodeSystem theCodeSystemResource) {
		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(theCodeSystemResource.getUrl(), "Can not store a CodeSystem without a valid URL");
	}

}
