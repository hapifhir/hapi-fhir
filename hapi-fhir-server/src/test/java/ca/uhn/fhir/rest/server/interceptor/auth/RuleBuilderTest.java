package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.model.primitive.IdDt;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
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

		assertEquals(2, list.size());

		assertEquals(RuleImplOp.class, list.get(0).getClass());
		RuleImplOp allowRead = (RuleImplOp) list.get(0);
		assertThat(allowRead.getAppliesToInstances(), contains(
			new IdDt("Patient/READ-1"),
			new IdDt("Patient/READ-2"),
			new IdDt("Patient/READ-3"),
			new IdDt("Patient/READ-4")
		));

		assertEquals(RuleImplOp.class, list.get(1).getClass());
		RuleImplOp allowWrite = (RuleImplOp) list.get(1);
		assertThat(allowWrite.getAppliesToInstances(), contains(
			new IdDt("Patient/WRITE-1"),
			new IdDt("Patient/WRITE-2"),
			new IdDt("Patient/WRITE-3"),
			new IdDt("Patient/WRITE-4")
		));
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
	public void testNullConditional() {
		IAuthRuleBuilder ruleBuilder = new RuleBuilder().allow().metadata().andThen();
		IAuthRuleTester writeAccessTester = mock(IAuthRuleTester.class);
		ruleBuilder.allow().createConditional().resourcesOfType("anystring").withTester(writeAccessTester).andThen();
	}

}
