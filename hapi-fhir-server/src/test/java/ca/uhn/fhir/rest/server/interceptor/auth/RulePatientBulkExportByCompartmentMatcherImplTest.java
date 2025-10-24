package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;

import static ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters.ExportStyle.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RulePatientBulkExportByCompartmentMatcherImplTest {

	private final RestOperationTypeEnum myOperation = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
	private final Pointcut myPointcut = Pointcut.STORAGE_INITIATE_BULK_EXPORT;

	@Mock
	private IRuleApplier myRuleApplier;
	@Mock
	private IAuthResourceResolver myAuthResourceResolver;
	@Mock
	private IBaseResource myResource;
	@Mock
	private IBaseResource myResource2;
	@Mock
	private IAuthorizationSearchParamMatcher mySearchParamMatcher;
	@Mock
	private RequestDetails myRequestDetails;
	@Mock
	private Set<AuthorizationFlagsEnum> myFlags;


	@ParameterizedTest
	@MethodSource("paramsInstanceLevel")
	void testPatientRule_instanceLevelExport_withCompartmentMatchers(Collection<String> theAllowedResourceTypes,
												 BulkExportJobParameters theBulkExportJobParams,
												 List<IAuthorizationSearchParamMatcher.MatchResult> theSearchParamMatcherMatchResults,
												 PolicyEnum theExpectedVerdict,
												 String theMessage) {
		RulePatientBulkExportByCompartmentMatcherImpl rule = new RulePatientBulkExportByCompartmentMatcherImpl("b");
		rule.addAppliesToPatientExportOnPatient("identifier=foo|bar");
		rule.setResourceTypes(theAllowedResourceTypes);
		rule.setMode(PolicyEnum.ALLOW);

		when(myRequestDetails.getUserData()).thenReturn(Map.of(AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS, theBulkExportJobParams));

		if (theSearchParamMatcherMatchResults != null) {
			when(myRuleApplier.getAuthResourceResolver()).thenReturn(myAuthResourceResolver);
			if (theSearchParamMatcherMatchResults.size() == 1) {
				when(myAuthResourceResolver.resolveCompartmentByIds(any(), eq("Patient"))).thenReturn(List.of(myResource));
			} else if (theSearchParamMatcherMatchResults.size() == 2) {
				when(myAuthResourceResolver.resolveCompartmentByIds(any(), eq("Patient"))).thenReturn(List.of(myResource, myResource2));
				when(myResource2.fhirType()).thenReturn("Patient");
				when(mySearchParamMatcher.match("Patient?identifier=foo|bar", myResource2)).thenReturn(theSearchParamMatcherMatchResults.get(1));
			}
			when(myRuleApplier.getSearchParamMatcher()).thenReturn(mySearchParamMatcher);
			when(myResource.fhirType()).thenReturn("Patient");
			when(mySearchParamMatcher.match("Patient?identifier=foo|bar", myResource)).thenReturn(theSearchParamMatcherMatchResults.get(0));
		}

		AuthorizationInterceptor.Verdict verdict = rule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		if (theExpectedVerdict != null) {
			// Expect a decision
			Assertions.assertNotNull(verdict, "Expected " + theExpectedVerdict + " but got abstain - " + theMessage);
			assertEquals(theExpectedVerdict, verdict.getDecision(), "Expected " + theExpectedVerdict + " but got " + verdict.getDecision() + " - " + theMessage);
		} else {
			// Expect abstain
			assertNull(verdict, "Expected abstain - " + theMessage);
		}
	}

	static Stream<Arguments> paramsInstanceLevel() {
		IAuthorizationSearchParamMatcher.MatchResult match = IAuthorizationSearchParamMatcher.MatchResult.buildMatched();
		IAuthorizationSearchParamMatcher.MatchResult noMatch = IAuthorizationSearchParamMatcher.MatchResult.buildUnmatched();

		// theAllowedResourceTypes, theBulkExportJobParams, theSearchParamMatcherMatchResults, theExpectedVerdict, theMessage
		return Stream.of(
			// One patient cases
			Arguments.of(List.of(), new BulkExportParamsBuilder().exportOnePatient().build(), List.of(match), PolicyEnum.ALLOW, "Allow request for all types, permit all types"),
			Arguments.of(List.of(), new BulkExportParamsBuilder().exportOnePatient().withResourceTypes("Patient", "Observation").build(), List.of(match), PolicyEnum.ALLOW, "Allow request for some types, permit all types"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportOnePatient().withResourceTypes("Patient","Observation").build(), List.of(match), PolicyEnum.ALLOW, "Allow request for exact set of allowable types"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportOnePatient().withResourceTypes("Patient").build(), List.of(match), PolicyEnum.ALLOW, "Allow request for subset of allowable types"),
			Arguments.of(List.of("Patient"), new BulkExportParamsBuilder().exportOnePatient().withResourceTypes("Patient").build(), List.of(noMatch), null, "Abstain when requesting some resource types, but no resources match the permission query"),
			Arguments.of(List.of(), new BulkExportParamsBuilder().exportOnePatient().build(), List.of(noMatch), null, "Abstain when requesting all resource types, but no resources match the permission query"),
			// Below is the narrowing case. Narrowing should happen at the SecurityInterceptor layer. Here, we expect deny
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportOnePatient().build(), null, PolicyEnum.DENY, "Deny request for all types when allowing some types"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportOnePatient().withResourceTypes("Patient","Observation","Encounter").build(), null, PolicyEnum.DENY, "Deny request for superset of allowable types"),
			Arguments.of(List.of(), new BulkExportParamsBuilder().exportOnePatient().withExportStyle(GROUP).build(), null, null, "Abstain when export style is not Group"),

			// Two patient cases
			Arguments.of(List.of(), new BulkExportParamsBuilder().exportTwoPatients().build(), List.of(match, match), PolicyEnum.ALLOW, "Allow request for all types on 2 patients, permit all types"),
			Arguments.of(List.of(), new BulkExportParamsBuilder().exportTwoPatients().withResourceTypes("Patient", "Observation").build(), List.of(match, match), PolicyEnum.ALLOW, "Allow request for some types on 2 patients, permit all types"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportTwoPatients().withResourceTypes("Patient","Observation").build(), List.of(match, match), PolicyEnum.ALLOW, "Allow request for exact set of allowable types on 2 patients"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportTwoPatients().withResourceTypes("Patient").build(), List.of(match, match), PolicyEnum.ALLOW, "Allow request on 2 patients for subset of allowable types"),
			Arguments.of(List.of("Patient"), new BulkExportParamsBuilder().exportTwoPatients().withResourceTypes("Patient").build(), List.of(noMatch), null, "Abstain when requesting some resource types on 2 patients, but no resources match the permission query"),
			Arguments.of(List.of("Patient"), new BulkExportParamsBuilder().exportTwoPatients().withResourceTypes("Patient").build(), List.of(match, noMatch), PolicyEnum.DENY, "Deny when requesting some resource types on 2 patients, but only one Patient match the permission query"),

			// Instance-level with _typeFilter
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportOnePatient().withTypeFilters("identifier=foo|bar").withResourceTypes("Patient","Observation").build(), List.of(match), PolicyEnum.ALLOW, "Allow request with typeFilter for exact set of allowable types"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportOnePatient().withTypeFilters("identifier=abc|def").withResourceTypes("Patient","Observation").build(), null, PolicyEnum.DENY, "Deny request with typeFilter when all don't match permissible filter"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportOnePatient().withTypeFilters("identifier=foo|bar", "name=Doe").withResourceTypes("Patient","Observation").build(), null, PolicyEnum.DENY, "Deny request with typeFilter when one doesn't match permissible filter"),
			Arguments.of(List.of("Patient", "Observation"), new BulkExportParamsBuilder().exportTwoPatients().withTypeFilters("identifier=foo|bar").withResourceTypes("Patient","Observation").build(), List.of(match, noMatch), PolicyEnum.DENY, "Deny request with typeFilter for exact set of allowable types, but matcher doesn't match")
		);
	}

	@ParameterizedTest
	@MethodSource("paramsTypeLevel")
	void testPatientRule_typeLevelExport_withCompartmentMatchers(Collection<String> theAllowedResourceTypes,
												 Collection<String> thePermissionFilters,
												 BulkExportJobParameters theBulkExportJobParams,
												 PolicyEnum theExpectedVerdict,
												 String theMessage) {
		RulePatientBulkExportByCompartmentMatcherImpl rule = new RulePatientBulkExportByCompartmentMatcherImpl("b");
		for (String filter : thePermissionFilters) {
			rule.addAppliesToPatientExportOnPatient(filter);
		}
		rule.setResourceTypes(theAllowedResourceTypes);
		rule.setMode(PolicyEnum.ALLOW);

		when(myRequestDetails.getUserData()).thenReturn(Map.of(AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS, theBulkExportJobParams));

		AuthorizationInterceptor.Verdict verdict = rule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		if (theExpectedVerdict != null) {
			// Expect a decision
			Assertions.assertNotNull(verdict, "Expected " + theExpectedVerdict + " but got abstain - " + theMessage);
			assertEquals(theExpectedVerdict, verdict.getDecision(), "Expected " + theExpectedVerdict + " but got " + verdict.getDecision() + " - " + theMessage);
		} else {
			// Expect abstain
			assertNull(verdict, "Expected abstain - " + theMessage);
		}
	}

	static Stream<Arguments> paramsTypeLevel() {
		// theAllowedResourceTypes, thePermissionFilters, theBulkExportJobParams, theExpectedVerdict, theMessage
		return Stream.of(
			// Type-Level export with _typeFilter
			Arguments.of(List.of(), List.of("identifier=foo|bar"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar").build(), PolicyEnum.ALLOW, "Allow request when filters match"),
			Arguments.of(List.of(), List.of("identifier=foo|bar", "name=Doe"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar").build(), PolicyEnum.ALLOW, "Allow request with subset of permitted filters"),
			Arguments.of(List.of(), List.of("identifier=foo|bar", "name=Doe"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar", "name=Doe").build(), PolicyEnum.ALLOW, "Allow request when multiple filters match"),
			Arguments.of(List.of("Patient"), List.of("identifier=foo|bar", "name=Doe"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar", "name=Doe").withResourceTypes("Patient").build(), PolicyEnum.ALLOW, "Allow request when multiple filters match including resource type"),
			Arguments.of(List.of("Observation"), List.of("identifier=foo|bar", "name=Doe"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar", "name=Doe").withResourceTypes("Patient").build(), PolicyEnum.DENY, "Deny request when multiple filters match, but resource type doesn't"),
			Arguments.of(List.of(), List.of("identifier=foo|bar&active=true", "name=Doe"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar&active=true", "name=Doe").build(), PolicyEnum.ALLOW, "Allow request when multiple filters match"),
			Arguments.of(List.of(), List.of("identifier=foo|bar", "name=Doe"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar&active=true", "name=Doe").build(), PolicyEnum.DENY, "Deny request when filters don't match"),
			Arguments.of(List.of(), List.of("identifier=abc|def"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar").build(), PolicyEnum.DENY, "Deny request when filters do not match"),
			Arguments.of(List.of(), List.of("identifier=foo|bar"), new BulkExportParamsBuilder().withTypeFilters("identifier=foo|bar","name=Doe").build(), PolicyEnum.DENY, "Deny request when requesting more filters than permitted")
		);
	}

	private static class BulkExportParamsBuilder {

		private final BulkExportJobParameters myBulkExportJobParameters;

		public BulkExportParamsBuilder() {
			myBulkExportJobParameters = new BulkExportJobParameters();
			myBulkExportJobParameters.setExportStyle(PATIENT);
		}

		public BulkExportParamsBuilder withExportStyle(BulkExportJobParameters.ExportStyle theStyle) {
			myBulkExportJobParameters.setExportStyle(theStyle);
			return this;
		}

		public BulkExportParamsBuilder withResourceTypes(String... theResourceTypes) {
			myBulkExportJobParameters.setResourceTypes(List.of(theResourceTypes));
			return this;
		}

		public BulkExportParamsBuilder exportOnePatient() {
			myBulkExportJobParameters.setPatientIds(List.of("Patient/1"));
			return this;
		}

		public BulkExportParamsBuilder exportTwoPatients() {
			myBulkExportJobParameters.setPatientIds(List.of("Patient/1", "Patient/2"));
			return this;
		}

		public BulkExportParamsBuilder withTypeFilters(String... theFilters) {
			myBulkExportJobParameters.setFilters(List.of(theFilters));
			return this;
		}

		public BulkExportJobParameters build() {
			return myBulkExportJobParameters;
		}
	}

}
