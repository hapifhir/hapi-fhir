package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.instance.model.api.IBaseBundle.LINK_NEXT;

@SuppressWarnings("Duplicates")
public class PatientEverythingPaginationR4Test extends BaseResourceProviderR4Test {

	private int myOriginalServerDefaultPageSize;

	@Autowired
	JpaStorageSettings myStorageSettings;

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
		createPatients(totalPatients);

		String url = myServerBase + "/Patient/$everything?_format=json&_count=" + BasePagingProvider.DEFAULT_MAX_PAGE_SIZE;

		// test
		Bundle bundle = fetchBundle(url);

		// first page
		List<Patient> patientsFirstPage = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Patient.class);
		assertThat(patientsFirstPage).hasSize(50);

		String nextUrl = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, LINK_NEXT);

		// 2nd/last page
		assertNotNull(nextUrl);
		Bundle page2 = fetchBundle(nextUrl);
		assertNotNull(page2);
		List<Patient> patientsPage2 = BundleUtil.toListOfResourcesOfType(myFhirContext, page2, Patient.class);

		assertThat(patientsPage2).hasSize(4);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testEverythingTypeOperationPagination_withDifferentPrefetchThresholds_coverageTest(boolean theProvideCountBool) throws IOException {
		// setup
		List<Integer> previousPrefetchThreshold = myStorageSettings.getSearchPreFetchThresholds();
		// other tests may be resetting this
		// so we'll set it
		int pageSize = myPagingProvider.getDefaultPageSize();
		int serverPageSize = myServer.getDefaultPageSize();
		try {
			int defaultPageSize = theProvideCountBool ? 50 : 10;
			// set our prefetch thresholds to ensure we run out of them
			List<Integer> prefetchThreshold = Arrays.asList(10, 50, -1);
			myStorageSettings.setSearchPreFetchThresholds(prefetchThreshold);

			// the number of patients to create
			int total = 154;
			String nextUrl;
			createPatients(total);
			Set<String> ids = new HashSet<>();

			String url = myServerBase + "/Patient/$everything?_format=json";
			if (theProvideCountBool) {
				url += "&_count=" + BasePagingProvider.DEFAULT_MAX_PAGE_SIZE;
			}
			myPagingProvider.setDefaultPageSize(defaultPageSize);
			myServer.setDefaultPageSize(defaultPageSize);

			// test
			Bundle bundle = fetchBundle(url);

			// first page
			List<Patient> patientsPage = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Patient.class);
			assertThat(patientsPage).hasSize(defaultPageSize);

			for (Patient p : patientsPage) {
				assertTrue(ids.add(p.getId()));
			}
			nextUrl = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, LINK_NEXT);
			assertNotNull(nextUrl);

			// all future pages
			do {
				bundle = fetchBundle(nextUrl);
				assertNotNull(bundle);
				patientsPage = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Patient.class);
				for (Patient p : patientsPage) {
					assertTrue(ids.add(p.getId()));
				}
				nextUrl = BundleUtil.getLinkUrlOfType(myFhirContext, bundle, LINK_NEXT);
				if (nextUrl != null) {
					assertThat(patientsPage).hasSize(defaultPageSize);
				} else {
					assertThat(patientsPage).hasSize(4);
				}
			} while (nextUrl != null);

			// ensure we found everything
			assertThat(ids).hasSize(total);
		} finally {
			// set it back, just in case
			myStorageSettings.setSearchPreFetchThresholds(previousPrefetchThreshold);
			myPagingProvider.setDefaultPageSize(pageSize);
			myServer.setDefaultPageSize(serverPageSize);
		}
	}

	private void createPatients(int theCount) {
		for (int i = 0; i < theCount; i++) {
			Patient patient = new Patient();
			patient.addName().setFamily("lastn").addGiven("name");
			myPatientDao.create(patient, new SystemRequestDetails()).getId().toUnqualifiedVersionless();
		}
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
