package ca.uhn.fhir.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.primitive.StringDt;

public class FhirTerserTest {

	private static FhirContext ourCtx = FhirContext.forDstu1();

	@Test
	public void testGetAllPopulatedChildElementsOfType() {

		Patient p = new Patient();
		p.setGender(AdministrativeGenderCodesEnum.M);
		p.addIdentifier().setSystem("urn:foo");
		p.addAddress().addLine("Line1");
		p.addAddress().addLine("Line2");
		p.addName().addFamily("Line3");

		FhirTerser t = ourCtx.newTerser();
		List<StringDt> strings = t.getAllPopulatedChildElementsOfType(p, StringDt.class);

		assertEquals(3, strings.size());
		assertThat(strings, containsInAnyOrder(new StringDt("Line1"), new StringDt("Line2"), new StringDt("Line3")));

	}

	@Test
	public void testMultiValueTypes() {

		Observation obs = new Observation();
		obs.setValue(new QuantityDt(123L));

		FhirTerser t = ourCtx.newTerser();

		// As string
		{
			List<Object> values = t.getValues(obs, "Observation.valueString");
			assertEquals(0, values.size());
		}

		// As quantity
		{
			List<Object> values = t.getValues(obs, "Observation.valueQuantity");
			assertEquals(1, values.size());
			QuantityDt actual = (QuantityDt) values.get(0);
			assertEquals("123", actual.getValue().getValueAsString());
		}
	}

	@Test
	public void testTerser() {

		//@formatter:off
		String msg = "<Observation xmlns=\"http://hl7.org/fhir\">\n" + 
			"    <text>\n" + 
			"        <status value=\"empty\"/>\n" + 
			"        <div xmlns=\"http://www.w3.org/1999/xhtml\"/>\n" + 
			"    </text>\n" + 
			"    <!-- The test code  - may not be correct -->\n" + 
			"    <name>\n" + 
			"        <coding>\n" + 
			"            <system value=\"http://loinc.org\"/>\n" + 
			"            <code value=\"43151-0\"/>\n" + 
			"            <display value=\"Glucose Meter Device Panel\"/>\n" + 
			"        </coding>\n" + 
			"    </name>\n" + 
			"    <valueQuantity>\n" + 
			"        <value value=\"7.7\"/>\n" + 
			"        <units value=\"mmol/L\"/>\n" + 
			"        <system value=\"http://unitsofmeasure.org\"/>\n" + 
			"    </valueQuantity>\n" + 
			"    <appliesDateTime value=\"2014-05-28T22:12:21Z\"/>\n" + 
			"    <status value=\"final\"/>\n" + 
			"    <reliability value=\"ok\"/>\n" + 
			"    <subject>\n" + 
			"        <reference value=\"cid:patient@bundle\"/>\n" + 
			"    </subject>\n" + 
			"    <performer>\n" + 
			"        <reference value=\"cid:device@bundle\"></reference>\n" + 
			"    </performer>\n" + 
			"</Observation>";
		//@formatter:on

		Observation parsed = ourCtx.newXmlParser().parseResource(Observation.class, msg);
		FhirTerser t = ourCtx.newTerser();

		List<ResourceReferenceDt> elems = t.getAllPopulatedChildElementsOfType(parsed, ResourceReferenceDt.class);
		assertEquals(2, elems.size());
		assertEquals("cid:patient@bundle", elems.get(0).getReference().getValue());
		assertEquals("cid:device@bundle", elems.get(1).getReference().getValue());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
