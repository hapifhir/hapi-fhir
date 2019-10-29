package ca.uhn.fhir.jpa.model.any;

import org.hl7.fhir.r5.model.ListResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnyListResourceTest {
	@Test
	public void getCodeFirstRep() {
		AnyListResource listResource = AnyListResource.fromResource(new ListResource());
		listResource.addCode("foo", "bar");
		assertEquals("foo", listResource.getCodeFirstRep().getSystem());
		assertEquals("bar", listResource.getCodeFirstRep().getValue());
	}

	@Test
	public void getIdentifierFirstRep() {
		AnyListResource listResource = AnyListResource.fromResource(new ListResource());
		listResource.addIdentifier("foo", "bar");
		assertEquals("foo", listResource.getIdentifierirstRep().getSystem());
		assertEquals("bar", listResource.getIdentifierirstRep().getValue());
	}
}
