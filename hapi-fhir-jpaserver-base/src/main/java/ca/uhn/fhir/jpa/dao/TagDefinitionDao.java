package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ThreadPoolUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is responsible for getting or creating tags.
 * The class is designed to do the tag retrieval in a separate thread and transaction to avoid any form of
 * suspension of any current transaction, an operation that is not widely supported in an XA environment.
 */
public class TagDefinitionDao {
	// total attempts to do a tag transaction
	private static final int TOTAL_TAG_READ_ATTEMPTS = 10;
	private static final Logger ourLog = LoggerFactory.getLogger(TagDefinitionDao.class);
	private final ThreadPoolTaskExecutor taskExecutor = ThreadPoolUtil.newThreadPool(1, 5, "getOrCreateTag-");

	private final EntityManagerFactory myEntityManagerFactory;
	private final PlatformTransactionManager myTransactionManager;

	public TagDefinitionDao(
			EntityManagerFactory theEntityManagerFactory, PlatformTransactionManager theTransactionManager) {
		this.myEntityManagerFactory = theEntityManagerFactory;
		this.myTransactionManager = theTransactionManager;
	}

	/**
	 * Gets the tag defined by the fed in values, or saves it if it does not
	 * exist.
	 * <p>
	 * Can also throw an InternalErrorException if something bad happens.
	 */
	TagDefinition getOrCreateTag(
			TagTypeEnum theTagType,
			String theScheme,
			String theTerm,
			String theLabel,
			String theVersion,
			Boolean theUserSelected) {
		try {
			// Execute tag retrieval in a separate thread to avoid transaction suspension issues,
			// which can cause problems in an XA environment that does not support suspend/resuming transactions,
			// such as when using the PostgreSQL JDBC driver.
			Future<TagDefinition> future = taskExecutor.submit(new RetryableGetOrCreateTagUnitOfWork(
					myEntityManagerFactory,
					myTransactionManager,
					theTagType,
					theScheme,
					theTerm,
					theLabel,
					theVersion,
					theUserSelected));
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new InternalErrorException(
					Msg.code(2547) + "Tag get/create thread failed to execute: " + e.getMessage());
		}
	}

	static class RetryableGetOrCreateTagUnitOfWork implements Callable<TagDefinition> {
		private final EntityManagerFactory myEntityManagerFactory;
		private final PlatformTransactionManager myTransactionManager;
		private final TagTypeEnum theTagType;
		private final String theScheme;
		private final String theTerm;
		private final String theLabel;
		private final String theVersion;
		private final Boolean theUserSelected;

		public RetryableGetOrCreateTagUnitOfWork(
				EntityManagerFactory theEntityManagerFactory,
				PlatformTransactionManager theTransactionManager,
				TagTypeEnum theTagType,
				String theScheme,
				String theTerm,
				String theLabel,
				String theVersion,
				Boolean theUserSelected) {
			this.myEntityManagerFactory = theEntityManagerFactory;
			this.myTransactionManager = theTransactionManager;
			this.theTagType = theTagType;
			this.theScheme = theScheme;
			this.theTerm = theTerm;
			this.theLabel = theLabel;
			this.theVersion = theVersion;
			this.theUserSelected = theUserSelected;
		}

		@Override
		public TagDefinition call() throws Exception {
			// Create a new entity manager for the current thread.
			try (EntityManager myEntityManager = myEntityManagerFactory.createEntityManager()) {
				TransactionTemplate template = new TransactionTemplate(myTransactionManager);
				template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

				// this transaction will attempt to get or create the tag,
				// repeating (on any failure) 10 times.
				// if it fails more than this, we will throw exceptions
				TagDefinition retVal;
				int count = 0;
				HashSet<Throwable> throwables = new HashSet<>();
				do {
					try {
						retVal = template.execute(new TransactionCallback<>() {
							// do the actual DB call(s) to read and/or write the values
							private TagDefinition readOrCreate() {
								TagDefinition val;
								try {
									// Join transaction if needed.
									if (!myEntityManager.isJoinedToTransaction()) {
										myEntityManager.joinTransaction();
									}

									TypedQuery<TagDefinition> q =
										buildTagQuery(myEntityManager, theTagType, theScheme, theTerm, theVersion, theUserSelected);
									q.setMaxResults(1);

									val = q.getSingleResult();
								} catch (NoResultException e) {
									val = new TagDefinition(theTagType, theScheme, theTerm, theLabel);
									val.setVersion(theVersion);
									val.setUserSelected(theUserSelected);
									myEntityManager.persist(val);
									myEntityManager.flush();
								}
								return val;
							}

							@Override
							public TagDefinition doInTransaction(TransactionStatus status) {
								TagDefinition tag = null;

								try {
									tag = readOrCreate();
								} catch (Exception ex) {
									// log any exceptions - just in case
									// they may be signs of things to come...
									ourLog.warn(
											"Tag read/write failed: "
													+ ex.getMessage() + ". "
													+ "This is not a failure on its own, "
													+ "but could be useful information in the result of an actual failure.",
											ex);
									throwables.add(ex);
								}

								return tag;
							}
						});
					} catch (Exception ex) {
						// transaction template can fail if connections to db are exhausted and/or timeout
						ourLog.warn(
								"Transaction failed with: {}. Transaction will rollback and be reattempted.",
								ex.getMessage());
						retVal = null;
					}

					// Clear the persistence context to avoid stale data on retry
					if (retVal == null) {
						myEntityManager.clear();
					}

					count++;
				} while (retVal == null && count < TOTAL_TAG_READ_ATTEMPTS);

				if (retVal == null) {
					// if tag is still null,
					// something bad must be happening
					// - throw
					String msg = throwables.stream().map(Throwable::getMessage).collect(Collectors.joining(", "));
					throw new InternalErrorException(Msg.code(2023)
							+ "Tag get/create failed after "
							+ TOTAL_TAG_READ_ATTEMPTS
							+ " attempts with error(s): "
							+ msg);
				}

				return retVal;
			}
		}

		private TypedQuery<TagDefinition> buildTagQuery(
				EntityManager theEntityManager,
				TagTypeEnum theTagType,
				String theScheme,
				String theTerm,
				String theVersion,
				Boolean theUserSelected) {
			CriteriaBuilder builder = theEntityManager.getCriteriaBuilder();
			CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
			Root<TagDefinition> from = cq.from(TagDefinition.class);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.and(
					builder.equal(from.get("myTagType"), theTagType), builder.equal(from.get("myCode"), theTerm)));

			predicates.add(
					isBlank(theScheme)
							? builder.isNull(from.get("mySystem"))
							: builder.equal(from.get("mySystem"), theScheme));

			predicates.add(
					isBlank(theVersion)
							? builder.isNull(from.get("myVersion"))
							: builder.equal(from.get("myVersion"), theVersion));

			predicates.add(
					isNull(theUserSelected)
							? builder.isNull(from.get("myUserSelected"))
							: builder.equal(from.get("myUserSelected"), theUserSelected));

			cq.where(predicates.toArray(new Predicate[0]));

			TypedQuery<TagDefinition> query = theEntityManager.createQuery(cq);
			query.setMaxResults(1);

			return query;
		}
	}
}
