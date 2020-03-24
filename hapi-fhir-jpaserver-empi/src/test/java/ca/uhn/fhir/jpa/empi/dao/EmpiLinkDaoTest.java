package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.empi.rules.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
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
		empiLink.setPerson(myPersonEntity);
		empiLink.setResource(myPatientEntity);
		myEmpiLinkDao.save(empiLink);
	}
}
