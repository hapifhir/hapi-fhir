/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringTokenizer;

/**
 * This class parses and serializes the <code>X-Transaction-Semantics</code>
 * header, which is a custom HAPI FHIR extension affecting the way that
 * FHIR transactions are processed.
 *
 * @see ca.uhn.fhir.jpa.dao.BaseTransactionProcessor
 * @since 8.2.0
 */
public class TransactionSemanticsHeader {

	public static final String RETRY_COUNT = "retryCount";
	public static final String MIN_DELAY = "minRetryDelay";
	public static final String MAX_DELAY = "maxRetryDelay";
	public static final String TRY_BATCH_AS_TRANSACTION_FIRST = "tryBatchAsTransactionFirst";
	public static final TransactionSemanticsHeader DEFAULT = newBuilder().build();
	public static final String HEADER_NAME = "X-Transaction-Semantics";

	private final Integer myRetryCount;
	private final Integer myMinRetryDelay;
	private final Integer myMaxRetryDelay;
	private final boolean myTryBatchAsTransactionFirst;

	/**
	 * Non-instantiable, see {@link #newBuilder()}
	 */
	private TransactionSemanticsHeader(
			Integer theRetryCount,
			Integer theMinRetryDelay,
			Integer theMaxRetryDelay,
			boolean theTryAsBatchAsTransactionFirst) {
		myRetryCount = theRetryCount;
		myMinRetryDelay = theMinRetryDelay;
		myMaxRetryDelay = theMaxRetryDelay;
		myTryBatchAsTransactionFirst = theTryAsBatchAsTransactionFirst;
	}

	/**
	 * Specifies the number of retry attempts which should be attempted
	 * if the initial transaction processing fails with any kind of error.
	 * A value of 0 (or {@literal null}) means that the transaction will be
	 * attempted only once (i.e. the default behaviour). A value of 2 means
	 * that the transaction will be attempted once, and if it fails, up to
	 * two more attempts will be made before giving up.
	 */
	public Integer getRetryCount() {
		return myRetryCount;
	}

	/**
	 * When automatically retrying a failed transaction, the system will
	 * first sleep for a minimum of this number of milliseconds.
	 */
	public Integer getMinRetryDelay() {
		return myMinRetryDelay;
	}

	/**
	 * When automatically retrying a failed transaction, the system will
	 * first sleep for a minimum of this number of milliseconds.
	 */
	public Integer getMaxRetryDelay() {
		return myMaxRetryDelay;
	}

	/**
	 * When processing a FHIR Batch bundle, try it as a FHIR transaction first, and only switch
	 * to batch mode on the first retry. This option is useful in cases where data can safely be
	 * ingested as a FHIR Batch, since FHIR Transaction processing is generally significantly
	 * faster. However, FHIR transactions exhibit an all-or-nothing failure mode which is not
	 * always desirable for batch ingestion, so this option provides an easy fallback which brings
	 * the benefits of both approaches.
	 */
	public boolean isTryBatchAsTransactionFirst() {
		return myTryBatchAsTransactionFirst;
	}

	/**
	 * Serializes the values as a header value (not including the header name)
	 */
	public String toHeaderValue() {
		StringBuilder b = new StringBuilder();

		if (myRetryCount != null) {
			b.append(RETRY_COUNT).append('=').append(myRetryCount);

			// None of the following settings has any meaning unless a
			// retry count is specified
			if (myMinRetryDelay != null) {
				b.append("; ");
				b.append(MIN_DELAY).append('=').append(myMinRetryDelay);
			}
			if (myMaxRetryDelay != null) {
				b.append("; ");
				b.append(MAX_DELAY).append('=').append(myMaxRetryDelay);
			}
			if (myTryBatchAsTransactionFirst) {
				b.append("; ");
				b.append(TRY_BATCH_AS_TRANSACTION_FIRST).append('=').append("true");
			}
		}

		return b.toString();
	}

	/**
	 * Add this header to a RequestDetails object
	 *
	 * @since 8.4.0
	 */
	public void applyTo(RequestDetails theRequestDetails) {
		theRequestDetails.addHeader(HEADER_NAME, toHeaderValue());
	}


