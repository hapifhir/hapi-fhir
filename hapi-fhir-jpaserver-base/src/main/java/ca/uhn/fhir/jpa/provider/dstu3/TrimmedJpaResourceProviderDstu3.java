package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.TrimmedBaseJpaResourceProvider;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IAnyResource;

public abstract class TrimmedJpaResourceProviderDstu3<T extends IAnyResource> extends TrimmedBaseJpaResourceProvider<T> {


	public TrimmedJpaResourceProviderDstu3(IFhirResourceDao<T> theDao) {
		super(theDao);

	}

	@Validate
	public MethodOutcome validate(@ResourceParam T theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
											@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
		return validate(theResource, null, theRawResource, theEncoding, theMode, theProfile, theRequestDetails);
	}

	@Validate
	public MethodOutcome validate(@ResourceParam T theResource, @IdParam IdType theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
											@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
		return getDao().validate(theResource, theId, theRawResource, theEncoding, theMode, theProfile, theRequestDetails);
	}
}
