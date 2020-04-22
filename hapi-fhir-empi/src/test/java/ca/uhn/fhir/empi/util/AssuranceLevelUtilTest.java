package ca.uhn.fhir.empi.util;

import junit.framework.TestCase;
import org.junit.Test;

import static ca.uhn.fhir.empi.api.EmpiLinkSourceEnum.*;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.*;
import static ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel.*;
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
