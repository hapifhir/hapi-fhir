package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.TokenIndexStrategy;
import ca.uhn.fhir.jpa.search.builder.predicate.CompressedTokenPredicateBuilder;

import static ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommonRes.HFJ_SPIDX2_TOKEN_COMMON_RES;
import static ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenIdentifier.HFJ_SPIDX2_TOKEN_IDENTIFIER;

/**
 * Determines which compressed token index table to query during search.
 *
 * <p>When {@link TokenIndexStrategy} enables compressed token indexing,
 * this mode controls which physical table structure is used:
 * <ul>
 *   <li>{@link #COMMON} — two-table design for general token parameters</li>
 *   <li>{@link #IDENTIFIER} — single-table design optimized for identifier lookups</li>
 * </ul>
 *
 * @see TokenIndexStrategy
 * @see CompressedTokenPredicateBuilder
 */
public enum TokenIndexMode {

	/**
	 * This mode is used for general token search parameters
	 * (e.g., {@code code}, {@code status}, {@code category}).
	 *
	 * <p>Uses a two-table structure for storage efficiency:
	 * <ul>
	 *   <li>{@code HFJ_SPIDX2_TOKEN_COMMON_RES} — links resources to token values via {@code RES_ID}</li>
	 *   <li>{@code HFJ_SPIDX2_TOKEN_COMMON} — stores distinct system|value hashes, shared across resources</li>
	 * </ul>
	 */
	COMMON(HFJ_SPIDX2_TOKEN_COMMON_RES),

	/**
	 * This mode is used for identifier search parameters, which typically have
	 * high cardinality (unique or near-unique values per resource).
	 *
	 * <p>Queries the {@code HFJ_SPIDX2_TOKEN_IDENTIFIER} table.
	 */
	IDENTIFIER(HFJ_SPIDX2_TOKEN_IDENTIFIER);

	private final String myTableName;

	TokenIndexMode(String theTableName) {
		myTableName = theTableName;
	}

	public String getTableName() {
		return myTableName;
	}

	/**
	 * Resolves which compressed token table to use for a given search parameter.
	 *
	 * <p>Currently checks {@link JpaStorageSettings#getIdentifierTokenSearchParams()}.
	 * A future enhancement could add support for an extension on SearchParameter
	 * resources to allow user-defined SPs to use the identifier table instead of
	 * the common table.
	 */
	public static TokenIndexMode resolve(String theParamName, JpaStorageSettings theStorageSettings) {
		if (theStorageSettings.getIdentifierTokenSearchParams().contains(theParamName)) {
			return IDENTIFIER;
		}
		return COMMON;
	}
}
