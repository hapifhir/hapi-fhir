package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.jpa.dao.r5.conditionalid.ConditionalIdFiltered_PartitioningEnabledTest.PARTITION_1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public abstract class TestDefinitions implements ITestDataBuilder {

	private final PartitionSelectorInterceptor myPartitionSelectorInterceptor;
	private final CircularQueueCaptureQueriesListener myCaptureQueriesListener;
	private final boolean myIncludePartitionIdsInSql;

	@Autowired
	private IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	private DaoRegistry myDaoRegistry;

	public TestDefinitions(@Nonnull CircularQueueCaptureQueriesListener theCaptureQueriesListener, @Nonnull PartitionSelectorInterceptor thePartitionSelectorInterceptor, boolean theIncludePartitionIdsInSql) {
		myCaptureQueriesListener = theCaptureQueriesListener;
		myPartitionSelectorInterceptor = thePartitionSelectorInterceptor;
		myIncludePartitionIdsInSql = theIncludePartitionIdsInSql;
	}

	@Test
	public void testRead() {
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		createPatient(withId("A"), withActiveTrue());

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.read(new IdType("Patient/A"), newRequest());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSql(0)).endsWith(" where rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID in ('A') and rt1_0.PARTITION_ID in ('1')");
		} else {
			assertThat(getSql(0)).endsWith(" where rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID in ('A')");
		}
	}

	private SystemRequestDetails newRequest() {
		return new SystemRequestDetails();
	}


	@Language("SQL")
	private String getSql(int theIndex) {
		return myCaptureQueriesListener.getSelectQueries().get(theIndex).getSql(true, false);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.create(theResource, newRequest()).getId().toUnqualifiedVersionless();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.update(theResource, newRequest()).getId().toUnqualifiedVersionless();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}

}
