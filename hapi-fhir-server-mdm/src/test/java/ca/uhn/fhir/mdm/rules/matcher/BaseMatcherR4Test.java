package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.matcher.ExtraMatchParams;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseMatcherR4Test {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();

	protected ExtraMatchParams myExtraMatchParams;

	@BeforeEach
	public void before() {
		myExtraMatchParams = new ExtraMatchParams();
	}
}
