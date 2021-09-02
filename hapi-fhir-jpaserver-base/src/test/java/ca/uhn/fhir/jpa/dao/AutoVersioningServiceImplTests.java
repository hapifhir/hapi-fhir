package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class AutoVersioningServiceImplTests {

	@Mock
	private DaoRegistry daoRegistry;

	@InjectMocks
	private AutoVersioningServiceImpl myAutoversioningService;

	@Test
	public void getAutoversionsForIds_whenResourceExists_returnsMapWithPIDAndVersion() {
		IIdType type = new IdDt("Patient/RED");
		ResourcePersistentId pid = new ResourcePersistentId(1);
		pid.setVersion(2L);
		HashMap<IIdType, ResourcePersistentId> map = new HashMap<>();
		map.put(type, pid);

		IFhirResourceDao daoMock = Mockito.mock(IFhirResourceDao.class);
		Mockito.when(daoMock.getIdsOfExistingResources(Mockito.anyList()))
			.thenReturn(map);
		Mockito.when(daoRegistry.getResourceDao(Mockito.anyString()))
			.thenReturn(daoMock);

		Map<IIdType, ResourcePersistentId> retMap = myAutoversioningService.getExistingAutoversionsForIds(Collections.singletonList(type));

		Assertions.assertTrue(retMap.containsKey(type));
		Assertions.assertEquals(pid.getVersion(), map.get(type).getVersion());
	}

	@Test
	public void getAutoversionsForIds_whenResourceDoesNotExist_returnsEmptyMap() {
		IIdType type = new IdDt("Patient/RED");

		IFhirResourceDao daoMock = Mockito.mock(IFhirResourceDao.class);
		Mockito.when(daoMock.getIdsOfExistingResources(Mockito.anyList()))
			.thenReturn(new HashMap<>());
		Mockito.when(daoRegistry.getResourceDao(Mockito.anyString()))
			.thenReturn(daoMock);

		Map<IIdType, ResourcePersistentId> retMap = myAutoversioningService.getExistingAutoversionsForIds(Collections.singletonList(type));

		Assertions.assertTrue(retMap.isEmpty());
	}

	@Test
	public void getAutoversionsForIds_whenSomeResourcesDoNotExist_returnsOnlyExistingElements() {
		IIdType type = new IdDt("Patient/RED");
		ResourcePersistentId pid = new ResourcePersistentId(1);
		pid.setVersion(2L);
		HashMap<IIdType, ResourcePersistentId> map = new HashMap<>();
		map.put(type, pid);
		IIdType type2 = new IdDt("Patient/BLUE");

		// when
		IFhirResourceDao daoMock = Mockito.mock(IFhirResourceDao.class);
		Mockito.when(daoMock.getIdsOfExistingResources(Mockito.anyList()))
			.thenReturn(map);
		Mockito.when(daoRegistry.getResourceDao(Mockito.anyString()))
			.thenReturn(daoMock);

		// test
		Map<IIdType, ResourcePersistentId> retMap = myAutoversioningService.getExistingAutoversionsForIds(
			Arrays.asList(type, type2)
		);

		// verify
		Assertions.assertEquals(map.size(), retMap.size());
		Assertions.assertTrue(retMap.containsKey(type));
		Assertions.assertFalse(retMap.containsKey(type2));
	}
}
