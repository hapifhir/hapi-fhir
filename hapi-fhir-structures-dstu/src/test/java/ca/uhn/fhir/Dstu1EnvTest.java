package ca.uhn.fhir;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

public class Dstu1EnvTest {

	@Test
	public void testCorrectDefault() {
		FhirContext ctx = new FhirContext();
		assertEquals("new FhirContext() is creating a context with the wrong FHIR versions. Something is probably wrong with the classpath.", FhirVersionEnum.DSTU1, ctx.getVersion().getVersion());
	}
}
