package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Created by Claude Opus 5.5
class TenantCheckingTesterTest {

	private static final String TENANT_A = "TENANT-A";
	private static final String TENANT_B = "TENANT-B";

	@ParameterizedTest
	@CsvSource(nullValues = "null", textBlock = """
			TENANT-A, true
			TENANT-B, false
			null,     false
			""")
	void appliesToTenant_forTenantIds_trueOnlyForListedTenant(String theRequestTenantId, boolean theExpectedApplies) {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), true);

		assertThat(tenantCheckingTester.appliesToTenant(theRequestTenantId))
			.isEqualTo(theExpectedApplies);
	}

	@ParameterizedTest
	@CsvSource(nullValues = "null", textBlock = """
			TENANT-A, false
			TENANT-B, true
			null,     true
			""")
	void appliesToTenant_notForTenantIds_trueForEveryOtherTenant(
			String theRequestTenantId, boolean theExpectedApplies) {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), false);

		assertThat(tenantCheckingTester.appliesToTenant(theRequestTenantId))
			.isEqualTo(theExpectedApplies);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			TENANT-A, true
			TENANT-B, false
			""")
	void matches_forTenantIds_requestWithoutResource_trueOnlyForListedTenant(
			String theRequestTenantId, boolean theExpectedMatches) {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), true);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(theRequestTenantId);
		boolean isMatching = tenantCheckingTester.matches(RestOperationTypeEnum.READ, systemRequestInTenant, null, null);
		assertThat(isMatching).isEqualTo(theExpectedMatches);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			TENANT-A, false
			TENANT-B, true
			""")
	void matches_notForTenantIds_requestWithoutResource_trueForEveryOtherTenant(
			String theRequestTenantId, boolean theExpectedMatches) {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), false);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(theRequestTenantId);
		boolean isMatching = tenantCheckingTester.matches(RestOperationTypeEnum.READ, systemRequestInTenant, null, null);
		assertThat(isMatching).isEqualTo(theExpectedMatches);
	}

	static Stream<Arguments> resourcePartitionsForTenantA() {
		return Stream.of(
				Arguments.of(RequestPartitionId.fromPartitionIdAndName(1, TENANT_A), true),
				Arguments.of(RequestPartitionId.fromPartitionIdAndName(2, TENANT_B), false),
				Arguments.of(null, true));
	}

	@ParameterizedTest
	@MethodSource("resourcePartitionsForTenantA")
	void matches_forTenantIds_requestInListedTenant_falseOnlyForResourceInOtherPartition(
			RequestPartitionId theResourcePartitionId, boolean theExpectedMatches) {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), true);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(TENANT_A);
		IBaseResource resourceInPartition = withResourceInPartition(theResourcePartitionId);
		boolean isMatching =
				tenantCheckingTester.matches(RestOperationTypeEnum.READ, systemRequestInTenant, null, resourceInPartition);
		assertThat(isMatching).isEqualTo(theExpectedMatches);
	}

	@Test
	void matches_forDefaultTenant_resourceInDefaultPartition_true() {
		TenantCheckingTester tenantCheckingTester =
				new TenantCheckingTester(Arrays.asList(ProviderConstants.DEFAULT_PARTITION_NAME), true);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(ProviderConstants.DEFAULT_PARTITION_NAME);
		IBaseResource resourceInDefaultPartition = withResourceInPartition(RequestPartitionId.fromPartitionId(null));
		boolean isMatching = tenantCheckingTester.matches(
				RestOperationTypeEnum.READ, systemRequestInTenant, null, resourceInDefaultPartition);
		assertThat(isMatching).isTrue();
	}

	@Test
	void matches_notForTenantIds_resourceInOtherPartition_true() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), false);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(TENANT_A);
		IBaseResource resourceInPartition =
				withResourceInPartition(RequestPartitionId.fromPartitionIdAndName(2, TENANT_B));
		boolean isMatching =
				tenantCheckingTester.matches(RestOperationTypeEnum.READ, systemRequestInTenant, null, resourceInPartition);
		assertThat(isMatching).isTrue();
	}

	@Test
	void matchesOutput_forTenantIds_requestInOtherTenant_false() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), true);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(TENANT_B);
		boolean isMatching = tenantCheckingTester.matchesOutput(RestOperationTypeEnum.READ, systemRequestInTenant, null);
		assertThat(isMatching).isFalse();
	}

	@Test
	void matchesOutput_forTenantIds_resourceInListedPartition_true() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), true);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(TENANT_A);
		IBaseResource resourceInPartition =
				withResourceInPartition(RequestPartitionId.fromPartitionIdAndName(1, TENANT_A));
		boolean isMatching =
				tenantCheckingTester.matchesOutput(RestOperationTypeEnum.READ, systemRequestInTenant, resourceInPartition);
		assertThat(isMatching).isTrue();
	}

	@Test
	void matchesOutput_forTenantIds_resourceInOtherPartition_false() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), true);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(TENANT_A);
		IBaseResource resourceInPartition =
				withResourceInPartition(RequestPartitionId.fromPartitionIdAndName(2, TENANT_B));
		boolean isMatching =
				tenantCheckingTester.matchesOutput(RestOperationTypeEnum.READ, systemRequestInTenant, resourceInPartition);
		assertThat(isMatching).isFalse();
	}

	@Test
	void matchesOutput_notForTenantIds_requestInOtherTenant_true() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(Arrays.asList(TENANT_A), false);
		RequestDetails systemRequestInTenant = withSystemRequestInTenant(TENANT_B);
		boolean isMatching = tenantCheckingTester.matchesOutput(RestOperationTypeEnum.READ, systemRequestInTenant, null);
		assertThat(isMatching).isTrue();
	}

	@Test
	void appliesToTenant_forTenantIdsInImmutableList_requestWithoutTenant_false() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(List.of(TENANT_A), true);
		boolean isApplying = tenantCheckingTester.appliesToTenant(null);
		assertThat(isApplying).isFalse();
	}

	@Test
	void matches_forTenantIdsInImmutableList_requestWithoutTenant_false() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(List.of(TENANT_A), true);
		RequestDetails systemRequestWithoutTenant = withSystemRequestInTenant(null);
		boolean isMatching =
				tenantCheckingTester.matches(RestOperationTypeEnum.READ, systemRequestWithoutTenant, null, null);
		assertThat(isMatching).isFalse();
	}

	@Test
	void matchesOutput_forTenantIdsInImmutableList_requestWithoutTenant_false() {
		TenantCheckingTester tenantCheckingTester = new TenantCheckingTester(List.of(TENANT_A), true);
		RequestDetails systemRequestWithoutTenant = withSystemRequestInTenant(null);
		boolean isMatching =
				tenantCheckingTester.matchesOutput(RestOperationTypeEnum.READ, systemRequestWithoutTenant, null);
		assertThat(isMatching).isFalse();
	}

	private static RequestDetails withSystemRequestInTenant(String theTenantId) {
		RequestDetails retVal = new SystemRequestDetails();
		retVal.setTenantId(theTenantId);
		return retVal;
	}

	private static IBaseResource withResourceInPartition(RequestPartitionId thePartitionId) {
		IBaseResource retVal = mock(IBaseResource.class);
		when(retVal.getUserData(Constants.RESOURCE_PARTITION_ID)).thenReturn(thePartitionId);
		return retVal;
	}
}
