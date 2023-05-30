package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseMatcherR4Test {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();

	protected MdmMatcherJson myMdmMatcherJson;

	@BeforeEach
	public void before() {
		myMdmMatcherJson = new MdmMatcherJson();
	}
}
