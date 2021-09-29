package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.slf4j.LoggerFactory.getLogger;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmSearchExpandingInterceptorIT extends BaseMdmR4Test {

	private static final Logger ourLog = getLogger(MdmSearchExpandingInterceptorIT.class);

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;
	@Autowired
	private DaoConfig myDaoConfig;

	/**
	 * creates a GoldenPatient and a bunch of Observations with
	 * linked patients.
	 * Returns a list of stringified ids for the various resources.
	 *
	 * Currently, order of returned resources is patientids first,
	 * observation ids next. But this can be refined as needed.
	 *
	 * @return
	 */
	private List<String> createAndLinkNewResources() throws InterruptedException {
		boolean expansion = myDaoConfig.isAllowMdmExpansion();
		myDaoConfig.setAllowMdmExpansion(false);
		List<String> createdResourceIds = new ArrayList<>();

		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));
		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch1 = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));
		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch2 = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));
		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch3 = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));

		assertLinkCount(4);

		String id = withLatch.getDaoMethodOutcome().getId().getIdPart();
		String id1 = withLatch1.getDaoMethodOutcome().getId().getIdPart();
		String id2 = withLatch2.getDaoMethodOutcome().getId().getIdPart();
		String id3 = withLatch3.getDaoMethodOutcome().getId().getIdPart();
		createdResourceIds.add(id);
		createdResourceIds.add(id1);
		createdResourceIds.add(id2);
		createdResourceIds.add(id3);

		//Create an Observation for each Patient
		Observation o1 = createObservationWithSubject(id);
		Observation o2 = createObservationWithSubject(id1);
		Observation o3 = createObservationWithSubject(id2);
		Observation o4 = createObservationWithSubject(id3);

		createdResourceIds.add(o1.getIdElement().getIdPart());
		createdResourceIds.add(o2.getIdElement().getIdPart());
		createdResourceIds.add(o3.getIdElement().getIdPart());
		createdResourceIds.add(o4.getIdElement().getIdPart());

		myDaoConfig.setAllowMdmExpansion(expansion);
		return createdResourceIds;
	}

	@Test
	public void testReferenceExpansionWorks() throws InterruptedException {
		List<String> ids = createAndLinkNewResources();
		String id = ids.get(0);

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
		referenceOrListParam.addOr(new ReferenceParam("Patient/" + id).setMdmExpand(true));
		searchParameterMap.add(Observation.SP_SUBJECT, referenceOrListParam);

		//With MDM Expansion disabled, this should return 1 result.
		IBundleProvider search = myObservationDao.search(searchParameterMap);
		assertThat(search.size(), is(equalTo(1)));

		//Once MDM Expansion is allowed, this should now return 4 resourecs.
		myDaoConfig.setAllowMdmExpansion(true);
		search = myObservationDao.search(searchParameterMap);
		assertThat(search.size(), is(equalTo(4)));
		List<MdmLink> all = myMdmLinkDao.findAll();
		Long goldenPid = all.get(0).getGoldenResourcePid();
		IIdType goldenId = myIdHelperService.translatePidIdToForcedId(myFhirContext, "Patient", new ResourcePersistentId(goldenPid));
		//Verify that expansion by the golden resource id resolves down to everything its links have.

		SearchParameterMap goldenSpMap = new SearchParameterMap();
		goldenSpMap.setLoadSynchronous(true);
		ReferenceOrListParam goldenReferenceOrListParam = new ReferenceOrListParam();
		goldenReferenceOrListParam.addOr(new ReferenceParam(goldenId).setMdmExpand(true));
		goldenSpMap.add(Observation.SP_SUBJECT, goldenReferenceOrListParam);

		search = myObservationDao.search(goldenSpMap);
		assertThat(search.size(), is(equalTo(4)));
	}

	@Test
	public void testMdmExpansionExpands_idParameter() throws InterruptedException {
		List<String> expectedIds = createAndLinkNewResources();
		String patientId = expectedIds.get(0);

		myDaoConfig.setAllowMdmExpansion(true);

		SearchParameterMap map = new SearchParameterMap();
		TokenOrListParam orListParam = new TokenOrListParam();
		orListParam.add(patientId);

		map.add("_id", orListParam);

		IBundleProvider outcome = myPatientDao.search(map);

		Assertions.assertNotNull(outcome);
		// we know 5 cause that's how many patients are created
		Assertions.assertEquals(5, outcome.size());
		List<String> resourceIds = outcome.getAllResourceIds();
		// check the patients - first 4 ids
		for (int i = 0; i < resourceIds.size() - 1; i++) {
			Assertions.assertTrue(resourceIds.contains(expectedIds.get(i)));
		}
	}

	@Test
	public void testMdmExpansionIsSupportedOnEverythingOperation() throws InterruptedException {
		List<String> expectedIds = createAndLinkNewResources();
		String id = expectedIds.get(0);

		HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
		RequestDetails theDetails = Mockito.mock(RequestDetails.class);

		// test
		myDaoConfig.setAllowMdmExpansion(true);
		IFhirResourceDaoPatient<Patient> dao = (IFhirResourceDaoPatient<Patient>) myPatientDao;
		IBundleProvider outcome = dao.patientInstanceEverything(
			req,
			new IdDt(id),
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			theDetails
		);

		// verify return results
		// we expect all the linked ids to be returned too
		Assertions.assertNotNull(outcome);
		Assertions.assertEquals(9, outcome.size());
		List<String> returnedIds = outcome.getAllResourceIds();
		for (String expected : expectedIds) {
			Assertions.assertTrue(returnedIds.contains(expected));
		}
	}

	@Test
	public void testReferenceExpansionQuietlyFailsOnMissingMdmMatches() {
		myDaoConfig.setAllowMdmExpansion(true);
		Patient patient = buildJanePatient();
		patient.getMeta().addTag(MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_NO_MDM_MANAGED, "Don't MDM on me!");
		DaoMethodOutcome daoMethodOutcome = myMdmHelper.doCreateResource(patient, true);
		String id = daoMethodOutcome.getId().getIdPart();
		createObservationWithSubject(id);

		//Even though the user has NO mdm links, that should not cause a request failure.
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam("Patient/" + id).setMdmExpand(true));
		IBundleProvider search = myObservationDao.search(map);
		assertThat(search.size(), is(equalTo(1)));
	}

	private Observation createObservationWithSubject(String thePatientId) {
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient/" + thePatientId));
		observation.setCode(new CodeableConcept().setText("Made for Patient/" + thePatientId));
		DaoMethodOutcome daoMethodOutcome = myObservationDao.create(observation);
		return (Observation) daoMethodOutcome.getResource();
	}
}
