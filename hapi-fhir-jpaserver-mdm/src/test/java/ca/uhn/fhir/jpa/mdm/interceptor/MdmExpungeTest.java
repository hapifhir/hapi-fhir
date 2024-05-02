package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.interceptor.IMdmStorageInterceptor;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class MdmExpungeTest extends BaseMdmR4Test {

	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
    IMdmStorageInterceptor myMdmStorageInterceptor;
	private ResourceTable myTargetEntity;
	private ResourceTable mySourceEntity;
	private IdDt myTargetId;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setExpungeEnabled(true);

		myTargetEntity = (ResourceTable) myPatientDao.create(new Patient()).getEntity();
		myTargetId = myTargetEntity.getIdDt().toVersionless();
		mySourceEntity = (ResourceTable) myPatientDao.create(new Patient()).getEntity();

		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		mdmLink.setGoldenResourcePid(mySourceEntity.getId());
		mdmLink.setSourcePid(myTargetEntity.getId());
		saveLink(mdmLink);
	}



	@Test
	public void testUninterceptedDeleteRemovesMdmReference() {
		assertThat(myMdmLinkDao.count()).isEqualTo(1);
		myPatientDao.delete(myTargetEntity.getIdDt());
		assertThat(myMdmLinkDao.count()).isEqualTo(1);
		ExpungeOptions expungeOptions = new ExpungeOptions();
		expungeOptions.setExpungeDeletedResources(true);
		try {
			myPatientDao.expunge(myTargetId.toVersionless(), expungeOptions, null);
			fail("");
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage()).contains("ViolationException"));
			assertThat(e.getMessage()).contains("FK_EMPI_LINK_TARGET"));
		}
		myInterceptorService.registerInterceptor(myMdmStorageInterceptor);
		myPatientDao.expunge(myTargetId.toVersionless(), expungeOptions, null);
		assertThat(myMdmLinkDao.count()).isEqualTo(0);
	}

	@AfterEach
	public void afterUnregisterInterceptor() {
		myInterceptorService.unregisterInterceptor(myMdmStorageInterceptor);
	}

}
