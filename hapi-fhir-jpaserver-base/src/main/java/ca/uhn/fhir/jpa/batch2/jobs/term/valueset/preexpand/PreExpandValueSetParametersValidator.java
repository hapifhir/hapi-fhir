package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class PreExpandValueSetParametersValidator implements IJobParametersValidator<PreExpandValueSetParameters> {

	@Autowired
	private IValidationSupport myValidationSupport;

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull PreExpandValueSetParameters theParameters) {
		List<String> retVal = new ArrayList<>();

		UrlUtil.CanonicalUrlParts canonicalUrl = theParameters.getCanonicalUrl();
		IBaseResource valueSet = myValidationSupport.fetchValueSet(canonicalUrl.toString());
		if (valueSet == null) {
			retVal.add("ValueSet not found: " + canonicalUrl);
		}

		return retVal;
	}
}
