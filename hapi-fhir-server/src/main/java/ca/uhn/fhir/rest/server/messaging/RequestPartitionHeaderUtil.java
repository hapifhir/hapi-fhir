package ca.uhn.fhir.rest.server.messaging;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.DEFAULT_PARTITION_NAME;

/**
 * Utility class for parsing and validating partition information from HTTP request headers.
 * <p>
 * This class provides methods to convert the X-Request-Partition-IDs header value into
 * a {@link ca.uhn.fhir.interceptor.model.RequestPartitionId} object that can be used
 * by the application to determine which partition(s) to operate on.
 * </p>
 */
public final class RequestPartitionHeaderUtil {

	public static final String HTTP_HEADER_SOURCE_NAME = "header: " + Constants.HEADER_X_REQUEST_PARTITION_IDS;

	private RequestPartitionHeaderUtil() {}

	/**
	 * Parses the X-Request-Partition-IDs header value and converts it to a {@link RequestPartitionId} object.
	 *
	 * @param theSourceName               The name of the source of the value, used for error messages
	 * @param thePartitionHeaderValue     The value of the X-Request-Partition-IDs header, may be null
	 * @param theDefaultPartitionSettings Settings that provide the default partition ID
	 * @return A {@link RequestPartitionId} object representing the partition(s) specified in the header, or null if the header is null
	 * @throws InvalidRequestException If the header value is invalid
	 */
	@Nullable
	public static RequestPartitionId fromHeader(
			@Nonnull String theSourceName,
			@Nullable String thePartitionHeaderValue,
			@Nullable IDefaultPartitionSettings theDefaultPartitionSettings) {
		return fromHeader(theSourceName, thePartitionHeaderValue, false, theDefaultPartitionSettings);
	}

	@Nullable
	public static RequestPartitionId fromHeader(
			@Nullable String thePartitionHeaderValue, @Nullable IDefaultPartitionSettings theDefaultPartitionSettings) {
		return fromHeader(HTTP_HEADER_SOURCE_NAME, thePartitionHeaderValue, false, theDefaultPartitionSettings);
	}

	/**
	 * Parses the X-Request-Partition-IDs header value and converts it to a {@link RequestPartitionId} object,
	 * including only the first partition ID from the header. This useful when using the RequestPartitionId for
	 * a write operation.
	 *
	 * @param theSourceName               The name of the source of the value, used for error messages
	 * @param thePartitionHeaderValue     The value of the X-Request-Partition-IDs header, may be null
	 * @param theDefaultPartitionSettings Settings that provide the default partition ID
	 * @return A {@link RequestPartitionId} object representing the first partition specified in the header, or null if the header is null
	 * @throws InvalidRequestException If the header value is invalid
	 */
	@SuppressWarnings("unused")
	@Nullable
	public static RequestPartitionId fromHeaderFirstPartitionOnly(
			@Nonnull String theSourceName,
			@Nullable String thePartitionHeaderValue,
			@Nullable IDefaultPartitionSettings theDefaultPartitionSettings) {
		return fromHeader(theSourceName, thePartitionHeaderValue, true, theDefaultPartitionSettings);
	}

	@Nullable
	public static RequestPartitionId fromHeaderFirstPartitionOnly(
			@Nullable String thePartitionHeaderValue, @Nullable IDefaultPartitionSettings theDefaultPartitionSettings) {
		return fromHeader(HTTP_HEADER_SOURCE_NAME, thePartitionHeaderValue, true, theDefaultPartitionSettings);
	}

	/**
	 * Validates the syntax of the X-Request-Partition-IDs header value.
	 * <p>
	 * This method checks if the header value can be successfully parsed into a {@link RequestPartitionId} object.
	 * It does not validate whether the partition IDs actually exist in the system.
	 * </p>
	 *
	 * @param theSourceName           The name of the source of the value, used for error messages
	 * @param thePartitionHeaderValue The value of the X-Request-Partition-IDs header to validate
	 * @throws InvalidRequestException If the header value is invalid
	 */
	public static void validateHeader(String theSourceName, String thePartitionHeaderValue) {
		// We're only validating syntax, so it doesn't matter what the default partition id is
		fromHeader(theSourceName, thePartitionHeaderValue, new IDefaultPartitionSettings() {});
	}

	public static void validateHeader(String thePartitionHeaderValue) {
		validateHeader(HTTP_HEADER_SOURCE_NAME, thePartitionHeaderValue);
	}

