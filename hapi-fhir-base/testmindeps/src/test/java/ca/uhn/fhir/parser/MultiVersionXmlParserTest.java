package ca.uhn.fhir.parser;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;

public class MultiVersionXmlParserTest {
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultiVersionXmlParserTest.class);
	
	@Test
	public void testEncodeExtensionFromDifferentVersion() {
		Patient p = new Patient();
		p.addIdentifier("urn:sys", "001");
		p.addUndeclaredExtension(false, "http://foo#ext", new QuantityDt(QuantityCompararatorEnum.LESSTHAN, 2.2, "g/L"));

		String str;
		str = FhirContext.forDstu1().newXmlParser().encodeResourceToString(p);
		ourLog.info(str);
		assertThat(str,StringContains.containsString("<extension url=\"http://foo#ext\"><valueQuantity><value value=\"2.2\"/><comparator value=\"&lt;\"/><units value=\"g/L\"/></valueQuantity></extension>"));

		str = FhirContext.forDev().newXmlParser().encodeResourceToString(p);
		ourLog.info(str);
		assertThat(str,StringContains.containsString("<extension url=\"http://foo#ext\"><valueQuantity><value value=\"2.2\"/><comparator value=\"&lt;\"/><units value=\"g/L\"/></valueQuantity></extension>"));
	}

}
