package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OperationServerInvalidDefinitionTest {

	@RegisterExtension
	private RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(FhirContext.forR4Cached());

	@Test
	public void testReturnTypeWrongVersion() {

		class OperationProvider {

			@Operation(name = ProviderConstants.OPERATION_MEMBER_MATCH, idempotent = false, returnParameters = {
				@OperationParam(name = "MemberIdentifier", type = StringDt.class)
			})
			public Parameters patientMemberMatch(
				@OperationParam(name = Constants.PARAM_MEMBER_PATIENT, min = 1, max = 1) Patient theMemberPatient
			) {
				return null;
			}

		}

		try {
			myRestfulServerExtension.registerProvider(new OperationProvider());
			fail();
		} catch (ConfigurationException e) {
			assertEquals("HAPI-0288: Failure scanning class OperationProvider: HAPI-0360: Incorrect use of type StringDt as return type for method when theContext is for version R4 in method: public org.hl7.fhir.r4.model.Parameters ca.uhn.fhir.rest.server.OperationServerInvalidDefinitionTest$1OperationProvider.patientMemberMatch(org.hl7.fhir.r4.model.Patient)", e.getMessage());
		}

	}

}
