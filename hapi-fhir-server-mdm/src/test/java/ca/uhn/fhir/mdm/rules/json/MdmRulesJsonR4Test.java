package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.matcher.util.MatchRuleUtil;
import ca.uhn.fhir.mdm.rules.similarity.MdmSimilarityEnum;
import ca.uhn.fhir.mdm.rules.svc.BaseMdmRulesR4Test;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class MdmRulesJsonR4Test extends BaseMdmRulesR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmRulesJsonR4Test.class);
	private MdmRulesJson myRules;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		myRules = buildActiveBirthdateIdRules();
	}

	@Test
	void eidSystems_withArrayValues_deserializesAllSystemsInDeclaredOrder() throws IOException {
		MdmRulesJson rules = deserializeRulesWithEidSystems(
			"""
			{"Patient": ["http://example.com/mrn", "http://example.com/npi"]}""");

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient"))
			.containsExactly("http://example.com/mrn", "http://example.com/npi");
	}

	@Test
	void eidSystems_withScalarValue_escalatesToSingletonList() throws IOException {
		MdmRulesJson rules = deserializeRulesWithEidSystems(
			"""
			{"Patient": "http://example.com/mrn"}""");

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient"))
			.containsExactly("http://example.com/mrn");
	}

	@Test
	void eidSystems_mixedScalarAndArray_bothFormsDeserialize() throws IOException {
		MdmRulesJson rules = deserializeRulesWithEidSystems(
			"""
			{"Patient": ["http://example.com/mrn", "http://example.com/npi"], "Practitioner": "http://example.com/npi"}""");

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient"))
			.containsExactly("http://example.com/mrn", "http://example.com/npi");
		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Practitioner"))
			.containsExactly("http://example.com/npi");
	}

	@Test
	void eidSystems_wildcardWithArray_appliesToEveryResourceType() throws IOException {
		MdmRulesJson rules = deserializeRulesWithEidSystems(
			"""
			{"*": ["http://example.com/mrn", "http://example.com/npi"]}""");

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient"))
			.containsExactly("http://example.com/mrn", "http://example.com/npi");
		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Practitioner"))
			.containsExactly("http://example.com/mrn", "http://example.com/npi");
	}

	@Test
	void getEnterpriseEIDSystemsForResourceType_unconfiguredType_returnsEmptyList() throws IOException {
		MdmRulesJson rules = deserializeRulesWithEidSystems(
			"""
			{"Patient": ["http://example.com/mrn"]}""");

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Practitioner")).isEmpty();
	}

	@Test
	void getEnterpriseEIDSystemsForResourceType_noEidSystemsConfigured_returnsEmptyList() {
		MdmRulesJson rules = new MdmRulesJson();

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient")).isEmpty();
	}

	/**
	 * Pins a consumer-visible change of format. A resource type mapped to a single EID system used to be
	 * written back out as a bare string, and is now always written back as a one-element array. Anything
	 * that re-serializes a rules document will see the new shape; both shapes still deserialize.
	 */
	@Test
	void eidSystems_serializesAsAnArrayEvenWhenOneSystemIsConfigured() {
		MdmRulesJson rules = buildActiveBirthdateIdRules();
		rules.addEnterpriseEIDSystems("Patient", List.of("http://example.com/mrn"));

		String json = JsonUtil.serialize(rules).replaceAll("\\s+", "");

		assertThat(json).contains("\"Patient\":[\"http://example.com/mrn\"]");
	}

	@Test
	void addEnterpriseEIDSystem_calledTwiceForOneType_appendsRatherThanReplaces() {
		MdmRulesJson rules = new MdmRulesJson();
		rules.addEnterpriseEIDSystem("Patient", "http://example.com/mrn");
		rules.addEnterpriseEIDSystem("Patient", "http://example.com/npi");

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient"))
			.containsExactly("http://example.com/mrn", "http://example.com/npi");
	}

	@Test
	void addEnterpriseEIDSystem_calledTwiceWithSameSystem_doesNotDuplicate() {
		MdmRulesJson rules = new MdmRulesJson();
		rules.addEnterpriseEIDSystem("Patient", "http://example.com/mrn");
		rules.addEnterpriseEIDSystem("Patient", "http://example.com/mrn");

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient"))
			.containsExactly("http://example.com/mrn");
	}

	@Test
	void getEnterpriseEIDSystemForResourceType_withMultipleSystems_returnsFirstConfigured() {
		MdmRulesJson rules = new MdmRulesJson();
		rules.addEnterpriseEIDSystems("Patient", List.of("http://example.com/mrn", "http://example.com/npi"));

		assertEquals("http://example.com/mrn", rules.getEnterpriseEIDSystemForResourceType("Patient"));
	}

	@Test
	void getEnterpriseEIDSystems_withMultipleSystems_returnsFirstSystemPerResourceType() {
		MdmRulesJson rules = new MdmRulesJson();
		rules.addEnterpriseEIDSystems("Patient", List.of("http://example.com/mrn", "http://example.com/npi"));
		rules.addEnterpriseEIDSystems("Practitioner", List.of("http://example.com/npi"));

		assertThat(rules.getEnterpriseEIDSystems())
			.containsEntry("Patient", "http://example.com/mrn")
			.containsEntry("Practitioner", "http://example.com/npi");
	}

	@Test
	void setEnterpriseEIDSystems_withScalarMap_escalatesEachValueToSingletonList() {
		MdmRulesJson rules = new MdmRulesJson();
		rules.setEnterpriseEIDSystems(Map.of("Patient", "http://example.com/mrn"));

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient"))
			.containsExactly("http://example.com/mrn");
	}

	/**
	 * The legacy singular {@code eidSystem} property is scoped to all resource types, so it must escalate
	 * to a one-element list under the wildcard key.
	 */
	@Test
	void legacyScalarEidSystem_escalatesToSingletonListForEveryResourceType() {
		MdmRulesJson rules = buildOldStyleEidRules();

		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Patient")).containsExactly(PATIENT_EID_FOR_TEST);
		assertThat(rules.getEnterpriseEIDSystemsForResourceType("Medication")).containsExactly(PATIENT_EID_FOR_TEST);
	}

	@Test
	public void testValidate() throws IOException {
		MdmRulesJson rules = new MdmRulesJson();
		try {
			JsonUtil.serialize(rules);
		} catch (NullPointerException e) {
			assertThat(e.getMessage()).contains("version may not be blank");
		}
	}

	@Test
	public void testSerDeser() throws IOException {
		String json = JsonUtil.serialize(myRules);
		ourLog.info(json);
		MdmRulesJson rulesDeser = JsonUtil.deserialize(json, MdmRulesJson.class);
		assertEquals(2, rulesDeser.size());
		assertEquals(MdmMatchResultEnum.MATCH, rulesDeser.getMatchResult(myBothNameFields));
		MdmFieldMatchJson second = rulesDeser.get(1);
		assertEquals("name.family", second.getResourcePath());
		assertEquals(MdmSimilarityEnum.JARO_WINKLER.name(), second.getSimilarity().getAlgorithm());
	}

	@Test
	public void testMatchResultMap() {
		assertEquals(MdmMatchResultEnum.MATCH, myRules.getMatchResult(3L));
	}

	@Test
	public void getVector_basicTest() {
		VectorMatchResultMap vectorMatchResultMap = myRules.getVectorMatchResultMapForUnitTest();
		assertEquals(1, vectorMatchResultMap.getVector(PATIENT_GIVEN));
		assertEquals(2, vectorMatchResultMap.getVector(PATIENT_FAMILY));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(",", PATIENT_GIVEN, PATIENT_FAMILY)));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(", ", PATIENT_GIVEN, PATIENT_FAMILY)));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(",  ", PATIENT_GIVEN, PATIENT_FAMILY)));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(", \n ", PATIENT_GIVEN, PATIENT_FAMILY)));
		try {
			vectorMatchResultMap.getVector("bad");
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1523) + "There is no matchField with name bad", e.getMessage());
		}
	}

	@Test
	public void validate_withTooManyFields_throws() {
		// setup
		MdmRulesJson rules = new MdmRulesJson();
		rules.setVersion("1");

		// we don't need real rules; just one that will hit our validate code correctly
		for (int i = 0; i < MatchRuleUtil.MAX_RULE_COUNT + 1; i++) {
			MdmFieldMatchJson fieldMatchJson = new MdmFieldMatchJson();
			fieldMatchJson.setName("field_" + i);
			rules.addMatchField(fieldMatchJson);
		}

		// test
		try {
			rules.initialize();
			fail(String.format("We currently only handle up to %s rules", MatchRuleUtil.MAX_RULE_COUNT));
		} catch (IllegalArgumentException ex) {
			assertEquals("MDM cannot guarantee accuracy with more than 64 match fields.", ex.getLocalizedMessage(), ex.getLocalizedMessage());
		}
	}

	@Test
	public void testInvalidResourceTypeDoesntDeserialize() throws IOException {
		myRules = buildOldStyleEidRules();

		String eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Patient");
		assertEquals(PATIENT_EID_FOR_TEST, eidSystem);

		eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Practitioner");
		assertEquals(PATIENT_EID_FOR_TEST, eidSystem);

		eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Medication");
		assertEquals(PATIENT_EID_FOR_TEST, eidSystem);
	}

	private MdmRulesJson deserializeRulesWithEidSystems(String theEidSystemsBlock) {
		String json =
			"""
			{
				"version": "1",
				"mdmTypes": ["Patient", "Practitioner"],
				"candidateSearchParams": [],
				"candidateFilterSearchParams": [],
				"matchFields": [],
				"matchResultMap": {},
				"eidSystems": %s
			}"""
				.formatted(theEidSystemsBlock);
		return JsonUtil.deserialize(json, MdmRulesJson.class);
	}

	@Override
	protected MdmRulesJson buildActiveBirthdateIdRules() {
		return super.buildActiveBirthdateIdRules();
	}

	private MdmRulesJson buildOldStyleEidRules() {
		MdmRulesJson mdmRulesJson = super.buildActiveBirthdateIdRules();
		mdmRulesJson.setEnterpriseEIDSystems(Collections.emptyMap());
		//This sets the new-style eid resource type to `*`
		mdmRulesJson.setEnterpriseEIDSystem(PATIENT_EID_FOR_TEST);
		return mdmRulesJson;
	}

}
