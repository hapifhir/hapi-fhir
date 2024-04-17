package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class CollectDataTest extends BaseCrR4TestServer{
	public Parameters runCollectData(String thePeriodStart, String thePeriodEnd, String theMeasureId, String theSubject, String thePractitioner){

		var parametersEval = new Parameters();
		parametersEval.addParameter("periodStart", new DateType(thePeriodStart));
		parametersEval.addParameter("periodEnd", new DateType(thePeriodEnd));
		parametersEval.addParameter("practitioner", thePractitioner);
		parametersEval.addParameter("subject", theSubject);


		var report = ourClient.operation().onInstance("Measure/" + theMeasureId)
			.named(ProviderConstants.CR_OPERATION_COLLECTDATA)
			.withParameters(parametersEval)
			.returnResourceType(Parameters.class)
			.execute();

		return report;
	}

	@Test
	void testCollectDataInvalidInterval() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		assertThrows(InternalErrorException.class, ()->runCollectData("2020-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR", null, null));
	}

	@Test
	void testCollectDataInvalidMeasure() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		assertThrows(ResourceNotFoundException.class, ()->runCollectData("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHI", null, null));
	}
	@Test
	void testMeasureDataRequirementsAllSubjects() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		var report = runCollectData("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR", null, null);
		Assertions.assertFalse(report.getParameter().isEmpty());
	}
	@Test
	void testCollectDataSubject() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		var report = runCollectData("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR", "Patient/numer-EXM130", null);
		Assertions.assertFalse(report.getParameter().isEmpty());
	}

	@Test
	void testCollectDataGroup() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		var report = runCollectData("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR", "Group/group-EXM130", null);
		Assertions.assertFalse(report.getParameter().isEmpty());
	}

	@Test
	void testCollectDataPractitioner() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		var report = runCollectData("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR", null, "Practitioner/practitioner-EXM130");
		Assertions.assertFalse(report.getParameter().isEmpty());
	}
}
