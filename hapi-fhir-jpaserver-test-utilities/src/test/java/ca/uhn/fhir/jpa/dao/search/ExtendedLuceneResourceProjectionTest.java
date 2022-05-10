package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

class ExtendedLuceneResourceProjectionTest {
	final FhirContext myFhirContext = FhirContext.forR4();
	final IParser myParser = myFhirContext.newJsonParser();
	ExtendedLuceneResourceProjection myProjection;
	IBaseResource myResource;

	@Test
	public void basicBodyReceivesId() {
		myProjection = new ExtendedLuceneResourceProjection(22, null, "{ \"resourceType\":\"Observation\"}");

		myResource = myProjection.toResource(myParser);

		assertThat(myResource, instanceOf(Observation.class));
		assertThat(myResource.getIdElement().getIdPart(), equalTo("22"));
	}

	@Test
	public void forcedIdOverridesPid() {
		myProjection = new ExtendedLuceneResourceProjection(22, "force-id", "{ \"resourceType\":\"Observation\"}");

		myResource = myProjection.toResource(myParser);

		assertThat(myResource, instanceOf(Observation.class));
		assertThat(myResource.getIdElement().getIdPart(), equalTo("force-id"));
	}


}