	/**
	 * Parses the X-Request-Partition-IDs header value and converts it to a {@link RequestPartitionId} object.
	 * <p>
	 * The header value can be:
	 * <ul>
	 *   <li>A single partition ID (e.g., "123")</li>
	 *   <li>Multiple partition IDs separated by commas (e.g., "123,456")</li>
	 *   <li>The special value "DEFAULT" to indicate the default partition</li>
	 *   <li>The special value "_ALL" to indicate all partitions</li>
	 * </ul>
	 * </p>
	 *
	 * @param theSourceName               The name of the source of the value, used for error messages
	 * @param thePartitionHeaderValue     The value of the X-Request-Partition-IDs header, may be null
	 * @param theIncludeOnlyTheFirst      If true, only the first partition ID in the header will be included in the result
	 * @param theDefaultPartitionSettings Settings that provide the default partition ID
	 * @return A {@link RequestPartitionId} object representing the partition(s) specified in the header, or null if the header is null
	 * @throws InvalidRequestException If the header value is invalid
	 */
	@Nullable
	private static RequestPartitionId fromHeader(
			@Nonnull String theSourceName,
			@Nullable String thePartitionHeaderValue,
			boolean theIncludeOnlyTheFirst,
			@Nullable IDefaultPartitionSettings theDefaultPartitionSettings) {
		if (thePartitionHeaderValue == null) {
			return null;
		}
		String[] partitionIdStrings = thePartitionHeaderValue.split(",");
		List<Integer> partitionIds = new ArrayList<>();

		for (String partitionIdString : partitionIdStrings) {

			String trimmedPartitionId = partitionIdString.trim();

			if (trimmedPartitionId.equals(ALL_PARTITIONS_TENANT_NAME)) {
				return RequestPartitionId.allPartitions();
			}

			@Nullable
			Integer partitionId = getPartitionId(theSourceName, theDefaultPartitionSettings, trimmedPartitionId);

			// return early if we only need the first partition ID
			if (theIncludeOnlyTheFirst) {
				return RequestPartitionId.fromPartitionId(partitionId);
			}
			partitionIds.add(partitionId);
		}

		if (partitionIds.isEmpty()) {
			// this case happens only when the header contains nothing but commas
			// since we already checked for blank header before calling this function
			String msg = String.format("No partition IDs provided in %s", theSourceName);
			throw new InvalidRequestException(Msg.code(2645) + msg);
		}

		return RequestPartitionId.fromPartitionIds(partitionIds);
	}

	/**
	 * Converts a partition ID string to an Integer.
	 * <p>
	 * If the string is "DEFAULT", returns the default partition ID from the settings.
	 * Otherwise, attempts to parse the string as an integer.
	 * </p>
	 *
	 * @param theSourceName               The name of the source of the value, used for error messages
	 * @param theDefaultPartitionSettings Settings that provide the default partition ID
	 * @param trimmedPartitionId          The partition ID string to convert, already trimmed of whitespace
	 * @return The partition ID as an Integer, or null if the default partition ID is null
	 * @throws InvalidRequestException If the partition ID string is not "DEFAULT" and cannot be parsed as an integer
	 */
	@Nullable
	private static Integer getPartitionId(
			@Nonnull String theSourceName,
			@Nullable IDefaultPartitionSettings theDefaultPartitionSettings,
			String trimmedPartitionId) {
		Integer partitionId;

		if (trimmedPartitionId.equals(DEFAULT_PARTITION_NAME)) {
			if (theDefaultPartitionSettings == null) {
				throw new InvalidRequestException(Msg.code(2722)
						+ "Can only use DEFAULT partitionId in contexts where the default partition ID is defined.");
			} else {
				partitionId = theDefaultPartitionSettings.getDefaultPartitionId();
			}
		} else {
			try {
				partitionId = Integer.parseInt(trimmedPartitionId);
			} catch (NumberFormatException e) {
				String msg =
						String.format("Invalid partition ID: '%s' provided in %s", trimmedPartitionId, theSourceName);
				throw new InvalidRequestException(Msg.code(2643) + msg);
			}
		}
		return partitionId;
	}

	/**
	 * Sets the partition ID on a message payload from the X-Request-Partition-IDs header if it's not already set.
	 *
	 * @param <T>                         The type of the payload, which must have methods for getting and setting a partition ID
	 * @param theMessage                  The message containing the payload and headers
	 * @param theDefaultPartitionSettings Settings that provide the default partition ID
	 */
	public static <T> void setRequestPartitionIdFromHeaderIfNotAlreadySet(
			@Nonnull IMessage<T> theMessage, @Nullable IDefaultPartitionSettings theDefaultPartitionSettings) {
		if (theMessage.getPayload() instanceof BaseResourceMessage baseResourceMessage) {
			if (baseResourceMessage.getPartitionId() != null) {
				// TODO KHS suggestion from MB: if partitions are also set in the header, log a warning if they don't
				// match.
				return;
			}

			Optional<Object> oHeader = theMessage.getHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS);
			if (oHeader.isEmpty()) {
				return;
			}

			RequestPartitionId headerPartitionId =
					RequestPartitionHeaderUtil.fromHeader((String) oHeader.get(), theDefaultPartitionSettings);
			baseResourceMessage.setPartitionId(headerPartitionId);
		}
	}
}
