package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

public class RuleBuilderTest {

	/**
	 * If the user creates multiple rules that allow read/write of individual
	 * instances, we will collapse these into a single rule for performance
	 */
	@Test
	public void testCollapseReadInstancesIntoSingleRule() {
		RuleBuilder builder = new RuleBuilder();
		builder.allow().read().instance(new IdDt("Patient/READ-1"));
		builder.allow().write().instance(new IdDt("Patient/WRITE-1"));
		builder.allow().read().instance(new IdDt("Patient/READ-2"));
		builder.allow().write().instance(new IdDt("Patient/WRITE-2"));
		builder.allow().read().instances(Lists.newArrayList(new IdDt("Patient/READ-3"), new IdDt("Patient/READ-4")));
		builder.allow().write().instances(Lists.newArrayList(new IdDt("Patient/WRITE-3"), new IdDt("Patient/WRITE-4")));
		List<IAuthRule> list = builder.build();

		assertThat(list).hasSize(2);

		assertEquals(RuleImplOp.class, list.get(0).getClass());
		RuleImplOp allowRead = (RuleImplOp) list.get(0);
		assertThat(allowRead.getAppliesToInstances()).containsExactly(new IdDt("Patient/READ-1"), new IdDt("Patient/READ-2"), new IdDt("Patient/READ-3"), new IdDt("Patient/READ-4"));

		assertEquals(RuleImplOp.class, list.get(1).getClass());
		RuleImplOp allowWrite = (RuleImplOp) list.get(1);
		assertThat(allowWrite.getAppliesToInstances()).containsExactly(new IdDt("Patient/WRITE-1"), new IdDt("Patient/WRITE-2"), new IdDt("Patient/WRITE-3"), new IdDt("Patient/WRITE-4"));
	}

	@Test
	public void testInCompartment_withMultipleReadInstances_collapsesIntoASingleRule() {
		RuleBuilder builder = new RuleBuilder();
		builder.allow().read().allResources().inCompartment("Patient", new IdDt("Patient/lob1patient"));
		builder.allow().read().allResources().inCompartment("Patient", new IdDt("Patient/lob2patient"));
		List<IAuthRule> list = builder.build();

		assertThat(list).hasSize(1);

		assertEquals(RuleImplOp.class, list.get(0).getClass());
	}

	@Test
	public void testInCompartmentWithFilter_withMultipleReadInstances_doesNotCollapseRules() {
		RuleBuilder builder = new RuleBuilder();
		builder.allow().read().allResources().inCompartmentWithFilter("Patient", new IdDt("Patient/lob1patient"), "code=foo");
		builder.allow().read().allResources().inCompartmentWithFilter("Patient", new IdDt("Patient/lob2patient"), "code=bar");
		List<IAuthRule> list = builder.build();

		assertThat(list).hasSize(2);

		assertEquals(RuleImplOp.class, list.get(0).getClass());
	}

	@Test
	public void testBulkExportPermitsIfASingleGroupMatches() {
		RuleBuilder builder = new RuleBuilder();
		List<String> resourceTypes = new ArrayList<>();
		resourceTypes.add("Patient");
		resourceTypes.add("Organization");

		builder.allow().bulkExport().groupExportOnGroup("group1").withResourceTypes(resourceTypes);
		builder.allow().bulkExport().groupExportOnGroup("group2").withResourceTypes(resourceTypes);
		List<IAuthRule> build = builder.build();

	}

	@Test
	public void testBulkExport_PatientExportOnPatient_MultiplePatientsSingleRule() {
		RuleBuilder builder = new RuleBuilder();
		List<String> resourceTypes = new ArrayList<>();
		resourceTypes.add("Patient");

		builder.allow().bulkExport().patientExportOnPatient("Patient/pat1").withResourceTypes(resourceTypes);
		builder.allow().bulkExport().patientExportOnPatient("Patient/pat2").withResourceTypes(resourceTypes);
		List<IAuthRule> rules = builder.build();
		assertEquals(rules.size(), 1);
		assertInstanceOf(RuleBulkExportImpl.class, rules.get(0));
	}

