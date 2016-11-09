package ca.uhn.fhir.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.util.TestUtil;

public class MultiVersionXmlParserTest {

	private static FhirContext ourCtxDstu1 = FhirContext.forDstu1();
	private static FhirContext ourCtxDstu2 = FhirContext.forDstu2();
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultiVersionXmlParserTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testEncodeExtensionFromDifferentVersion() {
		Patient p = new Patient();
		p.addIdentifier("urn:sys", "001");
		p.addUndeclaredExtension(false, "http://foo#ext", new QuantityDt(QuantityCompararatorEnum.LESSTHAN, 2.2, "g/L"));

		String str;
		str = ourCtxDstu1.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);
		assertThat(str, Matchers.stringContainsInOrder("<extension url=\"http://foo#ext\"><valueQuantity><value value=\"2.2\"", "<comparator value=\"&lt;\"", "<units value=\"g/L\"",
				"</valueQuantity></extension>"));

		try {
			FhirContext.forDstu2().newXmlParser().encodeResourceToString(p);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This parser is for FHIR version DSTU2 - Can not encode a structure for version DSTU1", e.getMessage());
		}
	}

	@Test
	public void testParseResourceReference() {

		Organization o = new Organization();
		o.getName().setValue("Some Org");
		o.getPartOf().setDisplay("Part Of");
		
		Patient p = new Patient();
		p.getText().getDiv().setValueAsString("<div>DIV</div>");
		p.getManagingOrganization().setDisplay("RR Display");
		p.getManagingOrganization().setResource(o);
		
		String res = ourCtxDstu1.newXmlParser().encodeResourceToString(p);
		
		try {
			ourCtxDstu1.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Patient.class, res);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("This context is for FHIR version \"DSTU1\" but the class \"ca.uhn.fhir.model.dstu2.resource.Patient\" is for version \"DSTU2\"", e.getMessage());
		}
		try {
			ourCtxDstu2.newXmlParser().parseResource(ca.uhn.fhir.model.dstu.resource.Patient.class, res);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("This context is for FHIR version \"DSTU2\" but the class \"ca.uhn.fhir.model.dstu.resource.Patient\" is for version \"DSTU1\"", e.getMessage());
		}
		
	}
	
	
}
