package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import ca.uhn.fhir.util.BundleUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hl7.fhir.instance.model.api.IBaseBundle.LINK_NEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("Duplicates")
public class PatientEverythingPaginationR4Test extends BaseResourceProviderR4Test {

	private int myOriginalServerDefaultPageSize;

	@BeforeEach
	public void beforeDisableResultReuse() {
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);

		myOriginalServerDefaultPageSize = myServer.getDefaultPageSize();
		myServer.setDefaultPageSize(50);

	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myServer.setDefaultPageSize(myOriginalServerDefaultPageSize);
	}

	/**
	 * Built to reproduce <a href="https://gitlab.com/simpatico.ai/cdr/-/issues/4940">this issue</a>
	 * Notice that the issue is not gateway related. Is a plain server issue.
	 */
	@Test
	public void testEverythingPaginatesThroughAllPatients_whenCountIsEqualToMaxPageSize() throws IOException {
		// setup
		int totalPatients = 54;
		for (int i = 0; i < totalPatients; i++) {
			Patient patient = new Patient();
			patient.addName().setFamily("lastn").addGiven("name");
			myPatientDao.create(patient, new SystemRequestDetails()).getId().toUnqualifiedVersionless();
		}

		// test
		Bundle bundle = fetchBundle(myServerBase + "/Patient/$everything?_format=json&_count=" + BasePagingProvider.DEFAULT_MAX_PAGE_SIZE);

		// verify
		List<Patient> patientsFirstPage = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Patient.class);
		assertEquals(50, patientsFirstPage.size());

		String nextUrl = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, LINK_NEXT);
		System.out.println(nextUrl);
		assertNotNull(nextUrl);
		Bundle page2 = fetchBundle(nextUrl);
		assertNotNull(page2);
		List<Patient> patientsPage2 = BundleUtil.toListOfResourcesOfType(myFhirContext, page2, Patient.class);

		assertEquals(4, patientsPage2.size());
	}


	private Bundle fetchBundle(String theUrl) throws IOException {
		Bundle bundle;
		HttpGet get = new HttpGet(theUrl);
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy(), resp.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue().replaceAll(";.*", ""));
			bundle = EncodingEnum.JSON.newParser(myFhirContext).parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
		} finally {
			IOUtils.closeQuietly(resp);
		}

		return bundle;
	}


}
