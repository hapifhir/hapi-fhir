/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IResourceIdentifierPatientUniqueEntityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIdentifierSystemEntityDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceIdentifierPatientUniqueEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceIdentifierSystemEntity;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.SleepUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;

import java.util.function.Supplier;

public class ResourceIdentifierCacheSvcImpl implements IResourceIdentifierCacheSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceIdentifierCacheSvcImpl.class);
	private final MemoryCacheService myMemoryCache;
	private final IResourceIdentifierSystemEntityDao myResourceIdentifierSystemEntityDao;
	private final IResourceIdentifierPatientUniqueEntityDao myResourceIdentifierPatientUniqueEntityDao;
	private final IHapiTransactionService myTransactionService;
	private final EntityManager myEntityManager;

	/**
	 * Constructor
	 */
	public ResourceIdentifierCacheSvcImpl(
			IHapiTransactionService theTransactionService,
			MemoryCacheService theMemoryCache,
			IResourceIdentifierSystemEntityDao theResourceIdentifierSystemEntityDao,
			IResourceIdentifierPatientUniqueEntityDao theResourceIdentifierPatientUniqueEntityDao,
			EntityManager theEntityManager) {
		myTransactionService = theTransactionService;
		myMemoryCache = theMemoryCache;
		myResourceIdentifierSystemEntityDao = theResourceIdentifierSystemEntityDao;
		myResourceIdentifierPatientUniqueEntityDao = theResourceIdentifierPatientUniqueEntityDao;
		myEntityManager = theEntityManager;
	}

	@Override
	public long getOrCreateResourceIdentifierSystem(
			RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId, String theSystem) {
		Validate.notBlank(theSystem, "theIdentifierSystem must not be blank");

		Long pid =
				lookupResourceIdentifierSystemFromCacheOrDatabase(theRequestDetails, theRequestPartitionId, theSystem);

		/*
		 * If we don't find an existing entry for the system in the database,
		 * we need to try to create a new one. We expect that identifier systems
		 * are relatively unique
		 */
		if (pid == null) {
			int max = 5;
			for (int i = 0; i < max && pid == null; i++) {
				try {
					pid = myTransactionService
							.withRequest(theRequestDetails)
							.withRequestPartitionId(theRequestPartitionId)
							.withPropagation(Propagation.REQUIRES_NEW)
							.execute(() -> {
								Long newPid = lookupResourceIdentifierSystemFromCacheOrDatabase(
										theRequestDetails, theRequestPartitionId, theSystem);
								if (newPid == null) {
									ResourceIdentifierSystemEntity newEntity = new ResourceIdentifierSystemEntity();
									newEntity.setSystem(theSystem);
									newEntity = myResourceIdentifierSystemEntityDao.save(newEntity);
									assert newEntity.getPid() != null;
									myMemoryCache.putAfterCommit(
											MemoryCacheService.CacheEnum.RESOURCE_IDENTIFIER_SYSTEM_TO_PID,
											theSystem,
											newEntity.getPid());
									ourLog.info(
											"Created identifier System[{}] with PID: {}",
											theSystem,
											newEntity.getPid());
									newPid = newEntity.getPid();
								}
								return newPid;
							});
				} catch (ResourceVersionConflictException e) {
					ourLog.info(
							"Concurrency failure (attempt {}/{}) creating identifier system cache: {}",
							(i + 1),
							max,
							theSystem);
					new SleepUtil().sleepAtLeast(500L);
				}
			}
		}

		Validate.isTrue(pid != null, "Failed to create resource identifier cache entry for system: %s", theSystem);
		return pid;
	}

	private Long lookupResourceIdentifierSystemFromCacheOrDatabase(
			RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId, String theSystem) {
		Long pid = myMemoryCache.get(
				MemoryCacheService.CacheEnum.RESOURCE_IDENTIFIER_SYSTEM_TO_PID,
				theSystem,
				t -> lookupResourceIdentifierSystemFromDatabase(theRequestDetails, theRequestPartitionId, theSystem));
		return pid;
	}

	@Nonnull
	@Override
	public String getFhirIdAssociatedWithUniquePatientIdentifier(
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId,
			String theSystem,
			String theValue,
			Supplier<String> theNewIdSupplier) {

		Validate.notBlank(theSystem, "theSystem must not be blank");
		Validate.notBlank(theValue, "theValue must not be blank");

		long identifierSystemPid =
				getOrCreateResourceIdentifierSystem(theRequestDetails, theRequestPartitionId, theSystem);
		MemoryCacheService.IdentifierKey key = new MemoryCacheService.IdentifierKey(theSystem, theValue);
		return myMemoryCache.getThenPutAfterCommit(
				MemoryCacheService.CacheEnum.PATIENT_IDENTIFIER_TO_FHIR_ID,
				key,
				t -> lookupResourceFhirIdForPatientIdentifier(
						theRequestDetails, theRequestPartitionId, identifierSystemPid, theValue, theNewIdSupplier));
	}

	@Nullable
	private String lookupResourceFhirIdForPatientIdentifier(
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId,
			long theSystem,
			String theValue,
			Supplier<String> theNewIdSupplier) {
		return myTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(theRequestPartitionId)
				.execute(() -> {
					String retVal = myResourceIdentifierPatientUniqueEntityDao
							.findById(
									new ResourceIdentifierPatientUniqueEntity.PatientIdentifierPk(theSystem, theValue))
							.map(ResourceIdentifierPatientUniqueEntity::getFhirId)
							.orElse(null);

					if (retVal == null) {
						retVal = theNewIdSupplier.get();
						ourLog.trace("Created FHIR ID [{}] for SystemPid[{}] Value[{}]", retVal, theSystem, theValue);
						ResourceIdentifierPatientUniqueEntity newEntity = new ResourceIdentifierPatientUniqueEntity();
						newEntity.setPk(
								new ResourceIdentifierPatientUniqueEntity.PatientIdentifierPk(theSystem, theValue));
						newEntity.setFhirId(retVal);
						myEntityManager.persist(newEntity);
					}

					assert retVal != null;
					return retVal;
				});
	}

	private Long lookupResourceIdentifierSystemFromDatabase(
			RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId, String theIdentifierSystem) {
		return myTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(theRequestPartitionId)
				.readOnly()
				.execute(() -> {
					Long retVal = myResourceIdentifierSystemEntityDao
							.findBySystemUrl(theIdentifierSystem)
							.orElse(null);
					ourLog.trace("Fetched PID[{}] for Identifier System: {}", retVal, theIdentifierSystem);
					return retVal;
				});
	}
}
