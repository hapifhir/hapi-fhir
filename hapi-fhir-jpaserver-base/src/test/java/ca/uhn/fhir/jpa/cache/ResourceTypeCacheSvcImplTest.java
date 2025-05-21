package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceTypeDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceTypeEntity;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.function.Function;

import static ca.uhn.fhir.jpa.util.MemoryCacheService.CacheEnum;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

	@Spy
	IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();

	@Spy
	MemoryCacheService myMemoryCacheService = new MemoryCacheService(new JpaStorageSettings());

	@Test
	void testInitResourceTypes() {
		// setup
		ResourceTypeEntity entity1 = createResourceTypeEntity((short) 1, "Encounter");
		ResourceTypeEntity entity2 = createResourceTypeEntity((short) 2, "Patient");
		ResourceTypeEntity entity3 = createResourceTypeEntity((short) 3, "ValueSet");

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
		Short resTypeId = (short) 100;
		String resType = "Encounter";
		doReturn(resTypeId).when(myMemoryCacheService)
			.get(eq(CacheEnum.RES_TYPE_TO_RES_TYPE_ID), anyString(), any(Function.class));

		// execute
		myResourceTypeCacheSvc.getResourceTypeId(resType);

		// verify
		verify(myResourceTypeDao, never()).findByResourceType(anyString());
		verify(myResourceTypeDao, never()).save(any(ResourceTypeEntity.class));
	}

	@Test
	void testGetResourceTypeId_whenCacheMiss_loadFromDb() {
		// setup
		Short resTypeId = (short) 100;
		String resType = "Encounter";
		when(myResourceTypeDao.findResourceIdByType(anyString())).thenReturn(resTypeId);

		// execute
		Short result = myResourceTypeCacheSvc.getResourceTypeId(resType);

		// verify
		verify(myResourceTypeDao, times(1)).findResourceIdByType(anyString());
		verify(myResourceTypeDao, never()).save(any(ResourceTypeEntity.class));
		assertThat(result).isEqualTo(resTypeId);
		assertThat(getResourceTypeIdFromCache(resType)).isEqualTo(resTypeId);
	}

	@Test
	void testGetResourceTypeId_whenCacheMissAndNotInDb_createAndAddToCache() {
		// setup
		Short resTypeId = (short) 100;
		String resType = "CustomType";
		ResourceTypeEntity entity = createResourceTypeEntity(resTypeId, resType);

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
	void testCreateResourceType_saveSuccessfully() {
		// setup
		Short resTypeId = (short) 100;
		String resType = "CustomType";
		ResourceTypeEntity entity = createResourceTypeEntity(resTypeId, resType);
		when(myResourceTypeDao.save(any(ResourceTypeEntity.class))).thenReturn(entity);

		// execute
		myResourceTypeCacheSvc.createResourceType(resType);

		// verify
		verify(myResourceTypeDao, times(1)).save(any(ResourceTypeEntity.class));
	}

	@Test
	void testCreateResourceType_whenResourceTypeExists_lookupInDbAndUpdateCache() {
		// setup
		Short resTypeId = (short) 100;
		String resType = "CustomType";
		ResourceTypeEntity entity = createResourceTypeEntity(resTypeId, resType);

		when(myResourceTypeDao.save(any(ResourceTypeEntity.class)))
			.thenThrow(new DataIntegrityViolationException("Resource type already exists"));
		when(myResourceTypeDao.findByResourceType(resType)).thenReturn(entity);

		// execute
		myResourceTypeCacheSvc.createResourceType(resType);

		// verify
		verify(myResourceTypeDao, times(1)).save(any(ResourceTypeEntity.class));
		verify(myResourceTypeDao, times(1)).findByResourceType(anyString());
	}

	@Test
	void testCreateResourceType_whenResourceTypeInvalid_throwException() {
		// setup
		String resType = "VeryLongCustomResourceTypeVeryLongCustomResourceTypeVeryLongCustomResourceTypeVeryLongCustomResourceType";
		when(myResourceTypeDao.save(any(ResourceTypeEntity.class)))
			.thenThrow(new DataIntegrityViolationException("Value too long for column \"RES_TYPE CHARACTER VARYING(100)\""));

		// execute
		InternalErrorException ex = assertThrows(
			InternalErrorException.class, () -> myResourceTypeCacheSvc.createResourceType(resType));

		// verify
		verify(myResourceTypeDao, times(1)).save(any(ResourceTypeEntity.class));
		verify(myResourceTypeDao, never()).findByResourceType(anyString());
		assertThat(ex.getMessage()).contains("Resource type name is too long");
	}

	private ResourceTypeEntity createResourceTypeEntity(Short resourceTypeId, String resourceType) {
		ResourceTypeEntity entity = new ResourceTypeEntity();
		entity.setResourceTypeId(resourceTypeId);
		entity.setResourceType(resourceType);
		return entity;
	}

	private Short getResourceTypeIdFromCache(String key) {
		return myMemoryCacheService.getIfPresent(CacheEnum.RES_TYPE_TO_RES_TYPE_ID, key);
	}
}
