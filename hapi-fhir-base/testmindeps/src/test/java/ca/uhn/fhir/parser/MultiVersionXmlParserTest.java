package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;

public class MultiVersionXmlParserTest {

	private static final FhirContext ourCtxDstu1 = FhirContext.forDstu1();
	private static final FhirContext ourCtxDstu2 = FhirContext.forDstu2();
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultiVersionXmlParserTest.class);

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

		str = ourCtxDstu2.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);
		assertThat(str, Matchers.stringContainsInOrder("<extension url=\"http://foo#ext\"><valueQuantity><value value=\"2.2\"", "<comparator value=\"&lt;\"", "<units value=\"g/L\"",
				"</valueQuantity></extension>"));
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
		
		{
			ca.uhn.fhir.model.dstu2.resource.Patient parsed = ourCtxDstu1.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Patient.class, res);
			assertEquals("RR Display", parsed.getManagingOrganization().getDisplayElement().getValue());
			assertEquals(1, parsed.getContained().getContainedResources().size());
			assertEquals("<div>DIV</div>", p.getText().getDiv().getValueAsString());

		}
		{
			ca.uhn.fhir.model.dstu.resource.Patient parsed = ourCtxDstu2.newXmlParser().parseResource(ca.uhn.fhir.model.dstu.resource.Patient.class, res);
			assertEquals("RR Display", parsed.getManagingOrganization().getDisplayElement().getValue());
			assertEquals(1, parsed.getContained().getContainedResources().size());
			assertEquals("<div>DIV</div>", p.getText().getDiv().getValueAsString());
		}
		
	}
	
	
}
