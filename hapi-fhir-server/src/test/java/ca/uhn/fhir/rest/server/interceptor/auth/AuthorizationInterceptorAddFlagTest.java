package ca.uhn.fhir.rest.server.interceptor.auth;

import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS;
import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationFlagsEnum.ENFORCE_REVERSE_CHAINED_AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

	@Test
	void testAddFlag_flagAlreadySet_keepsSingleFlag() {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor();
		interceptor.setFlags(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);

		interceptor.addFlag(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);

		assertThat(interceptor.getFlags()).containsExactly(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);
	}

	@Test
	void testAddFlag_null_throws() {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor();

		assertThatThrownBy(() -> interceptor.addFlag(null)).isInstanceOf(NullPointerException.class);
	}
}
