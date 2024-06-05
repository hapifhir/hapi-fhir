package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.util.NameUtil;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameUtilTestR4 {

	private static final FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testExtractName() {
		Patient patient = new Patient();
		patient.getNameFirstRep().setFamily("family");
		patient.getNameFirstRep().getGiven().add(new StringType("given1"));
		patient.getNameFirstRep().getGiven().add(new StringType("given2"));
		FhirTerser terser = myFhirContext.newTerser();
		List<IBase> names = terser.getValues(patient, "name", IBase.class);
		assertThat(names).hasSize(1);
		IBase name = names.get(0);

		{
			String familyName = NameUtil.extractFamilyName(myFhirContext, name );
			assertEquals("family", familyName);
		}

		{
			List<String> familyName = NameUtil.extractGivenNames(myFhirContext, name);
			assertThat(familyName).contains("given1", "given2");
		}
	}
}
