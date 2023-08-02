package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;

import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.repo.HapiFhirRepository;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StringType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class SubmitDataServiceR4Test extends BaseCrR4TestServer {
	@Autowired
	IRepositoryFactory myRepositoryFactory;

	@Autowired
	ISubmitDataProcessorFactory myR4SubmitDataProcessorFactory;

	@Autowired
	RestfulServer ourRestfulServer;

	@Test
	public void submitDataTest(){
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());

		//create resources
		MeasureReport mr = newResource(MeasureReport.class).setMeasure("Measure/A123");
		Observation obs = newResource(Observation.class).setValue(new StringType("ABC"));

		//submit-data operation
		var res = myR4SubmitDataProcessorFactory
			.create(myRepositoryFactory.create(requestDetails))
			.submitData(new IdType("Measure", "A123"), mr,
				Lists.newArrayList(obs));

		var repository = new HapiFhirRepository(myDaoRegistry, requestDetails, ourRestfulServer);
		var result = repository.search(Bundle.class, MeasureReport.class, null);
		var id1 = "MeasureReport/1";
		var id2 = "Observation/2";


		//find submitted resources
		var savedObs = ourClient.read().resource(Observation.class).withId("2").execute();
		var savedMr = ourClient.read().resource(MeasureReport.class).withId("1").execute();

		//validate resources match
		assertNotNull(savedObs);
		assertEquals("ABC", savedObs.getValue().primitiveValue());
		assertNotNull(savedMr);
		assertEquals("Measure/A123", savedMr.getMeasure());
	}
}
