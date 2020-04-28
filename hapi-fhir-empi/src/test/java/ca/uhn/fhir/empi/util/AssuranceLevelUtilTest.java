package ca.uhn.fhir.empi.util;

import junit.framework.TestCase;
import org.junit.Test;

import static ca.uhn.fhir.empi.api.EmpiLinkSourceEnum.AUTO;
import static ca.uhn.fhir.empi.api.EmpiLinkSourceEnum.MANUAL;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.MATCH;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.NO_MATCH;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_MATCH;
import static ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel.LEVEL2;
import static ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel.LEVEL3;
import static ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel.LEVEL4;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class AssuranceLevelUtilTest extends TestCase {

	@Test
	public void testValidPersonLinkLevels() {
		assertThat(AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_MATCH, AUTO), is(equalTo(LEVEL2)));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(MATCH, AUTO), is(equalTo(LEVEL3)));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(MATCH, MANUAL), is(equalTo(LEVEL4)));

	}
	@Test
	public void testInvalidPersonLinkLevels() {
		assertThat(AssuranceLevelUtil.getAssuranceLevel(NO_MATCH, AUTO), is(nullValue()));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_DUPLICATE, AUTO), is(nullValue()));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(NO_MATCH, MANUAL), is(nullValue()));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_MATCH, MANUAL), is(nullValue()));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_DUPLICATE, MANUAL), is(nullValue()));
	}

}
