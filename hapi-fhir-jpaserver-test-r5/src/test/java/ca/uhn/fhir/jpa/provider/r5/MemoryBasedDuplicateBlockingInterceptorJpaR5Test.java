package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.storage.interceptor.MemoryBasedDuplicateBlockingInterceptor;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

public class MemoryBasedDuplicateBlockingInterceptorJpaR5Test extends BaseResourceProviderR5Test {

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();

		registerInterceptor(new MemoryBasedDuplicateBlockingInterceptor(myFhirContext));
	}

	@Test
	void testCreateDuplicateBlocked() {
		Patient patient = (Patient) buildPatient(withIdentifier("http://foo", "123"));
		String id = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Patient patient2 = (Patient) buildPatient(withIdentifier("http://foo", "123"));
		assertThatThrownBy(()->myClient.create().resource(patient2).execute())
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("Can not create resource duplicating existing resource: " + id);
	}

}
