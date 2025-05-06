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
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceTypeEntity;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ResourceTypeCacheSvcImpl implements IResourceTypeCacheSvc {
	private static final Logger ourLog = getLogger(ResourceTypeCacheSvcImpl.class);

	private final HapiTransactionService myHapiTxService;
	private final IResourceTypeDao myResourceTypeDao;
	private final MemoryCacheService myMemoryCacheService;

	public ResourceTypeCacheSvcImpl(
			HapiTransactionService theHapiTxService,
			IResourceTypeDao theResourceTypeDao,
			MemoryCacheService theMemoryCacheService) {
		myHapiTxService = theHapiTxService;
		myResourceTypeDao = theResourceTypeDao;
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
		Short resTypeId = myMemoryCacheService.get(
				MemoryCacheService.CacheEnum.RES_TYPE_TO_RES_TYPE_ID, theResType, this::lookupResourceTypeId);

		if (resTypeId == null) {
			ourLog.info("Creating a new Resource Type [{}]", theResType);
			ResourceTypeEntity entity = createResourceType(theResType);
			resTypeId = entity != null ? entity.getResourceTypeId() : null;
		}
		return resTypeId;
	}

	@Override
	public void addToCache(String theResType, Short theResTypeId) {
		myMemoryCacheService.put(MemoryCacheService.CacheEnum.RES_TYPE_TO_RES_TYPE_ID, theResType, theResTypeId);
	}

	protected void initCache() {
		List<ResourceTypeEntity> resTypes = myHapiTxService
				.withSystemRequest()
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(t -> myResourceTypeDao.findAll());
		if (CollectionUtils.isEmpty(resTypes)) {
			ourLog.warn("No resource type found in database");
			return;
		}
		ourLog.info("{} resource types found in database.", resTypes.size());
		resTypes.forEach(resType -> addToCache(resType.getResourceType(), resType.getResourceTypeId()));
	}

	protected ResourceTypeEntity createResourceType(String theResourceType) {
		return myHapiTxService
				.withSystemRequest()
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(t -> {
					try {
						ResourceTypeEntity entity = new ResourceTypeEntity();
						entity.setResourceType(theResourceType);
						ResourceTypeEntity savedEntity = myResourceTypeDao.save(entity);
						addToCache(savedEntity.getResourceType(), savedEntity.getResourceTypeId());
						return savedEntity;
					} catch (DataIntegrityViolationException e) {
						// This can happen if the resource type already exists in the database
						ourLog.info("Resource type already exists: {}", theResourceType);
						return myResourceTypeDao.findByResourceType(theResourceType);
					}
				});
	}

	private Short lookupResourceTypeId(String theResourceType) {
		return myHapiTxService
				.withSystemRequest()
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(t -> myResourceTypeDao.findResourceIdByType(theResourceType));
	}
}
