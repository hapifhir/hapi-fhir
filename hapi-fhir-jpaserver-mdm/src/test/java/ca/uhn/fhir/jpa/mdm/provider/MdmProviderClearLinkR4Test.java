package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;

import static ca.uhn.fhir.mdm.api.MdmMatchOutcome.POSSIBLE_MATCH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderClearLinkR4Test extends BaseLinkR4Test {
	protected Practitioner myPractitioner;
	protected StringType myPractitionerId;
	protected IAnyResource myPractitionerGoldenResource;
	protected StringType myPractitionerGoldenResourceId;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPractitioner = createPractitionerAndUpdateLinks(new Practitioner());
		myPractitionerId = new StringType(myPractitioner.getIdElement().getValue());
		myPractitionerGoldenResource = getGoldenResourceFromTargetResource(myPractitioner);
		myPractitionerGoldenResourceId = new StringType(myPractitionerGoldenResource.getIdElement().getValue());

		setMdmRuleJson("mdm/nickname-mdm-rules.json");
	}

	@Test
	public void testClearAllLinks() {
		assertLinkCount(2);
		clearMdmLinks();
		assertNoLinksExist();
	}

	private void assertNoLinksExist() {
		assertNoPatientLinksExist();
		assertNoPractitionerLinksExist();
	}

	private void assertNoPatientLinksExist() {
		assertThat(getPatientLinks(), hasSize(0));
	}

	private void assertNoPractitionerLinksExist() {
		assertThat(getPractitionerLinks(), hasSize(0));
	}

	@Test
	public void testClearPatientLinks() {
		assertLinkCount(2);
		Patient read = myPatientDao.read(new IdDt(mySourcePatientId.getValueAsString()).toVersionless());
		assertThat(read, is(notNullValue()));
		clearMdmLinks("Patient");
		assertNoPatientLinksExist();
		try {
			myPatientDao.read(new IdDt(mySourcePatientId.getValueAsString()).toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {
			// Expected exception
		}

	}
	@Test
	public void testGoldenResourceWithMultipleHistoricalVersionsCanBeDeleted() {
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildJanePatient());
		IAnyResource goldenResource = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		assertThat(goldenResource, is(notNullValue()));
		clearMdmLinks();
		assertNoPatientLinksExist();
		goldenResource = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		assertThat(goldenResource, is(nullValue()));
	}

	@Test
	public void testGoldenResourceWithLinksToOtherGoldenResourcesCanBeDeleted() {
		createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildPaulPatient());

		IAnyResource goldenResourceFromTarget = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		IAnyResource goldenResourceFromTarget2 = getGoldenResourceFromTargetResource(patientAndUpdateLinks1);
		linkGoldenResources(goldenResourceFromTarget, goldenResourceFromTarget2);

		//SUT
		clearMdmLinks();

		assertNoPatientLinksExist();
		IBundleProvider search = myPatientDao.search(buildGoldenResourceParameterMap());
		assertThat(search.size(), is(equalTo(0)));
	}

	/**
	 * Build a SearchParameterMap which looks up Golden Records (Source resources).
	 * @return
	 */
	private SearchParameterMap buildGoldenResourceParameterMap() {
		return new SearchParameterMap().setLoadSynchronous(true).add("_tag", new TokenParam(MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_HAPI_MDM_MANAGED));
	}

	@Tag("intermittent")
