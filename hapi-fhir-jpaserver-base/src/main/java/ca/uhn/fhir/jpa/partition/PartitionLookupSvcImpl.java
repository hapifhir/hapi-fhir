package ca.uhn.fhir.jpa.partition;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class PartitionLookupSvcImpl implements IPartitionLookupSvc {

	public static final int DEFAULT_PERSISTED_PARTITION_ID = 0;
	public static final String DEFAULT_PERSISTED_PARTITION_NAME = "DEFAULT";
	private static final String DEFAULT_PERSISTED_PARTITION_DESC = "Default partition";
	private static final Pattern PARTITION_NAME_VALID_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionLookupSvcImpl.class);

	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private IPartitionDao myPartitionDao;

	private LoadingCache<String, PartitionEntity> myNameToPartitionCache;
	private LoadingCache<Integer, PartitionEntity> myIdToPartitionCache;
	private TransactionTemplate myTxTemplate;
	@Autowired
	private FhirContext myFhirCtx;

	@Override
	@PostConstruct
	public void start() {
		myNameToPartitionCache = Caffeine
			.newBuilder()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build(new NameToPartitionCacheLoader());
		myIdToPartitionCache = Caffeine
			.newBuilder()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build(new IdToPartitionCacheLoader());
		myTxTemplate = new TransactionTemplate(myTxManager);

		// Create default partition definition if it doesn't already exist
		myTxTemplate.executeWithoutResult(t -> {
			if (myPartitionDao.findById(DEFAULT_PERSISTED_PARTITION_ID).isPresent() == false) {
				ourLog.info("Creating default partition definition");
				PartitionEntity partitionEntity = new PartitionEntity();
				partitionEntity.setId(DEFAULT_PERSISTED_PARTITION_ID);
				partitionEntity.setName(DEFAULT_PERSISTED_PARTITION_NAME);
				partitionEntity.setDescription(DEFAULT_PERSISTED_PARTITION_DESC);
				myPartitionDao.save(partitionEntity);
			}
		});
	}

	@Override
	public PartitionEntity getPartitionByName(String theName) {
		Validate.notBlank(theName, "The name must not be null or blank");
		return myNameToPartitionCache.get(theName);
	}

	@Override
	public PartitionEntity getPartitionById(Integer thePartitionId) {
		validatePartitionIdSupplied(myFhirCtx, thePartitionId);
		return myIdToPartitionCache.get(thePartitionId);
	}

	@Override
	public void clearCaches() {
		myNameToPartitionCache.invalidateAll();
		myIdToPartitionCache.invalidateAll();
	}

	@Override
	@Transactional
	public PartitionEntity createPartition(PartitionEntity thePartition) {
		validateHaveValidPartitionIdAndName(thePartition);
		validatePartitionNameDoesntAlreadyExist(thePartition.getName());

		if (thePartition.getId() == DEFAULT_PERSISTED_PARTITION_ID) {
			String msg = myFhirCtx.getLocalizer().getMessage(PartitionLookupSvcImpl.class, "cantCreatePartition0");
			throw new InvalidRequestException(msg);
		}

		ourLog.info("Creating new partition with ID {} and Name {}", thePartition.getId(), thePartition.getName());

		myPartitionDao.save(thePartition);
		return thePartition;
	}

	@Override
	@Transactional
	public PartitionEntity updatePartition(PartitionEntity thePartition) {
		validateHaveValidPartitionIdAndName(thePartition);

		Optional<PartitionEntity> existingPartitionOpt = myPartitionDao.findById(thePartition.getId());
		if (existingPartitionOpt.isPresent() == false) {
			String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "unknownPartitionId", thePartition.getId());
			throw new InvalidRequestException(msg);
		}

		PartitionEntity existingPartition = existingPartitionOpt.get();
		if (!thePartition.getName().equalsIgnoreCase(existingPartition.getName())) {
			validatePartitionNameDoesntAlreadyExist(thePartition.getName());
		}

		if (DEFAULT_PERSISTED_PARTITION_ID == thePartition.getId()) {
			if (!DEFAULT_PERSISTED_PARTITION_NAME.equals(thePartition.getName())) {
				String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "cantRenameDefaultPartition");
				throw new InvalidRequestException(msg);
			}
		}

		existingPartition.setName(thePartition.getName());
		existingPartition.setDescription(thePartition.getDescription());
		myPartitionDao.save(existingPartition);
		clearCaches();
		return existingPartition;
	}

	@Override
	@Transactional
	public void deletePartition(Integer thePartitionId) {
		validatePartitionIdSupplied(myFhirCtx, thePartitionId);

		if (DEFAULT_PERSISTED_PARTITION_ID == thePartitionId) {
			String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "cantDeleteDefaultPartition");
			throw new InvalidRequestException(msg);
		}

		Optional<PartitionEntity> partition = myPartitionDao.findById(thePartitionId);
		if (!partition.isPresent()) {
			String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "unknownPartitionId", thePartitionId);
			throw new IllegalArgumentException(msg);
		}

		myPartitionDao.delete(partition.get());

		clearCaches();
	}

	private void validatePartitionNameDoesntAlreadyExist(String theName) {
		if (myPartitionDao.findForName(theName).isPresent()) {
			String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "cantCreateDuplicatePartitionName", theName);
			throw new InvalidRequestException(msg);
		}
	}

	private void validateHaveValidPartitionIdAndName(PartitionEntity thePartition) {
		if (thePartition.getId() == null || isBlank(thePartition.getName())) {
			String msg = myFhirCtx.getLocalizer().getMessage(PartitionLookupSvcImpl.class, "missingPartitionIdOrName");
			throw new InvalidRequestException(msg);
		}

		if (!PARTITION_NAME_VALID_PATTERN.matcher(thePartition.getName()).matches()) {
			String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "invalidName", thePartition.getName());
			throw new InvalidRequestException(msg);
		}

	}

	private class NameToPartitionCacheLoader implements @NonNull CacheLoader<String, PartitionEntity> {
		@Nullable
		@Override
		public PartitionEntity load(@NonNull String theName) {
			return myTxTemplate.execute(t -> myPartitionDao
				.findForName(theName)
				.orElseThrow(() -> {
					String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "invalidName", theName);
					return new ResourceNotFoundException(msg);
				}));
		}
	}

	private class IdToPartitionCacheLoader implements @NonNull CacheLoader<Integer, PartitionEntity> {
		@Nullable
		@Override
		public PartitionEntity load(@NonNull Integer theId) {
			return myTxTemplate.execute(t -> myPartitionDao
				.findById(theId)
				.orElseThrow(() -> {
					String msg = myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "unknownPartitionId", theId);
					return new ResourceNotFoundException(msg);
				}));
		}
	}

	public static void validatePartitionIdSupplied(FhirContext theFhirContext, Integer thePartitionId) {
		if (thePartitionId == null) {
			String msg = theFhirContext.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "noIdSupplied");
			throw new InvalidRequestException(msg);
		}
	}
}
