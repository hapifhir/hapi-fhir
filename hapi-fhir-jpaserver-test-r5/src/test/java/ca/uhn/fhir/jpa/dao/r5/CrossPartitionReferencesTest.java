package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrossPartitionReferencesTest extends BaseJpaR5Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_QUALIFIED);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setAlwaysOpenNewTransactionForDifferentPartition(true);

		myInterceptorRegistry.registerInterceptor(new MyPartitionSelectorInterceptor());
	}

	@AfterEach
	public void after() {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(new PartitionSettings().getAllowReferencesAcrossPartitions());
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
		myPartitionSettings.setAlwaysOpenNewTransactionForDifferentPartition(new PartitionSettings().isAlwaysOpenNewTransactionForDifferentPartition());

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyPartitionSelectorInterceptor);
	}


	@Test
	public void testCreateSamePartitionReference() {
		Patient p1 = new Patient();
		p1.setActive(true);
		IIdType patient1Id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addLink().setOther(new Reference(patient1Id));
		IIdType patient2Id = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false, false),
			matchesPattern("select.*from HFJ_RESOURCE.*where.*RES_ID in.*PARTITION_ID in.*"));
		assertEquals(1, myCaptureQueriesListener.getSelectQueries().get(0).getRequestPartitionId().getFirstPartitionIdOrNull());
	}


	@Test
	public void testCreateCrossPartitionReferences() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Patient ID: {}", patientId);
		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(patientId.getIdPartAsLong()).orElseThrow();
			assertEquals(1, resourceTable.getPartitionId().getPartitionId());
		});

		Observation o = new Observation();
		o.setStatus(Enumerations.ObservationStatus.FINAL);
		IIdType observationId = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless();
		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(observationId.getIdPartAsLong()).orElseThrow();
			assertEquals(2, resourceTable.getPartitionId().getPartitionId());
		});

	}


	public class MyPartitionSelectorInterceptor {

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId selectPartitionCreate(IBaseResource theResource) {
			String resourceType = myFhirContext.getResourceType(theResource);
			return selectPartition(resourceType);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId selectPartitionRead(ReadPartitionIdRequestDetails theReadPartitionIdRequestDetails) {
			String resourceType = theReadPartitionIdRequestDetails.getResourceType();
			return selectPartition(resourceType);
		}

		@Nonnull
		private static RequestPartitionId selectPartition(String resourceType) {
			switch (resourceType) {
				case "Patient":
					return RequestPartitionId.fromPartitionId(1);
				case "Observation":
					return RequestPartitionId.fromPartitionId(2);
				default:
					throw new InternalErrorException("Don't know how to handle resource type");
			}
		}

	}

}
