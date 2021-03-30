package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

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

	@Test
	public void testReferenceExpansionWorks() throws InterruptedException {
		myDaoConfig.setAllowMdmExpansion(false);
		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));
		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch1 = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));
		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch2 = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));
		MdmHelperR4.OutcomeAndLogMessageWrapper withLatch3 = myMdmHelper.createWithLatch(addExternalEID(buildJanePatient(), "123"));

		assertLinkCount(4);

		String id = withLatch.getDaoMethodOutcome().getId().getIdPart();
		String id1 = withLatch1.getDaoMethodOutcome().getId().getIdPart();
		String id2 = withLatch2.getDaoMethodOutcome().getId().getIdPart();
		String id3 = withLatch3.getDaoMethodOutcome().getId().getIdPart();

		//Create an Observation for each Patient
		createObservationWithSubject(id);
		createObservationWithSubject(id1);
		createObservationWithSubject(id2);
		createObservationWithSubject(id3);

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
	}

	private Observation createObservationWithSubject(String thePatientId) {
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient/" + thePatientId));
		observation.setCode(new CodeableConcept().setText("Made for Patient/" + thePatientId));
		DaoMethodOutcome daoMethodOutcome = myObservationDao.create(observation);
		return (Observation) daoMethodOutcome.getResource();

	}
}
