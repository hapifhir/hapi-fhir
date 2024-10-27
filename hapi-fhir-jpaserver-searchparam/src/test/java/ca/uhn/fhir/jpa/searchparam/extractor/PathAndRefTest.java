package ca.uhn.fhir.jpa.searchparam.extractor;

import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathAndRefTest {

	@Test
	public void testToString() {
		PathAndRef ref = new PathAndRef("foo", "Foo.bar", new Reference("Patient/123"), false);
		assertEquals("PathAndRef[paramName=foo,ref=Patient/123,path=Foo.bar,resource=<null>,canonical=false]", ref.toString());
	}

}
