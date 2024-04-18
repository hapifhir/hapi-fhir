package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class DataRequirementsTest extends BaseCrR4TestServer{
	public Library runDataRequirements(String periodStart, String periodEnd, String measureId){

		var parametersEval = new Parameters();
		parametersEval.addParameter("periodStart", new DateType(periodStart));
		parametersEval.addParameter("periodEnd", new DateType(periodEnd));


		var report = ourClient.operation().onInstance("Measure/" + measureId)
			.named(ProviderConstants.CR_OPERATION_DATAREQUIREMENTS)
			.withParameters(parametersEval)
			.returnResourceType(Library.class)
			.execute();

		return report;

	}
	@Test
	void testMeasureDataRequirements() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		Assertions.assertFalse(runDataRequirements("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR").getDataRequirement().isEmpty());
		testMeasureDataRequirementsInvalidInterval();
		testMeasureDataRequirementsInvalidMeasure();
	}
	void testMeasureDataRequirementsInvalidInterval() {
		assertThrows(InternalErrorException.class, ()->runDataRequirements("2020-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR"));
	}

	void testMeasureDataRequirementsInvalidMeasure() {
		assertThrows(ResourceNotFoundException.class, ()->runDataRequirements("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHI"));
	}

}
