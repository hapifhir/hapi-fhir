package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.config.BlockListConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Exercises the mdm-unmatched tag over the broker, where MDM runs on its own thread against the resource carried in
 * the message rather than one the test handed it. The synchronous tests in {@link MdmMatchLinkSvcTest} cannot show
 * whether that payload still carries the tag, which is what decides if the tag can be cleared at all.
 */
public class MdmUnmatchedTagIT extends BaseMdmR4Test {

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	private int myOriginalCandidateSearchLimit;

	// JUnit also runs these for the nested class's tests, so the limit is saved here rather than in a test
	@BeforeEach
	public void saveCandidateSearchLimit() {
		myOriginalCandidateSearchLimit = myMdmSettings.getCandidateSearchLimit();
	}

	@AfterEach
	public void restoreCandidateSearchLimit() {
		myMdmSettings.setCandidateSearchLimit(myOriginalCandidateSearchLimit);
	}

	@Test
	public void asyncResubmission_ofAResourceThatNowMatches_clearsTheUnmatchedTag() throws InterruptedException {

		// setup - one jane already in the repository to act as a candidate, then a limit low enough that the
		// next one cannot be narrowed down
		Patient firstJane = buildJanePatient();
		firstJane.setActive(true);
		myMdmHelper.createWithLatch(firstJane);

		myMdmSettings.setCandidateSearchLimit(1);

		Patient jane = buildJanePatient();
		jane.setActive(true);
		IIdType id = myMdmHelper.createWithLatch(jane).getDaoMethodOutcome()
				.getId()
				.toUnqualifiedVersionless();
		assertTrue(MdmResourceUtil.resourceHasTagWithSystem(
				myPatientDao.read(id, new SystemRequestDetails()), MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE));

		// test - with the limit restored, resubmit her and let the broker drive the MDM pass
		myMdmSettings.setCandidateSearchLimit(myOriginalCandidateSearchLimit);
		Patient resubmitted = myPatientDao.read(id, new SystemRequestDetails());
		// an update with identical content is a no-op, and a no-op is never submitted to MDM. Gender is neither a
		// candidate search param nor a match field in the test rules, so this changes the resource without
		// changing who it matches.
		resubmitted.setGender(Enumerations.AdministrativeGender.FEMALE);
		myMdmHelper.updateWithLatch(resubmitted);

		// verify
		assertFalse(MdmResourceUtil.resourceHasTagWithSystem(
				myPatientDao.read(id, new SystemRequestDetails()), MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE));
	}

	@Nested
	@ContextConfiguration(classes = {BlockListConfig.class})
	public class BlockedResourceTest extends BaseMdmR4Test {

		@RegisterExtension
		@Autowired
		public MdmHelperR4 myMdmHelper;

		@Autowired
		private IBlockListRuleProvider myBlockListRuleProvider;

		private boolean myOriginalTriggerForNonVersioningChanges;

		@AfterEach
		public void restoreSubscriptionSettings() {
			mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(
					myOriginalTriggerForNonVersioningChanges);
		}

		/**
		 * Tagging a blocked resource is a non-versioning update. When such updates trigger subscriptions, the tag write
		 * sends the resource through MDM again, and that pass must keep the golden resource the first pass created.
		 */
		@Test
		public void create_blockedResourceWithNonVersioningChangesTriggeringSubscriptions_createsOneGoldenResource() {
			// setup
			myOriginalTriggerForNonVersioningChanges =
					mySubscriptionSettings.isTriggerSubscriptionsForNonVersioningChanges();
			mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(true);

			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField().setFhirPath("name.single().family").setBlockedValue("Doe");
			rule.addBlockListField().setFhirPath("name.single().given.first()").setBlockedValue("Jane");
			blockListJson.addBlockListRule(rule);
			when(myBlockListRuleProvider.getBlocklistRules()).thenReturn(blockListJson);

			// one tag write can reach MDM more than once (twice in VERSIONED tag storage mode), which the helper's
			// latch rejects, so count the passes instead
			myInterceptorRegistry.unregisterInterceptor(myMdmHelper.getAfterMdmLatch());
			AtomicInteger mdmPasses = new AtomicInteger();
			IAnonymousInterceptor passCounter = (thePointcut, theArgs) -> mdmPasses.incrementAndGet();
			myInterceptorRegistry.registerAnonymousInterceptor(
					Pointcut.MDM_AFTER_PERSISTED_RESOURCE_CHECKED, passCounter);
			try {
				// execute - the create is the first MDM pass; the tag it writes triggers the others
				Patient blocked = (Patient) myMdmHelper.doCreateResource(buildJanePatient(), true).getResource();
				await().atMost(Duration.ofSeconds(30))
						.during(Duration.ofSeconds(1))
						.until(() -> mdmPasses.get() >= 2 && myMdmHelper.getExecutorQueueSize() == 0);

				// validate
				assertEquals(1, getAllGoldenPatients().size());
				assertEquals(1, myMdmLinkDaoSvc.findMdmLinksBySourceResource(blocked).size());
			} finally {
				myInterceptorRegistry.unregisterInterceptor(passCounter);
			}
		}
	}
}
