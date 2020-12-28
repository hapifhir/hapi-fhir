package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;

class RuleDisallowProfile extends BaseTypedRule {
	private final String myProfileUrl;

	RuleDisallowProfile(FhirContext theFhirContext, String theResourceType, String theProfileUrl) {
		super(theFhirContext, theResourceType);
		Validate.notBlank(theProfileUrl);
		myProfileUrl = UrlUtil.normalizeCanonicalUrlForComparison(theProfileUrl);
	}

	@Nonnull
	@Override
	public RuleEvaluation evaluate(@Nonnull IBaseResource theResource) {
		for (IPrimitiveType<String> next : theResource.getMeta().getProfile()) {
			String nextUrl = next.getValueAsString();
			String nextUrlNormalized = UrlUtil.normalizeCanonicalUrlForComparison(nextUrl);
			if (myProfileUrl.equals(nextUrlNormalized)) {
				String msg = getFhirContext().getLocalizer().getMessage(RuleRequireProfileDeclaration.class, "illegalProfile", getResourceType(), nextUrl);
				return RuleEvaluation.forFailure(this, msg);
			}
		}

		return RuleEvaluation.forSuccess(this);
	}
}
