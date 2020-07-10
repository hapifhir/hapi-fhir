package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.empi.api.EmpiLinkSourceEnum.AUTO;
import static ca.uhn.fhir.empi.api.EmpiLinkSourceEnum.MANUAL;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.MATCH;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.NO_MATCH;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_MATCH;
import static ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel.LEVEL1;
import static ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel.LEVEL2;
import static ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel.LEVEL3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AssuranceLevelUtilTest {

	@Test
	public void testValidPersonLinkLevels() {
		assertThat(AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_MATCH, AUTO), is(equalTo(LEVEL1)));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(MATCH, AUTO), is(equalTo(LEVEL2)));
		assertThat(AssuranceLevelUtil.getAssuranceLevel(MATCH, MANUAL), is(equalTo(LEVEL3)));

	}

	@Test
	public void testInvalidPersonLinkLevels() {
		try {
			AssuranceLevelUtil.getAssuranceLevel(NO_MATCH, AUTO);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("An AUTO EMPI Link may not have a match result of NO_MATCH", e.getMessage());
		}
		try {
			AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_DUPLICATE, AUTO);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("An AUTO EMPI Link may not have a match result of POSSIBLE_DUPLICATE", e.getMessage());
		}
		try {
			AssuranceLevelUtil.getAssuranceLevel(NO_MATCH, MANUAL);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL EMPI Link may not have a match result of NO_MATCH", e.getMessage());
		}
		try {
			AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_MATCH, MANUAL);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL EMPI Link may not have a match result of POSSIBLE_MATCH", e.getMessage());
		}
		try {
			AssuranceLevelUtil.getAssuranceLevel(POSSIBLE_DUPLICATE, MANUAL);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("A MANUAL EMPI Link may not have a match result of POSSIBLE_DUPLICATE", e.getMessage());
		}
	}

}
