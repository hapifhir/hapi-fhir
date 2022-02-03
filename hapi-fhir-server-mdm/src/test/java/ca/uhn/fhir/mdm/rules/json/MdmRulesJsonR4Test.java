package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.similarity.MdmSimilarityEnum;
import ca.uhn.fhir.mdm.rules.svc.BaseMdmRulesR4Test;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
	public void testValidate() throws IOException {
		MdmRulesJson rules = new MdmRulesJson();
		try {
			JsonUtil.serialize(rules);
		} catch (NullPointerException e) {
			assertThat(e.getMessage(), containsString("version may not be blank"));
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
		assertEquals(MdmSimilarityEnum.JARO_WINKLER, second.getSimilarity().getAlgorithm());
	}

	@Test
	public void testMatchResultMap() {
		assertEquals(MdmMatchResultEnum.MATCH, myRules.getMatchResult(3L));
	}

	@Test
	public void getVector() {
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
	public void testInvalidResourceTypeDoesntDeserialize() throws IOException {
		myRules = buildOldStyleEidRules();

		String eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Patient");
		assertThat(eidSystem, is(equalTo(PATIENT_EID_FOR_TEST)));

		eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Practitioner");
		assertThat(eidSystem, is(equalTo(PATIENT_EID_FOR_TEST)));

		eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Medication");
		assertThat(eidSystem, is(equalTo(PATIENT_EID_FOR_TEST)));
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
