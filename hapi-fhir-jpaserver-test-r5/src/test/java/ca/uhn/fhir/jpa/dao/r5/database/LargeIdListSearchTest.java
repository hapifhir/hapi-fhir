package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
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
 * Shared search test cases: above a configured threshold the <code>_id</code> and reference
 * predicates bind their ID list as a single JSON array string which the database unpacks with its
 * own JSON function, instead of emitting one bind variable per ID. This prevents exceeding DB parameter
 * limits for large ID lists.
 * <p>
 * Implemented by both {@link BaseDatabaseVerificationIT} and {@link BaseDatabasePartitionModeIT} so every
 * case runs against every supported database vendor, in and out of database partition mode.
 * </p>
 */
// Created by claude-opus-5
interface LargeIdListSearchTest extends ITestDataBuilder {

	/**
	 * Roughly the number of IDs whose JSON array exceeds Oracle's default 4,000 byte VARCHAR2 bind limit.
	 */
	int LARGE_PAYLOAD_PATIENT_COUNT = 800;

	record Context(
		JpaStorageSettings storageSettings,
		RestfulServerExtension server,
		CircularQueueCaptureQueriesListener captureQueriesListener,
		DriverTypeEnum driverType,
		boolean databasePartitionMode
	) {}

	Context getLargeIdListSearchTestContext();

	/**
	 * An _id list above the threshold returns the same resources as today, and
	 * the generated SQL unpacks the IDs with the engine's JSON function.
	 * In database partition mode the partition predicate must still be there beside it.
	 */
	@Test
	default void testIdSearchOverThreshold_unpacksJsonArray() {
		Context ctx = getLargeIdListSearchTestContext();
		withLargeIdListJsonThreshold(ctx, 3, () -> {
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
		});
	}

	/**
	 * An search with references list (?subject=) above the threshold returns the same resources as today, and
	 * the generated SQL unpacks the IDs with the engine's JSON function.
	 * In database partition mode the partition predicate must still be there beside it.
	 */
	@Test
	default void testReferenceSearchOverThreshold_unpacksJsonArray() {
		Context ctx = getLargeIdListSearchTestContext();
		withLargeIdListJsonThreshold(ctx, 3, () -> {
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
		});
	}

	/**
	 * IT-3: at or under the threshold nothing changes on any engine.
	 */
	@Test
	default void testIdSearchUnderThreshold_keepsInList() {
		Context ctx = getLargeIdListSearchTestContext();
		withLargeIdListJsonThreshold(ctx, 10, () -> {
			List<String> patientIds = createPatients(5);

			ctx.captureQueriesListener().clear();
			Bundle results = search(ctx, "Patient?_id=" + String.join(",", patientIds));

			assertThat(SearchTestUtil.toUnqualifiedVersionlessIdValues(results))
				.containsExactlyInAnyOrderElementsOf(patientIds);

			String sql = findSelectQueryContaining(ctx, "RES_ID");
			assertThat(sql).as(sql).doesNotContain("jsonb_array_elements_text");
			assertThat(sql).as(sql).doesNotContain("JSON_TABLE");
			assertThat(sql).as(sql).doesNotContain("OPENJSON");
		});
	}

	/**
	 * Exercises Oracle's 4,000-byte VARCHAR2 bind limit - well under the JSON array of a payload this
	 * size, so the array has to be bound as a CLOB or the statement fails with ORA-01461 - and SQL
	 * Server's own switch from <code>nvarchar(4000)</code>/<code>varchar(8000)</code> to <code>(max)</code>
	 * at the same boundary. Runs on every JSON engine; this is the only place either boundary is actually
	 * exercised rather than asserted.
	 * <p>
	 * Sent as a POST because 800 IDs do not fit in a request line, and the maximum page size and offset are
	 * raised, for the same reason as in production: search narrowing puts the IDs into the parameter map
	 * server side and never onto a URL at all, so all matches must come back in one page to compare.
	 * </p>
	 */
	@Test
	default void testIdSearchOverVarcharBindLimit_bindsLargePayload() {
		Context ctx = getLargeIdListSearchTestContext();
		assumeTrue(jsonFunctionForDriver(ctx.driverType()) != null, "JSON engines only");

		Integer previousMaximumPageSize = ctx.server().getRestfulServer().getMaximumPageSize();
		ctx.server().getRestfulServer().setMaximumPageSize(LARGE_PAYLOAD_PATIENT_COUNT);
		try {
			withLargeIdListJsonThreshold(ctx, 3, () -> {
				List<String> patientIds = createPatients(LARGE_PAYLOAD_PATIENT_COUNT);

				ctx.captureQueriesListener().clear();
				Bundle results = ctx.server().getFhirClient()
					.search()
					.forResource("Patient")
					.where(new TokenClientParam("_id").exactly().codes(patientIds))
					.count(LARGE_PAYLOAD_PATIENT_COUNT)
					.offset(0)
					.usingStyle(SearchStyleEnum.POST)
					.returnBundle(Bundle.class)
					.execute();

				assertThat(SearchTestUtil.toUnqualifiedVersionlessIdValues(results))
					.containsExactlyInAnyOrderElementsOf(patientIds);
				assertIdListUnpacking(ctx, findSelectQueryContaining(ctx, "RES_ID"));
			});
		} finally {
			ctx.server().getRestfulServer().setMaximumPageSize(previousMaximumPageSize);
		}
	}

	/**
	 * Runs the given test with {@link JpaStorageSettings#setLargeIdListJsonThreshold(int)} set to the
	 * given value, and restores the previous value afterwards even if the test throws.
	 */
	private void withLargeIdListJsonThreshold(Context theContext, int theThreshold, Runnable theTest) {
		int previousThreshold = theContext.storageSettings().getLargeIdListJsonThreshold();
		theContext.storageSettings().setLargeIdListJsonThreshold(theThreshold);
		try {
			theTest.run();
		} finally {
			theContext.storageSettings().setLargeIdListJsonThreshold(previousThreshold);
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
