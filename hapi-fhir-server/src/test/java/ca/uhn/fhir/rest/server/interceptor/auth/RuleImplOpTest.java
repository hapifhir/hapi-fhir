package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class RuleImplOpTest {

	public static final String COMPARTMENT_NAME = "Patient";
	private static final ClassifierTypeEnum CLASSIFIER_TYPE = ClassifierTypeEnum.IN_COMPARTMENT;

	@Test
	public void testToString() {
		assertEquals("RuleImplOp[testers=<null>,op=<null>,transactionAppliesToOp=<null>,appliesTo=<null>,appliesToTypes=<null>,classifierCompartmentName=<null>,classifierCompartmentOwners=<null>,classifierType=<null>]", new RuleImplOp("").toString());
	}

	@Test
	public void testMatchesTypes() {
		RuleImplOp aRuleOp = new RuleImplOp("a");
		aRuleOp.setOp(RuleOpEnum.READ);
		aRuleOp.setAppliesTo(AppliesTypeEnum.TYPES);
		aRuleOp.setClassifierType(CLASSIFIER_TYPE);
		Set<String> types = new HashSet<>();
		types.add("ABC");
		types.add("DEF");
		aRuleOp.setAppliesToTypes(types);
		aRuleOp.setClassifierCompartmentName(COMPARTMENT_NAME);

		Set<String> matchTypes = new HashSet<>();
		matchTypes.add("ABC");
		matchTypes.add("DEF");

		Set<String> noMatchTypes = new HashSet<>();
		noMatchTypes.add("ABC");
		noMatchTypes.add("XYZ");

		assertTrue(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.TYPES, Collections.emptyList(), matchTypes, CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.TYPES, Collections.emptyList(), noMatchTypes, CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.TYPES, Collections.emptyList(), Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.WRITE, AppliesTypeEnum.TYPES, Collections.emptyList(), matchTypes, CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.INSTANCES, Collections.emptyList(), matchTypes, CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.TYPES, Collections.emptyList(), matchTypes, CLASSIFIER_TYPE, "Observation"));
	}

	@Test
	public void testMatchesInstances() {
		RuleImplOp aRuleOp = new RuleImplOp("a");
		aRuleOp.setOp(RuleOpEnum.READ);
		aRuleOp.setAppliesTo(AppliesTypeEnum.INSTANCES);
		aRuleOp.setClassifierType(CLASSIFIER_TYPE);
		List<IIdType> instances = new ArrayList<>();
		instances.add(new IdDt("ABC"));
		instances.add(new IdDt("DEF"));
		aRuleOp.setAppliesToInstances(instances);
		aRuleOp.setClassifierCompartmentName(COMPARTMENT_NAME);

		List<IIdType> matchInstances = new ArrayList<>();
		matchInstances.add(new IdDt("ABC"));
		matchInstances.add(new IdDt("DEF"));

		List<IIdType> noMatchInstances = new ArrayList<>();
		noMatchInstances.add(new IdDt("ABC"));
		noMatchInstances.add(new IdDt("XYZ"));

		assertTrue(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.INSTANCES, matchInstances, Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.INSTANCES, noMatchInstances, Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.INSTANCES, Collections.emptyList(), Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.WRITE, AppliesTypeEnum.INSTANCES, matchInstances, Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.TYPES, matchInstances, Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.INSTANCES, matchInstances, Collections.emptySet(), CLASSIFIER_TYPE, "Observation"));
	}

	@Test
	public void testMatchesAllResources() {
		RuleImplOp aRuleOp = new RuleImplOp("a");
		aRuleOp.setOp(RuleOpEnum.READ);
		aRuleOp.setAppliesTo(AppliesTypeEnum.ALL_RESOURCES);
		aRuleOp.setClassifierType(CLASSIFIER_TYPE);
		aRuleOp.setClassifierCompartmentName("Patient");

		assertTrue(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.ALL_RESOURCES, Collections.emptyList(), Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.WRITE, AppliesTypeEnum.ALL_RESOURCES, Collections.emptyList(), Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.TYPES, Collections.emptyList(), Collections.emptySet(), CLASSIFIER_TYPE, "Patient"));
		assertFalse(aRuleOp.matches(RuleOpEnum.READ, AppliesTypeEnum.ALL_RESOURCES, Collections.emptyList(), Collections.emptySet(), CLASSIFIER_TYPE, "Observation"));
	}

	@Test
	public void testApplyRule_transactionEntryWithMetaAddOperation() {
		// arrange
		RuleImplOp rule = new RuleImplOp("rule");
		rule.setOp(RuleOpEnum.TRANSACTION);

		IBaseBundle transactionBundle = mock(IBaseBundle.class);
		IBaseResource metaAddPayload = mock(IBaseResource.class);

		IFhirVersion fhirVersion = mock(IFhirVersion.class);
		when(fhirVersion.newIdType()).thenReturn(new IdDt());
		FhirContext ctx = mock(FhirContext.class);
		when(ctx.getResourceType(transactionBundle)).thenReturn("Bundle");
		when(ctx.getVersion()).thenReturn(fhirVersion);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(ctx);

		BundleEntryParts metaAddEntry = new BundleEntryParts(
				null, RequestTypeEnum.POST, "Patient/p1/$meta-add", metaAddPayload, null, RequestTypeEnum.POST);

		// The instance-level authorization of Patient/p1 is delegated back to the rule applier; stub it to ALLOW.
		IRuleApplier ruleApplier = mock(IRuleApplier.class);
		AuthorizationInterceptor.Verdict allow = new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, rule);
		when(ruleApplier.applyRulesAndReturnDecision(any(), any(), any(), any(), any(), any()))
				.thenReturn(allow);

		try (MockedStatic<BundleUtil> bundleUtil = mockStatic(BundleUtil.class)) {
			bundleUtil.when(() -> BundleUtil.getBundleType(ctx, transactionBundle)).thenReturn("transaction");
			bundleUtil.when(() -> BundleUtil.toListOfEntries(ctx, transactionBundle))
					.thenReturn(List.of(metaAddEntry));

			// act
			AuthorizationInterceptor.Verdict verdict = rule.applyRule(
					RestOperationTypeEnum.TRANSACTION,
					requestDetails,
					transactionBundle,
					null,
					null,
					ruleApplier,
					EnumSet.noneOf(AuthorizationFlagsEnum.class),
					Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED);

			// assert
			assertThat(verdict).isNotNull();
			assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.ALLOW);
		}
	}
}
