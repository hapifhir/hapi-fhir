package ca.uhn.fhir.rest.server.interceptor.consent;

import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.rest.server.interceptor.consent.ConsentOperationStatusEnum.AUTHORIZED;
import static ca.uhn.fhir.rest.server.interceptor.consent.ConsentOperationStatusEnum.PROCEED;
import static ca.uhn.fhir.rest.server.interceptor.consent.ConsentOperationStatusEnum.REJECT;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsentOperationStatusEnumTest {


	@Test
	void testStrengthOrder() {
	    assertTrue(REJECT.getPrecedence() > AUTHORIZED.getPrecedence());
	    assertTrue(AUTHORIZED.getPrecedence() > PROCEED.getPrecedence());
	}

}
