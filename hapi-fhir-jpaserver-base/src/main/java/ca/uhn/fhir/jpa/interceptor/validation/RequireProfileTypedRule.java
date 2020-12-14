package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Optional;

class RequireProfileTypedRule implements IRule {
	private final String myType;
	private final Collection<String> myProfileOptions;
	private final FhirContext myFhirContext;

	RequireProfileTypedRule(FhirContext theFhirContext, String theType, Collection<String> theProfileOptions) {
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
			return RuleEvaluation.forSuccess();
		}
		String msg = myFhirContext.getLocalizer().getMessage(RequireProfileTypedRule.class, "noMatchingProfile", getResourceType(), myProfileOptions);
		return RuleEvaluation.forFailure(msg);
	}
}
