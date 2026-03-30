/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IExceptionAwareRollbackAction;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This {@link ISearchParamPreSynchronizeHook} is invoked right before synchronizing
 * unique index search parameters (i.e. {@link ResourceIndexedComboStringUnique}) to the database.
 * It registers a rollback action (which will only be executed if the transaction is rolled back)
 * that checks to see if the rollback is related to a unique constraint violation. If so,
 * it executes a database query to figure out which unique indexes entity caused the violation
 * (i.e. which duplicate value actually caused the failure) so that we can provide a friendly
 * error message.
 */
class UniqueIndexPreExistenceChecker implements ISearchParamPreSynchronizeHook<ResourceIndexedComboStringUnique> {

	/*
	 * Note: This class is tested by FhirResourceDaoR4ComboUniqueParamTest
	 */
	private static final String INDEXES_REQUEST_USERDATA_KEY =
			UniqueIndexPreExistenceChecker.class.getName() + "_INDEXES";

	private final FhirContext myFhirContext;
	private final IResourceIndexedComboStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	private final IHapiTransactionService myTransactionSvc;

	UniqueIndexPreExistenceChecker(
			FhirContext theFhirContext,
			IResourceIndexedComboStringUniqueDao theResourceIndexedCompositeStringUniqueDao,
			IHapiTransactionService theTransactionSvc) {
		myFhirContext = theFhirContext;
		myResourceIndexedCompositeStringUniqueDao = theResourceIndexedCompositeStringUniqueDao;
		myTransactionSvc = theTransactionSvc;
	}

	@Override
	public void preSave(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			Collection<ResourceIndexedComboStringUnique> theParamsToRemove,
			Collection<ResourceIndexedComboStringUnique> theParamsToAdd) {

		assert !theParamsToAdd.isEmpty() || !theParamsToRemove.isEmpty();

		if (theRequestDetails != null) {

			/// If we're operating inside a FHIR Transaction, we could potentially be writing
			/// many resources that all have unique indexes. Instead of performing an individual
			/// lookup for each of them, we batch them all into a single collection
			/// within {@link TransactionIndexes}, and store that in the {@link TransactionDetails}
			/// UserData.
			TransactionIndexes transactionIndexes =
					(TransactionIndexes) theTransactionDetails.getUserData().get(INDEXES_REQUEST_USERDATA_KEY);
			if (transactionIndexes == null) {
				HashSet<ResourceIndexedComboStringUnique> indexesToRemove = new HashSet<>();
				HashSet<ResourceIndexedComboStringUnique> indexesToAdd = new HashSet<>();
				transactionIndexes = new TransactionIndexes(indexesToRemove, indexesToAdd);
				theTransactionDetails.getUserData().put(INDEXES_REQUEST_USERDATA_KEY, transactionIndexes);
				theTransactionDetails.addRollbackUndoAction(
						new MyRollbackAction(theRequestDetails, indexesToRemove, indexesToAdd));
			}

			transactionIndexes.indexesToRemove().addAll(theParamsToRemove);
			for (ResourceIndexedComboStringUnique param : theParamsToAdd) {
				boolean added = transactionIndexes.indexesToAdd().add(param);
				if (theTransactionDetails.isFhirTransaction() && !added) {
					String msg = myFhirContext
							.getLocalizer()
							.getMessage(
									BaseHapiFhirDao.class,
									"uniqueIndexConflictFailureInSameFhirTransaction",
									param.getResource().getResourceType(),
									param.getIndexString(),
									getSearchParameterId(param));

					// Use ResourceVersionConflictException here because the HapiTransactionService
					// catches this and can retry it if needed
					throw new ResourceVersionConflictException(Msg.code(2883) + msg);
				}
			}
		}
	}

	private static String getSearchParameterId(ResourceIndexedComboStringUnique theIndex) {
		String searchParameterId = "(unknown)";
		if (theIndex.getSearchParameterId() != null) {
			searchParameterId = theIndex.getSearchParameterId().getValue();
		}
		return searchParameterId;
	}

	/**
	 * This record is stored in the {@link TransactionDetails} UserData, and is used to
	 * maintain collections of all the {@link ResourceIndexedComboStringUnique} entities
	 * that are being added or removed in the current transaction.
	 */
	private record TransactionIndexes(
			Set<ResourceIndexedComboStringUnique> indexesToRemove,
			Set<ResourceIndexedComboStringUnique> indexesToAdd) {}

	/**
	 * This action is performed only if the transaction is rolled back.
	 */
	private class MyRollbackAction implements IExceptionAwareRollbackAction {
		private final RequestDetails myRequestDetails;
		private final HashSet<ResourceIndexedComboStringUnique> myIndexesToRemove;
		private final HashSet<ResourceIndexedComboStringUnique> myIndexesToAdd;

		public MyRollbackAction(
				RequestDetails theRequestDetails,
				HashSet<ResourceIndexedComboStringUnique> theIndexesToRemove,
				HashSet<ResourceIndexedComboStringUnique> theIndexesToAdd) {
			myRequestDetails = theRequestDetails;
			myIndexesToRemove = theIndexesToRemove;
			myIndexesToAdd = theIndexesToAdd;
		}