	/**
	 * Parses a header value (not including the header name) into a new
	 * {@link TransactionSemanticsHeader} instance.
	 */
	public static TransactionSemanticsHeader parse(@Nonnull String theHeaderValue) {
		Validate.notNull(theHeaderValue, "theHeaderValue must not be null");
		Integer retryCount = null;
		Integer minRetryDelay = null;
		Integer maxRetryDelay = null;
		boolean tryBatchAsTransactionFirst = false;

		StringTokenizer tok = new StringTokenizer(theHeaderValue, ";");
		while (tok.hasNext()) {
			String next = tok.nextToken();
			int equalsIdx = next.indexOf('=');
			if (equalsIdx == -1) {
				continue;
			}

			String name = next.substring(0, equalsIdx).trim();
			String value = next.substring(equalsIdx + 1).trim();

			switch (name) {
				case RETRY_COUNT:
					retryCount = parsePositiveInteger(value);
					break;
				case MIN_DELAY:
					minRetryDelay = parsePositiveInteger(value);
					break;
				case MAX_DELAY:
					maxRetryDelay = parsePositiveInteger(value);
					break;
				case TRY_BATCH_AS_TRANSACTION_FIRST:
					tryBatchAsTransactionFirst = parseBoolean(value);
					break;
			}
		}

		return new TransactionSemanticsHeader(retryCount, minRetryDelay, maxRetryDelay, tryBatchAsTransactionFirst);
	}

	/**
	 * Begin building a new {@link TransactionSemanticsHeader} instance
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	private static Integer parsePositiveInteger(String theValue) {
		try {
			int retVal = Integer.parseInt(theValue);
			if (retVal <= 0) {
				return null;
			}
			return retVal;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static boolean parseBoolean(String theValue) {
		return "true".equalsIgnoreCase(theValue);
	}

	public static final class Builder {

		private Integer myRetryCount;
		private Integer myMinRetryDelay;
		private Integer myMaxRetryDelay;
		private boolean myTryBatchAsTransactionFirst;

		private Builder() {}

		/**
		 * Specifies the number of retry attempts which should be attempted
		 * if the initial transaction processing fails with any kind of error.
		 * A value of 0 (or {@literal null} means that the transaction will be
		 * attempted once only (i.e. the default behaviour). A value of 2 means
		 * that the transaction will be attempted once, and if it fails, up to
		 * two more attempts will be made before giving up.
		 */
		public Builder withRetryCount(Integer theRetryCount) {
			Validate.isTrue(
					theRetryCount == null || theRetryCount >= 0, "Retry count must be null or a non-negative integer");
			myRetryCount = theRetryCount;
			return this;
		}

		/**
		 * When automatically retrying a failed transaction, the system will
		 * first sleep for a minimum of this number of milliseconds.
		 */
		public Builder withMinRetryDelay(Integer theMinRetryDelay) {
			Validate.isTrue(
					theMinRetryDelay == null || theMinRetryDelay >= 0,
					"Retry delay must be null or a non-negative integer");
			myMinRetryDelay = theMinRetryDelay;
			return this;
		}

		/**
		 * When automatically retrying a failed transaction, the system will
		 * first sleep for a minimum of this number of milliseconds.
		 */
		public Builder withMaxRetryDelay(Integer theMaxRetryDelay) {
			Validate.isTrue(
					theMaxRetryDelay == null || theMaxRetryDelay >= 0,
					"Retry delay must be null or a non-negative integer");
			myMaxRetryDelay = theMaxRetryDelay;
			return this;
		}

		/**
		 * When processing a FHIR Batch bundle, try it as a FHIR transaction first, and only switch
		 * to batch mode on the first retry. This option is useful in cases where data can safely be
		 * ingested as a FHIR Batch, since FHIR Transaction processing is generally significantly
		 * faster. However, FHIR transactions exhibit an all-or-nothing failure mode which is not
		 * always desirable for batch ingestion, so this option provides an easy fallback which brings
		 * the benefits of both approaches.
		 */
		public Builder withTryBatchAsTransactionFirst(boolean theTryBatchAsTransactionFirst) {
			myTryBatchAsTransactionFirst = theTryBatchAsTransactionFirst;
			return this;
		}

		/**
		 * Construct the header
		 */
		public TransactionSemanticsHeader build() {
			return new TransactionSemanticsHeader(
					myRetryCount, myMinRetryDelay, myMaxRetryDelay, myTryBatchAsTransactionFirst);
		}
	}
}
