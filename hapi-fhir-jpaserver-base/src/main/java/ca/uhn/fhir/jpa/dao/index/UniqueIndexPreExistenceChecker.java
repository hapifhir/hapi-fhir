package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IExceptionAwareRollbackAction;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This {@link ISearchParamPreSynchronizeHook} is invoked right before synchronizing
 * unique index search parameters (i.e. {@link ResourceIndexedComboStringUnique}) to the database.
 * It registers a rollback action (which will only be executed if the transaction is rolled back)
 * that checks to see if the rollback is related to a unique constraint violation, and if so
 * it executes a database query to figure out which unique indexes entity caused the violation
 * (i.e. which duplicate value actually caused the failure) so that we can provide a friendly
 * error message.
 */
class UniqueIndexPreExistenceChecker implements ISearchParamPreSynchronizeHook<ResourceIndexedComboStringUnique> {
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
				theTransactionDetails.addRollbackUndoAction(new MyRollbackAction(theRequestDetails, indexesToRemove, indexesToAdd));
			}

			transactionIndexes.indexesToRemove().addAll(theParamsToRemove);
			for (ResourceIndexedComboStringUnique param : theParamsToAdd) {
				boolean added = transactionIndexes.indexesToAdd().add(param);
				if (!added) {
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
		Set<ResourceIndexedComboStringUnique> indexesToAdd) {
	}

	/**
	 * This action is performed only if the transaction is rolled back.
	 */
	private class MyRollbackAction implements IExceptionAwareRollbackAction {
		private final RequestDetails myRequestDetails;
		private final HashSet<ResourceIndexedComboStringUnique> myIndexesToRemove;
		private final HashSet<ResourceIndexedComboStringUnique> myIndexesToAdd;

		public MyRollbackAction(RequestDetails theRequestDetails, HashSet<ResourceIndexedComboStringUnique> theIndexesToRemove, HashSet<ResourceIndexedComboStringUnique> theIndexesToAdd) {
			myRequestDetails = theRequestDetails;
			myIndexesToRemove = theIndexesToRemove;
			myIndexesToAdd = theIndexesToAdd;
		}

		@Override
		public void onRollback(@Nonnull Exception cause) {
			if (isCausedByUniqueComboConstraintFailure(cause)) {
				validateNoExistingUniqueIndexesMatchAny(myRequestDetails, myIndexesToRemove, myIndexesToAdd);
			}
		}

		private boolean isCausedByUniqueComboConstraintFailure(Exception theException) {
			if (theException instanceof ResourceVersionConflictException) {
				Throwable cause = theException.getCause();
				if (cause instanceof org.hibernate.exception.ConstraintViolationException constraintViolationException) {
					String constraintName = constraintViolationException.getConstraintName();
					constraintName = StringUtils.upperCase(constraintName, Locale.US);
					return Strings.CS.contains(constraintName, ResourceIndexedComboStringUnique.IDX_IDXCMPSTRUNIQ_STRING);
				}
			}
			return false;
		}

		private void validateNoExistingUniqueIndexesMatchAny(
			RequestDetails theRequestDetails,
			Collection<ResourceIndexedComboStringUnique> theParamsToRemove,
			Collection<ResourceIndexedComboStringUnique> theParamsToAdd) {
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
					throw new ResourceVersionConflictException(Msg.code(1093) + msg);
				}
			}
		}


		private Map<String, ResourceIndexedComboStringUnique> fetchExistingMatchingParams(
			RequestDetails theRequestDetails, Collection<ResourceIndexedComboStringUnique> theParamsToRemove) {
			ImmutableListMultimap<Optional<Integer>, ResourceIndexedComboStringUnique> partitionIdToParam = Multimaps.index(
				theParamsToRemove, t -> Optional.ofNullable(t.getPartitionId().getPartitionId()));
			Map<String, ResourceIndexedComboStringUnique> existingStringToParam = null;
			for (Optional<Integer> partitionId : partitionIdToParam.keySet()) {
				List<ResourceIndexedComboStringUnique> params = partitionIdToParam.get(partitionId);
				Stream<String> paramIndexStringsStream =
					params.stream().map(ResourceIndexedComboStringUnique::getIndexString);
				List<List<String>> paramIndexStringsChunks =
					QueryChunker.chunk(paramIndexStringsStream).toList();
				for (List<String> paramIndexStrings : paramIndexStringsChunks) {

					List<ResourceIndexedComboStringUnique> existingParams = myTransactionSvc
						.withRequest(theRequestDetails)
						.withRequestPartitionId(RequestPartitionId.fromPartitionId(partitionId.orElse(null)))
						.execute(() -> {
							if (partitionId.isEmpty()) {
								return myResourceIndexedCompositeStringUniqueDao.findByQueryStringNullPartitionAndFetchResource(
									paramIndexStrings);
							} else {
								return myResourceIndexedCompositeStringUniqueDao.findByQueryStringAndFetchResource(
									partitionId.get(), paramIndexStrings);
							}
						});

					if (!existingParams.isEmpty()) {
						if (existingStringToParam == null) {
							existingStringToParam = new HashMap<>();
						}
						for (ResourceIndexedComboStringUnique existingParam : existingParams) {
							existingStringToParam.put(existingParam.getIndexString(), existingParam);
						}
					}
				}
			}

			if (existingStringToParam == null) {
				return Map.of();
			}

			return existingStringToParam;
		}

	}
}
