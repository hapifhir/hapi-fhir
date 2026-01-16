package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.method.ConformanceMethodBinding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HapiFhirRepositoryTest {

	/**
	 * These are the implemented methods in HapiFhirRepository
	 */
	public enum Method {
		READ,
		CREATE,
		PATCH,
		UPDATE,
		DELETE,
		SEARCH,
		LINK,
		CAPABILITIES,
		TRANSACTION
	}

	private static class TestHapiFhirRepository extends HapiFhirRepository {

		private Consumer<RequestDetails> myConsumerLatch;

		public TestHapiFhirRepository(DaoRegistry theDaoRegistry, RequestDetails theRequestDetails, RestfulServer theRestfulServer) {
			super(theDaoRegistry, theRequestDetails, theRestfulServer);
		}

		public void setConsumerLatch(Consumer<RequestDetails> theLatch) {
			myConsumerLatch = theLatch;
		}

		@Override
		protected RequestDetails createRequestDetails(Consumer<RequestDetailsCloner.DetailsBuilder> theConsumer) {
			RequestDetails details = super.createRequestDetails(theConsumer);

			if (myConsumerLatch != null) {
				myConsumerLatch.accept(details);
			}
			return details;
		}
	}


	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private RestfulServer myRestfulServer;

	@Mock
	private IFhirResourceDao<?> myResourceDao;

	@BeforeEach
	public void before() {
		// lenient because it's not always needed, but always true
		lenient().when(myDaoRegistry.getFhirContext())
			.thenReturn(myFhirContext);
		lenient().when(myRestfulServer.getFhirContext())
			.thenReturn(myFhirContext);
	}

	@ParameterizedTest
	@EnumSource(Method.class)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void apis_copyRequestDetailsAsExpected(Method theAPI) {
		// setup
		Class<? extends IBaseResource> patientClass = Patient.class;
		Map<String, String> headers = new HashMap<>();

		Map<String, String[]> parameters = new HashMap<>();
		parameters.put("key", new String[]{"value"});
		IBaseResource resource = new Patient().setId("Patient/123");

		// these values don't matter; we just want to make sure they're copied over
		RequestDetails startingRequestDetails = new SystemRequestDetails();
		startingRequestDetails.setParameters(parameters);
		startingRequestDetails.setRequestType(RequestTypeEnum.PUT);
		startingRequestDetails.setOperation("$operation");
		startingRequestDetails.setResourceName("BaseResource");
		startingRequestDetails.setResource(resource);
		startingRequestDetails.setRestOperationType(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE);

		AtomicBoolean latch = new AtomicBoolean();
		TestHapiFhirRepository repository = new TestHapiFhirRepository(
			myDaoRegistry, startingRequestDetails, myRestfulServer
		);
		repository.setConsumerLatch(details -> latch.set(true));

		// test
		boolean hasAdditionalParams = false;
		RestOperationTypeEnum expectedRestOpType = null;
		ArgumentCaptor<RequestDetails> requestDetailsArgumentCaptor = ArgumentCaptor.forClass(RequestDetails.class);
		switch (theAPI) {
			case READ -> {
				expectedRestOpType = RestOperationTypeEnum.READ;
				when(myDaoRegistry.getResourceDao(any(Class.class)))
					.thenReturn(myResourceDao);

				repository.read(patientClass, new IdType("Patient/1"), headers);

				verify(myResourceDao)
					.read(any(), requestDetailsArgumentCaptor.capture());
			}
			case CREATE -> {
				expectedRestOpType = RestOperationTypeEnum.CREATE;
				Patient p = new Patient();
				when(myDaoRegistry.getResourceDao(p))
					.thenReturn((IFhirResourceDao<Patient>) myResourceDao);

				repository.create(p, headers);

				verify(myResourceDao)
					.create(any(), requestDetailsArgumentCaptor.capture());
			}
			case PATCH -> {
				expectedRestOpType = RestOperationTypeEnum.PATCH;
				when(myDaoRegistry.getResourceDao(anyString()))
					.thenReturn(myResourceDao);

				repository.patch(new IdType("Patient/123"), new Parameters(), headers);

				verify(myResourceDao)
					.patch(any(), any(), any(), any(), any(), requestDetailsArgumentCaptor.capture());
			}
			case UPDATE -> {
				expectedRestOpType = RestOperationTypeEnum.UPDATE;
				Patient p = new Patient();
				DaoMethodOutcome outcome = new DaoMethodOutcome();
				when(myDaoRegistry.getResourceDao(p))
					.thenReturn((IFhirResourceDao<Patient>) myResourceDao);
				when(myResourceDao.update(any(), any(RequestDetails.class)))
					.thenReturn(outcome);

				repository.update(p, headers);

				verify(myResourceDao)
					.update(any(), requestDetailsArgumentCaptor.capture());
			}
			case DELETE -> {
				expectedRestOpType = RestOperationTypeEnum.DELETE;
				when(myDaoRegistry.getResourceDao(any(Class.class)))
					.thenReturn(myResourceDao);

				repository.delete(patientClass, new IdType("Patient/123"), headers);

				verify(myResourceDao)
					.delete(any(), requestDetailsArgumentCaptor.capture());
			}
			case SEARCH -> {
				expectedRestOpType = RestOperationTypeEnum.SEARCH_TYPE;
				when(myDaoRegistry.getResourceDao(any(Class.class)))
					.thenReturn(myResourceDao);
				// this will be updated by the request
				startingRequestDetails.setResourceName("Patient");

				hasAdditionalParams = true;

				repository.setConsumerLatch(editedDetails -> {
					Map<String, String[]> providedParams = editedDetails.getParameters();

					HashMap<String, String[]> newParams = new HashMap<>(providedParams);
					// preserve our parameters please
					// (implementers will do this via override since RequestDetails are set final)
					newParams.putAll(parameters);
					editedDetails.setParameters(newParams);
				});

				IRepository.IRepositoryRestQueryContributor contributorMock = mock(IRepository.IRepositoryRestQueryContributor.class);
				repository.search(Bundle.class, Patient.class, contributorMock, headers);

				verify(myResourceDao)
					.search(any(), requestDetailsArgumentCaptor.capture());
			}
			case LINK -> {
				expectedRestOpType = RestOperationTypeEnum.GET_PAGE;
				IPagingProvider pagingProvider = mock(IPagingProvider.class);
				IBundleProvider provider = mock(IBundleProvider.class);

				hasAdditionalParams = true;

				// required for the request to work, so we'll add it here
				parameters.put(Constants.PARAM_PAGINGACTION, new String[] { "searchId" });

				when(myRestfulServer.getPagingProvider())
					.thenReturn(pagingProvider);
				when(pagingProvider.retrieveResultList(any(), any()))
					.thenReturn(provider);

				repository.link(Bundle.class, "http://localhost.com?_pageCount=1", headers);

				verify(pagingProvider)
					.retrieveResultList(requestDetailsArgumentCaptor.capture(), any());
			}
			case CAPABILITIES -> {
				expectedRestOpType = RestOperationTypeEnum.METADATA;
				ConformanceMethodBinding methodBinding = mock(ConformanceMethodBinding.class);
				when(myRestfulServer.getServerConformanceMethod())
					.thenReturn(methodBinding);

				repository.capabilities(CapabilityStatement.class, headers);

				verify(methodBinding)
					.provideCapabilityStatement(any(), requestDetailsArgumentCaptor.capture());
			}
			case TRANSACTION -> {
				expectedRestOpType = RestOperationTypeEnum.TRANSACTION;
				IFhirSystemDao systemDao = mock(IFhirSystemDao.class);
				when(myDaoRegistry.getSystemDao())
					.thenReturn(systemDao);

				repository.transaction(new Bundle(), headers);

				verify(systemDao)
					.transaction(requestDetailsArgumentCaptor.capture(), any());
			}
			default -> {
				fail("Unsupported operation type " + theAPI.name());
			}
		}

		// validate
		// ensure that our method is overwrite-able - this is imperative
		assertTrue(latch.get());

		RequestDetails submittedRequestDetails = requestDetailsArgumentCaptor.getValue();

		assertEquals(expectedRestOpType, submittedRequestDetails.getRestOperationType());
		assertEquals(startingRequestDetails.getOperation(), submittedRequestDetails.getOperation());
		assertEquals(startingRequestDetails.getRequestType(), submittedRequestDetails.getRequestType());
		assertEquals(startingRequestDetails.getId(), submittedRequestDetails.getId());
		assertEquals(startingRequestDetails.getResourceName(), submittedRequestDetails.getResourceName());
		assertEquals(startingRequestDetails.getResource(), submittedRequestDetails.getResource());

		Map<String, String[]> submittedParams = submittedRequestDetails.getParameters();
		Map<String, String[]> startingParams = startingRequestDetails.getParameters();
		if (!hasAdditionalParams) {
			assertEquals(startingParams.size(), submittedParams.size());
		} else {
			assertTrue(startingParams.size() <= submittedParams.size());
		}

		// we should still contain all previous parameters
		// (since our test cases don't overwrite)
		for (String key : startingParams.keySet()) {
			assertTrue(submittedParams.containsKey(key), String.join(", ", submittedParams.keySet()));

			Set<String> startingValues = Arrays.stream(startingParams.get(key)).collect(Collectors.toSet());
			String[] submittedValues = submittedParams.get(key);
			assertEquals(startingValues.size(), submittedValues.length);
			for (String submittedValue : submittedValues) {
				assertTrue(startingValues.contains(submittedValue));
			}
		}
	}
}
