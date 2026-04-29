package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;

/**
 * Routing strategy for token-index reads and writes between the legacy
 * {@code HFJ_SPIDX_TOKEN} table and the new compressed token index tables
 * ({@code HFJ_SPIDX2_TOKEN_COMMON}, {@code HFJ_SPIDX2_TOKEN_COMMON_RES},
 * {@code HFJ_SPIDX2_TOKEN_IDENTIFIER}).
 *
 * @see JpaStorageSettings#setTokenIndexStrategy(TokenIndexStrategyEnum)
 */
public enum TokenIndexStrategyEnum {
	/**
	 * Write to and query the legacy {@code HFJ_SPIDX_TOKEN} table only.
	 */
	WRITE_OLD_QUERY_OLD,

	/**
	 * Write to both the legacy and the new {@code HFJ_SPIDX2_TOKEN_*} tables, and query the legacy
	 * {@code HFJ_SPIDX_TOKEN} table.
	 */
	WRITE_BOTH_QUERY_OLD,

	/**
	 * Write to both the legacy and the new {@code HFJ_SPIDX2_TOKEN_*} tables, and query the new
	 * {@code HFJ_SPIDX2_TOKEN_*} tables.
	 */
	WRITE_BOTH_QUERY_NEW,

	/**
	 * Write to and query the new {@code HFJ_SPIDX2_TOKEN_*} tables only.
	 */
	WRITE_NEW_QUERY_NEW;

	/**
	 * Returns {@code true} if token index rows should be written to the legacy
	 * {@code HFJ_SPIDX_TOKEN} table under this strategy.
	 * Only {@link #WRITE_NEW_QUERY_NEW} omits the legacy table.
	 */
	public boolean writeToLegacyTokenTable() {
		return switch (this) {
			case WRITE_OLD_QUERY_OLD, WRITE_BOTH_QUERY_OLD, WRITE_BOTH_QUERY_NEW -> true;
			default -> false;
		};
	}

	/**
	 * Returns {@code true} if token index rows should be written to the compressed token
	 * tables ({@code HFJ_SPIDX2_TOKEN_COMMON}, {@code HFJ_SPIDX2_TOKEN_COMMON_RES},
	 * {@code HFJ_SPIDX2_TOKEN_IDENTIFIER}) under this strategy.
	 * Only {@link #WRITE_OLD_QUERY_OLD} skips the compressed tables.
	 */
	public boolean writeToCompressedTokenTables() {
		return switch (this) {
			case WRITE_BOTH_QUERY_OLD, WRITE_BOTH_QUERY_NEW, WRITE_NEW_QUERY_NEW -> true;
			default -> false;
		};
	}

	/**
	 * Returns {@code true} if token search predicates should target the legacy
	 * {@code HFJ_SPIDX_TOKEN} table under this strategy.
	 * The {@code WRITE_*_QUERY_OLD} strategies read from legacy.
	 */
	public boolean readFromLegacyTokenTable() {
		return switch (this) {
			case WRITE_OLD_QUERY_OLD, WRITE_BOTH_QUERY_OLD -> true;
			default -> false;
		};
	}

	/**
	 * Returns {@code true} if token search predicates should target the compressed token
	 * tables ({@code HFJ_SPIDX2_TOKEN_COMMON}, {@code HFJ_SPIDX2_TOKEN_COMMON_RES},
	 * {@code HFJ_SPIDX2_TOKEN_IDENTIFIER}) under this strategy.
	 * The {@code WRITE_*_QUERY_NEW} strategies read from the new tables.
	 */
	public boolean readFromCompressedTokenTables() {
		return switch (this) {
			case WRITE_BOTH_QUERY_NEW, WRITE_NEW_QUERY_NEW -> true;
			default -> false;
		};
	}
}
