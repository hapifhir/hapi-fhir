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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


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
			assertThat(e.getMessage()).contains("version may not be blank");
		}
	}

	@Test
	public void testSerDeser() throws IOException {
		String json = JsonUtil.serialize(myRules);
		ourLog.info(json);
		MdmRulesJson rulesDeser = JsonUtil.deserialize(json, MdmRulesJson.class);
		assertThat(rulesDeser.size()).isEqualTo(2);
		assertThat(rulesDeser.getMatchResult(myBothNameFields)).isEqualTo(MdmMatchResultEnum.MATCH);
		MdmFieldMatchJson second = rulesDeser.get(1);
		assertThat(second.getResourcePath()).isEqualTo("name.family");
		assertThat(second.getSimilarity().getAlgorithm()).isEqualTo(MdmSimilarityEnum.JARO_WINKLER);
	}

	@Test
	public void testMatchResultMap() {
		assertThat(myRules.getMatchResult(3L)).isEqualTo(MdmMatchResultEnum.MATCH);
	}

	@Test
	public void getVector() {
		VectorMatchResultMap vectorMatchResultMap = myRules.getVectorMatchResultMapForUnitTest();
		assertThat(vectorMatchResultMap.getVector(PATIENT_GIVEN)).isEqualTo(1);
		assertThat(vectorMatchResultMap.getVector(PATIENT_FAMILY)).isEqualTo(2);
		assertThat(vectorMatchResultMap.getVector(String.join(",", PATIENT_GIVEN, PATIENT_FAMILY))).isEqualTo(3);
		assertThat(vectorMatchResultMap.getVector(String.join(", ", PATIENT_GIVEN, PATIENT_FAMILY))).isEqualTo(3);
		assertThat(vectorMatchResultMap.getVector(String.join(",  ", PATIENT_GIVEN, PATIENT_FAMILY))).isEqualTo(3);
		assertThat(vectorMatchResultMap.getVector(String.join(", \n ", PATIENT_GIVEN, PATIENT_FAMILY))).isEqualTo(3);
		try {
			vectorMatchResultMap.getVector("bad");
			fail("");		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1523) + "There is no matchField with name bad");
		}
	}

	@Test
	public void testInvalidResourceTypeDoesntDeserialize() throws IOException {
		myRules = buildOldStyleEidRules();

		String eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Patient");
		assertThat(eidSystem).isEqualTo(PATIENT_EID_FOR_TEST);

		eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Practitioner");
		assertThat(eidSystem).isEqualTo(PATIENT_EID_FOR_TEST);

		eidSystem = myRules.getEnterpriseEIDSystemForResourceType("Medication");
		assertThat(eidSystem).isEqualTo(PATIENT_EID_FOR_TEST);
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
