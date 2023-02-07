package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.CrossPartitionReferenceDetails;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CrossPartitionReferencesTest extends BaseJpaR5Test {

	public static final RequestPartitionId PARTITION_PATIENT = RequestPartitionId.fromPartitionId(1);
	public static final RequestPartitionId PARTITION_OBSERVATION = RequestPartitionId.fromPartitionId(2);
	@Mock
	private ICrossPartitionReferenceDetectedHandler myCrossPartitionReferencesDetectedInterceptor;
	@Captor
	private ArgumentCaptor<CrossPartitionReferenceDetails> myCrossPartitionReferenceDetailsCaptor;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_QUALIFIED);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setAlwaysOpenNewTransactionForDifferentPartition(true);

		myInterceptorRegistry.registerInterceptor(new MyPartitionSelectorInterceptor());
		myInterceptorRegistry.registerInterceptor(myCrossPartitionReferencesDetectedInterceptor);
	}

	@AfterEach
	public void after() {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(new PartitionSettings().getAllowReferencesAcrossPartitions());
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
		myPartitionSettings.setAlwaysOpenNewTransactionForDifferentPartition(new PartitionSettings().isAlwaysOpenNewTransactionForDifferentPartition());

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyPartitionSelectorInterceptor);
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof ICrossPartitionReferenceDetectedHandler);
	}

	@Test
	public void testSamePartitionReference_Create() {
		// Setup
		Patient p1 = new Patient();
		p1.setActive(true);
		IIdType patient1Id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addLink().setOther(new Reference(patient1Id));

		// Test
		IIdType patient2Id = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();
		myCaptureQueriesListener.logSelectQueries();

		// Verify
		assertEquals(1, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false, false),
			matchesPattern("select.*from HFJ_RESOURCE.*where.*RES_ID in.*PARTITION_ID in.*"));
		assertEquals(1, myCaptureQueriesListener.getSelectQueries().get(0).getRequestPartitionId().getFirstPartitionIdOrNull());

		// Test fetching with _include
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(Constants.PARAM_ID, new TokenParam(patient2Id.getValue()))
			.addInclude(Patient.INCLUDE_LINK);
		IBundleProvider search = myPatientDao.search(params, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(patient2Id.getValue(), patient1Id.getValue()));
		assertEquals(2, search.getAllResources().size());
		search.getAllResources().forEach(p -> assertTrue(((Patient) p).getActive()));

		verify(myCrossPartitionReferencesDetectedInterceptor, never()).handle(any(), any());
	}

	@Test
	public void testSamePartitionReference_SearchWithInclude() {
		// Setup
		Patient p1 = new Patient();
		p1.setActive(true);
		IIdType patient1Id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addLink().setOther(new Reference(patient1Id));
		IIdType patient2Id = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		// Test
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(Constants.PARAM_ID, new TokenParam(patient2Id.getValue()))
			.addInclude(Patient.INCLUDE_LINK);
		IBundleProvider search = myPatientDao.search(params, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(patient2Id.getValue(), patient1Id.getValue()));
		assertEquals(2, search.getAllResources().size());
		search.getAllResources().forEach(p -> assertTrue(((Patient) p).getActive()));

		verify(myCrossPartitionReferencesDetectedInterceptor, never()).handle(any(), any());
	}

	@Test
	public void testCrossPartitionReference_Create() {
		// Setup
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		Observation observation = new Observation();
		observation.setSubject(new Reference(patientId));
		IIdType observationId = myObservationDao.create(observation, mySrd).getId().toUnqualifiedVersionless();

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false),
			matchesPattern("select .* from HFJ_RESOURCE .* where .*RES_ID in.*PARTITION_ID in \\('1'\\).*"));
		assertEquals(1, myCaptureQueriesListener.getSelectQueries().get(0).getRequestPartitionId().getFirstPartitionIdOrNull());
	}

	@Test
	public void testCreateCrossPartitionReferences() {
		doAnswer(t -> {
			CrossPartitionReferenceDetails refDetails = t.getArgument(1, CrossPartitionReferenceDetails.class);
			return refDetails.getTargetResource();
		}).when(myCrossPartitionReferencesDetectedInterceptor).handle(any(), any());

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
		o.setSubject(new Reference(patientId));
		IIdType observationId = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless();
		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(observationId.getIdPartAsLong()).orElseThrow();
			assertEquals(2, resourceTable.getPartitionId().getPartitionId());
		});

		verify(myCrossPartitionReferencesDetectedInterceptor, times(1)).handle(eq(Pointcut.JPA_CROSS_PARTITION_REFERENCE_DETECTED), myCrossPartitionReferenceDetailsCaptor.capture());
		CrossPartitionReferenceDetails referenceDetails = myCrossPartitionReferenceDetailsCaptor.getValue();
		assertEquals(PARTITION_OBSERVATION, referenceDetails.getSourceResourcePartitionId());
		assertEquals(PARTITION_PATIENT, referenceDetails.getTargetResourcePartitionId());
	}

	@Interceptor
	public interface ICrossPartitionReferenceDetectedHandler {

		@Hook(Pointcut.JPA_CROSS_PARTITION_REFERENCE_DETECTED)
		IResourceLookup<JpaPid> handle(Pointcut thePointcut, CrossPartitionReferenceDetails theDetails);

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
					return PARTITION_PATIENT;
				case "Observation":
					return PARTITION_OBSERVATION;
				default:
					throw new InternalErrorException("Don't know how to handle resource type");
			}
		}

	}

}
