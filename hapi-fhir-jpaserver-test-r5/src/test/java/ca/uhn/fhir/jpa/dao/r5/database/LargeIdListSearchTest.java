package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.SearchTestUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Shared search test cases for GL-9268: above a configured threshold the <code>_id</code> and
 * reference predicates bind their ID list as a single JSON array string which the database unpacks
 * with its own JSON function, instead of emitting one bind variable per ID.
 * <p>
 * Implemented by both {@link BaseDatabaseVerificationIT} and {@link BaseDatabasePartitionModeIT} so every
 * case runs against every supported database vendor, in and out of database partition mode.
 * </p>
 */
// Created by claude-opus-5
interface LargeIdListSearchTest extends ITestDataBuilder {

	int THRESHOLD_BELOW_TEST_LIST_SIZE = 3;
	int THRESHOLD_ABOVE_TEST_LIST_SIZE = 10;

	/**
	 * Roughly the number of IDs whose JSON array exceeds Oracle's default 4,000 byte VARCHAR2 bind limit.
	 */
	int ORACLE_CLOB_PATIENT_COUNT = 800;

	record Context(
		JpaStorageSettings storageSettings,
		RestfulServerExtension server,
		CircularQueueCaptureQueriesListener captureQueriesListener,
		DriverTypeEnum driverType,
		boolean databasePartitionMode
	) {}

	Context getLargeIdListSearchTestContext();

	/**
	 * IT-1 / IT-5: an <code>_id</code> list above the threshold returns the same resources as today, and
	 * the generated SQL unpacks the IDs with the engine's JSON function. In database partition mode the
	 * partition predicate must still be there beside it.
	 */
	@Test
	default void testIdSearchOverThreshold_unpacksJsonArray() {
		Context ctx = getLargeIdListSearchTestContext();
		int previousThreshold = ctx.storageSettings().getLargeIdListJsonThreshold();
		ctx.storageSettings().setLargeIdListJsonThreshold(THRESHOLD_BELOW_TEST_LIST_SIZE);
		try {
			List<String> patientIds = createPatients(5);

			ctx.captureQueriesListener().clear();
			Bundle results = search(ctx, "Patient?_id=" + String.join(",", patientIds));

			assertThat(SearchTestUtil.toUnqualifiedVersionlessIdValues(results))
				.containsExactlyInAnyOrderElementsOf(patientIds);

			String sql = findSelectQueryContaining(ctx, "RES_ID");
			assertIdListUnpacking(ctx, sql);
			if (ctx.databasePartitionMode()) {
				assertThat(sql).as(sql).contains("PARTITION_ID");
			}
		} finally {
			ctx.storageSettings().setLargeIdListJsonThreshold(previousThreshold);
		}
	}

	/**
	 * IT-2: the reference site (<code>subject=</code>) gets the same treatment, and a search that is over
	 * the threshold on both <code>_id</code> and <code>subject</code> puts two JSON placeholders in one statement.
	 */
	@Test
	default void testReferenceSearchOverThreshold_unpacksJsonArray() {
		Context ctx = getLargeIdListSearchTestContext();
		int previousThreshold = ctx.storageSettings().getLargeIdListJsonThreshold();
		ctx.storageSettings().setLargeIdListJsonThreshold(THRESHOLD_BELOW_TEST_LIST_SIZE);
		try {
			List<String> patientIds = createPatients(5);
			List<String> observationIds = patientIds.stream()
				.map(t -> createObservation(withSubject(t)).toUnqualifiedVersionless().getValue())
				.collect(Collectors.toList());

			ctx.captureQueriesListener().clear();
			Bundle results = search(ctx, "Observation?subject=" + String.join(",", patientIds));

			assertThat(SearchTestUtil.toUnqualifiedVersionlessIdValues(results))
				.containsExactlyInAnyOrderElementsOf(observationIds);

			assertIdListUnpacking(ctx, findSelectQueryContaining(ctx, "TARGET_RESOURCE_ID"));

			// Both sites over the threshold in one statement
			ctx.captureQueriesListener().clear();
			Bundle combinedResults = search(ctx, "Observation?_id=" + String.join(",", observationIds)
				+ "&subject=" + String.join(",", patientIds));

			assertThat(SearchTestUtil.toUnqualifiedVersionlessIdValues(combinedResults))
				.containsExactlyInAnyOrderElementsOf(observationIds);

			String jsonFunction = jsonFunctionForDriver(ctx.driverType());
			if (jsonFunction != null) {
				String combinedSql = findSelectQueryContaining(ctx, "TARGET_RESOURCE_ID");
				assertThat(StringUtils.countMatches(combinedSql, jsonFunction))
					.as(combinedSql)
					.isEqualTo(2);
			}
		} finally {
			ctx.storageSettings().setLargeIdListJsonThreshold(previousThreshold);
		}
	}

