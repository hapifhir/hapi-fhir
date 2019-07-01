package ca.uhn.fhir.model;

import org.hl7.fhir.dstu2.model.DecimalType;
import org.hl7.fhir.dstu2.model.StringType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrimitiveTest {

  @Test
  public void testHasValue() {
    StringType type = new StringType();
    assertFalse(type.hasValue());
    type.addExtension().setUrl("http://foo").setValue(new DecimalType(123));
    assertFalse(type.hasValue());
    type.setValue("Hello");
    assertTrue(type.hasValue());
  }
  
}
