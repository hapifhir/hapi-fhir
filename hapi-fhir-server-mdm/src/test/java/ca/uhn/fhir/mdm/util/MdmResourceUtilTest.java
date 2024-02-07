package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.mdm.api.MdmConstants;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdmResourceUtilTest {

	//See https://github.com/hapifhir/hapi-fhir/issues/2876
	@Test
	public void testNoNpeOnTagWithNoSystem() {
		//Given
		Organization organization = new Organization();
		organization.getMeta().addTag(null, "Some Code", "Some Display");

		boolean hasGoldenRecordTag = MdmResourceUtil.hasGoldenRecordSystemTag(organization);

		assertThat(hasGoldenRecordTag).isEqualTo(false);
	}

	@Test
	public void testSetGoldenAndBlockedResource() {
		// setup
		Patient patient = new Patient();
		patient.setActive(true);

		// test
		Patient changed = (Patient) MdmResourceUtil.setGoldenResourceAsBlockedResourceGoldenResource(
			MdmResourceUtil.setGoldenResource(patient)
		);

		// verify
		assertNotNull(changed);
		List<Coding> tags = changed.getMeta().getTag();
		Set<String> codes = new HashSet<>();
		codes.add(MdmConstants.CODE_BLOCKED);
		codes.add(MdmConstants.CODE_GOLDEN_RECORD);
		assertEquals(2, tags.size());
		for (Coding code : tags) {
			assertEquals(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, code.getSystem());
			assertTrue(codes.contains(code.getCode()));
		}
	}
}
