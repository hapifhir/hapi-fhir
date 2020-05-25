package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
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

public class EmpiLinkDaoSvcTest extends BaseEmpiR4Test {
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@Test
	public void testCreate() {
		EmpiLink empiLink = createResourcesAndBuildTestEmpiLink();
		assertThat(empiLink.getCreated(), is(nullValue()));
		assertThat(empiLink.getUpdated(), is(nullValue()));
		myEmpiLinkDaoSvc.save(empiLink);
		assertThat(empiLink.getCreated(), is(notNullValue()));
		assertThat(empiLink.getUpdated(), is(notNullValue()));
		assertEquals(empiLink.getCreated(), empiLink.getUpdated());
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

}
