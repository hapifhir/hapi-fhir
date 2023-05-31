package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseHapiFhirResourceDaoTest {

	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Mock
	private IIdHelperService myIdHelperService;

	@Mock
	private EntityManager myEntityManager;

	@Mock
	private JpaStorageSettings myStorageSettings;

	@Mock
	private IJobCoordinator myJobCoordinator;

	@Mock
	private UrlPartitioner myUrlPartitioner;

	@Mock
	private ApplicationContext myApplicationContext;

	@Mock
	private ISearchParamRegistry mySearchParamRegistry;

	// we won't inject this
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@InjectMocks
	private TestResourceDao mySvc;

	@BeforeEach
	public void init() {
		// set our context
		// NB: if other tests need to
		// have access to resourcetype/name
		// the individual tests will have to start
		// by calling setup themselves
		mySvc.setContext(myFhirContext);
	}

	/**
	 * To be called for tests that require additional
	 * setup
	 *
	 * @param clazz
	 */
	private void setup(Class clazz) {
		mySvc.setResourceType(clazz);
		mySvc.start();
	}

	@Test
	public void validateResourceIdCreation_asSystem() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new SystemRequestDetails();
		mySvc.getStorageSettings().setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	@Test
	public void validateResourceIdCreation_asUser() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new ServletRequestDetails();
		when(myStorageSettings.getResourceClientIdStrategy()).thenReturn(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(959) + "failedToCreateWithClientAssignedIdNotAllowed", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withNumber() {
		Patient patient = new Patient();
		patient.setId("2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		when(myStorageSettings.getResourceClientIdStrategy()).thenReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(960) + "failedToCreateWithClientAssignedNumericId", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withAlpha() {
		Patient patient = new Patient();
		patient.setId("P2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getStorageSettings().setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	@Test
	public void delete_nonExistentEntity_doesNotThrow404() {
		// initialize our class
		setup(Patient.class);

		// setup
		when(myStorageSettings.isDeleteEnabled()).thenReturn(true);

		IIdType id = new IdType("Patient/123"); // id part is only numbers
		DeleteConflictList deleteConflicts = new DeleteConflictList();
		RequestDetails requestDetails = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();

		RequestPartitionId partitionId = Mockito.mock(RequestPartitionId.class);
		JpaPid jpaPid = JpaPid.fromIdAndVersion(123L, 1L);
		ResourceTable entity = new ResourceTable();
		entity.setForcedId(new ForcedId());

		// mock
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
			Mockito.any(RequestDetails.class),
			Mockito.anyString(),
			Mockito.any(IIdType.class)
		)).thenReturn(partitionId);
		when(myIdHelperService.resolveResourcePersistentIds(
			Mockito.any(RequestPartitionId.class),
			Mockito.anyString(),
			Mockito.anyString()
		)).thenReturn(jpaPid);
		when(myEntityManager.find(
			Mockito.any(Class.class),
			Mockito.anyLong()
		)).thenReturn(entity);
		// we don't stub myConfig.getResourceClientIdStrategy()
		// because even a null return isn't ANY...
		// if this changes, though, we will have to stub it.
		// but for now, Mockito will complain, so we'll leave it out

		// test
		DaoMethodOutcome outcome = mySvc.delete(id, deleteConflicts, requestDetails, transactionDetails);

		// verify
		Assertions.assertNotNull(outcome);
		Assertions.assertEquals(id.getValue(), outcome.getId().getValue());
	}

	@Test
	public void requestReindexForRelatedResources_withValidBase_includesUrlsInJobParameters() {
		when(myStorageSettings.isMarkResourcesForReindexingUponSearchParameterChange()).thenReturn(true);

		List<String> base = Lists.newArrayList("Patient", "Group");

		when(myUrlPartitioner.partitionUrl(Mockito.any(), Mockito.any())).thenAnswer(i -> {
			PartitionedUrl partitionedUrl = new PartitionedUrl();
			partitionedUrl.setUrl(i.getArgument(0));
			return partitionedUrl;
		});

		mySvc.requestReindexForRelatedResources(false, base, new ServletRequestDetails());

		ArgumentCaptor<JobInstanceStartRequest> requestCaptor = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		Mockito.verify(myJobCoordinator).startInstance(isNotNull(), requestCaptor.capture());

		JobInstanceStartRequest actualRequest = requestCaptor.getValue();
		assertNotNull(actualRequest);
		assertNotNull(actualRequest.getParameters());
		ReindexJobParameters actualParameters = actualRequest.getParameters(ReindexJobParameters.class);

		assertEquals(2, actualParameters.getPartitionedUrls().size());
		assertEquals("Patient?", actualParameters.getPartitionedUrls().get(0).getUrl());
		assertEquals("Group?", actualParameters.getPartitionedUrls().get(1).getUrl());
	}

	@Test
	public void requestReindexForRelatedResources_withSpecialBaseResource_doesNotIncludeUrlsInJobParameters() {
		when(myStorageSettings.isMarkResourcesForReindexingUponSearchParameterChange()).thenReturn(true);

		List<String> base = Lists.newArrayList("Resource");

		mySvc.requestReindexForRelatedResources(false, base, new ServletRequestDetails());

		ArgumentCaptor<JobInstanceStartRequest> requestCaptor = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		Mockito.verify(myJobCoordinator).startInstance(isNotNull(), requestCaptor.capture());

		JobInstanceStartRequest actualRequest = requestCaptor.getValue();
		assertNotNull(actualRequest);
		assertNotNull(actualRequest.getParameters());
		ReindexJobParameters actualParameters = actualRequest.getParameters(ReindexJobParameters.class);

		assertEquals(0, actualParameters.getPartitionedUrls().size());
	}

	static class TestResourceDao extends BaseHapiFhirResourceDao<Patient> {

		@Override
		public JpaStorageSettings getStorageSettings() {
			return myStorageSettings;
		}

		@Override
		protected String getMessageSanitized(String theKey, String theIdPart) {
			return theKey;
		}
	}
}
