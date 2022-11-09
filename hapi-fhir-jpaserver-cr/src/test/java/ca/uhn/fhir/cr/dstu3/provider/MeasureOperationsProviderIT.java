package ca.uhn.fhir.cr.dstu3.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.config.CrDstu3Config;
import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestCrConfig.class, CrDstu3Config.class})
class MeasureOperationsProviderIT extends BaseJpaDstu3Test {
	private static final FhirContext ourFhirContext = FhirContext.forDstu3Cached();

	@Autowired
    MeasureOperationsProvider measureOperationsProvider;

	@Autowired
	DaoRegistry daoRegistry;

	private void loadBundle(String theLocation) throws IOException {
		var bundle = loadResource(ourFhirContext, Bundle.class, theLocation);
		daoRegistry.getSystemDao().transaction(new SystemRequestDetails(), bundle);
	}

	@Test
	void testMeasureEvaluate() throws IOException {
		this.loadBundle("Exm105Fhir3Measure.json");

		MeasureReport returnMeasureReport = this.measureOperationsProvider.evaluateMeasure(
			new SystemRequestDetails(),
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/denom-EXM105-FHIR3",
			null,
			"2019-12-12",
			null, null, null
		);

		assertNotNull(returnMeasureReport);
	}
}
