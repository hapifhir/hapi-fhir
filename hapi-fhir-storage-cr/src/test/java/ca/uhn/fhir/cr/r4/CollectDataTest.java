package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

		assertNotNull(report.getParameter().size()>0);

		return report;
	}
	@Test
	void testMeasureDataRequirements_EXM130() {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		runCollectData("2019-01-01", "2019-12-31", "ColorectalCancerScreeningsFHIR", null, null);
	}
}
