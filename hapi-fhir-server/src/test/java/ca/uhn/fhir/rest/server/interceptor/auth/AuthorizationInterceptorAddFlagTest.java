package ca.uhn.fhir.rest.server.interceptor.auth;

import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS;
import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationFlagsEnum.ENFORCE_REVERSE_CHAINED_AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class AuthorizationInterceptorAddFlagTest {

	@Test
	void testAddFlag_noFlagsSet_addsFlag() {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor();

		interceptor.addFlag(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);

		assertThat(interceptor.getFlags()).containsExactly(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);
	}

	@Test
	void testAddFlag_flagsSet_keepsExistingFlags() {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor();
		interceptor.setFlags(DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS);

		interceptor.addFlag(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);

		assertThat(interceptor.getFlags())
				.containsExactlyInAnyOrder(
						DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS, ENFORCE_REVERSE_CHAINED_AUTHORIZATION);
	}
}
