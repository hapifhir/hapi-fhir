package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EmpiExpungeTest extends BaseEmpiR4Test {

	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
	IEmpiStorageInterceptor myEmpiStorageInterceptor;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;
	private ResourceTable myTargetEntity;
	private ResourceTable myPersonEntity;
	private IdDt myTargetId;

	@Before
	public void before() {
		myDaoConfig.setExpungeEnabled(true);

		myTargetEntity = (ResourceTable) myPatientDao.create(new Patient()).getEntity();
		myTargetId = myTargetEntity.getIdDt().toVersionless();
		myPersonEntity = (ResourceTable) myPersonDao.create(new Person()).getEntity();

		EmpiLink empiLink = new EmpiLink();
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		empiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		empiLink.setPersonPid(myPersonEntity.getId());
		empiLink.setTargetPid(myTargetEntity.getId());
		myEmpiLinkDao.save(empiLink);
	}

	@Test
	public void testUninterceptedDeleteRemovesEMPIReference() {
		assertEquals(1, myEmpiLinkDao.count());
		myPatientDao.delete(myTargetEntity.getIdDt());
		assertEquals(1, myEmpiLinkDao.count());
		ExpungeOptions expungeOptions = new ExpungeOptions();
		expungeOptions.setExpungeDeletedResources(true);
		try {
			myPatientDao.expunge(myTargetId.toVersionless(), expungeOptions, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), containsString("ViolationException"));
			assertThat(e.getMessage(), containsString("FK_EMPI_LINK_TARGET"));
		}
		myInterceptorService.registerInterceptor(myEmpiStorageInterceptor);
		myPatientDao.expunge(myTargetId.toVersionless(), expungeOptions, null);
		assertEquals(0, myEmpiLinkDao.count());
	}

	@After
	public void afterUnregisterInterceptor() {
		myInterceptorService.unregisterInterceptor(myEmpiStorageInterceptor);
	}

}
