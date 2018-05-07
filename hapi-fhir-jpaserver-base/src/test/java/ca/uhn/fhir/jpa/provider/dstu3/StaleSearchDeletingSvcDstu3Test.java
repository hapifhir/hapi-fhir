package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.AopTestUtils;

import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvcImpl;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.TestUtil;

public class StaleSearchDeletingSvcDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcDstu3Test.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@After()
	public void after() throws Exception {
		super.after();
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(StaleSearchDeletingSvcImpl.DEFAULT_CUTOFF_SLACK);
	}

	@Before
	public void before() throws Exception {
		super.before();
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(0);
	}

	@Test
	public void testEverythingInstanceWithContentFilter() throws Exception {

		for (int i = 0; i < 20; i++) {
			Patient pt1 = new Patient();
			pt1.addName().setFamily("Everything").addGiven("Arthur");
			myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();
		}

		//@formatter:off
		IClientExecutable<IQuery<Bundle>, Bundle> search = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("Everything"))
			.returnBundle(Bundle.class);
		//@formatter:on

		Bundle resp1 = search.execute();

		for (int i = 0; i < 20; i++) {
			search.execute();
		}

		BundleLinkComponent nextLink = resp1.getLink("next");
		assertNotNull(nextLink);
		String nextLinkUrl = nextLink.getUrl();
		assertThat(nextLinkUrl, not(blankOrNullString()));

		Bundle resp2 = ourClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp2));

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		ourClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();

		Thread.sleep(20);
		myDaoConfig.setExpireSearchResultsAfterMillis(10);
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		try {
			ourClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();
			fail();
		} catch (ResourceGoneException e) {
			assertThat(e.getMessage(), containsString("does not exist and may have expired"));
		}
	}

}
