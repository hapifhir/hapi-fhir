package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchResourceProjection;
import ca.uhn.fhir.jpa.dao.search.ResourceNotFoundInIndexException;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.jpa.dao.search.ExtendedHSearchResourceProjection.RESOURCE_NOT_STORED_ERROR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
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

		assertThat(myResource, instanceOf(Observation.class));
		assertThat(myResource.getIdElement().getIdPart(), equalTo("22"));
	}

	@Test
	public void forcedIdOverridesPid() {
		myProjection = new ExtendedHSearchResourceProjection(22, "force-id", "{ \"resourceType\":\"Observation\"}");

		myResource = myProjection.toResource(myParser);

		assertThat(myResource, instanceOf(Observation.class));
		assertThat(myResource.getIdElement().getIdPart(), equalTo("force-id"));
	}


	@Test
	public void nullResourceStringThrows() {
		ResourceNotFoundInIndexException ex = assertThrows(
			ResourceNotFoundInIndexException.class,
			() -> new ExtendedHSearchResourceProjection(22, null, null));
		assertThat(ex.getMessage(), equalTo(Msg.code(2130) + RESOURCE_NOT_STORED_ERROR + "22"));
	}


	@Test
	public void emptyResourceStringThrows() {
		ResourceNotFoundInIndexException ex = assertThrows(
			ResourceNotFoundInIndexException.class,
			() -> new ExtendedHSearchResourceProjection(22, null, ""));
		assertThat(ex.getMessage(), equalTo(Msg.code(2130) + RESOURCE_NOT_STORED_ERROR + "22"));
	}


	

}
