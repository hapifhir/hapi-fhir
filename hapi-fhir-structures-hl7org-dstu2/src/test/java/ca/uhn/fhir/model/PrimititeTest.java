package ca.uhn.fhir.model;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu2.model.DecimalType;
import org.hl7.fhir.dstu2.model.StringType;
import org.junit.Test;
import org.thymeleaf.standard.expression.NumberTokenExpression;

public class PrimititeTest {

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
