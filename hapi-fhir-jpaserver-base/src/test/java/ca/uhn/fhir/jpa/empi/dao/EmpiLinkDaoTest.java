package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.jpalink.dao.IEmpiLinkDao;
import ca.uhn.fhir.empi.jpalink.entity.EmpiLink;
import ca.uhn.fhir.empi.rules.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class EmpiLinkDaoTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testSave() {
		EmpiLink empiLink = new EmpiLink();
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		empiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		empiLink.setPerson(myPersonEntity);
		empiLink.setResource(myPatientEntity);
		myEmpiLinkDao.save(empiLink);
		List<EmpiLink> result = myEmpiLinkDao.findAll();
		assertEquals(1, result.size());
		assertEquals(EmpiMatchResultEnum.MATCH, result.get(0).getMatchResult());
	}
}
