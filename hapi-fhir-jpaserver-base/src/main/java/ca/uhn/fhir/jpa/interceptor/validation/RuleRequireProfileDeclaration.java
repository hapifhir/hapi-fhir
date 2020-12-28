package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

class RuleRequireProfileDeclaration extends BaseTypedRule {
	private final Collection<String> myProfileOptions;

	RuleRequireProfileDeclaration(FhirContext theFhirContext, String theType, Collection<String> theProfileOptions) {
		super(theFhirContext, theType);
		myProfileOptions = theProfileOptions;
	}

	@Nonnull
	@Override
	public RuleEvaluation evaluate(@Nonnull IBaseResource theResource) {
		Optional<String> matchingProfile = theResource
			.getMeta()
			.getProfile()
			.stream()
			.map(t -> t.getValueAsString())
			.filter(t -> myProfileOptions.contains(t))
			.findFirst();
		if (matchingProfile.isPresent()) {
			return RuleEvaluation.forSuccess(this);
		}
		String msg = getFhirContext().getLocalizer().getMessage(RuleRequireProfileDeclaration.class, "noMatchingProfile", getResourceType(), myProfileOptions);
		return RuleEvaluation.forFailure(this, msg);
	}


}
