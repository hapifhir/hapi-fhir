package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.repo.HapiFhirRepository;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.fhir.utility.search.Searches;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubmitDataServiceR4Test extends BaseCrR4TestServer {

	@Autowired
	ISubmitDataProcessorFactory myR4SubmitDataProcessorFactory;

	@Test
	public void submitDataTest(){
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());

		//create resources
		MeasureReport mr = newResource(MeasureReport.class).setMeasure("Measure/A123");
		Observation obs = newResource(Observation.class).setValue(new StringType("ABC"));

		//submit-data operation
		var res = myR4SubmitDataProcessorFactory
			.create(requestDetails)
			.submitData(new IdType("Measure", "A123"), mr,
				Lists.newArrayList(obs));

		var repository = new HapiFhirRepository(myDaoRegistry, requestDetails, ourRestfulServer);

		var resultMr = repository.search(Bundle.class, MeasureReport.class, Searches.ALL);
		var mrSize = resultMr.getEntry().size();
		MeasureReport report = null;
		for (int i = 0; i < mrSize; i++){
			var getEntry = resultMr.getEntry();
			var mrResource = (MeasureReport) getEntry.get(i).getResource();
			var measure = mrResource.getMeasure();
			if (measure.equals("Measure/A123")){
				report = mrResource;
				break;
			}
		}
		//found submitted MeasureReport!
		assertNotNull(report);

		var resultOb = repository.search(Bundle.class, Observation.class, Searches.ALL);
		var obSize = resultOb.getEntry().size();
		Observation observation = null;
		for (int i = 0; i < obSize; i++){
			var getEntry = resultOb.getEntry();
			var obResource = (Observation) getEntry.get(i).getResource();
			var val = obResource.getValue().primitiveValue();
			if (val.equals("ABC")){
				observation = obResource;
				break;
			}
		}
		//found submitted Observation!
		assertNotNull(observation);

	}
}
