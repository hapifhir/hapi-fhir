package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.*;

@Lazy
@Service
public final class EIDHelper {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IEmpiConfig myEmpiConfig;

	private EIDHelper(){}

	@VisibleForTesting
	EIDHelper(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}


	public CanonicalEID createInternalEid() {
		return new CanonicalEID(
			INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM,
			UUID.randomUUID().toString(),
			"secondary"
		);
	}

	public Optional<CanonicalEID> getExternalEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(), theResource);
	}

	public Optional<CanonicalEID> getInternalEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM, theResource);
	}

}