		@Nonnull
		@Override
		public RuntimeException onRollback(@Nonnull RuntimeException theCause) {
			RuntimeException cause = theCause;
			if (isCausedByUniqueComboConstraintFailure(cause)) {
				cause = validateNoExistingUniqueIndexesMatchAny(
						myRequestDetails, myIndexesToRemove, myIndexesToAdd, cause);
			}
			return cause;
		}

		/**
		 * Given an exception thrown during processing in the JPA server, checks whether
		 * the exception is caused by a unique constraint violation on the
		 * {@link ResourceIndexedComboStringUnique} table.
		 */
		private boolean isCausedByUniqueComboConstraintFailure(Exception theException) {
			if (theException instanceof ResourceVersionConflictException) {
				Throwable cause = theException.getCause();
				if (cause
						instanceof org.hibernate.exception.ConstraintViolationException constraintViolationException) {
					String constraintName = constraintViolationException.getConstraintName();
					constraintName = StringUtils.upperCase(constraintName, Locale.US);
					return Strings.CS.contains(
							constraintName, ResourceIndexedComboStringUnique.IDX_IDXCMPSTRUNIQ_STRING);
				}
			}
			return false;
		}

		private RuntimeException validateNoExistingUniqueIndexesMatchAny(
				RequestDetails theRequestDetails,
				Collection<ResourceIndexedComboStringUnique> theParamsToRemove,
				Collection<ResourceIndexedComboStringUnique> theParamsToAdd,
				RuntimeException theCause) {
			Map<String, ResourceIndexedComboStringUnique> existingStringToParam =
					fetchExistingMatchingParams(theRequestDetails, theParamsToAdd);
			for (ResourceIndexedComboStringUnique theIndex : theParamsToAdd) {
				ResourceIndexedComboStringUnique existing = existingStringToParam.get(theIndex.getIndexString());
				if (existing != null) {

					/*
					 * If we're reindexing, and the previous index row is being updated
					 * to add previously missing hashes, we may falsely detect that the index
					 * creation is going to fail.
					 */
					boolean existingIndexIsScheduledForRemoval = false;
					for (var next : theParamsToRemove) {
						if (existing == next) {
							existingIndexIsScheduledForRemoval = true;
							break;
						}
					}
					if (existingIndexIsScheduledForRemoval) {
						continue;
					}

					String msg = myFhirContext
							.getLocalizer()
							.getMessage(
									BaseHapiFhirDao.class,
									"uniqueIndexConflictFailure",
									existing.getResource().getResourceType(),
									theIndex.getIndexString(),
									existing.getResource()
											.getIdDt()
											.toUnqualifiedVersionless()
											.getValue(),
									getSearchParameterId(theIndex));

					// Use ResourceVersionConflictException here because the HapiTransactionService
					// catches this and can retry it if needed
					return new ResourceVersionConflictException(Msg.code(1093) + msg, theCause);
				}
			}

			return theCause;
		}

		private Map<String, ResourceIndexedComboStringUnique> fetchExistingMatchingParams(
				RequestDetails theRequestDetails, Collection<ResourceIndexedComboStringUnique> theParamsToAdd) {

			ListMultimap<PartitionablePartitionId, String> partitionToQueryString =
					MultimapBuilder.hashKeys().arrayListValues().build();
			for (ResourceIndexedComboStringUnique param : theParamsToAdd) {
				partitionToQueryString.put(param.getPartitionId(), param.getIndexString());
			}

			Map<String, ResourceIndexedComboStringUnique> existingStringToParam = new HashMap<>();
			for (PartitionablePartitionId partitionId : partitionToQueryString.keySet()) {
				List<String> params = partitionToQueryString.get(partitionId);
				QueryChunker.chunk(params, chunk -> {
					fetchMatchingIndexStrings(theRequestDetails, partitionId, chunk, existingStringToParam);
				});
			}

			return existingStringToParam;
		}

		private void fetchMatchingIndexStrings(
				RequestDetails theRequestDetails,
				PartitionablePartitionId thePartitionId,
				List<String> theUniqueIndexStrings,
				Map<String, ResourceIndexedComboStringUnique> theMapToPopulate) {
			List<ResourceIndexedComboStringUnique> existingParams = myTransactionSvc
					.withRequest(theRequestDetails)
					.withRequestPartitionId(thePartitionId.toPartitionId())
					.execute(() -> {
						if (thePartitionId.getPartitionId() == null) {
							return myResourceIndexedCompositeStringUniqueDao
									.findByQueryStringNullPartitionAndFetchResource(theUniqueIndexStrings);
						} else {
							return myResourceIndexedCompositeStringUniqueDao.findByQueryStringAndFetchResource(
									thePartitionId.getPartitionId(), theUniqueIndexStrings);
						}
					});

			if (!existingParams.isEmpty()) {
				for (ResourceIndexedComboStringUnique existingParam : existingParams) {
					theMapToPopulate.put(existingParam.getIndexString(), existingParam);
				}
			}
		}
	}
}
