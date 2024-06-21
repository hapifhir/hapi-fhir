package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.slf4j.LoggerFactory.getLogger;

public class MdmMatchLinkSvcSurvivorshipTest extends BaseMdmR4Test {

	private static final Logger ourLog = getLogger(MdmMatchLinkSvcSurvivorshipTest.class);

	@Autowired
	IMdmLinkSvc myMdmLinkSvc;

	@SpyBean
	IMdmSurvivorshipService myMdmSurvivorshipService;

	@Autowired
	private GoldenResourceHelper myGoldenResourceHelper;

	@Captor
	ArgumentCaptor<Patient> myPatientCaptor;
	@Captor
	ArgumentCaptor<MdmTransactionContext> myContext;

	private IParser myParser;

	@BeforeEach
	public void before() throws Exception {
		super.before();

		myParser = myFhirContext.newJsonParser();
	}

	@Test
	public void testSurvivorshipIsCalledOnMatchingToTheSameGoldenResource() {
		// no candidates
		createPatientAndUpdateLinks(buildJanePatient());
		verifySurvivorshipCalled(1);

		// single candidate
		createPatientAndUpdateLinks(buildJanePatient());
		verifySurvivorshipCalled(2);

		// multiple candidates matching to the same golden record
		createPatientAndUpdateLinks(buildJanePatient());
		verifySurvivorshipCalled(3);
	}

	@Test
	public void testUpdateLinkToNoMatch_rebuildsGoldenResource() {
		// setup
		int resourceCount = 5;

		Address address = new Address();
		address.setCity("Toronto");
		address.addLine("123 fake st");

		ContactPoint tele = new ContactPoint();
		tele.setSystem(ContactPoint.ContactPointSystem.PHONE);
		tele.setValue("555-555-5555");

		Date birthdate = new Date();

		Patient r = null;
		for (int i = 0; i < resourceCount; i++) {
			// new patient with name
			Patient p = buildJanePatient();
			p.addName()
				.addGiven("Jane")
				.setFamily("Doe");
			p.setTelecom(null);
			p.setAddress(null);
			p.setBirthDate(null);

			/*
			 * We will add some different values to
			 * the final resource we create.
			 *
			 * We do this only on the final one, because
			 * the default IMdmSurvivorship rules is to replace
			 * all of the current values in a golden resource
			 * with all of the new values from the newly added
			 * one (ie, overwriting non-null values with null).
			 */
			if (i == resourceCount - 1) {
				p.setTelecom(Arrays.asList(tele));
				p.setAddress(Arrays.asList(address));
				p.setBirthDate(birthdate);
			}

			r = createPatientAndUpdateLinks(p);
		}

		Optional<MdmLink> linkop = myMdmLinkDaoSvc.findMdmLinkBySource(r);
		assertThat(linkop).isPresent();
		MdmLink link = linkop.get();
		JpaPid gpid = link.getGoldenResourcePersistenceId();

		Patient golden = myPatientDao.readByPid(gpid);

		// we should have a link for each resource all linked
		// to the same golden resource
		assertThat(myMdmLinkDaoSvc.findMdmLinksByGoldenResource(golden)).hasSize(resourceCount);
		assertThat(golden.getAddress()).hasSize(1);
		assertThat(golden.getTelecom()).hasSize(1);
		assertEquals(r.getTelecom().get(0).getValue(), golden.getTelecom().get(0).getValue());

		// test
		// unmatch final link
		MdmCreateOrUpdateParams params = new MdmCreateOrUpdateParams();
		params.setGoldenResourceId(golden.getId());
		params.setGoldenResource(golden);
		params.setResourceId(r.getId());
		params.setSourceResource(r);
		params.setMdmContext(createContextForCreate("Patient"));
		params.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		golden = (Patient) myMdmLinkUpdaterSvc.updateLink(params);

		// verify
		assertTrue(golden.getTelecom() == null || golden.getTelecom().isEmpty());
		assertNull(golden.getBirthDate());
	}

	private void verifySurvivorshipCalled(int theNumberOfTimes) {
		Mockito.verify(myMdmSurvivorshipService, times(theNumberOfTimes))
			.applySurvivorshipRulesToGoldenResource(
				myPatientCaptor.capture(),
				myPatientCaptor.capture(),
				myContext.capture()
			);
	}
}