// TODO KHS I know intermittent tags aren't used by hapi but this will help me find this test when I review intermittents.
//  Last time this test failed, this is what was in the logs:
//	2022-07-17 19:57:27.103 [main] INFO  c.u.f.batch2.channel.BatchJobSender [BatchJobSender.java:43] Sending work notification for job[MDM_CLEAR] instance[6f6d6fc5-f74a-426f-b215-7a383893f4bc] step[generate-ranges] chunk[219e29d5-1ee7-47dd-99a1-c636b1b221ae]
//	2022-07-17 19:57:27.193 [batch2-work-notification-1] INFO  c.u.f.m.b.MdmGenerateRangeChunksStep [MdmGenerateRangeChunksStep.java:49] Initiating mdm clear of [Patient]] Golden Resources from Sat Jan 01 00:00:00 UTC 2000 to Sun Jul 17 19:57:27 UTC 2022
//	2022-07-17 19:57:27.275 [batch2-work-notification-1] INFO  c.u.f.m.b.MdmGenerateRangeChunksStep [MdmGenerateRangeChunksStep.java:49] Initiating mdm clear of [Practitioner]] Golden Resources from Sat Jan 01 00:00:00 UTC 2000 to Sun Jul 17 19:57:27 UTC 2022
//	2022-07-17 19:57:27.381 [awaitility-thread] INFO  c.u.f.b.p.JobInstanceProgressCalculator [JobInstanceProgressCalculator.java:67] Job 6f6d6fc5-f74a-426f-b215-7a383893f4bc of type MDM_CLEAR has status IN_PROGRESS - 0 records processed (null/sec) - ETA: null
//	2022-07-17 19:57:27.510 [awaitility-thread] INFO  c.u.f.b.p.JobInstanceProgressCalculator [JobInstanceProgressCalculator.java:67] Job 6f6d6fc5-f74a-426f-b215-7a383893f4bc of type MDM_CLEAR has status IN_PROGRESS - 0 records processed (null/sec) - ETA: null
//	2022-07-17 19:57:37.175 [awaitility-thread] INFO  c.u.f.b.p.JobInstanceProgressCalculator [JobInstanceProgressCalculator.java:67] Job 6f6d6fc5-f74a-426f-b215-7a383893f4bc of type MDM_CLEAR has status IN_PROGRESS - 0 records processed (null/sec) - ETA: null
//	2022-07-17 19:57:37.329 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:116] Validating MDM types [Patient, Practitioner, Medication]
//	2022-07-17 19:57:37.330 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:133] Validating search parameters [ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@799225ca, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@f03b50d, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@5f19ad6b, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@4976b9, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@681dbf0f]
//	2022-07-17 19:57:37.330 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:161] Validating match fields [ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@7aa4d4dc, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@68444c16, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@23f30319, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@261325af, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@7acd1785, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@30a3d036, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@bf3e6f0]
//	2022-07-17 19:57:37.330 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:253] Validating system URI http://company.io/fhir/NamingSystem/custom-eid-system
//	2022-07-17 19:57:37.335 [main] INFO  c.u.f.j.s.r.ResourceReindexingSvcImpl [ResourceReindexingSvcImpl.java:235] Cancelling and purging all resource reindexing jobs

	//	@Test
	public void testGoldenResourceWithCircularReferenceCanBeCleared() {
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildPaulPatient());
		Patient patientAndUpdateLinks1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks2 = createPatientAndUpdateLinks(buildFrankPatient());

		IAnyResource goldenResourceFromTarget = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		IAnyResource goldenResourceFromTarget1 = getGoldenResourceFromTargetResource(patientAndUpdateLinks1);
		IAnyResource goldenResourceFromTarget2 = getGoldenResourceFromTargetResource(patientAndUpdateLinks2);

		// A -> B -> C -> A linkages.
		linkGoldenResources(goldenResourceFromTarget, goldenResourceFromTarget1);
		linkGoldenResources(goldenResourceFromTarget1, goldenResourceFromTarget2);
		linkGoldenResources(goldenResourceFromTarget2, goldenResourceFromTarget);

		//SUT
		clearMdmLinks();

		printLinks();

		assertNoPatientLinksExist();
		IBundleProvider search = myPatientDao.search(buildGoldenResourceParameterMap());
		assertThat(search.size(), is(equalTo(0)));

	}

	//TODO GGG unclear if we actually need to reimplement this.
	private void linkGoldenResources(IAnyResource theGoldenResource, IAnyResource theTargetResource) {
		// TODO NG - Should be ok to leave this - not really
		// throw new UnsupportedOperationException("We need to fix this!");
		myMdmLinkDaoSvc.createOrUpdateLinkEntity(theGoldenResource, theTargetResource, POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
	}

	@Test
	public void testClearPractitionerLinks() {
		assertLinkCount(2);
		Practitioner read = myPractitionerDao.read(new IdDt(myPractitionerGoldenResourceId.getValueAsString()).toVersionless());
		assertThat(read, is(notNullValue()));
		clearMdmLinks("Practitioner");
		assertNoPractitionerLinksExist();
		try {
			myPractitionerDao.read(new IdDt(myPractitionerGoldenResourceId.getValueAsString()).toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {
		}
	}

	@Test
	public void testClearInvalidTargetType() {
		try {
			myMdmProvider.clearMdmLinks(getResourceNames("Observation"), null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), is(equalTo("HAPI-1500: $mdm-clear does not support resource type: Observation")));
		}
	}

	@Nonnull
	protected List<MdmLink> getPractitionerLinks() {
		return (List<MdmLink>) myMdmLinkDaoSvc.findMdmLinksBySourceResource(myPractitioner);
	}
}
