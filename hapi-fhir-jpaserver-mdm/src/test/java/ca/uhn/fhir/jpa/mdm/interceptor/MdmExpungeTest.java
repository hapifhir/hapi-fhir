package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmExpungeTest extends BaseMdmR4Test {

	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
	IMdmStorageInterceptor myMdmStorageInterceptor;
	@Autowired
	DaoConfig myDaoConfig;
	private ResourceTable myTargetEntity;
	private ResourceTable mySourceEntity;
	private IdDt myTargetId;

	@BeforeEach
	public void before() {
		myDaoConfig.setExpungeEnabled(true);

		myTargetEntity = (ResourceTable) myPatientDao.create(new Patient()).getEntity();
		myTargetId = myTargetEntity.getIdDt().toVersionless();
		mySourceEntity = (ResourceTable) myPatientDao.create(new Patient()).getEntity();

		MdmLink mdmLink = myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		mdmLink.setGoldenResourcePid(mySourceEntity.getId());
		mdmLink.setSourcePid(myTargetEntity.getId());
		saveLink(mdmLink);
	}



	@Test
	public void testUninterceptedDeleteRemovesMdmReference() {
		assertEquals(1, myMdmLinkDao.count());
		myPatientDao.delete(myTargetEntity.getIdDt());
		assertEquals(1, myMdmLinkDao.count());
		ExpungeOptions expungeOptions = new ExpungeOptions();
		expungeOptions.setExpungeDeletedResources(true);
		try {
			myPatientDao.expunge(myTargetId.toVersionless(), expungeOptions, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), containsString("ViolationException"));
			assertThat(e.getMessage(), containsString("FK_EMPI_LINK_TARGET"));
		}
		myInterceptorService.registerInterceptor(myMdmStorageInterceptor);
		myPatientDao.expunge(myTargetId.toVersionless(), expungeOptions, null);
		assertEquals(0, myMdmLinkDao.count());
	}

	@AfterEach
	public void afterUnregisterInterceptor() {
		myInterceptorService.unregisterInterceptor(myMdmStorageInterceptor);
	}

}
