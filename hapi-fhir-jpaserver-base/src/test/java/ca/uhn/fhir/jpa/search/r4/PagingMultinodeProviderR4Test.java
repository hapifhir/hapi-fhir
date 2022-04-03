package ca.uhn.fhir.jpa.search.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.parser.StrictErrorHandler;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AopTestUtils;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class PagingMultinodeProviderR4Test extends BaseResourceProviderR4Test {

	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(SearchCoordinatorSvcImpl.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(false);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		
		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
	}

	@Test
	public void testSearch() {
		{
			for (int i = 0; i < 100; i++) {
				Patient patient = new Patient();
				String id = "A" + leftPad(Integer.toString(i), 3, '0');
				patient.setId(id);
				patient.addIdentifier().setSystem("urn:system").setValue("A" + i);
				patient.addName().setFamily(id);
				myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
			}
		}

		Bundle found;

		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(50);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(10);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(true);

		found = myClient
				.search()
				.forResource(Patient.class)
				.sort().ascending(Patient.SP_FAMILY)
				.count(10)
				.returnBundle(Bundle.class)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A000", "Patient/A001", "Patient/A002", "Patient/A003", "Patient/A004", "Patient/A005", "Patient/A006", "Patient/A007", "Patient/A008", "Patient/A009"));

		found = myClient
				.loadPage()
				.next(found)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A010", "Patient/A011", "Patient/A012", "Patient/A013", "Patient/A014", "Patient/A015", "Patient/A016", "Patient/A017", "Patient/A018", "Patient/A019"));

		found = myClient
				.loadPage()
				.next(found)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A020", "Patient/A021", "Patient/A022", "Patient/A023", "Patient/A024", "Patient/A025", "Patient/A026", "Patient/A027", "Patient/A028", "Patient/A029"));

		found = myClient
				.loadPage()
				.next(found)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A030", "Patient/A031", "Patient/A032", "Patient/A033", "Patient/A034", "Patient/A035", "Patient/A036", "Patient/A037", "Patient/A038", "Patient/A039"));
	}


}
