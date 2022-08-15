package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestSecurity;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtensionsDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testParseExtensions() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/smart-conf.xml"));
		Conformance conf = (Conformance) ourCtx.newXmlParser().parseResource(input);
		
		RestSecurity sec = conf.getRest().get(0).getSecurity();
		List<ExtensionDt> uris = sec.getUndeclaredExtensionsByUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
		assertEquals(1, uris.size());
	}
	
}
