package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static ca.uhn.fhir.rest.api.Constants.*;

@Lazy
@Service
public final class EIDHelper {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IEmpiConfig myEmpiConfig;

	@VisibleForTesting
	EIDHelper(FhirContext theFhirContext, IEmpiConfig theEmpiConfig) {
		myFhirContext = theFhirContext;
		myEmpiConfig = theEmpiConfig;
	}

	public CanonicalEID createInternalEid() {
		return new CanonicalEID(
			HAPI_ENTERPRISE_IDENTIFIER_SYSTEM,
			UUID.randomUUID().toString(),
			"secondary"
		);
	}

	public Optional<CanonicalEID> getExternalEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(), theResource);
	}

	public Optional<CanonicalEID> getHapiEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, HAPI_ENTERPRISE_IDENTIFIER_SYSTEM, theResource);
	}

}
