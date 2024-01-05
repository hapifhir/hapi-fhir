/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ICallable;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class PartitionLookupSvcImpl implements IPartitionLookupSvc {

	private static final Pattern PARTITION_NAME_VALID_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionLookupSvcImpl.class);

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private IInterceptorService myInterceptorService;

	@Autowired
	private IPartitionDao myPartitionDao;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Autowired
	private FhirContext myFhirCtx;

	@Autowired
	private PlatformTransactionManager myTxManager;

	private TransactionTemplate myTxTemplate;

	/**
	 * Constructor
	 */
	public PartitionLookupSvcImpl() {
		super();
	}

	@Override
	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);
	}

	@Override
	public PartitionEntity getPartitionByName(String theName) {
		Validate.notBlank(theName, "The name must not be null or blank");
		validateNotInUnnamedPartitionMode();
		if (JpaConstants.DEFAULT_PARTITION_NAME.equals(theName)) {
			return null;
		}
		return myMemoryCacheService.get(
				MemoryCacheService.CacheEnum.NAME_TO_PARTITION, theName, this::lookupPartitionByName);
	}

	@Override
	public PartitionEntity getPartitionById(Integer thePartitionId) {
		validatePartitionIdSupplied(myFhirCtx, thePartitionId);
		if (myPartitionSettings.isUnnamedPartitionMode()) {
			return new PartitionEntity().setId(thePartitionId);
		}
		if (myPartitionSettings.getDefaultPartitionId() != null
				&& myPartitionSettings.getDefaultPartitionId().equals(thePartitionId)) {
			return new PartitionEntity().setId(thePartitionId).setName(JpaConstants.DEFAULT_PARTITION_NAME);
		}
		return myMemoryCacheService.get(
				MemoryCacheService.CacheEnum.ID_TO_PARTITION, thePartitionId, this::lookupPartitionById);
	}

	@Override
	public void invalidateCaches() {
		myMemoryCacheService.invalidateCaches(
				MemoryCacheService.CacheEnum.NAME_TO_PARTITION, MemoryCacheService.CacheEnum.ID_TO_PARTITION);
	}

	/**
	 * Generate a random postive integer between 1 and Integer.MAX_VALUE, which is guaranteed to  be unused by an existing partition.
	 * @return an integer representing a partition ID that is not currently in use by the system.
	 */
	public int generateRandomUnusedPartitionId() {
		int candidate = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		Optional<PartitionEntity> partition = myPartitionDao.findById(candidate);
		while (partition.isPresent()) {
			candidate = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
			partition = myPartitionDao.findById(candidate);
		}
		return candidate;
	}

	@Override
	@Transactional
	public PartitionEntity createPartition(PartitionEntity thePartition, RequestDetails theRequestDetails) {

		validateNotInUnnamedPartitionMode();
		validateHaveValidPartitionIdAndName(thePartition);
		validatePartitionNameDoesntAlreadyExist(thePartition.getName());
		validIdUponCreation(thePartition);
		ourLog.info("Creating new partition with ID {} and Name {}", thePartition.getId(), thePartition.getName());

		PartitionEntity retVal = myPartitionDao.save(thePartition);

		// Interceptor call: STORAGE_PARTITION_CREATED
		if (myInterceptorService.hasHooks(Pointcut.STORAGE_PARTITION_CREATED)) {
			HookParams params = new HookParams()
					.add(RequestPartitionId.class, thePartition.toRequestPartitionId())
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			myInterceptorService.callHooks(Pointcut.STORAGE_PARTITION_CREATED, params);
		}

		return retVal;
	}

	@Override
	@Transactional
	public PartitionEntity updatePartition(PartitionEntity thePartition) {
		validateNotInUnnamedPartitionMode();
		validateHaveValidPartitionIdAndName(thePartition);

		Optional<PartitionEntity> existingPartitionOpt = myPartitionDao.findById(thePartition.getId());
		if (existingPartitionOpt.isPresent() == false) {
			String msg = myFhirCtx
					.getLocalizer()
					.getMessageSanitized(PartitionLookupSvcImpl.class, "unknownPartitionId", thePartition.getId());
			throw new InvalidRequestException(Msg.code(1307) + msg);
		}

		PartitionEntity existingPartition = existingPartitionOpt.get();
		if (!thePartition.getName().equalsIgnoreCase(existingPartition.getName())) {
			validatePartitionNameDoesntAlreadyExist(thePartition.getName());
		}

		existingPartition.setName(thePartition.getName());
		existingPartition.setDescription(thePartition.getDescription());
		myPartitionDao.save(existingPartition);
		invalidateCaches();
		return existingPartition;
	}

	@Override
	@Transactional
	public void deletePartition(Integer thePartitionId) {
		validatePartitionIdSupplied(myFhirCtx, thePartitionId);
		validateNotInUnnamedPartitionMode();

		Optional<PartitionEntity> partition = myPartitionDao.findById(thePartitionId);
		if (!partition.isPresent()) {
			String msg = myFhirCtx
					.getLocalizer()
					.getMessageSanitized(PartitionLookupSvcImpl.class, "unknownPartitionId", thePartitionId);
			throw new IllegalArgumentException(Msg.code(1308) + msg);
		}

		myPartitionDao.delete(partition.get());

		invalidateCaches();
	}

	@Override
	public List<PartitionEntity> listPartitions() {
		List<PartitionEntity> allPartitions = myPartitionDao.findAll();
		return allPartitions;
	}

	private void validatePartitionNameDoesntAlreadyExist(String theName) {
		if (myPartitionDao.findForName(theName).isPresent()) {
			String msg = myFhirCtx
					.getLocalizer()
					.getMessageSanitized(PartitionLookupSvcImpl.class, "cantCreateDuplicatePartitionName", theName);
			throw new InvalidRequestException(Msg.code(1309) + msg);
		}
	}

	private void validIdUponCreation(PartitionEntity thePartition) {
		if (myPartitionDao.findById(thePartition.getId()).isPresent()) {
			String msg =
					myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "duplicatePartitionId");
			throw new InvalidRequestException(Msg.code(2366) + msg);
		}
	}

	private void validateHaveValidPartitionIdAndName(PartitionEntity thePartition) {
		if (thePartition.getId() == null || isBlank(thePartition.getName())) {
			String msg = myFhirCtx.getLocalizer().getMessage(PartitionLookupSvcImpl.class, "missingPartitionIdOrName");
			throw new InvalidRequestException(Msg.code(1310) + msg);
		}

		if (thePartition.getName().equals(JpaConstants.DEFAULT_PARTITION_NAME)) {
			String msg = myFhirCtx
					.getLocalizer()
					.getMessageSanitized(PartitionLookupSvcImpl.class, "cantCreateDefaultPartition");
			throw new InvalidRequestException(Msg.code(1311) + msg);
		}

		if (!PARTITION_NAME_VALID_PATTERN.matcher(thePartition.getName()).matches()) {
			String msg = myFhirCtx
					.getLocalizer()
					.getMessageSanitized(PartitionLookupSvcImpl.class, "invalidName", thePartition.getName());
			throw new InvalidRequestException(Msg.code(1312) + msg);
		}
	}

	private void validateNotInUnnamedPartitionMode() {
		if (myPartitionSettings.isUnnamedPartitionMode()) {
			throw new MethodNotAllowedException(
					Msg.code(1313) + "Can not invoke this operation in unnamed partition mode");
		}
	}

	private PartitionEntity lookupPartitionByName(@Nonnull String theName) {
		return executeInTransaction(() -> myPartitionDao.findForName(theName)).orElseThrow(() -> {
			String msg =
					myFhirCtx.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "invalidName", theName);
			return new ResourceNotFoundException(msg);
		});
	}

	private PartitionEntity lookupPartitionById(@Nonnull Integer theId) {
		try {
			return executeInTransaction(() -> myPartitionDao.findById(theId)).orElseThrow(() -> {
				String msg = myFhirCtx
						.getLocalizer()
						.getMessageSanitized(PartitionLookupSvcImpl.class, "unknownPartitionId", theId);
				return new ResourceNotFoundException(msg);
			});
		} catch (ResourceNotFoundException e) {
			List<PartitionEntity> allPartitions = executeInTransaction(() -> myPartitionDao.findAll());
			String allPartitionsString = allPartitions.stream()
					.map(t -> t.getId() + "/" + t.getName())
					.collect(Collectors.joining(", "));
			ourLog.warn("Failed to find partition with ID {}.  Current partitions: {}", theId, allPartitionsString);
			throw e;
		}
	}

	protected <T> T executeInTransaction(ICallable<T> theCallable) {
		return myTxTemplate.execute(tx -> theCallable.call());
	}

	public static void validatePartitionIdSupplied(FhirContext theFhirContext, Integer thePartitionId) {
		if (thePartitionId == null) {
			String msg =
					theFhirContext.getLocalizer().getMessageSanitized(PartitionLookupSvcImpl.class, "noIdSupplied");
			throw new InvalidRequestException(Msg.code(1314) + msg);
		}
	}
}
