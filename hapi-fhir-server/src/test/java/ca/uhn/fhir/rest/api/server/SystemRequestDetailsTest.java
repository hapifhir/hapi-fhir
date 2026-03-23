package ca.uhn.fhir.rest.api.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SystemRequestDetailsTest {

	@Test
	void testCopyConstructor_CopiesHeaders_ModifyingTheHeadersInOriginalDoesNotAffectTheCopy () {
		SystemRequestDetails original = new SystemRequestDetails();
		original.addHeader("header1", "value1");
		original.addHeader("header1", "value2");
		original.addHeader("header2", "value3");

		SystemRequestDetails copy = new SystemRequestDetails(original);
		assertThat(copy.getHeaders("header1")).containsExactly("value1", "value2");
		assertThat(copy.getHeaders("header2")).containsExactly("value3");

		//now modify the original headers
		original.addHeader("header1", "value4");
		original.addHeader("header2", "value5");
		original.addHeader("headerNew", "valueNew");

		//the copy should not be affected
		assertThat(copy.getHeaders("header1")).containsExactly("value1", "value2");
		assertThat(copy.getHeaders("header2")).containsExactly("value3");
		assertThat(copy.getHeader("headerNew")).isNull();
	}

	@Test
	void setTenantId_withNonBlankValue_retainsValue() {
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setTenantId("TENANT_A");
		assertThat(srd.getTenantId()).isEqualTo("TENANT_A");
	}

	@Test
	void setTenantId_withNullValue_normalizesToNull() {
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setTenantId(null);
		assertThat(srd.getTenantId()).isNull();
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "  "})
	void setTenantId_withBlankValue_normalizesToNull(String theTenantId) {
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setTenantId(theTenantId);
		assertThat(srd.getTenantId()).isNull();
	}

	@Test
	void setTenantId_withBlankValue_clearsExistingTenant() {
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setTenantId("TENANT_A");
		srd.setTenantId("");
		assertThat(srd.getTenantId()).isNull();
	}
}
