package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class BaseHapiFhirResourceDaoTest {

	// our simple concrete test class for BaseHapiFhirResourceDao
	private class SimpleTestDao extends BaseHapiFhirResourceDao<Patient> {
		public SimpleTestDao() {
			super();
			// post inject hooks
			setResourceType(Patient.class);
			RuntimeResourceDefinition resourceDefinition = Mockito.mock(RuntimeResourceDefinition.class);
			Mockito.when(resourceDefinition.getName()).thenReturn("Patient");
			FhirContext myFhirContextMock = Mockito.mock(FhirContext.class);
			Mockito.when(myFhirContextMock.getResourceDefinition(Mockito.any(Class.class)))
					.thenReturn(resourceDefinition);
			setContext(myFhirContextMock);
			postConstruct();
		}

	}

	@InjectMocks
	private BaseHapiFhirResourceDao myBaseHapiFhirResourceDao = new SimpleTestDao();

	@Mock
	private IForcedIdDao myIForcedIdDao;

	//TODO - all other dependency mocks

	/**
	 * Creates a match entry to be returned by myIForcedIdDao.
	 * This ordering matters (see IForcedIdDao)
	 * @param theId
	 * @param thePID
	 * @param theResourceVersion
	 * @return
	 */
	private Object[] createMatchEntryForGetIdsOfExistingResources(IIdType theId, long thePID, long theResourceVersion) {
		Object[] arr = new Object[] {
			theId.getResourceType(),
			theId.getIdPart(),
			thePID,
			theResourceVersion
		};
		return arr;
	}

	@Test
	public void getIdsOfExistingResources_forExistingResources_returnsMapOfIdToPIDWithVersion() {
		// setup
		IIdType patientIdAndType = new IdDt("Patient/RED");
		long patientPID = 1L;
		long patientResourceVersion = 2L;
		IIdType patient2IdAndType = new IdDt("Patient/BLUE");
		long patient2PID = 3L;
		long patient2ResourceVersion = 4L;
		List<IIdType> inputList = new ArrayList<>();
		inputList.add(patientIdAndType);
		inputList.add(patient2IdAndType);

		Collection<Object[]> matches = Arrays.asList(
			createMatchEntryForGetIdsOfExistingResources(patientIdAndType, patientPID, patientResourceVersion),
			createMatchEntryForGetIdsOfExistingResources(patient2IdAndType, patient2PID, patient2ResourceVersion)
		);

		// when
		Mockito.when(myIForcedIdDao.findResourcesByForcedId(Mockito.anyString(),
			Mockito.anyList())).thenReturn(matches);

		Map<IIdType, ResourcePersistentId> idToPIDOfExistingResources = myBaseHapiFhirResourceDao.getIdsOfExistingResources(inputList);

		Assertions.assertEquals(inputList.size(), idToPIDOfExistingResources.size());
		Assertions.assertTrue(idToPIDOfExistingResources.containsKey(patientIdAndType));
		Assertions.assertTrue(idToPIDOfExistingResources.containsKey(patient2IdAndType));
		Assertions.assertEquals(idToPIDOfExistingResources.get(patientIdAndType).getIdAsLong(), patientPID);
		Assertions.assertEquals(idToPIDOfExistingResources.get(patient2IdAndType).getIdAsLong(), patient2PID);
		Assertions.assertEquals(idToPIDOfExistingResources.get(patientIdAndType).getVersion(), patientResourceVersion);
		Assertions.assertEquals(idToPIDOfExistingResources.get(patient2IdAndType).getVersion(), patient2ResourceVersion);
	}

	@Test
	public void getIdsOfExistingResources_forNonExistentResources_returnsEmptyMap() {
		//setup
		IIdType patient = new IdDt("Patient/RED");

		// when
		Mockito.when(myIForcedIdDao.findResourcesByForcedId(Mockito.anyString(), Mockito.anyList()))
			.thenReturn(new ArrayList<>());

		Map<IIdType, ResourcePersistentId> map = myBaseHapiFhirResourceDao.getIdsOfExistingResources(Collections.singletonList(patient));

		Assertions.assertTrue(map.isEmpty());
	}

	@Test
	public void getIdsOfExistingResources_whenSomeResourcesExist_returnsOnlyExistingResourcesInMap() {
		// setup
		IIdType patientIdAndType = new IdDt("Patient/RED");
		long patientPID = 1L;
		long patientResourceVersion = 2L;
		IIdType patient2IdAndType = new IdDt("Patient/BLUE");
		List<IIdType> inputList = new ArrayList<>();
		inputList.add(patientIdAndType);
		inputList.add(patient2IdAndType);

		Collection<Object[]> matches = Collections.singletonList(
			createMatchEntryForGetIdsOfExistingResources(patientIdAndType, patientPID, patientResourceVersion)
		);

		// when
		Mockito.when(myIForcedIdDao.findResourcesByForcedId(Mockito.anyString(), Mockito.anyList()))
			.thenReturn(matches);

		Map<IIdType, ResourcePersistentId> map = myBaseHapiFhirResourceDao.getIdsOfExistingResources(inputList);

		// verify
		Assertions.assertFalse(map.isEmpty());
		Assertions.assertEquals(inputList.size() - 1, map.size());
		Assertions.assertTrue(map.containsKey(patientIdAndType));
		Assertions.assertFalse(map.containsKey(patient2IdAndType));
	}

	/*******************/

	TestResourceDao mySvc = new TestResourceDao();

	@Test
	public void validateResourceIdCreation_asSystem() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new SystemRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.NOT_ALLOWED);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	@Test
	public void validateResourceIdCreation_asUser() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.NOT_ALLOWED);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("failedToCreateWithClientAssignedIdNotAllowed", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withNumber() {
		Patient patient = new Patient();
		patient.setId("2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ALPHANUMERIC);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("failedToCreateWithClientAssignedNumericId", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withAlpha() {
		Patient patient = new Patient();
		patient.setId("P2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ALPHANUMERIC);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	static class TestResourceDao extends BaseHapiFhirResourceDao<Patient> {
		private final DaoConfig myDaoConfig = new DaoConfig();

		@Override
		public DaoConfig getConfig() {
			return myDaoConfig;
		}

		@Override
		protected String getMessageSanitized(String theKey, String theIdPart) {
			return theKey;
		}
	}
}
