package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmpiLinkDaoSvcTest extends BaseEmpiR4Test {
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	IEmpiSettings myEmpiSettings;

	@Test
	public void testCreate() {
		EmpiLink empiLink = createResourcesAndBuildTestEmpiLink();
		assertThat(empiLink.getCreated(), is(nullValue()));
		assertThat(empiLink.getUpdated(), is(nullValue()));
		myEmpiLinkDaoSvc.save(empiLink);
		assertThat(empiLink.getCreated(), is(notNullValue()));
		assertThat(empiLink.getUpdated(), is(notNullValue()));
		assertTrue(empiLink.getUpdated().getTime() - empiLink.getCreated().getTime() < 1000);
	}

	@Test
	public void testUpdate() {
		EmpiLink createdLink = myEmpiLinkDaoSvc.save(createResourcesAndBuildTestEmpiLink());
		assertThat(createdLink.getLinkSource(), is(EmpiLinkSourceEnum.MANUAL));
		TestUtil.sleepOneClick();
		createdLink.setLinkSource(EmpiLinkSourceEnum.AUTO);
		EmpiLink updatedLink = myEmpiLinkDaoSvc.save(createdLink);
		assertNotEquals(updatedLink.getCreated(), updatedLink.getUpdated());
	}

	@Test
	public void testNew() {
		EmpiLink newLink = myEmpiLinkDaoSvc.newEmpiLink();
		EmpiRulesJson rules = myEmpiSettings.getEmpiRules();
		assertEquals("1", rules.getVersion());
		assertEquals(rules.getVersion(), newLink.getVersion());
	}

}
