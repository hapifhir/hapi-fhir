package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionCriteriaParserTest {

	@Test
	public void testSearchExpression() {
		String expression = "Patient?foo=bar";
		SubscriptionCriteriaParser.SubscriptionCriteria criteria = SubscriptionCriteriaParser.parse(expression);
		assertEquals(SubscriptionCriteriaParser.TypeEnum.SEARCH_EXPRESSION, criteria.getType());
		assertEquals(expression, criteria.getCriteria());
		assertThat(criteria.getApplicableResourceTypes(), containsInAnyOrder("Patient"));
		assertEquals("SubscriptionCriteriaParser.SubscriptionCriteria[type=SEARCH_EXPRESSION,criteria=Patient?foo=bar,applicableResourceTypes=[Patient]]", criteria.toString());
	}

	@Test
	public void testTypeExpression() {
		String expression = "Patient";
		SubscriptionCriteriaParser.SubscriptionCriteria criteria = SubscriptionCriteriaParser.parse(expression);
		assertEquals(SubscriptionCriteriaParser.TypeEnum.SEARCH_EXPRESSION, criteria.getType());
		assertEquals(expression, criteria.getCriteria());
		assertThat(criteria.getApplicableResourceTypes(), containsInAnyOrder("Patient"));
		assertEquals("SubscriptionCriteriaParser.SubscriptionCriteria[type=SEARCH_EXPRESSION,criteria=Patient,applicableResourceTypes=[Patient]]", criteria.toString());
	}

	@Test
	public void testStarExpression() {
		String expression = "[*]";
		SubscriptionCriteriaParser.SubscriptionCriteria criteria = SubscriptionCriteriaParser.parse(expression);
		assertEquals(SubscriptionCriteriaParser.TypeEnum.STARTYPE_EXPRESSION, criteria.getType());
		assertEquals(null, criteria.getCriteria());
		assertEquals(null, criteria.getApplicableResourceTypes());
		assertEquals("SubscriptionCriteriaParser.SubscriptionCriteria[type=STARTYPE_EXPRESSION]", criteria.toString());
	}

	@Test
	public void testMultitypeExpression() {
		String expression = "[Patient   , Observation]";
		SubscriptionCriteriaParser.SubscriptionCriteria criteria = SubscriptionCriteriaParser.parse(expression);
		assertEquals(SubscriptionCriteriaParser.TypeEnum.MULTITYPE_EXPRESSION, criteria.getType());
		assertEquals(null, criteria.getCriteria());
		assertThat(criteria.getApplicableResourceTypes(), containsInAnyOrder("Patient", "Observation"));
		assertEquals("SubscriptionCriteriaParser.SubscriptionCriteria[type=MULTITYPE_EXPRESSION,applicableResourceTypes=[Observation, Patient]]", criteria.toString());
	}

	@Test
	public void testInvalidExpression() {
		assertNull(SubscriptionCriteriaParser.parse("[]"));
		assertNull(SubscriptionCriteriaParser.parse(""));
		assertNull(SubscriptionCriteriaParser.parse(null));
		assertNull(SubscriptionCriteriaParser.parse(" "));
		assertNull(SubscriptionCriteriaParser.parse("#123"));
	}

}
