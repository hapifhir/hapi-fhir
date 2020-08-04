package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.FhirContext;
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class ThymeleafNarrativeGeneratorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ThymeleafNarrativeGeneratorTest.class);
	private FhirContext myCtx = FhirContext.forDstu3();

	@Test
	public void testGenerateCompositionWithContextPath() throws IOException {
		DiagnosticReport dr1 = new DiagnosticReport();
		dr1.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
		dr1.setIssuedElement(new InstantType("2019-01-01T12:12:12-05:00"));
		dr1.getCode().getCodingFirstRep().setDisplay("Complete Blood Count");

		Observation obs1 = new Observation();
		obs1.getCode().getCodingFirstRep().setDisplay("Hemoglobin [Mass/volume] in Blood");
		obs1.setValue(new Quantity(null, 176, "http://unitsofmeasure.org", "g/L", "g/L"));
		obs1.getReferenceRangeFirstRep().getLow().setValue(135).setSystem("http://unitsofmeasure.org").setCode("g/L").setUnit("g/L");
		obs1.getReferenceRangeFirstRep().getHigh().setValue(180).setSystem("http://unitsofmeasure.org").setCode("g/L").setUnit("g/L");
		obs1.getReferenceRangeFirstRep().getTextElement().setValue("135 - 180");
		dr1.addResult().setResource(obs1);

		Observation obs2 = new Observation();
		obs2.getCode().getCodingFirstRep().setDisplay("Erythrocytes [#/volume] in Blood by Automated count");
		obs2.setValue(new Quantity(null, 5.9, "http://unitsofmeasure.org", "x10*12/L", "x10*12/L"));
		obs2.getReferenceRangeFirstRep().getLow().setValue(4.2).setSystem("http://unitsofmeasure.org").setCode("x10*12/L").setUnit("x10*12/L");
		obs2.getReferenceRangeFirstRep().getHigh().setValue(6.0).setSystem("http://unitsofmeasure.org").setCode("x10*12/L").setUnit("x10*12/L");
		obs2.getReferenceRangeFirstRep().getTextElement().setValue("4.2 - 6.0");
		dr1.addResult().setResource(obs2);

		Composition composition = new Composition();

		Composition.SectionComponent sect = composition.addSection();
		sect.setTitle("History of Medication use Narrative");
		sect.getCode().getCodingFirstRep().setSystem("2.16.840.1.113883.6.1");
		sect.getCode().getCodingFirstRep().setCode("10160-0");
		sect.getCode().getCodingFirstRep().setDisplay("History of Medication use Narrative");

		sect = composition.addSection();
		sect.setTitle("Relevant diagnostic tests/laboratory data Narrative");
		sect.getCode().getCodingFirstRep().setSystem("2.16.840.1.113883.6.1");
		sect.getCode().getCodingFirstRep().setCode("30954-2");
		sect.getCode().getCodingFirstRep().setDisplay("Relevant diagnostic tests/laboratory data Narrative");
		Reference ref = new Reference();
		ref.setReference("DiagnosticReport/1").setResource(dr1);
		sect.getEntry().add(ref);

		NarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:narrative2/narratives.properties");
		ThymeleafNarrativeGenerator gen = new ThymeleafNarrativeGenerator();
		gen.setManifest(manifest);

		gen.populateResourceNarrative(myCtx, composition);

		// First narrative should be empty
		String narrative = composition.getSection().get(0).getText().getDiv().getValueAsString();
		assertThat(narrative, Matchers.emptyOrNullString());

		// Second narrative should have details
		narrative = composition.getSection().get(1).getText().getDiv().getValueAsString();
		ourLog.info("Narrative:\n{}", narrative);

		assertThat(narrative, containsString("<thead><tr><td>Name</td><td>Value</td>"));
		assertThat(narrative, containsString("<td> 4.2 - 6.0 </td>"));
	}

	@Test
	public void testTemplateCount() throws IOException {
		NarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:narrative2/narratives.properties");
		assertEquals(4, manifest.getNamedTemplateCount());
	}

}
