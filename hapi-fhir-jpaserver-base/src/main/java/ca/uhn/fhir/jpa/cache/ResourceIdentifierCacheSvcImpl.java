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

import ca.uhn.fhir.jpa.dao.data.IResourceIdentifierPatientUniqueEntityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIdentifierSystemEntityDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceIdentifierPatientUniqueEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceIdentifierSystemEntity;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ResourceIdentifierCacheSvcImpl implements IResourceIdentifierCacheSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceIdentifierCacheSvcImpl.class);
	private final MemoryCacheService myMemoryCache;
	private final IResourceIdentifierSystemEntityDao myResourceIdentifierSystemEntityDao;
	private final IResourceIdentifierPatientUniqueEntityDao myResourceIdentifierPatientUniqueEntityDao;

	/**
	 * Constructor
	 */
	public ResourceIdentifierCacheSvcImpl(
			MemoryCacheService theMemoryCache,
			IResourceIdentifierSystemEntityDao theResourceIdentifierSystemEntityDao,
			IResourceIdentifierPatientUniqueEntityDao theResourceIdentifierPatientUniqueEntityDao) {
		myMemoryCache = theMemoryCache;
		myResourceIdentifierSystemEntityDao = theResourceIdentifierSystemEntityDao;
		myResourceIdentifierPatientUniqueEntityDao = theResourceIdentifierPatientUniqueEntityDao;
	}

	@Override
	public long getOrCreateResourceIdentifierSystem(String theSystem) {
		HapiTransactionService.requireTransaction();
		Validate.notBlank(theSystem, "theIdentifierSystem must not be blank");

		Long pid = myMemoryCache.get(
				MemoryCacheService.CacheEnum.RESOURCE_IDENTIFIER_SYSTEM_TO_PID,
				theSystem,
				t -> lookupResourceIdentifierSystem(theSystem));

		if (pid == null) {
			ResourceIdentifierSystemEntity newEntity = new ResourceIdentifierSystemEntity();
			newEntity.setSystem(theSystem);
			newEntity = myResourceIdentifierSystemEntityDao.save(newEntity);
			pid = newEntity.getPid();
			assert pid != null;
			myMemoryCache.putAfterCommit(
					MemoryCacheService.CacheEnum.RESOURCE_IDENTIFIER_SYSTEM_TO_PID, theSystem, pid);
		}

		return pid;
	}

	@Override
	public String getFhirIdAssociatedWithUniquePatientIdentifier(
			String theSystem, String theValue, Supplier<String> theNewIdSupplier) {
		HapiTransactionService.requireTransaction();

		Validate.notBlank(theSystem, "theSystem must not be blank");
		Validate.notBlank(theValue, "theValue must not be blank");

		long identifierSystemPid = getOrCreateResourceIdentifierSystem(theSystem);
		MemoryCacheService.IdentifierKey key = new MemoryCacheService.IdentifierKey(theSystem, theValue);
		String fhirId = myMemoryCache.get(
				MemoryCacheService.CacheEnum.PATIENT_IDENTIFIER_TO_FHIR_ID,
				key,
				t -> lookupResourceFhirIdForPatientIdentifier(identifierSystemPid, theValue));

		if (fhirId == null) {
			fhirId = theNewIdSupplier.get();
			Validate.notBlank(fhirId, "Supplied fhirId must not be blank");

			ResourceIdentifierPatientUniqueEntity newEntity = new ResourceIdentifierPatientUniqueEntity();
			newEntity.setPk(
					new ResourceIdentifierPatientUniqueEntity.PatientIdentifierPk(identifierSystemPid, theValue));
			newEntity.setFhirId(fhirId);
			myResourceIdentifierPatientUniqueEntityDao.save(newEntity);
			myMemoryCache.putAfterCommit(MemoryCacheService.CacheEnum.PATIENT_IDENTIFIER_TO_FHIR_ID, key, fhirId);
		}

		return fhirId;
	}

	@Nullable
	private String lookupResourceFhirIdForPatientIdentifier(long theSystem, String theValue) {
		return myResourceIdentifierPatientUniqueEntityDao
				.findById(new ResourceIdentifierPatientUniqueEntity.PatientIdentifierPk(theSystem, theValue))
				.map(ResourceIdentifierPatientUniqueEntity::getFhirId)
				.orElse(null);
	}

	private Long lookupResourceIdentifierSystem(String theIdentifierSystem) {
		Long retVal = myResourceIdentifierSystemEntityDao
				.findBySystemUrl(theIdentifierSystem)
				.orElse(null);
		ourLog.trace("Fetched PID[{}] for Identifier System: {}", retVal, theIdentifierSystem);
		return retVal;
	}
}
