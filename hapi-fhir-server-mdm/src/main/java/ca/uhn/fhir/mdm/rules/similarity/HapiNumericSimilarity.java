package ca.uhn.fhir.mdm.rules.similarity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.phonetic.NumericEncoder;
import ca.uhn.fhir.mdm.rules.matcher.util.StringMatcherUtils;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public class HapiNumericSimilarity extends HapiStringSimilarity {
	private final NumericEncoder encoder = new NumericEncoder();

	public HapiNumericSimilarity(NormalizedStringSimilarity theStringSimilarity) {
		super(theStringSimilarity);
	}

	@Override
	public double similarity(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {

		if (theLeftBase instanceof IPrimitiveType && theRightBase instanceof IPrimitiveType) {
			String leftString = encoder.encode(StringMatcherUtils.extractString((IPrimitiveType<?>) theLeftBase, theExact));
			String rightString = encoder.encode(StringMatcherUtils.extractString((IPrimitiveType<?>) theRightBase, theExact));

			return myStringSimilarity.similarity(leftString, rightString);
		}
		return 0.0;
	}
}
