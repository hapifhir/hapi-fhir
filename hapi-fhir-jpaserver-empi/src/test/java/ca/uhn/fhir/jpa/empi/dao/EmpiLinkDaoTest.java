package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiLinkDaoTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testSave() {
		Person person = createPerson();
		Patient patient = createPatient();

		EmpiLink empiLink = new EmpiLink();
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		empiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		empiLink.setPersonPid(ResourceTableHelper.getPidOrNull(person));
		empiLink.setResourcePid(ResourceTableHelper.getPidOrNull(patient));
		myEmpiLinkDao.save(empiLink);
	}
}
