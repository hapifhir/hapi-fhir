package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;

import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters.ExportStyle;

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

import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RuleGroupBulkExportByCompartmentMatcherImplTest {
	@Mock
	private IRuleApplier myRuleApplier;
	@Mock
	private IAuthResourceResolver myAuthResourceResolver;
	@Mock
	private IBaseResource myResource;
	@Mock
	private IAuthorizationSearchParamMatcher mySearchParamMatcher;
	@Mock
	private RequestDetails myRequestDetails;

	@ParameterizedTest
	@MethodSource("params")
	void testGroupRule_withCompartmentMatchers(IAuthorizationSearchParamMatcher.MatchResult theSearchParamMatcherMatchResult, Collection<String> theAllowedResourceTypes, ExportStyle theExportStyle, Collection<String> theRequestedResourceTypes, PolicyEnum theExpectedVerdict, String theMessage) {
		RuleGroupBulkExportByCompartmentMatcherImpl rule = new RuleGroupBulkExportByCompartmentMatcherImpl("b");
		rule.setAppliesToGroupExportOnGroup("identifier=foo|bar");
		rule.setResourceTypes(theAllowedResourceTypes);
		rule.setMode(PolicyEnum.ALLOW);

		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(theExportStyle);
		options.setResourceTypes(theRequestedResourceTypes);
		options.setGroupId("Group/G1");

		when(myRequestDetails.getUserData()).thenReturn(Map.of(AuthorizationInterceptor.REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS, options));

		if (theSearchParamMatcherMatchResult != null) {
			when(myRuleApplier.getAuthResourceResolver()).thenReturn(myAuthResourceResolver);
			when(myAuthResourceResolver.resolveResourceById(any())).thenReturn(myResource);
			when(myRuleApplier.getSearchParamMatcher()).thenReturn(mySearchParamMatcher);
			when(myResource.fhirType()).thenReturn("Group");
			when(mySearchParamMatcher.match("Group?identifier=foo|bar", myResource)).thenReturn(theSearchParamMatcherMatchResult);
		}

		AuthorizationInterceptor.Verdict verdict = rule.applyRule(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER, myRequestDetails, null, null, null, myRuleApplier, Set.of(), Pointcut.STORAGE_INITIATE_BULK_EXPORT);

		if (theExpectedVerdict != null) {
			// Expect a decision
			Assertions.assertNotNull(verdict, "Expected " + theExpectedVerdict + " but got abstain - " + theMessage);
			assertEquals(theExpectedVerdict, verdict.getDecision(), "Expected " + theExpectedVerdict + " but got " + verdict.getDecision() + " - " + theMessage);
		} else {
			// Expect abstain
			assertNull(verdict, "Expected abstain - " + theMessage);
		}
	}

	static Stream<Arguments> params() {
		IAuthorizationSearchParamMatcher.MatchResult match = IAuthorizationSearchParamMatcher.MatchResult.buildMatched();
		IAuthorizationSearchParamMatcher.MatchResult noMatch = IAuthorizationSearchParamMatcher.MatchResult.buildUnmatched();

		return Stream.of(
			// theSearchParamMatcherMatchResult, theAllowedResourceTypes, theExportStyle, theRequestedResourceTypes, theExpectedDecision, theMessage
			Arguments.of(match, List.of(), ExportStyle.GROUP, List.of(), PolicyEnum.ALLOW, "Allow request for all types, allow all types"),
			Arguments.of(match, List.of(), ExportStyle.GROUP, List.of("Patient", "Observation"), PolicyEnum.ALLOW, "Allow request for some types, allow all types"),
			Arguments.of(match, List.of("Patient", "Observation"), ExportStyle.GROUP, List.of("Patient", "Observation"), PolicyEnum.ALLOW, "Allow request for exact set of allowable types"),
			Arguments.of(match, List.of("Patient", "Observation"), ExportStyle.GROUP, List.of("Patient"), PolicyEnum.ALLOW, "Allow request for subset of allowable types"),
			Arguments.of(noMatch, List.of("Patient", "Observation"), ExportStyle.GROUP, List.of("Patient", "Observation"), null, "Abstain when requesting some resource types, but no resources match the permission query"),
			Arguments.of(noMatch, List.of(), ExportStyle.GROUP, List.of(), null, "Abstain when requesting all resource types, but no resources match the permission query"),
			// The case below is the narrowing case. Narrowing should happen at the SecurityInterceptor layer
			Arguments.of(null, List.of("Patient", "Observation"), ExportStyle.GROUP, List.of(), PolicyEnum.DENY, "Deny request for all types when allowing some types"),
			Arguments.of(null, List.of("Patient", "Observation"), ExportStyle.GROUP, List.of("Patient", "Observation", "Encounter"), PolicyEnum.DENY, "Deny request for superset of allowable types"),
			Arguments.of(null, List.of("Patient", "Observation"), ExportStyle.PATIENT, List.of(), null, "Abstain when export style is not Group")
		);
	}
}
