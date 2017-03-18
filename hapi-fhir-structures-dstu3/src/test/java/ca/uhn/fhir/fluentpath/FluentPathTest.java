package ca.uhn.fhir.fluentpath;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.util.List;

import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class FluentPathTest {

	@Test
	public void testEvaluateNormal() {
		Patient p = new Patient();
		p.addName().setFamily("N1F1").addGiven("N1G1").addGiven("N1G2");
		p.addName().setFamily("N2F1").addGiven("N2G1").addGiven("N2G2");
		
		IFluentPath fp = ourCtx.newFluentPath();
		List<HumanName> names = fp.evaluate(p, "Patient.name", HumanName.class);
		assertEquals(2, names.size());
		assertEquals("N1F1", names.get(0).getFamily());
		assertEquals("N1G1 N1G2", names.get(0).getGivenAsSingleString());
		assertEquals("N2F1", names.get(1).getFamily());
		assertEquals("N2G1 N2G2", names.get(1).getGivenAsSingleString());
	}
	
	@Test
	public void testEvaluateUnknownPath() {
		Patient p = new Patient();
		p.addName().setFamily("N1F1").addGiven("N1G1").addGiven("N1G2");
		p.addName().setFamily("N2F1").addGiven("N2G1").addGiven("N2G2");
		
		IFluentPath fp = ourCtx.newFluentPath();
		List<HumanName> names = fp.evaluate(p, "Patient.nameFOO", HumanName.class);
		assertEquals(0, names.size());
	}
	
	@Test
	public void testEvaluateInvalidPath() {
		Patient p = new Patient();
		p.addName().setFamily("N1F1").addGiven("N1G1").addGiven("N1G2");
		p.addName().setFamily("N2F1").addGiven("N2G1").addGiven("N2G2");
		
		IFluentPath fp = ourCtx.newFluentPath();
		try {
			fp.evaluate(p, "Patient....nameFOO", HumanName.class);
		} catch (FluentPathExecutionException e) {
			assertThat(e.getMessage(), containsString("termination at unexpected token"));
		}
	}

	@Test
	public void testEvaluateWrongType() {
		Patient p = new Patient();
		p.addName().setFamily("N1F1").addGiven("N1G1").addGiven("N1G2");
		p.addName().setFamily("N2F1").addGiven("N2G1").addGiven("N2G2");
		
		IFluentPath fp = ourCtx.newFluentPath();
		try {
			fp.evaluate(p, "Patient.name", StringType.class);
		} catch (FluentPathExecutionException e) {
			assertEquals("FluentPath expression \"Patient.name\" returned unexpected type HumanName - Expected org.hl7.fhir.dstu3.model.StringType", e.getMessage());
		}
	}

	private static FhirContext ourCtx = FhirContext.forDstu3();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
