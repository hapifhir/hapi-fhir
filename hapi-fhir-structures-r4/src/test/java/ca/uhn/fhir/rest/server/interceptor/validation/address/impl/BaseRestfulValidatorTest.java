package ca.uhn.fhir.rest.server.interceptor.validation.address.impl;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.JsonNode;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseRestfulValidatorTest {

	@Test
	public void testHappyPath() throws Exception {
		ResponseEntity responseEntity = mock(ResponseEntity.class);
		when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
		when(responseEntity.getBody()).thenReturn("{}");

		TestRestfulValidator val = spy(new TestRestfulValidator(responseEntity));
		assertNotNull(val.isValid(new Address(), FhirContext.forR4()));

		verify(val, times(1)).getResponseEntity(any(IBase.class), any(FhirContext.class));
		verify(val, times(1)).getValidationResult(any(), any(), any());
	}

	@Test
	public void testIsValid() throws Exception {
		ResponseEntity responseEntity = mock(ResponseEntity.class);
		when(responseEntity.getStatusCode()).thenReturn(HttpStatus.REQUEST_TIMEOUT);

		TestRestfulValidator val = new TestRestfulValidator(responseEntity);
		try {
			assertNotNull(val.isValid(new Address(), FhirContext.forR4()));
			fail();
		} catch (Exception e) {
		}
	}

	private static class TestRestfulValidator extends BaseRestfulValidator {
		ResponseEntity<String> myResponseEntity;

		public TestRestfulValidator(ResponseEntity<String> theResponseEntity) {
			super(null);
			myResponseEntity = theResponseEntity;
		}

		@Override
		protected AddressValidationResult getValidationResult(AddressValidationResult theResult, JsonNode response, FhirContext theFhirContext) throws Exception {
			return new AddressValidationResult();
		}

		@Override
		protected ResponseEntity<String> getResponseEntity(IBase theAddress, FhirContext theFhirContext) throws Exception {
			return myResponseEntity;
		}
	}


}
