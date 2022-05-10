package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RuleImplOpTest {

	public static final String COMPARTMENT_NAME = "Patient";
	private static final ClassifierTypeEnum CLASSIFIER_TYPE = ClassifierTypeEnum.IN_COMPARTMENT;

	@Test
	public void testToString() {
		assertEquals("RuleImplOp[op=<null>,transactionAppliesToOp=<null>,appliesTo=<null>,appliesToTypes=<null>,classifierCompartmentName=<null>,classifierCompartmentOwners=<null>,classifierType=<null>]", new RuleImplOp("").toString());
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
}
