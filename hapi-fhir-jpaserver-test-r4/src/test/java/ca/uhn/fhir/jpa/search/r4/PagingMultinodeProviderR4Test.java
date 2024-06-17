package ca.uhn.fhir.jpa.search.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.AopTestUtils;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;

public class PagingMultinodeProviderR4Test extends BaseResourceProviderR4Test {

	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(QueryParameterUtils.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(false);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);
		
		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
	}

	/**
	 *
	 * @param theUseCacheBoolean - true if we're using offset search,
	 *                           false if we're using paging id
	 */
	@ParameterizedTest
	@ValueSource(booleans = {
		true, false
	})
	public void testSearch(boolean theUseCacheBoolean) {
		for (int i = 0; i < 100; i++) {
			Patient patient = new Patient();
			String id = "A" + leftPad(Integer.toString(i), 3, '0');
			patient.setId(id);
			patient.addIdentifier().setSystem("urn:system").setValue("A" + i);
			patient.addName().setFamily(id);
			myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		CacheControlDirective directive = new CacheControlDirective();
		directive.setNoStore(theUseCacheBoolean);

		Bundle found;

		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(50);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(10);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(true);

		String[][] resultsPages = new String[][]{
			new String[]{"Patient/A000", "Patient/A001", "Patient/A002", "Patient/A003", "Patient/A004", "Patient/A005", "Patient/A006", "Patient/A007", "Patient/A008", "Patient/A009"},
			new String[]{"Patient/A010", "Patient/A011", "Patient/A012", "Patient/A013", "Patient/A014", "Patient/A015", "Patient/A016", "Patient/A017", "Patient/A018", "Patient/A019"},
			new String[]{"Patient/A020", "Patient/A021", "Patient/A022", "Patient/A023", "Patient/A024", "Patient/A025", "Patient/A026", "Patient/A027", "Patient/A028", "Patient/A029"},
			new String[]{"Patient/A030", "Patient/A031", "Patient/A032", "Patient/A033", "Patient/A034", "Patient/A035", "Patient/A036", "Patient/A037", "Patient/A038", "Patient/A039"}
		};

		// page forward
		int index = 0;
		found = myClient
			.search()
			.forResource(Patient.class)
			.sort().ascending(Patient.SP_FAMILY)
			.count(10)
			.totalMode(SearchTotalModeEnum.ACCURATE)
			.cacheControl(directive)
			.offset(0)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found)).containsExactly(resultsPages[index++]);

		found = myClient
			.loadPage()
			.next(found)
			.cacheControl(directive)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found)).containsExactly(resultsPages[index++]);

		found = myClient
			.loadPage()
			.next(found)
			.cacheControl(directive)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found)).containsExactly(resultsPages[index++]);

		found = myClient
			.loadPage()
			.next(found)
			.cacheControl(directive)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found)).containsExactly(resultsPages[index]);

		// page backwards
		while (index > 0) {
			ourLog.info("Fetching back page {}", index);
			found = myClient
				.loadPage()
				.previous(found)
				.cacheControl(directive)
				.execute();
			assertThat(toUnqualifiedVersionlessIdValues(found)).containsExactly(resultsPages[--index]);
		}
	}


}
