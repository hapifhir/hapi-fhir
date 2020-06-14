package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;

public class HapiDateMatcher implements IEmpiFieldMatcher {
	private final HapiDateMatcherDstu3 myHapiDateMatcherDstu3 = new HapiDateMatcherDstu3();
	private final HapiDateMatcherR4 myHapiDateMatcherR4 = new HapiDateMatcherR4();

	@Override
	public boolean matches(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {
		switch (theFhirContext.getVersion().getVersion()) {
			case DSTU3:
				return myHapiDateMatcherDstu3.match(theLeftBase, theRightBase);
			case R4:
				return myHapiDateMatcherR4.match(theLeftBase, theRightBase);
			default:
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());
		}
	}
}
