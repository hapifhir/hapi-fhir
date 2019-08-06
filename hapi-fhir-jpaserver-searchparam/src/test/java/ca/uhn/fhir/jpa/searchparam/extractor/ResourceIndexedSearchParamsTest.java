package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ResourceIndexedSearchParamsTest {

	public static final String STRING_ID = "StringId";
	public static final String LONG_ID = "123";
	private ResourceIndexedSearchParams myParams;
	private ResourceTable myTarget;

	@Before
	public void before() {
		ResourceTable source = new ResourceTable();
		source.setResourceType("Patient");

		myTarget = new ResourceTable();
		myTarget.setResourceType("Organization");

		myParams = new ResourceIndexedSearchParams(source);
		ResourceLink link = new ResourceLink("organization", source, myTarget, new Date());
		myParams.getResourceLinks().add(link);
	}

	@Test
	public void matchResourceLinksStringCompareToLong() {
		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		myTarget.setId(123L);

		boolean result = myParams.matchResourceLinks("Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	public void matchResourceLinksStringCompareToString() {
		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		ForcedId forcedid = new ForcedId();
		forcedid.setForcedId(STRING_ID);
		myTarget.setForcedId(forcedid);

		boolean result = myParams.matchResourceLinks("Patient", "organization", referenceParam, "organization");
		assertTrue(result);
	}

	@Test
	public void matchResourceLinksLongCompareToString() {
		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		ForcedId forcedid = new ForcedId();
		forcedid.setForcedId(STRING_ID);
		myTarget.setForcedId(forcedid);

		boolean result = myParams.matchResourceLinks("Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	public void matchResourceLinksLongCompareToLong() {
		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		myTarget.setId(123L);

		boolean result = myParams.matchResourceLinks("Patient", "organization", referenceParam, "organization");
		assertTrue(result);
	}

	private ReferenceParam getReferenceParam(String theId) {
		ReferenceParam retval = new ReferenceParam();
		retval.setValue(theId);
		return retval;
	}

}
