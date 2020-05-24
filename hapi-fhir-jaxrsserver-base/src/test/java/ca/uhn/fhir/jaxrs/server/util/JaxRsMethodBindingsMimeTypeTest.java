package ca.uhn.fhir.jaxrs.server.util;

import ca.uhn.fhir.jaxrs.server.test.AbstractDummyPatientProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderR4;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderR4MimeType;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.DEFAULT)
public class JaxRsMethodBindingsMimeTypeTest {
	
	 @Before
	 public void setUp() {
		 JaxRsMethodBindings.getClassBindings().clear();
	 }

	@Test
	public void testFindMethodsFor2ProvidersWithMethods() {
		assertEquals(AbstractDummyPatientProvider.class, new TestJaxRsDummyPatientProviderR4().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getMethod().getDeclaringClass());
		assertEquals(AbstractDummyPatientProvider.class, new TestJaxRsDummyPatientProviderR4MimeType().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getMethod().getDeclaringClass());
	}
	
	}
