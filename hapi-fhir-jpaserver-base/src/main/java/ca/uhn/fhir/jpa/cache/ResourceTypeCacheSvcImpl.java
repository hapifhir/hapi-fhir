/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.dao.data.IResourceTypeDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTypeEntity;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ResourceTypeCacheSvcImpl implements IResourceTypeCacheSvc {
	private static final Logger ourLog = getLogger(ResourceTypeCacheSvcImpl.class);

	private final IResourceTypeDao myResourceTypeDao;
	private final TransactionTemplate myTxTemplate;
	private final MemoryCacheService myMemoryCacheService;

	public ResourceTypeCacheSvcImpl(
			IResourceTypeDao theResourceTypeDao,
			PlatformTransactionManager theTxManager,
			MemoryCacheService theMemoryCacheService) {
		myResourceTypeDao = theResourceTypeDao;
		myTxTemplate = new TransactionTemplate(theTxManager);
		myMemoryCacheService = theMemoryCacheService;
	}

	@PostConstruct
	public void start() {
		initCache();
		ourLog.info(
				"Resource type cache size: {}",
				myMemoryCacheService.getEstimatedSize(MemoryCacheService.CacheEnum.RES_TYPE_TO_RES_TYPE_ID));
	}

	@Override
	public Short getResourceTypeId(String theResType) {
		// theResType = "CustomRes101";
		Short resTypeId = myMemoryCacheService.get(
				MemoryCacheService.CacheEnum.RES_TYPE_TO_RES_TYPE_ID, theResType, this::lookupResourceTypeId);

		if (resTypeId == null) {
			ourLog.info("Create new Resource Type [{}]", theResType);
			resTypeId = createResourceType(theResType);
		}
		return resTypeId;
	}

	public Short getResourceTypeId0(String theResType) {
		Short resTypeId =
				myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.RES_TYPE_TO_RES_TYPE_ID, theResType);
		if (resTypeId != null) {
			return resTypeId;
		}

		// Cache miss, check the database and add to cache if found
		Optional<ResourceTypeEntity> resTypeEntity = myResourceTypeDao.findByResourceType(theResType);
		if (resTypeEntity.isPresent()) {
			resTypeId = resTypeEntity.get().getResourceTypeId();
			addToCache(theResType, resTypeId);
		} else {
			ourLog.info("Adding new Resource Type [{}]", theResType);
			resTypeId = createResourceType(theResType);
		}
		return resTypeId;
	}

	protected void initCache() {
		List<ResourceTypeEntity> resTypes = myTxTemplate.execute(t -> myResourceTypeDao.findAll());
		if (CollectionUtils.isEmpty(resTypes)) {
			ourLog.warn("No resource types found in database");
			return;
		}
		ourLog.info("{} resource types found in database.", resTypes.size());
		resTypes.forEach(resType -> addToCache(resType.getResourceType(), resType.getResourceTypeId()));
	}

	protected Short createResourceType(String theResourceType) {
		ResourceTypeEntity resTypeEntity = myTxTemplate.execute(t -> {
			ResourceTypeEntity entity = new ResourceTypeEntity();
			entity.setResourceType(theResourceType);
			return myResourceTypeDao.save(entity);
		});

		assert resTypeEntity != null;
		addToCache(resTypeEntity.getResourceType(), resTypeEntity.getResourceTypeId());
		return resTypeEntity.getResourceTypeId();
	}

	private void addToCache(String theResType, Short theResTypeId) {
		myMemoryCacheService.put(MemoryCacheService.CacheEnum.RES_TYPE_TO_RES_TYPE_ID, theResType, theResTypeId);
	}

	private Short lookupResourceTypeId(String theResourceType) {
		return myTxTemplate.execute(t -> myResourceTypeDao.findResourceIdByType(theResourceType));
	}
}
