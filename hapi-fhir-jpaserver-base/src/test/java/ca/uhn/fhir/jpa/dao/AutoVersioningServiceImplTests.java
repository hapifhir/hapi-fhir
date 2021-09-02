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

import java.util.Collection;
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

		Map<IIdType, ResourcePersistentId> retMap = myAutoversioningService.getAutoversionsForIds(Collections.singletonList(type));

		Assertions.assertTrue(retMap.containsKey(type));
		Assertions.assertEquals(pid.getVersion(), map.get(type).getVersion());
	}

	@Test
	public void getAutoversionsForIds_whenResourceDoesNotExist_returnsEmptyMap() {
		IIdType type = new IdDt("Patient/RED");

		IFhirResourceDao daoMock = Mockito.mock(IFhirResourceDao.class);
		Mockito.when(daoMock.getIdsOfExistingResources(Mockito.anyList()))
			.thenReturn(new HashMap<>());

		Map<IIdType, ResourcePersistentId> retMap = myAutoversioningService.getAutoversionsForIds(Collections.singletonList(type));

		Assertions.assertTrue(retMap.isEmpty());
	}


}
