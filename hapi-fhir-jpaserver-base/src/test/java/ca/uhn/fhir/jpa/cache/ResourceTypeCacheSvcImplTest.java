package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceTypeDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTypeEntity;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.function.Function;

import static ca.uhn.fhir.jpa.util.MemoryCacheService.CacheEnum;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceTypeCacheSvcImplTest {

	@InjectMocks
	ResourceTypeCacheSvcImpl myResourceTypeCacheSvc;

	@Mock
	IResourceTypeDao myResourceTypeDao;

	@Mock
	PlatformTransactionManager myTxManager;

	@Spy
	MemoryCacheService myMemoryCacheService = new MemoryCacheService(new JpaStorageSettings());

	@Test
	void testInitResourceTypes() {
		// setup
		ResourceTypeEntity entity1 = new ResourceTypeEntity();
		entity1.setResourceTypeId((short) 1);
		entity1.setResourceType("Encounter");

		ResourceTypeEntity entity2 = new ResourceTypeEntity();
		entity2.setResourceTypeId((short) 2);
		entity2.setResourceType("Patient");

		ResourceTypeEntity entity3 = new ResourceTypeEntity();
		entity3.setResourceTypeId((short) 3);
		entity3.setResourceType("ValueSet");

		List<ResourceTypeEntity> entities = List.of(entity1, entity2, entity3);
		when(myResourceTypeDao.findAll()).thenReturn(entities);

		// execute
		myResourceTypeCacheSvc.initResourceTypes();

		// verify
		verify(myMemoryCacheService, times(3)).put(eq(CacheEnum.RES_TYPE_TO_RES_TYPE_ID), anyString(), anyShort());
		assertThat((short) 1).isEqualTo(getResourceTypeIdFromCache("Encounter"));
		assertThat((short) 2).isEqualTo(getResourceTypeIdFromCache("Patient"));
		assertThat((short) 3).isEqualTo(getResourceTypeIdFromCache("ValueSet"));
	}

	@Test
	void testGetResourceTypeId_whenCacheFound_noDatabaseHit() {
		// setup
		doReturn((short) 100).when(myMemoryCacheService)
			.get(eq(CacheEnum.RES_TYPE_TO_RES_TYPE_ID), anyString(), any(Function.class));

		// execute
		myResourceTypeCacheSvc.getResourceTypeId("Patient");

		// verify
		verify(myResourceTypeDao, never()).findByResourceType(anyString());
		verify(myResourceTypeDao, never()).save(any(ResourceTypeEntity.class));
	}

	@Test
	void testGetResourceTypeId_whenCacheMiss_loadFromDb() {
		// setup
		Short resTypeId = (short) 100;
		String resType = "Encounter";
		ResourceTypeEntity entity = new ResourceTypeEntity();
		entity.setResourceTypeId(resTypeId);
		entity.setResourceType(resType);
		when(myResourceTypeDao.findResourceIdByType(anyString())).thenReturn(resTypeId);

		// execute
		Short result = myResourceTypeCacheSvc.getResourceTypeId(resType);

		// verify
		assertThat(result).isEqualTo((short) 100);
		verify(myResourceTypeDao, times(1)).findResourceIdByType(anyString());
		verify(myResourceTypeDao, never()).save(any(ResourceTypeEntity.class));
	}
	
	@Test
	void testGetResourceTypeId_whenCacheMissAndNotInDb_createAndAddToCache() {
		// setup
		Short resTypeId = (short) 100;
		String resType = "CustomType";
		ResourceTypeEntity entity = new ResourceTypeEntity();
		entity.setResourceTypeId(resTypeId);
		entity.setResourceType(resType);

		when(myResourceTypeDao.findResourceIdByType(resType)).thenReturn(null);
		when(myResourceTypeDao.save(any(ResourceTypeEntity.class))).thenReturn(entity);

		// execute
		Short result = myResourceTypeCacheSvc.getResourceTypeId(resType);

		// verify
		verify(myResourceTypeDao, times(1)).findResourceIdByType(anyString());
		verify(myResourceTypeDao, times(1)).save(any(ResourceTypeEntity.class));
		assertThat(result).isEqualTo(resTypeId);
		assertThat(getResourceTypeIdFromCache(resType)).isEqualTo(resTypeId);
	}

	@Test
	void testCreateResourceType() {
		// setup
		Short resTypeId = (short) 100;
		String resType = "CustomType";
		ResourceTypeEntity entity = new ResourceTypeEntity();
		entity.setResourceTypeId(resTypeId);
		entity.setResourceType(resType);

		// execute
		when(myResourceTypeDao.save(any(ResourceTypeEntity.class))).thenReturn(entity);
		myResourceTypeCacheSvc.createResourceType(resType);

		// verify it is saved and added to cache
		verify(myResourceTypeDao, times(1)).save(any(ResourceTypeEntity.class));
		assertThat(resTypeId).isEqualTo(getResourceTypeIdFromCache(resType));
	}

	private Short getResourceTypeIdFromCache(String key) {
		return myMemoryCacheService.getIfPresent(CacheEnum.RES_TYPE_TO_RES_TYPE_ID, key);
	}
}
