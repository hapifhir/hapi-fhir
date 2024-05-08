package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleBulkExportImplTest {
	private RestOperationTypeEnum myOperation = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
	private Pointcut myPointcut = Pointcut.STORAGE_INITIATE_BULK_EXPORT;
	@Mock
	private RequestDetails myRequestDetails;
	@Mock
	private IRuleApplier myRuleApplier;
	@Mock
	private Set<AuthorizationFlagsEnum> myFlags;

	@Test
	public void testDenyBulkRequestWithInvalidResourcesTypes() {
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("a");

		Set<String> myTypes = new HashSet<>();
		myTypes.add("Patient");
		myTypes.add("Practitioner");
		myRule.setResourceTypes(myTypes);

		Set<String> myWantTypes = new HashSet<>();
		myWantTypes.add("Questionnaire");

		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(myWantTypes);
		
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);
		assertEquals(PolicyEnum.DENY, verdict.getDecision());
	}

	@Test
	public void testBulkRequestWithValidResourcesTypes() {
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("a");
		myRule.setAppliesToSystem();
		myRule.setMode(PolicyEnum.ALLOW);

		Set<String> myTypes = new HashSet<>();
		myTypes.add("Patient");
		myTypes.add("Practitioner");
		myRule.setResourceTypes(myTypes);

		Set<String> myWantTypes = new HashSet<>();
		myWantTypes.add("Patient");
		myWantTypes.add("Practitioner");

		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		options.setResourceTypes(myWantTypes);
		
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		assertAllow(verdict);
	}

	@Test
	public void testWrongGroupIdDelegatesToNextRule() {
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("a");
		myRule.setAppliesToGroupExportOnGroup("invalid group");
		myRule.setMode(PolicyEnum.ALLOW);

		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setGroupId("Group/123");

		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);
		assertAbstain(verdict);
	}

	@Test
	public void testAllowBulkRequestWithValidGroupId() {
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("a");
		myRule.setAppliesToGroupExportOnGroup("Group/1");
		myRule.setMode(PolicyEnum.ALLOW);

		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setGroupId("Group/1");

		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);
		assertAllow(verdict);
	}

	@Test
	public void testPatientExportRulesInBounds() {
		//Given
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setPatientIds(Set.of("Patient/123"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: We permit the request, as a patient ID that was requested is honoured by this rule.
		assertAllow(verdict);
	}

	@Test
	public void testPatientExportRulesOutOfBounds() {
		//Given
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setPatientIds(Set.of("Patient/456"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: abstain
		assertAbstain(verdict);
	}

	@Test
	public void testPatientExportRulesOnTypeLevelExport() {
		//Given
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: We make no claims about type-level export on Patient.
		assertAbstain(verdict);
	}

	@Test
	public void testPatientExportRulesWithId_withRequestNoIds_abstains() {
		//Given
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		BulkExportJobParameters options = new BulkExportJobParameters();

		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: We make no claims about type-level export on Patient.
		assertAbstain(verdict);
	}

	@Test
	public void testPatientExportRuleWithNoIds_withRequestNoIds_allows() {
		//Given
		RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExportAllPatients();
		myRule.setMode(PolicyEnum.ALLOW);
		BulkExportJobParameters options = new BulkExportJobParameters();

		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		assertAllow(verdict);
	}


	@Test
	public void testPatientExportRulesOnTypeLevelExportWithTypeFilterResourceTypePatientAndFilterHasResources() {
		//Given
		final RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setFilters(Set.of("Patient?_id=123"));
		options.setResourceTypes(Set.of("Patient", "Condition", "Immunization"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		final AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: The patient IDs match so this is permitted
		assertAbstain(verdict);
	}


	@Test
	public void testPatientExportRulesOnTypeLevelExportWithTypeFilterNoResourceType() {
		//Given
		final RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setFilters(Set.of("Patient?_id=123"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		final AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: Filters are ignored for auth purposes. The rule has an ID, indicating it is for instance level, but the job requested type level. Abstain
		assertAbstain(verdict);
	}

	@Test
	public void testPatientExportRulesOnTypeLevelExportUnpermittedPatient() {
		//Given
		final RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setPatientIds(Set.of("Patient/456"));
		options.setResourceTypes(Set.of("Patient"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		final AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: We do not have permissions on the requested patient so we abstain
		assertAbstain(verdict);
	}

	@Test
	public void testPatientExportRulesOnTypeLevelExportPermittedPatient() {
		//Given
		final RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setPatientIds(Set.of("Patient/123"));
		options.setResourceTypes(Set.of("Patient"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		final AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: We have permissions on the requested patient so this is permitted.
		assertEquals(PolicyEnum.ALLOW, verdict.getDecision());
	}

	@Test
	public void testPatientExportRulesOnTypeLevelExportPermittedPatients() {
		//Given
		final RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setAppliesToPatientExport("Patient/456");
		myRule.setMode(PolicyEnum.ALLOW);
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setPatientIds(Set.of("Patient/123", "Patient/456"));
		options.setResourceTypes(Set.of("Patient"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		final AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: We have permissions on both requested patients so this is permitted.
		assertEquals(PolicyEnum.ALLOW, verdict.getDecision());
	}

	@Test
	public void testPatientExportRulesOnTypeLevelExportWithPermittedAndUnpermittedPatients() {
		//Given
		final RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setPatientIds(Set.of("Patient/123","Patient/456"));
		options.setResourceTypes(Set.of("Patient"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		final AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: There are unpermitted patients in the request so this is not permitted.
		assertAbstain(verdict);
	}
	@Test
	public void testPatientExportRulesOnTypeLevelExportWithPermittedAndUnpermittedPatientFilters() {
		//Given
		final RuleBulkExportImpl myRule = new RuleBulkExportImpl("b");
		myRule.setAppliesToPatientExport("Patient/123");
		myRule.setMode(PolicyEnum.ALLOW);
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
		options.setFilters(Set.of("Patient?_id=123","Patient?_id=456"));
		options.setResourceTypes(Set.of("Patient"));
		when(myRequestDetails.getAttribute(any())).thenReturn(options);

		//When
		final AuthorizationInterceptor.Verdict verdict = myRule.applyRule(myOperation, myRequestDetails, null, null, null, myRuleApplier, myFlags, myPointcut);

		//Then: There are unpermitted patients in the request so this is not permitted. abstain.
		assertAbstain(verdict);

	}

	private static void assertAbstain(AuthorizationInterceptor.Verdict verdict) {
		Assertions.assertEquals(null, verdict, "Expect abstain");
	}

	private static void assertAllow(AuthorizationInterceptor.Verdict verdict) {
		Assertions.assertNotNull(verdict, "Expect ALLOW, got abstain");
		Assertions.assertEquals(PolicyEnum.ALLOW, verdict.getDecision(), "Expect ALLOW");
	}
}
