package ca.uhn.fhir.jaxrs.server.util;

import ca.uhn.fhir.jaxrs.server.test.AbstractDummyPatientProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderR4;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderR4MimeType;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JaxRsMethodBindingsMimeTypeTest {

	@BeforeEach
	public void setUp() {
		JaxRsMethodBindings.getClassBindings().clear();
	}

	@Test
	public void testFindMethodsFor2ProvidersWithMethods() {
		assertEquals(AbstractDummyPatientProvider.class, new TestJaxRsDummyPatientProviderR4().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getMethod().getDeclaringClass());
		assertEquals(AbstractDummyPatientProvider.class, new TestJaxRsDummyPatientProviderR4MimeType().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getMethod().getDeclaringClass());
	}

}
