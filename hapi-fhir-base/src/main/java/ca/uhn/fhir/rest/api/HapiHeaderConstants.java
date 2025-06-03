package ca.uhn.fhir.rest.api;

public class HapiHeaderConstants {
	/**
	 * @deprecated Use {@link #REQUEST_ID} instead.
	 */
	@Deprecated
	public static final String DEPRECATED_REQUEST_ID = "X-Request-ID";
	public static final String REQUEST_ID = "HAPI-Request-Id";

	/**
	 * @deprecated Use {@link #REQUEST_SOURCE} instead.
	 */
	@Deprecated
	public static final String DEPRECATED_REQUEST_SOURCE = "X-Request-Source";
	public static final String REQUEST_SOURCE = "HAPI-Request-Source";

	/**
	 * @deprecated Use {@link #REWRITE_HISTORY} instead.
	 */
	@Deprecated
	public static final String DEPRECATED_REWRITE_HISTORY = "X-Rewrite-History";
	public static final String REWRITE_HISTORY = "HAPI-Rewrite-History";
}
