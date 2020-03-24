package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmpiLinkDaoTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testSave() {
		EmpiLink empiLink = new EmpiLink();
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		empiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		// FIXME KHS get this to work with pids
		empiLink.setPersonPid(myPersonPid);
		empiLink.setResourcePid(myPatientPid);
		myEmpiLinkDao.save(empiLink);
	}
}
