package ca.uhn.fhir.jpa.searchparam.fulltext;

import javax.annotation.Nullable;

/**
 * This is a response object containing the FullText index data which should be stored during
 * indexing, or an instruction to skip FullText indexing for the given resource.
 *
 * @see ca.uhn.fhir.interceptor.api.Pointcut#JPA_INDEX_EXTRACT_FULLTEXT for a description of how this should be used.
 * @since 8.4.0
 */
public class FullTextExtractionResponse {

    private final boolean myDoNotIndex;
    private final boolean myPayloadPresent;
    private final String myPayload;

    /**
     * Private Constructor - Use the factory methods in order to instantiate this class.
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
     * <b>Note the difference between invoking {@literal doNotIndex()} and invoking
     * {@link #indexPayload(String)} with a <code>null</code> payload!</b>
     * Invoking {@literal doNotIndex()} signals to the fulltext engine that the fulltext
     * indexer should skip processing this resource, whereas invoking
     * {@link #indexPayload(String)} with a <code>null</code> payload signals to
     * the fulltext engine that any existing row should be cleared.
     * This distinction is important in the case of resource deletions, since invoking
     * {@literal #doNotIndex()} when deleting a previously fulltext indexed row will
     * leave the previous fulltext index in place, which can waste index space and
     * make queries inefficient.
     * </p>
     */
    public static FullTextExtractionResponse doNotIndex() {
        return new FullTextExtractionResponse(true, false, null);
    }

    /**
     * Creates a response signalling that the given resource should be indexed
     * with the given payload. Calling this method with a <code>null</code> payload value
     * is subtly different from calling {@link #doNotIndex()}. This method will clear any
     * existing indexing and write an empty index, where {@link #doNotIndex()} will not
     * touch any existing index.
     *
     * @param thePayload The fulltext payload string. May be <code>null</code> if no payload should be specified for the given resource.
     */
    public static FullTextExtractionResponse indexPayload(@Nullable String thePayload) {
        return new FullTextExtractionResponse(false, true, thePayload);
    }

    /**
     * Creates a response signalling that the standard indexing should be used.
     */
    public static FullTextExtractionResponse indexNormally() {
        return new FullTextExtractionResponse(false, false, null);
    }

    public boolean isDoNotIndex() {
        return myDoNotIndex;
    }

    public String getPayload() {
        return myPayload;
    }

}
