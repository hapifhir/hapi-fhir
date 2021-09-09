package ca.uhn.fhir.mdm.util;

import org.hl7.fhir.r4.model.Organization;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class MdmResourceUtilTest {

	//See https://github.com/hapifhir/hapi-fhir/issues/2876
	@Test
	public void testNoNpeOnTagWithNoSystem() {
		//Given
		Organization organization = new Organization();
		organization.getMeta().addTag(null, "Some Code", "Some Display");

		boolean hasGoldenRecordTag = MdmResourceUtil.hasGoldenRecordSystemTag(organization);

		assertThat(hasGoldenRecordTag, is(equalTo(false)));
	}
}
