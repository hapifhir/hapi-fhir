package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.model.primitive.StringDt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmptyFieldMatcherTest extends BaseMatcherR4Test {

	@Test
	public void testEmptyFieldMatch() {
		StringDt left = new StringDt("other value");
		StringDt leftEmpty = new StringDt("");
		StringDt rightEmpty = new StringDt("");
		StringDt right = new StringDt("a value");
		MdmMatcherJson matcher = new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.EMPTY_FIELD);
		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson().setMatcher(matcher);

		assertTrue(fieldMatch.match(ourFhirContext, null, null).match);
		assertTrue(fieldMatch.match(ourFhirContext, null, rightEmpty).match);
		assertTrue(fieldMatch.match(ourFhirContext, leftEmpty, null).match);
		assertTrue(fieldMatch.match(ourFhirContext, leftEmpty, rightEmpty).match);
		assertFalse(fieldMatch.match(ourFhirContext, null, right).match);
		assertFalse(fieldMatch.match(ourFhirContext, left, null).match);
		assertFalse(fieldMatch.match(ourFhirContext, left, right).match);
	}


}
