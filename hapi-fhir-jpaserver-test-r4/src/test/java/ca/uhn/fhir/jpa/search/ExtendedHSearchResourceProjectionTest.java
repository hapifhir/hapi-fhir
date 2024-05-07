package ca.uhn.fhir.jpa.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchResourceProjection;
import ca.uhn.fhir.jpa.dao.search.ResourceNotFoundInIndexException;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.jpa.dao.search.ExtendedHSearchResourceProjection.RESOURCE_NOT_STORED_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExtendedHSearchResourceProjectionTest {
	final FhirContext myFhirContext = FhirContext.forR4();
	final IParser myParser = myFhirContext.newJsonParser();
	ExtendedHSearchResourceProjection myProjection;
	IBaseResource myResource;

	@Test
	public void basicBodyReceivesId() {
		myProjection = new ExtendedHSearchResourceProjection(22, null, "{ \"resourceType\":\"Observation\"}");

		myResource = myProjection.toResource(myParser);

		assertThat(myResource).isInstanceOf(Observation.class);
		assertEquals("22", myResource.getIdElement().getIdPart());
	}

	@Test
	public void forcedIdOverridesPid() {
		myProjection = new ExtendedHSearchResourceProjection(22, "force-id", "{ \"resourceType\":\"Observation\"}");

		myResource = myProjection.toResource(myParser);

		assertThat(myResource).isInstanceOf(Observation.class);
		assertEquals("force-id", myResource.getIdElement().getIdPart());
	}


	@Test
	public void nullResourceStringThrows() {
		ResourceNotFoundInIndexException ex = assertThrows(
			ResourceNotFoundInIndexException.class,
			() -> new ExtendedHSearchResourceProjection(22, null, null));
		assertEquals(Msg.code(2130) + RESOURCE_NOT_STORED_ERROR + "22", ex.getMessage());
	}


	@Test
	public void emptyResourceStringThrows() {
		ResourceNotFoundInIndexException ex = assertThrows(
			ResourceNotFoundInIndexException.class,
			() -> new ExtendedHSearchResourceProjection(22, null, ""));
		assertEquals(Msg.code(2130) + RESOURCE_NOT_STORED_ERROR + "22", ex.getMessage());
	}


	

}
