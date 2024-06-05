package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.CrossPartitionReferenceDetails;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.jpa.util.JpaHapiTransactionService;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;

import jakarta.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CrossPartitionReferencesTest extends BaseJpaR5Test {

	public static final RequestPartitionId PARTITION_PATIENT = RequestPartitionId.fromPartitionId(1);
	public static final RequestPartitionId PARTITION_OBSERVATION = RequestPartitionId.fromPartitionId(2);
	@Autowired
	private JpaHapiTransactionService myTransactionSvc;
	@Autowired
	private IRequestPartitionHelperSvc myPartitionHelperSvc;
	@Autowired
	private IHapiTransactionService myTransactionService;
	@Autowired
	private IResourceLinkResolver myResourceLinkResolver;
	@Mock
	private ICrossPartitionReferenceDetectedHandler myCrossPartitionReferencesDetectedInterceptor;
	@Captor
	private ArgumentCaptor<CrossPartitionReferenceDetails> myCrossPartitionReferenceDetailsCaptor;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setAlwaysOpenNewTransactionForDifferentPartition(true);

		myInterceptorRegistry.registerInterceptor(new MyPartitionSelectorInterceptor());
		myInterceptorRegistry.registerInterceptor(myCrossPartitionReferencesDetectedInterceptor);

		myTransactionSvc.setTransactionPropagationWhenChangingPartitions(Propagation.REQUIRES_NEW);
	}

	@AfterEach
	public void after() {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(new PartitionSettings().getAllowReferencesAcrossPartitions());
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
		myPartitionSettings.setAlwaysOpenNewTransactionForDifferentPartition(new PartitionSettings().isAlwaysOpenNewTransactionForDifferentPartition());

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyPartitionSelectorInterceptor);
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof ICrossPartitionReferenceDetectedHandler);

		myTransactionSvc.setTransactionPropagationWhenChangingPartitions(Propagation.REQUIRED);
	}

	@Test
	public void testSamePartitionReference_Create() {
		// Setup
		Patient p1 = new Patient();
		p1.setActive(true);
		IIdType patient1Id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		initializeCrossReferencesInterceptor();

		myCaptureQueriesListener.clear();
		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addLink().setOther(new Reference(patient1Id));

		// Test
		myCaptureQueriesListener.clear();
		IIdType patient2Id = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		SearchParameterMap params = SearchParameterMap
			.newSynchronous(Constants.PARAM_ID, new TokenParam(patient2Id.getValue()))
			.addInclude(Patient.INCLUDE_LINK);
		IBundleProvider search = myPatientDao.search(params, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(search)).containsExactly(patient2Id.getValue(), patient1Id.getValue());
		assertThat(search.getAllResources()).hasSize(2);
		search.getAllResources().forEach(p -> assertTrue(((Patient) p).getActive()));
	}

	@Test
	public void testSamePartitionReference_SearchWithInclude() {
		// Setup
		Patient p1 = new Patient();
		p1.setActive(true);
		IIdType patient1Id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		initializeCrossReferencesInterceptor();

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
		assertThat(toUnqualifiedVersionlessIdValues(search)).containsExactly(patient2Id.getValue(), patient1Id.getValue());
		assertThat(search.getAllResources()).hasSize(2);
		search.getAllResources().forEach(p -> assertTrue(((Patient) p).getActive()));
	}

	@Test
	public void testCrossPartitionReference_Create() {

		// Setup
		Patient p = new Patient();
		p.setActive(true);
		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Patient ID: {}", patientId);
		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(patientId.getIdPartAsLong()).orElseThrow();
			assertEquals(1, resourceTable.getPartitionId().getPartitionId());
		});

		initializeCrossReferencesInterceptor();

		// Test

		Observation o = new Observation();
		o.setStatus(Enumerations.ObservationStatus.FINAL);
		o.setSubject(new Reference(patientId));
		myCaptureQueriesListener.clear();
		IIdType observationId = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless();

		// Verify
		assertEquals(2, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(observationId.getIdPartAsLong()).orElseThrow();
			assertEquals(2, resourceTable.getPartitionId().getPartitionId());
		});

		verify(myCrossPartitionReferencesDetectedInterceptor, times(1)).handle(eq(Pointcut.JPA_RESOLVE_CROSS_PARTITION_REFERENCE), myCrossPartitionReferenceDetailsCaptor.capture());
		CrossPartitionReferenceDetails referenceDetails = myCrossPartitionReferenceDetailsCaptor.getValue();
		assertEquals(PARTITION_OBSERVATION, referenceDetails.getSourceResourcePartitionId());
		assertEquals(patientId.getValue(), referenceDetails.getPathAndRef().getRef().getReferenceElement().getValue());
	}

	private void initializeCrossReferencesInterceptor() {
		when(myCrossPartitionReferencesDetectedInterceptor.handle(any(),any())).thenAnswer(t->{
			CrossPartitionReferenceDetails theDetails = t.getArgument(1, CrossPartitionReferenceDetails.class);
			IIdType targetId = theDetails.getPathAndRef().getRef().getReferenceElement();
			RequestPartitionId referenceTargetPartition = myPartitionHelperSvc.determineReadPartitionForRequestForRead(theDetails.getRequestDetails(), targetId.getResourceType(), targetId);

			IResourceLookup targetResource = myTransactionService
				.withRequest(theDetails.getRequestDetails())
				.withTransactionDetails(theDetails.getTransactionDetails())
				.withRequestPartitionId(referenceTargetPartition)
				.execute(() -> myResourceLinkResolver.findTargetResource(referenceTargetPartition, theDetails.getSourceResourceName(), theDetails.getPathAndRef(), theDetails.getRequestDetails(), theDetails.getTransactionDetails()));

			return targetResource;
		});
	}

	@Interceptor
	public interface ICrossPartitionReferenceDetectedHandler {

		@Hook(Pointcut.JPA_RESOLVE_CROSS_PARTITION_REFERENCE)
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
