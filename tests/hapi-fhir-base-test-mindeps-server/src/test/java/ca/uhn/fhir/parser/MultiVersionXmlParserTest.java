package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.util.TestUtil;

public class MultiVersionXmlParserTest {

	private static FhirContext ourCtxDstu2 = FhirContext.forDstu2();
	private static FhirContext ourCtxDstu3 = FhirContext.forDstu3();
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultiVersionXmlParserTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testEncodeExtensionFromDifferentVersion() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:sys").setValue("001");
		p.addUndeclaredExtension(false, "http://foo#ext", new QuantityDt(QuantityComparatorEnum.LESS_THAN, 2.2, "g/L"));

		String str;
		str = ourCtxDstu2.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);
		assertThat(str, Matchers.stringContainsInOrder("<extension url=\"http://foo#ext\"><valueQuantity><value value=\"2.2\"", "<comparator value=\"&lt;\"", "<unit value=\"g/L\"",
				"</valueQuantity></extension>"));

		try {
			FhirContext.forDstu3().newXmlParser().encodeResourceToString(p);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This parser is for FHIR version DSTU3 - Can not encode a structure for version DSTU2", e.getMessage());
		}
	}

	@Test
	public void testParseResourceReference() {

		Organization o = new Organization();
		o.getNameElement().setValue("Some Org");
		o.getPartOf().setDisplay("Part Of");
		
		Patient p = new Patient();
		p.getText().getDiv().setValueAsString("<div>DIV</div>");
		p.getManagingOrganization().setDisplay("RR Display");
		p.getManagingOrganization().setResource(o);
		
		String res = ourCtxDstu2.newXmlParser().encodeResourceToString(p);
		
		try {
			ourCtxDstu3.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Patient.class, res);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("This context is for FHIR version \"DSTU3\" but the class \"ca.uhn.fhir.model.dstu2.resource.Patient\" is for version \"DSTU2\"", e.getMessage());
		}
		try {
			ourCtxDstu3.newXmlParser().parseResource(Patient.class, res);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("This context is for FHIR version \"DSTU3\" but the class \"ca.uhn.fhir.model.dstu2.resource.Patient\" is for version \"DSTU2\"", e.getMessage());
		}
		
	}
	
	
}
