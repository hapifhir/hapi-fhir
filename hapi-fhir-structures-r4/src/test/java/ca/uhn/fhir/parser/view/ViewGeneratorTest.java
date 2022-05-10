package ca.uhn.fhir.parser.view;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViewGeneratorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ViewGeneratorTest.class);
	private static FhirContext ourCtx = FhirContext.forR4();

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@Test
	public void testView() {

		ExtPatient src = new ExtPatient();
		src.addIdentifier().setSystem("urn:sys").setValue("id1");
		src.addIdentifier().setSystem("urn:sys").setValue("id2");
		src.getExt().setValue(100);
		src.getModExt().setValue(200);

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(src);

		ourLog.info(enc);

		IParser parser = ourCtx.newXmlParser();
		Patient nonExt = parser.parseResource(Patient.class, enc);

		assertEquals(Patient.class, nonExt.getClass());
		assertEquals("urn:sys", nonExt.getIdentifier().get(0).getSystem());
		assertEquals("id1", nonExt.getIdentifier().get(0).getValue());
		assertEquals("urn:sys", nonExt.getIdentifier().get(1).getSystem());
		assertEquals("id2", nonExt.getIdentifier().get(1).getValue());

		List<Extension> ext = nonExt.getExtensionsByUrl("urn:ext");
		assertEquals(1, ext.size());
		assertEquals("urn:ext", ext.get(0).getUrl());
		assertEquals(IntegerType.class, ext.get(0).getValueAsPrimitive().getClass());
		assertEquals("100", ext.get(0).getValueAsPrimitive().getValueAsString());

		List<Extension> modExt = nonExt.getExtensionsByUrl("urn:modExt");
		assertEquals(1, modExt.size());
		assertEquals("urn:modExt", modExt.get(0).getUrl());
		assertEquals(IntegerType.class, modExt.get(0).getValueAsPrimitive().getClass());
		assertEquals("200", modExt.get(0).getValueAsPrimitive().getValueAsString());

		ExtPatient va = ourCtx.newViewGenerator().newView(nonExt, ExtPatient.class);
		assertEquals("urn:sys", va.getIdentifier().get(0).getSystem());
		assertEquals("id1", va.getIdentifier().get(0).getValue());
		assertEquals("urn:sys", va.getIdentifier().get(1).getSystem());
		assertEquals("id2", va.getIdentifier().get(1).getValue());
		assertEquals(100, va.getExt().getValue().intValue());
		assertEquals(200, va.getModExt().getValue().intValue());

		assertEquals(0, va.getExtension().size());
	}

	@Test
	public void testViewJson() {

		ExtPatient src = new ExtPatient();
		src.addIdentifier().setSystem("urn:sys").setValue("id1");
		src.addIdentifier().setSystem("urn:sys").setValue("id2");
		src.getExt().setValue(100);
		src.getModExt().setValue(200);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(src);

		ourLog.info(enc);

		IParser parser = ourCtx.newJsonParser();
		Patient nonExt = parser.parseResource(Patient.class, enc);

		assertEquals(Patient.class, nonExt.getClass());
		assertEquals("urn:sys", nonExt.getIdentifier().get(0).getSystem());
		assertEquals("id1", nonExt.getIdentifier().get(0).getValue());
		assertEquals("urn:sys", nonExt.getIdentifier().get(1).getSystem());
		assertEquals("id2", nonExt.getIdentifier().get(1).getValue());

		List<Extension> ext = nonExt.getExtensionsByUrl("urn:ext");
		assertEquals(1, ext.size());
		assertEquals("urn:ext", ext.get(0).getUrl());
		assertEquals(IntegerType.class, ext.get(0).getValueAsPrimitive().getClass());
		assertEquals("100", ext.get(0).getValueAsPrimitive().getValueAsString());

		List<Extension> modExt = nonExt.getExtensionsByUrl("urn:modExt");
		assertEquals(1, modExt.size());
		assertEquals("urn:modExt", modExt.get(0).getUrl());
		assertEquals(IntegerType.class, modExt.get(0).getValueAsPrimitive().getClass());
		assertEquals("200", modExt.get(0).getValueAsPrimitive().getValueAsString());

		ExtPatient va = ourCtx.newViewGenerator().newView(nonExt, ExtPatient.class);
		assertEquals("urn:sys", va.getIdentifier().get(0).getSystem());
		assertEquals("id1", va.getIdentifier().get(0).getValue());
		assertEquals("urn:sys", va.getIdentifier().get(1).getSystem());
		assertEquals("id2", va.getIdentifier().get(1).getValue());
		assertEquals(100, va.getExt().getValue().intValue());
		assertEquals(200, va.getModExt().getValue().intValue());

		assertEquals(0, va.getExtension().size());
	}

}
