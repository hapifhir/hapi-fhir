package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.TestUtil;

public class StaleSearchDeletingSvcDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcDstu3Test.class);

	@Override
	public void after() throws Exception {
		super.after();

		myDaoConfig.setExpireSearchResultsAfterMillis(DateUtils.MILLIS_PER_HOUR);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
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
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		try {
			ourClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();
			fail();
		} catch (ResourceGoneException e) {
			assertThat(e.getMessage(), containsString("does not exist and may have expired"));
		}
	}

}
