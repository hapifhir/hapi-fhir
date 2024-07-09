package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


public class SchemaBaseValidatorTest extends BaseValidationTestWithInlineMocks {

	@Test
	public void testLoadXmlSuccess() {
		SchemaBaseValidator validator = new SchemaBaseValidator(FhirContext.forR4());
		Source schema = validator.loadXml("fhir-single.xsd");
		assertNotNull(schema);
	}


	@Test
	public void testLoadXmlFail() {
		SchemaBaseValidator validator = new SchemaBaseValidator(FhirContext.forR4());
		try {
			validator.loadXml("foo.xsd");
			fail();		} catch (InternalErrorException e) {
			assertThat(e.getMessage()).contains("Unable to find classpath resource");
		}
	}
}
