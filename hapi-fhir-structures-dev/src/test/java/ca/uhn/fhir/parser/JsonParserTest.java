package ca.uhn.fhir.parser;

import static org.junit.Assert.*;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dev.resource.MedicationPrescription;
import ca.uhn.fhir.model.dev.resource.Patient;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.InstantDt;

public class JsonParserTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserTest.class);
	private static final FhirContext ourCtx = FhirContext.forDev();

	@Test
	public void testParseBundleWithBinary() {
		// TODO: implement this test, make sure we handle ID and meta correctly in Binary
	}

	@Test
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(JsonParserTest.class.getResourceAsStream("/bundle-example.json"));

		Bundle parsed = ourCtx.newJsonParser().parseBundle(content);
		assertEquals("http://example.com/base/Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals("1", parsed.getId().getVersionIdPart());
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("transaction", parsed.getType().getValue());
		assertEquals(3, parsed.getTotalResults().getValue().intValue());
		assertEquals("http://example.com/base", parsed.getLinkBase().getValue());
		assertEquals("https://example.com/base/MedicationPrescription?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLinkNext().getValue());
		assertEquals("https://example.com/base/MedicationPrescription?patient=347", parsed.getLinkSelf().getValue());

		assertEquals(1, parsed.getEntries().size());
		assertEquals("update", parsed.getEntries().get(0).getStatus().getValue());

		MedicationPrescription p = (MedicationPrescription) parsed.getEntries().get(0).getResource();
		assertEquals("Patient/example", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationPrescription/3123/_history/1", p.getId().getValue());

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(parsed);
		ourLog.info(reencoded);

		JSON expected = JSONSerializer.toJSON(content.trim());
		JSON actual = JSONSerializer.toJSON(reencoded.trim());

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}
	
	@Test
	public void testParseAndEncodeNewExtensionFormat() {
		//@formatter:off
		String resource = "{\n" + 
			"  \"resourceType\" : \"Patient\",\n" + 
			"  \"http://acme.org/fhir/ExtensionDefinition/trial-status\" : [{\n" + 
			"    \"code\" : [{ \"valueCode\" : \"unsure\" }],\n" + 
			"    \"date\" : [{ \"valueDate\" : \"2009-03-14\" }], \n" + 
			"    \"registrar\" : [{ \"valueReference\" : {\n" + 
			"      \"reference\" : \"Practitioner/example\"\n" + 
			"      }\n" + 
			"    }]\n" + 
			"  }],\n" +
			"  \"gender\" : \"M\",\n" +
			"  \"name\" : [{\n" + 
			"      \"family\": [\n" + 
			"        \"du\",\n" + 
			"        \"Marché\"\n" + 
			"      ],\n" + 
			"      \"_family\": [\n" + 
			"        {\n" + 
			"          \"http://hl7.org/fhir/ExtensionDefinition/iso21090-EN-qualifier\": [\n" + 
			"            {\n" + 
			"               \"valueCode\": \"VV\"\n" + 
			"            }\n" + 
			"          ]\n" + 
			"        },\n" + 
			"        null\n" + 
			"      ],\n" + 
			"      \"given\": [\n" + 
			"        \"Bénédicte\"\n" + 
			"      ]\n" + 
			"    }]" +
			"}";
		//@formatter:on
		
		ourLog.info(resource);
		
		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, resource);
		
		// Gender
		assertEquals("M",parsed.getGender());
		assertEquals(1, parsed.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/trial-status").size());
		
		// Trial status
		ExtensionDt ext = parsed.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/trial-status").get(0);
		assertNull(ext.getValue());
		assertEquals(1, ext.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/code").size());
		assertEquals(CodeDt.class, ext.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/code").get(0).getValue().getClass());
		assertEquals("unsure", ext.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/code").get(0).getValueAsPrimitive().getValueAsString());
		assertEquals(1, ext.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/date").size());
		assertEquals(DateDt.class, ext.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/date").get(0).getValue().getClass());
		assertEquals("2009-03-14", ext.getUndeclaredExtensionsByUrl("http://acme.org/fhir/ExtensionDefinition/date").get(0).getValueAsPrimitive().getValueAsString());

		// Name
		assertEquals(1, parsed.getName().size());
		assertEquals(2, parsed.getName().get(0).getFamily().size());
		assertEquals("du Marché", parsed.getName().get(0).getFamilyAsSingleString());
		assertEquals(1, parsed.getName().get(0).getGiven().size());
		assertEquals("Bénédicte", parsed.getName().get(0).getGivenAsSingleString());
		
		// Patient.name[0].family extensions
		assertEquals(1, parsed.getNameFirstRep().getFamily().get(0).getUndeclaredExtensionsByUrl("http://hl7.org/fhir/ExtensionDefinition/iso21090-EN-qualifier").size());
		assertEquals(CodeDt.class, parsed.getNameFirstRep().getFamily().get(0).getUndeclaredExtensionsByUrl("http://hl7.org/fhir/ExtensionDefinition/iso21090-EN-qualifier").get(0).getValueAsPrimitive().getClass());
		assertEquals("VV", parsed.getNameFirstRep().getFamily().get(0).getUndeclaredExtensionsByUrl("http://hl7.org/fhir/ExtensionDefinition/iso21090-EN-qualifier").get(0).getValueAsPrimitive().getValueAsString());
		
	}
	
	

}
