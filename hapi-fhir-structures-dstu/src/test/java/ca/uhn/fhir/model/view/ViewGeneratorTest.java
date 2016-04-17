package ca.uhn.fhir.model.view;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;

public class ViewGeneratorTest {

	private static FhirContext ourCtx  = FhirContext.forDstu1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ViewGeneratorTest.class);
	
	@Test
	public void testView() {
		
		ExtPatient src = new ExtPatient();
		src.addIdentifier("urn:sys", "id1");
		src.addIdentifier("urn:sys", "id2");
		src.getExt().setValue(100);
		src.getModExt().setValue(200);
		
		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(src);
		
		ourLog.info(enc);
		
		IParser parser = ourCtx.newXmlParser();
		Patient nonExt = parser.parseResource(Patient.class, enc);
		
		assertEquals(Patient.class, nonExt.getClass());
		assertEquals("urn:sys", nonExt.getIdentifier().get(0).getSystem().getValueAsString());
		assertEquals("id1", nonExt.getIdentifier().get(0).getValue().getValue());
		assertEquals("urn:sys", nonExt.getIdentifier().get(1).getSystem().getValueAsString());
		assertEquals("id2", nonExt.getIdentifier().get(1).getValue().getValueAsString());
		
		List<ExtensionDt> ext = nonExt.getUndeclaredExtensionsByUrl("urn:ext");
		assertEquals(1,ext.size());
		assertEquals("urn:ext", ext.get(0).getUrlAsString());
		assertEquals(IntegerDt.class, ext.get(0).getValueAsPrimitive().getClass());
		assertEquals("100", ext.get(0).getValueAsPrimitive().getValueAsString());

		List<ExtensionDt> modExt = nonExt.getUndeclaredExtensionsByUrl("urn:modExt");
		assertEquals(1,modExt.size());
		assertEquals("urn:modExt", modExt.get(0).getUrlAsString());
		assertEquals(IntegerDt.class, modExt.get(0).getValueAsPrimitive().getClass());
		assertEquals("200", modExt.get(0).getValueAsPrimitive().getValueAsString());

		ExtPatient va = ourCtx.newViewGenerator().newView(nonExt, ExtPatient.class);
		assertEquals("urn:sys", va.getIdentifier().get(0).getSystem().getValueAsString());
		assertEquals("id1", va.getIdentifier().get(0).getValue().getValue());
		assertEquals("urn:sys", va.getIdentifier().get(1).getSystem().getValueAsString());
		assertEquals("id2", va.getIdentifier().get(1).getValue().getValueAsString());
		assertEquals(100, va.getExt().getValue().intValue());
		assertEquals(200, va.getModExt().getValue().intValue());
		
		assertEquals(0, va.getAllUndeclaredExtensions().size());
	}
	
	
	@Test
	public void testViewJson() {
		
		ExtPatient src = new ExtPatient();
		src.addIdentifier("urn:sys", "id1");
		src.addIdentifier("urn:sys", "id2");
		src.getExt().setValue(100);
		src.getModExt().setValue(200);
		
		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(src);
		
		ourLog.info(enc);
		
		IParser parser = ourCtx.newJsonParser();
		Patient nonExt = parser.parseResource(Patient.class, enc);
		
		assertEquals(Patient.class, nonExt.getClass());
		assertEquals("urn:sys", nonExt.getIdentifier().get(0).getSystem().getValueAsString());
		assertEquals("id1", nonExt.getIdentifier().get(0).getValue().getValue());
		assertEquals("urn:sys", nonExt.getIdentifier().get(1).getSystem().getValueAsString());
		assertEquals("id2", nonExt.getIdentifier().get(1).getValue().getValueAsString());
		
		List<ExtensionDt> ext = nonExt.getUndeclaredExtensionsByUrl("urn:ext");
		assertEquals(1,ext.size());
		assertEquals("urn:ext", ext.get(0).getUrlAsString());
		assertEquals(IntegerDt.class, ext.get(0).getValueAsPrimitive().getClass());
		assertEquals("100", ext.get(0).getValueAsPrimitive().getValueAsString());

		List<ExtensionDt> modExt = nonExt.getUndeclaredExtensionsByUrl("urn:modExt");
		assertEquals(1,modExt.size());
		assertEquals("urn:modExt", modExt.get(0).getUrlAsString());
		assertEquals(IntegerDt.class, modExt.get(0).getValueAsPrimitive().getClass());
		assertEquals("200", modExt.get(0).getValueAsPrimitive().getValueAsString());

		ExtPatient va = ourCtx.newViewGenerator().newView(nonExt, ExtPatient.class);
		assertEquals("urn:sys", va.getIdentifier().get(0).getSystem().getValueAsString());
		assertEquals("id1", va.getIdentifier().get(0).getValue().getValue());
		assertEquals("urn:sys", va.getIdentifier().get(1).getSystem().getValueAsString());
		assertEquals("id2", va.getIdentifier().get(1).getValue().getValueAsString());
		assertEquals(100, va.getExt().getValue().intValue());
		assertEquals(200, va.getModExt().getValue().intValue());
		
		assertEquals(0, va.getAllUndeclaredExtensions().size());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
