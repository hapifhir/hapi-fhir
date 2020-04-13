package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.empi.svc.ResourceTableHelper;
import ca.uhn.fhir.jpa.empi.util.EmpiHelperR4;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmpiLinkDaoTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	ResourceTableHelper myResourceTableHelper;
	@Rule
	@Autowired
	public EmpiHelperR4 myEmpiHelper;

	@Test
	public void testSave() throws InterruptedException {
		Person person = createPerson();
		long patientPid = myEmpiHelper.createWithLatch(new Patient()).getEntity().getPersistentId().getIdAsLong();

		EmpiLink empiLink = new EmpiLink();
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		empiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		empiLink.setPersonPid(myResourceTableHelper.getPidOrNull(person));
		empiLink.setTargetPid(patientPid);
		myEmpiLinkDao.save(empiLink);
	}
}

