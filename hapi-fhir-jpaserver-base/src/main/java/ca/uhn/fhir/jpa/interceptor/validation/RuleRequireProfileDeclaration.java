package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Optional;

class RuleRequireProfileDeclaration implements IRepositoryValidatingRule {
	private final String myType;
	private final Collection<String> myProfileOptions;
	private final FhirContext myFhirContext;

	RuleRequireProfileDeclaration(FhirContext theFhirContext, String theType, Collection<String> theProfileOptions) {
		myFhirContext = theFhirContext;
		myType = theType;
		myProfileOptions = theProfileOptions;
	}


	@Override
	public String getResourceType() {
		return myType;
	}

	@Override
	public RuleEvaluation evaluate(IBaseResource theResource) {
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
		String msg = myFhirContext.getLocalizer().getMessage(RuleRequireProfileDeclaration.class, "noMatchingProfile", getResourceType(), myProfileOptions);
		return RuleEvaluation.forFailure(this, msg);
	}
}
