package ca.uhn.hapi.fhir.docs.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionRequest;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionResponse;

import java.util.Set;

@Interceptor
public class FullTextSelectiveIndexingInterceptor {

	/**
	 * These types will be indexed for structured data content (supporting
	 * the <code>_content</code> Search Parameter)
	 */
	private static final Set<String> INDEX_CONTENT_TYPES = Set.of(
		"Patient", "Observation"
	);

	/**
	 * These types will be indexed for narrative (supporting
	 * the <code>_text</code> Search Parameter)
	 */
	private static final Set<String> INDEX_TEXT_TYPES = Set.of(
		"DiagnosticReport"
	);

	/**
	 * This method is called twice for each resource being stored, to cover
	 * the two FullText search parameters.
	 */
	@Hook(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT)
	public FullTextExtractionResponse indexPayload(FullTextExtractionRequest theRequest) {

		if (theRequest.getIndexType() == FullTextExtractionRequest.IndexTypeEnum.CONTENT) {
			if (!INDEX_CONTENT_TYPES.contains(theRequest.getResourceType())) {
				return FullTextExtractionResponse.doNotIndex();
			}
		}

		if (theRequest.getIndexType() == FullTextExtractionRequest.IndexTypeEnum.TEXT) {
			if (!INDEX_TEXT_TYPES.contains(theRequest.getResourceType())) {
				return FullTextExtractionResponse.doNotIndex();
			}
		}

		return FullTextExtractionResponse.indexNormally();
	}

}

