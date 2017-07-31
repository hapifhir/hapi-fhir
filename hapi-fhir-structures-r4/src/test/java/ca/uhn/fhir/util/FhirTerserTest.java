package ca.uhn.fhir.util;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

public class FhirTerserTest {

  private static FhirContext ourCtx = FhirContext.forR4();

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirTerserTest.class);

  @Test
  public void testGetAllPopulatedChildElementsOfType() {

    Patient p = new Patient();
    p.setGender(AdministrativeGender.MALE);
    p.addIdentifier().setSystem("urn:foo");
    p.addAddress().addLine("Line1");
    p.addAddress().addLine("Line2");
    p.addName().setFamily("Line3");

    FhirTerser t = ourCtx.newTerser();
    List<StringType> strings = t.getAllPopulatedChildElementsOfType(p, StringType.class);

    assertEquals(3, strings.size());

    Set<String> allStrings = new HashSet<>();
    for (StringType next : strings) {
      allStrings.add(next.getValue());
    }

    assertThat(allStrings, containsInAnyOrder("Line1", "Line2", "Line3"));

  }

  @Test
  public void testMultiValueTypes() {

    Observation obs = new Observation();
    obs.setValue(new Quantity(123L));

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
      Quantity actual = (Quantity) values.get(0);
      assertEquals("123", actual.getValueElement().getValueAsString());
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

    List<Reference> elems = t.getAllPopulatedChildElementsOfType(parsed, Reference.class);
    assertEquals(2, elems.size());
    assertEquals("cid:patient@bundle", elems.get(0).getReferenceElement().getValue());
    assertEquals("cid:device@bundle", elems.get(1).getReferenceElement().getValue());
  }

  @AfterClass
  public static void afterClassClearContext() {
    TestUtil.clearAllStaticFieldsForUnitTest();
  }

}
