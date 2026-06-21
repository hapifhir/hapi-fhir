package ca.uhn.fhir.jpa.util.jsonpatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.patch.JsonPatchUtils;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Group;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonPatchUtilsTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JsonPatchUtilsTest.class);

	@SuppressWarnings("JsonStandardCompliance")
	@Test
	public void testInvalidPatchJsonError() {

		// Quotes are incorrect in the "value" body
		String patchText = "[ {\n" +
			"        \"comment\": \"add image to examination\",\n" +
			"        \"patch\": [ {\n" +
			"            \"op\": \"add\",\n" +
			"            \"path\": \"/derivedFrom/-\",\n" +
			"            \"value\": [{'reference': '/Media/465eb73a-bce3-423a-b86e-5d0d267638f4'}]\n" +
			"        } ]\n" +
			"    } ]";

		try {
			JsonPatchUtils.apply(myFhirContext, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			assertThat(e.toString()).contains("was expecting double-quote to start field name");
			// The error message should not contain the patch body
			assertThat(e.toString()).doesNotContain("add image to examination");
		}

	}

	@Test
	public void testInvalidPatchSyntaxError() {

		// Quotes are incorrect in the "value" body
		String patchText = "[ {" +
			"        \"comment\": \"add image to examination\"," +
			"        \"patch\": [ {" +
			"            \"op\": \"foo\"," +
			"            \"path\": \"/derivedFrom/-\"," +
			"            \"value\": [{\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}]" +
			"        } ]\n" +
			"    } ]";

		try {
			JsonPatchUtils.apply(myFhirContext, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			assertThat(e.toString()).contains("missing type id property 'op'");
			// The error message should not contain the patch body
			assertThat(e.toString()).doesNotContain("add image to examination");
		}

	}


	@Test
	public void testPatchAddArray() {

		String patchText = "[ " +
			"      {" +
			"        \"op\": \"add\"," +
			"        \"path\": \"/derivedFrom\"," +
			"        \"value\": [" +
			"          {\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}" +
			"        ]" +
			"      } " +
			"]";

		Observation toUpdate = new Observation();
		toUpdate = JsonPatchUtils.apply(myFhirContext, toUpdate, patchText);

		String outcome = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(toUpdate);
		ourLog.info(outcome);

		assertThat(outcome).contains("\"reference\": \"Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"");
	}

	@Test
	public void testPatchAddMemberToEmptyGroup() {
		Group group = new Group();
		group.setId("Group/test-group");
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);

		String patchText = "[{" +
			"\"op\":\"add\"," +
			"\"path\":\"/member/0\"," +
			"\"value\":{" +
			"\"entity\":{\"reference\":\"Patient/123\"}," +
			"\"inactive\":false" +
			"}" +
			"}]";

		Group result = JsonPatchUtils.apply(myFhirContext, group, patchText);

		assertThat(result.getMember()).hasSize(1);
		assertThat(result.getMember().get(0).getEntity().getReference()).isEqualTo("Patient/123");
		assertThat(result.getMember().get(0).getInactive()).isFalse();
	}

	@Test
	public void testPatchAddMemberToEmptyGroup_AppendPath() {
		Group group = new Group();
		group.setId("Group/test-group");
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);

		String patchText = "[{" +
			"\"op\":\"add\"," +
			"\"path\":\"/member/-\"," +
			"\"value\":{" +
			"\"entity\":{\"reference\":\"Patient/456\"}," +
			"\"inactive\":false" +
			"}" +
			"}]";

		Group result = JsonPatchUtils.apply(myFhirContext, group, patchText);

		assertThat(result.getMember()).hasSize(1);
		assertThat(result.getMember().get(0).getEntity().getReference()).isEqualTo("Patient/456");
	}

	@Test
	public void testPatchReplaceMemberOnEmptyGroup_Fails() {
		Group group = new Group();
		group.setId("Group/test-group");
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);

		String patchText = "[{" +
			"\"op\":\"replace\"," +
			"\"path\":\"/member/0\"," +
			"\"value\":{" +
			"\"entity\":{\"reference\":\"Patient/123\"}," +
			"\"inactive\":false" +
			"}" +
			"}]";

		assertThatThrownBy(() -> JsonPatchUtils.apply(myFhirContext, group, patchText))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-1272");
	}

	@Test
	public void testPatchAddInvalidElement() {

		String patchText = "[ " +
			"      {" +
			"        \"op\": \"add\"," +
			"        \"path\": \"/derivedFromXXX\"," +
			"        \"value\": [" +
			"          {\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}" +
			"        ]" +
			"      } " +
			"]";

		Observation toUpdate = new Observation();
		try {
			JsonPatchUtils.apply(myFhirContext, toUpdate, patchText);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1271) + "Failed to apply JSON patch to Observation: " + Msg.code(1825) + "Unknown element 'derivedFromXXX' found during parse", e.getMessage());
		}

	}
}