	/**
	 * IT-3: at or under the threshold nothing changes on any engine.
	 */
	@Test
	default void testIdSearchUnderThreshold_keepsInList() {
		Context ctx = getLargeIdListSearchTestContext();
		int previousThreshold = ctx.storageSettings().getLargeIdListJsonThreshold();
		ctx.storageSettings().setLargeIdListJsonThreshold(THRESHOLD_ABOVE_TEST_LIST_SIZE);
		try {
			List<String> patientIds = createPatients(5);

			ctx.captureQueriesListener().clear();
			Bundle results = search(ctx, "Patient?_id=" + String.join(",", patientIds));

			assertThat(SearchTestUtil.toUnqualifiedVersionlessIdValues(results))
				.containsExactlyInAnyOrderElementsOf(patientIds);

			String sql = findSelectQueryContaining(ctx, "RES_ID");
			assertThat(sql).as(sql).doesNotContain("jsonb_array_elements_text");
			assertThat(sql).as(sql).doesNotContain("JSON_TABLE");
			assertThat(sql).as(sql).doesNotContain("OPENJSON");
		} finally {
			ctx.storageSettings().setLargeIdListJsonThreshold(previousThreshold);
		}
	}

	/**
	 * IT-4: Oracle only. A JSON array of 800 IDs is well over Oracle's default 4,000 byte VARCHAR2 SQL bind
	 * limit, so the array has to be bound as a CLOB or the statement fails with ORA-01461. This is the only
	 * place that decision is actually executed rather than asserted.
	 */
	@Test
	default void testOracleIdSearchOverVarcharBindLimit_bindsClob() {
		Context ctx = getLargeIdListSearchTestContext();
		assumeTrue(ctx.driverType() == DriverTypeEnum.ORACLE_12C, "Oracle only");

		int previousThreshold = ctx.storageSettings().getLargeIdListJsonThreshold();
		ctx.storageSettings().setLargeIdListJsonThreshold(THRESHOLD_BELOW_TEST_LIST_SIZE);
		try {
			List<String> patientIds = createPatients(ORACLE_CLOB_PATIENT_COUNT);

			ctx.captureQueriesListener().clear();
			Bundle results = ctx.server().getFhirClient()
				.search()
				.byUrl("Patient?_id=" + String.join(",", patientIds) + "&_count=" + ORACLE_CLOB_PATIENT_COUNT)
				.returnBundle(Bundle.class)
				.execute();

			assertThat(SearchTestUtil.toUnqualifiedVersionlessIdValues(results))
				.containsExactlyInAnyOrderElementsOf(patientIds);
			assertThat(findSelectQueryContaining(ctx, "RES_ID")).contains("JSON_TABLE");
		} finally {
			ctx.storageSettings().setLargeIdListJsonThreshold(previousThreshold);
		}
	}

	private List<String> createPatients(int theCount) {
		List<String> patientIds = new ArrayList<>();
		for (int i = 0; i < theCount; i++) {
			IIdType id = createPatient(withActiveTrue());
			patientIds.add(id.toUnqualifiedVersionless().getValue());
		}
		return patientIds;
	}

	private static Bundle search(Context theContext, String theSearchUrl) {
		return theContext.server().getFhirClient()
			.search()
			.byUrl(theSearchUrl)
			.returnBundle(Bundle.class)
			.execute();
	}

	/**
	 * Asserts that the ID list in the given SQL is unpacked by the engine's JSON function, or - on engines
	 * which keep the IN list by design - that it is still an IN list.
	 */
	private static void assertIdListUnpacking(Context theContext, String theSql) {
		String jsonFunction = jsonFunctionForDriver(theContext.driverType());
		if (jsonFunction == null) {
			assertThat(theSql).as(theSql).contains("IN (");
		} else {
			assertThat(theSql).as(theSql).contains(jsonFunction);
		}
	}

	private static String findSelectQueryContaining(Context theContext, String theColumnName) {
		List<SqlQuery> selectQueries = theContext.captureQueriesListener().getSelectQueries();
		return selectQueries.stream()
			.map(t -> t.getSql(false, false))
			.filter(t -> t.contains(theColumnName))
			.findFirst()
			.orElseThrow(() -> new AssertionError(
				"No captured select query mentions " + theColumnName + ": " + selectQueries));
	}

	@Nullable
	private static String jsonFunctionForDriver(DriverTypeEnum theDriverType) {
		return switch (theDriverType) {
			case POSTGRES_9_4 -> "jsonb_array_elements_text";
			case ORACLE_12C -> "JSON_TABLE";
			case MSSQL_2012 -> "OPENJSON";
			default -> null;
		};
	}
}
