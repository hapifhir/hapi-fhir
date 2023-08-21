package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.cr.r4.measure.SubmitDataService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class SubmitDataServiceR4Test extends BaseCrR4TestServer {

	Function<RequestDetails, SubmitDataService> mySubmitDataServiceFunction;

	@BeforeEach
	public void beforeEach() {
		mySubmitDataServiceFunction = rs -> {
			return new SubmitDataService(getDaoRegistry(), new SystemRequestDetails());
		};
	}

	@Test
	public void submitDataTest(){
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(getFhirContext());
		MeasureReport mr = newResource(MeasureReport.class).setMeasure("Measure/A123");
		Observation obs = newResource(Observation.class).setValue(new StringType("ABC"));
		mySubmitDataServiceFunction.apply(requestDetails)
			.submitData(new IdType("Measure", "A123"), mr,
				Lists.newArrayList(obs));

		Iterable<IBaseResource> resourcesResult = search(Observation.class, Searches.all());
		Observation savedObs = null;
		Iterator<IBaseResource> iterator = resourcesResult.iterator();
		while(iterator.hasNext()){
			savedObs = (Observation) iterator.next();
			break;
		}
		assertNotNull(savedObs);
		assertEquals("ABC", savedObs.getValue().primitiveValue());

		resourcesResult = search(MeasureReport.class, Searches.all());
		MeasureReport savedMr = null;
		iterator = resourcesResult.iterator();
		while(iterator.hasNext()){
			savedMr = (MeasureReport) iterator.next();
			break;
		}
		assertNotNull(savedMr);
		assertEquals("Measure/A123", savedMr.getMeasure());
	}

}
