package ca.uhn.fhir.jpa.searchparam.fulltext;

import javax.annotation.Nullable;

public class FullTextExtractionResponse {

	private final boolean myDoNotIndex;
	private final boolean myPayloadPresent;
	private final String myPayload;

	/**
	 * Constructor
	 */
	private FullTextExtractionResponse(boolean theDoNotIndex, boolean thePayloadPresent, String thePayload) {
		myDoNotIndex = theDoNotIndex;
		myPayloadPresent = thePayloadPresent;
		myPayload = thePayload;
	}

	/**
	 * Creates a response signalling that the given resource should not be
	 * indexed.
	 * <p>
	 * <b>Note the difference between invoking {@link #doNotIndex()} and invoking
	 * {@link #indexPayload(String)} with a <code>null</code> payload!</b>
	 * Invoking {@link #doNotIndex()} signals to the fulltext engine that the fulltext
	 * indexer should skip processing this resource, whereas invoking
	 * {@link #indexPayload(String)} with a <code>null</code> payload signals to
	 * the fulltext engine that any existing row should be cleared.
	 * This distinction is important in the case of resource deletions, since invoking
	 * {@link #doNotIndex()} when deleting a previously fulltext indexed row will
	 * leave the previous fulltext index in place, which can waste index space and
	 * make queries inefficient.
	 * FIXME copy thios
	 * </p>
	 */
	public static FullTextExtractionResponse doNotIndex() {
		return new FullTextExtractionResponse(true, false, null);
	}

	/**
	 * Creates a response signalling that the given resource should be indexed
	 * with the given payload.
	 *
	 * @param thePayload The fulltext payload string. May be <code>null</code> if no payload should be specified for the given resource.
	 */
	public static FullTextExtractionResponse indexPayload(@Nullable String thePayload) {
		return new FullTextExtractionResponse(false, true, thePayload);
	}

}
