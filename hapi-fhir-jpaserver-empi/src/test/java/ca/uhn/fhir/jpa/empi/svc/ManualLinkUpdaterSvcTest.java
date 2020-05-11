package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IManualLinkUpdaterSvc;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.helper.EmpiLinkHelper;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ManualLinkUpdaterSvcTest extends BaseEmpiR4Test {
	@Autowired
	IManualLinkUpdaterSvc myManualLinkUpdaterSvc;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiLinkHelper myEmpiLinkHelper;

	@Test
	public void testUpdateLink() {
		Patient patient = createPatientAndUpdateLinks(new Patient());
		Person person = getPersonFromTarget(patient);

		EmpiLink link = myEmpiLinkDaoSvc.findEmpiLinkByTarget(patient).get();
		assertEquals(EmpiLinkSourceEnum.AUTO, link.getLinkSource());
		assertEquals(EmpiMatchResultEnum.MATCH, link.getMatchResult());

		myEmpiLinkHelper.logEmpiLinks();
		myManualLinkUpdaterSvc.updateLink(person, patient, EmpiMatchResultEnum.NO_MATCH);
		myEmpiLinkHelper.logEmpiLinks();

		link = myEmpiLinkDaoSvc.findEmpiLinkByTarget(patient).get();
		assertEquals(EmpiLinkSourceEnum.MANUAL, link.getLinkSource());
		assertEquals(EmpiMatchResultEnum.NO_MATCH, link.getMatchResult());


	}
}
