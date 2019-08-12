package ca.uhn.fhir.jpa.search;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.test.util.AopTestUtils;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.util.TestUtil;

public class PagingMultinodeProviderDstu3Test extends BaseResourceProviderDstu3Test {

	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(SearchCoordinatorSvcImpl.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(false);
	}

	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		
		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(ourSearchCoordinatorSvc);
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

		found = ourClient
				.search()
				.forResource(Patient.class)
				.sort().ascending(Patient.SP_FAMILY)
				.count(10)
				.returnBundle(Bundle.class)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A000", "Patient/A001", "Patient/A002", "Patient/A003", "Patient/A004", "Patient/A005", "Patient/A006", "Patient/A007", "Patient/A008", "Patient/A009"));

		found = ourClient
				.loadPage()
				.next(found)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A010", "Patient/A011", "Patient/A012", "Patient/A013", "Patient/A014", "Patient/A015", "Patient/A016", "Patient/A017", "Patient/A018", "Patient/A019"));

		found = ourClient
				.loadPage()
				.next(found)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A020", "Patient/A021", "Patient/A022", "Patient/A023", "Patient/A024", "Patient/A025", "Patient/A026", "Patient/A027", "Patient/A028", "Patient/A029"));

		found = ourClient
				.loadPage()
				.next(found)
				.execute();
		assertThat(toUnqualifiedVersionlessIdValues(found), contains("Patient/A030", "Patient/A031", "Patient/A032", "Patient/A033", "Patient/A034", "Patient/A035", "Patient/A036", "Patient/A037", "Patient/A038", "Patient/A039"));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
