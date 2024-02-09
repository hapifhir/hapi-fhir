package ca.uhn.fhir.model;

import org.hl7.fhir.dstu2.model.DecimalType;
import org.hl7.fhir.dstu2.model.StringType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PrimititeTest {

  @Test
  public void testHasValue() {
    StringType type = new StringType();
		assertThat(type.hasValue()).isFalse();
    type.addExtension().setUrl("http://foo").setValue(new DecimalType(123));
		assertThat(type.hasValue()).isFalse();
    type.setValue("Hello");
		assertThat(type.hasValue()).isTrue();
  }
  
}
