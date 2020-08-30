package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class SchemaBaseValidatorTest {

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
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), containsString("Unable to find classpath resource"));
		}
	}
}
