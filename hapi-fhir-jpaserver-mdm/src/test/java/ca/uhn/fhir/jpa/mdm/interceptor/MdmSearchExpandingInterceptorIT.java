package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmSearchExpandingInterceptorIT extends BaseMdmR4Test {

	private static final Logger ourLog = getLogger(MdmSearchExpandingInterceptorIT.class);

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	/**
	 * creates a GoldenPatient
	 * a number of patients,
	 * and Observations for each created patient.
	 * <p>
	 * Returns a list of stringified ids for the various resources.
	 * <p>
	 * Currently, order of returned resources is patientids first,
	 * observation ids next. But this can be refined as needed.
	 *
	 * @param theResourceCount - number of patients to create
	 * @return
	 */
	private List<String> createAndLinkNewResources(int theResourceCount) throws InterruptedException {
		boolean expansion = myStorageSettings.isAllowMdmExpansion();
		myStorageSettings.setAllowMdmExpansion(false);
		List<String> createdResourceIds = new ArrayList<>();

		List<String> observationIds = new ArrayList<>();
		for (int i = 0; i < theResourceCount; i++) {
			// create patient
			MdmHelperR4.OutcomeAndLogMessageWrapper withLatch = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));
			createdResourceIds.add(withLatch.getDaoMethodOutcome().getId().getIdPart());

			// create observation with patient
			Observation observation = createObservationWithSubject(createdResourceIds.get(i));
			// we put the observation ids in a separate list so we can
			// ensure our returned list is
			// patient ids, followed by observation ids
			observationIds.add(observation.getIdElement().getIdPart());
		}

		assertLinkCount(theResourceCount);

		// add in our observationIds
		createdResourceIds.addAll(observationIds);

		myStorageSettings.setAllowMdmExpansion(expansion);
		return createdResourceIds;
	}

	private List<String> updateAndLinkNewResources(int theResourceCount) throws InterruptedException {
		boolean expansion = myStorageSettings.isAllowMdmExpansion();
		myStorageSettings.setAllowMdmExpansion(false);
		List<String> createdResourceIds = new ArrayList<>();

		List<String> observationIds = new ArrayList<>();
		for (int i = 0; i < theResourceCount; i++) {
			// create patient
			Patient patient = buildJanePatient();
			patient.setId("jane-" + i);
			MdmHelperR4.OutcomeAndLogMessageWrapper withLatch = myMdmHelper.updateWithLatch(addExternalEID(patient, "123"));
			createdResourceIds.add(withLatch.getDaoMethodOutcome().getId().getIdPart());

			// create observation with patient
			Observation observation = createObservationWithSubject(createdResourceIds.get(i));
			// we put the observation ids in a separate list so we can
			// ensure our returned list is
			// patient ids, followed by observation ids
			observationIds.add(observation.getIdElement().getIdPart());
		}

		assertLinkCount(theResourceCount);

		// add in our observationIds
		createdResourceIds.addAll(observationIds);

		myStorageSettings.setAllowMdmExpansion(expansion);
		return createdResourceIds;
	}


	@Test
	public void testReferenceExpansionWorksWithForcedIds() throws InterruptedException {
		int resourceCount = 4;
		List<String> ids = updateAndLinkNewResources(resourceCount);
		String id = ids.get(0);

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
		referenceOrListParam.addOr(new ReferenceParam("Patient/" + id).setMdmExpand(true));
		searchParameterMap.add(Observation.SP_SUBJECT, referenceOrListParam);

		//With MDM Expansion disabled, this should return 1 result.
		myStorageSettings.setAllowMdmExpansion(false);
		IBundleProvider search = myObservationDao.search(searchParameterMap);
		assertEquals(1, search.size());

		//Once MDM Expansion is allowed, this should now return 4 resourecs.
		myStorageSettings.setAllowMdmExpansion(true);
		search = myObservationDao.search(searchParameterMap);
		assertEquals(4, search.size());
		List<MdmLink> all = myMdmLinkDao.findAll();
		Long goldenPid = all.get(0).getGoldenResourcePid();
		IIdType goldenId = myIdHelperService.translatePidIdToForcedId(myFhirContext, "Patient", JpaPid.fromId(goldenPid));
		//Verify that expansion by the golden resource id resolves down to everything its links have.

		SearchParameterMap goldenSpMap = new SearchParameterMap();
		goldenSpMap.setLoadSynchronous(true);
		ReferenceOrListParam goldenReferenceOrListParam = new ReferenceOrListParam();
		goldenReferenceOrListParam.addOr(new ReferenceParam(goldenId).setMdmExpand(true));
		goldenSpMap.add(Observation.SP_SUBJECT, goldenReferenceOrListParam);

		search = myObservationDao.search(goldenSpMap);
		assertEquals(resourceCount, search.size());
	}

	@Test
	public void testReferenceExpansionWorks() throws InterruptedException {
		int resourceCount = 4;
		List<String> ids = createAndLinkNewResources(resourceCount);
		String id = ids.get(0);

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
		referenceOrListParam.addOr(new ReferenceParam("Patient/" + id).setMdmExpand(true));
		searchParameterMap.add(Observation.SP_SUBJECT, referenceOrListParam);

		//With MDM Expansion disabled, this should return 1 result.
		myStorageSettings.setAllowMdmExpansion(false);
		IBundleProvider search = myObservationDao.search(searchParameterMap);
		assertEquals(1, search.size());

		//Once MDM Expansion is allowed, this should now return 4 resourecs.
		myStorageSettings.setAllowMdmExpansion(true);
		search = myObservationDao.search(searchParameterMap);
		assertEquals(4, search.size());
		List<MdmLink> all = myMdmLinkDao.findAll();
		Long goldenPid = all.get(0).getGoldenResourcePid();
		IIdType goldenId = myIdHelperService.translatePidIdToForcedId(myFhirContext, "Patient", JpaPid.fromId(goldenPid));
		//Verify that expansion by the golden resource id resolves down to everything its links have.

		SearchParameterMap goldenSpMap = new SearchParameterMap();
		goldenSpMap.setLoadSynchronous(true);
		ReferenceOrListParam goldenReferenceOrListParam = new ReferenceOrListParam();
		goldenReferenceOrListParam.addOr(new ReferenceParam(goldenId).setMdmExpand(true));
		goldenSpMap.add(Observation.SP_SUBJECT, goldenReferenceOrListParam);

		search = myObservationDao.search(goldenSpMap);
		assertEquals(resourceCount, search.size());
	}

	@Test
	public void testMdmExpansionExpands_idParameter() throws InterruptedException {
		int resourceCount = 4;
		List<String> expectedIds = createAndLinkNewResources(resourceCount);
		String patientId = expectedIds.get(0);

		myStorageSettings.setAllowMdmExpansion(true);

		SearchParameterMap map = new SearchParameterMap();
		TokenOrListParam orListParam = new TokenOrListParam();
		TokenParam patientIdParam = new TokenParam();
		patientIdParam.setValue(patientId);
		patientIdParam.setMdmExpand(true);
		orListParam.add(patientIdParam);
		map.add("_id", orListParam);

		IBundleProvider outcome = myPatientDao.search(map);

		assertNotNull(outcome);
		// we know 4 cause that's how many patients are created
		// plus one golden resource
		assertEquals(resourceCount + 1, outcome.size());
		List<String> resourceIds = outcome.getAllResourceIds();
		// check the patients - first 4 ids
		for (int i = 0; i < resourceIds.size() - 1; i++) {
			assertThat(resourceIds).contains(expectedIds.get(i));
		}
	}

	@Test
	public void testMdmExpansionIsSupportedOnEverythingOperation() throws InterruptedException {
		int resourceCount = 4;
		List<String> expectedIds = createAndLinkNewResources(resourceCount);
		String id = expectedIds.get(0);

		HashMap<String, String[]> queryParameters = new HashMap<>();

		HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
		RequestDetails theDetails = Mockito.mock(RequestDetails.class);

		Mockito.when(theDetails.getParameters())
			.thenReturn(queryParameters);

		// test
		myStorageSettings.setAllowMdmExpansion(true);
		IFhirResourceDaoPatient<Patient> dao = (IFhirResourceDaoPatient<Patient>) myPatientDao;
		final PatientEverythingParameters queryParams = new PatientEverythingParameters();
		queryParams.setMdmExpand(true);
		IBundleProvider outcome = dao.patientInstanceEverything(
			req,
			theDetails, queryParams, new IdDt(id)
		);

		// verify return results
		// we expect all the linked ids to be returned too
		assertNotNull(outcome);
		// plus 1 for the golden resource
		assertEquals(expectedIds.size() + 1, outcome.size());
		List<String> returnedIds = outcome.getAllResourceIds();
		for (String expected : expectedIds) {
			assertThat(returnedIds).contains(expected);
		}
	}

	@Test
	public void testReferenceExpansionQuietlyFailsOnMissingMdmMatches() throws InterruptedException {
		myStorageSettings.setAllowMdmExpansion(true);
		Patient patient = buildJanePatient();
		patient.getMeta().addTag(MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_NO_MDM_MANAGED, "Don't MDM on me!");
		String id = myMdmHelper.executeWithLatch(() -> myMdmHelper.doCreateResource(patient, true)).getId().getIdPart();
		createObservationWithSubject(id);

		//Even though the user has NO mdm links, that should not cause a request failure.
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam("Patient/" + id).setMdmExpand(true));
		IBundleProvider search = myObservationDao.search(map);
		assertEquals(1, search.size());
	}

	private Observation createObservationWithSubject(String thePatientId) {
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient/" + thePatientId));
		observation.setCode(new CodeableConcept().setText("Made for Patient/" + thePatientId));
		DaoMethodOutcome daoMethodOutcome = myObservationDao.create(observation);
		return (Observation) daoMethodOutcome.getResource();
	}
}