	public static Stream<Arguments> multipleInstancesParams() {
		return Stream.of(
			Arguments.of(List.of("Patient/pat1"), List.of("Patient"), PolicyEnum.ALLOW),
			Arguments.of(List.of("Patient/pat1", "Patient/pat2"), List.of("Patient"), PolicyEnum.ALLOW),
			Arguments.of(List.of("Patient/pat1", "Patient/pat2"), List.of("Patient", "Observation"), PolicyEnum.ALLOW),
			Arguments.of(List.of("Patient/pat1"), List.of("Patient"), PolicyEnum.DENY),
			Arguments.of(List.of("Patient/pat1", "Patient/pat2"), List.of("Patient"), PolicyEnum.DENY),
			Arguments.of(List.of("Patient/pat1", "Patient/pat2"), List.of("Patient", "Observation"), PolicyEnum.DENY)
		);
	}

	@ParameterizedTest
	@MethodSource("multipleInstancesParams")
	public void testBulkExport_PatientExportOnPatients_MultiplePatientsSingleRule(Collection<String> theExpectedPatientIds, Collection<String> theExpectedResourceTypes, PolicyEnum thePolicyEnum) {
		final RuleBuilder builder = new RuleBuilder();
		final IAuthRuleBuilderRule rule = switch (thePolicyEnum) {
			case ALLOW -> builder.allow();
			case DENY -> builder.deny();
		};
		rule.bulkExport().patientExportOnPatientStrings(theExpectedPatientIds).withResourceTypes(theExpectedResourceTypes);
		final List<IAuthRule> rules = builder.build();
		assertEquals(rules.size(),1);
		final IAuthRule authRule = rules.get(0);
		assertInstanceOf(RuleBulkExportImpl.class, authRule);
		final RuleBulkExportImpl ruleBulkExport = (RuleBulkExportImpl) authRule;
		assertEquals(theExpectedPatientIds, ruleBulkExport.getPatientIds());
		assertEquals(theExpectedResourceTypes, ruleBulkExport.getResourceTypes());
		assertEquals(thePolicyEnum, ruleBulkExport.getMode());
	}

	public static Stream<Arguments> owners() {
		return Stream.of(
			Arguments.of(List.of(new IdDt("Patient/pat1")), PolicyEnum.ALLOW),
			Arguments.of(List.of(new IdDt("Patient/pat1")), PolicyEnum.DENY),
			Arguments.of(List.of(new IdDt("Patient/pat1"), new IdDt("Patient/pat2")), PolicyEnum.ALLOW),
			Arguments.of(List.of(new IdDt("Patient/pat1"), new IdDt("Patient/pat2")), PolicyEnum.DENY)
		);
	}

	@ParameterizedTest
	@MethodSource("owners")
	public void testBulkExport_PatientExportOnPatients_onInstances(List<IIdType> theExpectedOwners, PolicyEnum thePolicyEnum) {
		final RuleBuilder builder = new RuleBuilder();
		final IAuthRuleBuilderRule rule = switch (thePolicyEnum) {
			case ALLOW -> builder.allow();
			case DENY -> builder.deny();
		};

		final List<IAuthRule> rules = rule
			.operation()
			.named(ProviderConstants.OPERATION_EXPORT)
			.onInstances(theExpectedOwners)
			.andAllowAllResponses()
			.andThen()
			.build();

		assertEquals(rules.size(),1);
		final IAuthRule authRule = rules.get(0);
		assertInstanceOf(OperationRule.class, authRule);
		final OperationRule operationRule = (OperationRule) authRule;
		assertEquals(theExpectedOwners, operationRule.getAppliesToIds());
		assertEquals(ProviderConstants.OPERATION_EXPORT, operationRule.getOperationName());
		assertEquals(thePolicyEnum, operationRule.getMode());
	}

	@Test
	public void testNullConditional() {
		IAuthRuleBuilder ruleBuilder = new RuleBuilder().allow().metadata().andThen();
		IAuthRuleTester writeAccessTester = mock(IAuthRuleTester.class);
		ruleBuilder.allow().createConditional().resourcesOfType("anystring").withTester(writeAccessTester).andThen();
	}

}
