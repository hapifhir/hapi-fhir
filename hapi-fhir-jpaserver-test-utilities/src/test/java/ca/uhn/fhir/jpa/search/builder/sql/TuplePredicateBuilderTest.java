package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirMariaDBDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import com.healthmarketscience.sqlbuilder.Condition;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SearchQueryBuilderTest.MyConfig.class})
class TuplePredicateBuilderTest {

	private static final String PLACEHOLDER = "'[^']*'";

	@Autowired
	private SqlObjectFactory mySqlBuilderFactory;

	private FhirContext myFhirContext;
	private StorageSettings myStorageSettings;
	private PartitionSettings myPartitionSettings;

	@BeforeEach
	void before() {
		myFhirContext = FhirContext.forR4Cached();
		myStorageSettings = new StorageSettings();
		myPartitionSettings = new PartitionSettings();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDatabasePartitionMode(true);
	}

	static Stream<Dialect> allDialects() {
		return Stream.of(
			new PostgreSQLDialect(),
			new HapiFhirSQLServerDialect(),
			new HapiFhirOracleDialect(),
			new HapiFhirMariaDBDialect()
		);
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toNotInSubquery_singleColumn_producesNotIn(Dialect theDialect) {
		SearchQueryBuilder outerBuilder = createNonPartitionBuilder(theDialect);
		BaseJoiningPredicateBuilder outerRoot = outerBuilder.getOrCreateFirstPredicateBuilder();
		PartitionableJoinColumns singleColumn = PartitionableJoinColumns.newNonPartitioned(outerRoot.getResourceIdColumn());

		SearchQueryBuilder childBuilder = outerBuilder.newChildSqlBuilder(false);
		BaseJoiningPredicateBuilder childRoot = childBuilder.getOrCreateFirstPredicateBuilder();

		Condition result = outerBuilder.getTuplePredicateBuilder().toNotInSubquery(childBuilder, childRoot, singleColumn);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"((t0.RES_ID) NOT IN ("
				+ "SELECT t0.RES_ID FROM HFJ_RESOURCE t0"
				+ " WHERE ((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL))"
				+ ") )");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toNotInSubquery_multiColumn_producesNotExistsWithCorrelation(Dialect theDialect) {
		SearchQueryBuilder outerBuilder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder outerRoot = outerBuilder.getOrCreateFirstPredicateBuilder();
		PartitionableJoinColumns multiColumn = PartitionableJoinColumns.from(outerRoot.getJoinColumns());

		SearchQueryBuilder childBuilder = outerBuilder.newChildSqlBuilder(true);
		BaseJoiningPredicateBuilder childRoot = childBuilder.getOrCreateFirstPredicateBuilder();

		Condition result = outerBuilder.getTuplePredicateBuilder().toNotInSubquery(childBuilder, childRoot, multiColumn);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(NOT (EXISTS ("
				+ "SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_RESOURCE s0"
				+ " WHERE (((s0.RES_TYPE = ?) AND (s0.RES_DELETED_AT IS NULL))"
				+ " AND (s0.PARTITION_ID = t0.PARTITION_ID)"
				+ " AND (s0.RES_ID = t0.RES_ID))"
				+ ")))");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toInSubquery_singleChildBuilder_producesExistsWithCorrelation(Dialect theDialect) {
		SearchQueryBuilder outerBuilder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder outerRoot = outerBuilder.getOrCreateFirstPredicateBuilder();
		PartitionableJoinColumns multiColumn = PartitionableJoinColumns.from(outerRoot.getJoinColumns());

		SearchQueryBuilder childBuilder = outerBuilder.newChildSqlBuilder(true);

		Condition result = outerBuilder.getTuplePredicateBuilder().toInSubquery(List.of(childBuilder), multiColumn);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(EXISTS ("
				+ "SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_RESOURCE s0"
				+ " WHERE (((s0.RES_TYPE = ?) AND (s0.RES_DELETED_AT IS NULL))"
				+ " AND (s0.PARTITION_ID = t0.PARTITION_ID)"
				+ " AND (s0.RES_ID = t0.RES_ID))"
				+ "))");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toInSubquery_multipleChildBuilders_producesOrExistsWithCorrelation(Dialect theDialect) {
		SearchQueryBuilder outerBuilder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder outerRoot = outerBuilder.getOrCreateFirstPredicateBuilder();
		PartitionableJoinColumns multiColumn = PartitionableJoinColumns.from(outerRoot.getJoinColumns());

		SearchQueryBuilder childBuilder1 = outerBuilder.newChildSqlBuilder(true);
		SearchQueryBuilder childBuilder2 = outerBuilder.newChildSqlBuilder(true);

		Condition result = outerBuilder.getTuplePredicateBuilder().toInSubquery(List.of(childBuilder1, childBuilder2), multiColumn);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"((EXISTS ("
				+ "SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_RESOURCE s0"
				+ " WHERE (((s0.RES_TYPE = ?) AND (s0.RES_DELETED_AT IS NULL))"
				+ " AND (s0.PARTITION_ID = t0.PARTITION_ID)"
				+ " AND (s0.RES_ID = t0.RES_ID))"
				+ ")) OR (EXISTS ("
				+ "SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_RESOURCE s0"
				+ " WHERE (((s0.RES_TYPE = ?) AND (s0.RES_DELETED_AT IS NULL))"
				+ " AND (s0.PARTITION_ID = t0.PARTITION_ID)"
				+ " AND (s0.RES_ID = t0.RES_ID))"
				+ ")))");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toInPredicate_nonPartitionMode_producesSimpleIn(Dialect theDialect) {
		SearchQueryBuilder builder = createNonPartitionBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newNonPartitioned(root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L), JpaPid.fromId(200L)), false);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(t0.RES_ID IN (?,?) )");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toInPredicate_nonPartitionMode_negated_singlePid_producesNotEqual(Dialect theDialect) {
		SearchQueryBuilder builder = createNonPartitionBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newNonPartitioned(root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L)), true);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(t0.RES_ID <> ?)");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toInPredicate_nonPartitionMode_negated_multiplePids_producesNotIn(Dialect theDialect) {
		SearchQueryBuilder builder = createNonPartitionBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newNonPartitioned(root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L), JpaPid.fromId(200L)), true);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(t0.RES_ID NOT IN (?,?) )");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toExpandedTupleInPredicate_singlePid(Dialect theDialect) {
		SearchQueryBuilder builder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newPartitioned(root.getPartitionIdColumn(), root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L, 1)), false);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?))");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toExpandedTupleInPredicate_multiplePids_samePartition(Dialect theDialect) {
		SearchQueryBuilder builder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newPartitioned(root.getPartitionIdColumn(), root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L, 1), JpaPid.fromId(200L, 1)), false);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?)) OR ((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?)))");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toExpandedTupleInPredicate_negated(Dialect theDialect) {
		SearchQueryBuilder builder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newPartitioned(root.getPartitionIdColumn(), root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L, 1)), true);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(NOT ((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?)))");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toExpandedTupleInPredicate_negated_multiplePids(Dialect theDialect) {
		SearchQueryBuilder builder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newPartitioned(root.getPartitionIdColumn(), root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L, 1), JpaPid.fromId(200L, 2)), true);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(NOT (((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?)) OR ((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?))))");
	}

	@ParameterizedTest
	@MethodSource("allDialects")
	void toExpandedTupleInPredicate_multiplePids_differentPartitions(Dialect theDialect) {
		SearchQueryBuilder builder = createBuilder(theDialect);
		BaseJoiningPredicateBuilder root = builder.getOrCreateFirstPredicateBuilder();

		Condition result = builder.getTuplePredicateBuilder().toInPredicate(
			PartitionableJoinColumns.newPartitioned(root.getPartitionIdColumn(), root.getResourceIdColumn()),
			List.of(JpaPid.fromId(100L, 1), JpaPid.fromId(200L, 2)), false);

		assertThat(normalizePlaceholders(result.toString())).isEqualTo(
			"(((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?)) OR ((t0.PARTITION_ID = ?) AND (t0.RES_ID = ?)))");
	}

	private SearchQueryBuilder createBuilder(Dialect theDialect) {
		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(theDialect);
		return new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings,
			RequestPartitionId.fromPartitionId(1), "Patient", mySqlBuilderFactory, dialectProvider, false, false);
	}

	private SearchQueryBuilder createNonPartitionBuilder(Dialect theDialect) {
		PartitionSettings nonPartitionSettings = new PartitionSettings();
		nonPartitionSettings.setPartitioningEnabled(false);
		nonPartitionSettings.setDatabasePartitionMode(false);
		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(theDialect);
		return new SearchQueryBuilder(myFhirContext, myStorageSettings, nonPartitionSettings,
			RequestPartitionId.defaultPartition(), "Patient", mySqlBuilderFactory, dialectProvider, false, false);
	}

	/**
	 * Replaces UUID-based bind variable placeholders with {@code ?} for readable assertions.
	 */
	private String normalizePlaceholders(String theSql) {
		return theSql.replaceAll(PLACEHOLDER, "?");
	}
}
