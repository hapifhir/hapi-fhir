package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import ca.uhn.fhir.jpa.search.builder.models.TokenIndexMode;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenCompressedPredicateBuilderTest {

	@Mock
	private SearchQueryBuilder mySearchQueryBuilder;
	@Mock
	private ResourceTablePredicateBuilder myResourceTablePredicateBuilder;
	@Mock
	private ISearchParamIdentityCacheSvc mySearchParamIdentityCacheSvc;

	private DbTable myPrimaryTable;
	private DbTable myCommonTable;
	private DbColumn myResourceIdColumn;

	@BeforeEach
	void beforeEach() {
		DbSpec spec = new DbSpec();
		DbSchema schema = new DbSchema(spec, "schema");
		myPrimaryTable = new DbTable(schema, "primary");
		myCommonTable = new DbTable(schema, "common");
		DbTable resourceTable = new DbTable(schema, "resource");
		myResourceIdColumn = resourceTable.addColumn("RES_ID");
		when(mySearchQueryBuilder.getPartitionSettings()).thenReturn(new PartitionSettings());
		Mockito.lenient()
				.when(mySearchQueryBuilder.generatePlaceholder(any()))
				.thenAnswer(inv -> "?" + inv.getArgument(0));
		Mockito.lenient()
				.when(mySearchQueryBuilder.getRequestPartitionId())
				.thenReturn(RequestPartitionId.defaultPartition(new PartitionSettings()));
		Mockito.lenient().when(myResourceTablePredicateBuilder.getResourceIdColumn()).thenReturn(myResourceIdColumn);
		Mockito.lenient().when(myResourceTablePredicateBuilder.getResourceType()).thenReturn("Patient");
	}

	@Test
	void commonMode_constructorSelectsTokenCommonResTable() {
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(myPrimaryTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.COMMON);

		assertThat(builder.getResourceIdColumn()).isNotNull();
		assertThat(builder.getResourceIdColumn().getColumnNameSQL()).isEqualTo("RES_ID");
	}

	@Test
	void identifierMode_constructorSelectsTokenIdentifierTable() {
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(myPrimaryTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);

		assertThat(builder.getResourceIdColumn()).isNotNull();
		assertThat(builder.getResourceIdColumn().getColumnNameSQL()).isEqualTo("RES_ID");
	}

	@Test
	void commonMode_systemAndValue_producesDirectHashSysAndValuePredicate() {
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(myPrimaryTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.COMMON);

		RuntimeSearchParam searchParam = new RuntimeSearchParam(
				null, null, "Patient", null, null, null, null, null, null, null);
		List<IQueryParameterType> params = List.of(new TokenParam("http://example.com", "abc"));

		Condition predicate =
				builder.createPredicateToken(params, "Patient", null, searchParam, RequestPartitionId.defaultPartition(new PartitionSettings()));

		assertThat(predicate).isNotNull();
		// system+value → should be a BinaryCondition on HASH_SYS_AND_VALUE, not a subquery
		assertThat(predicate).isNotInstanceOf(InCondition.class);
		String sql = predicate.toString();
		assertThat(sql).doesNotContain("SELECT");
	}

	@Test
	void commonMode_valueOnly_producesSubqueryIntoTokenCommon() {
		// First call: addTable for primary (TOKEN_COMMON_RES), second: for subquery (TOKEN_COMMON)
		when(mySearchQueryBuilder.addTable(Mockito.anyString()))
				.thenReturn(myPrimaryTable)
				.thenReturn(myCommonTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.COMMON);

		RuntimeSearchParam searchParam = new RuntimeSearchParam(
				null, null, "Patient", null, null, null, null, null, null, null);
		// value-only: no system
		List<IQueryParameterType> params = List.of(new TokenParam(null, "abc"));

		Condition predicate =
				builder.createPredicateToken(params, "Patient", null, searchParam, RequestPartitionId.defaultPartition(new PartitionSettings()));

		assertThat(predicate).isNotNull();
		// value-only → should produce an IN (subquery) condition
		assertThat(predicate).isInstanceOf(InCondition.class);
	}

	@Test
	void identifierMode_valueOnly_producesHashIdentityAndHashValuePredicate() {
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(myPrimaryTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);

		RuntimeSearchParam searchParam = new RuntimeSearchParam(
				null, null, "Patient", null, null, null, null, null, null, null);
		List<IQueryParameterType> params = List.of(new TokenParam(null, "abc"));

		Condition predicate =
				builder.createPredicateToken(params, "Patient", null, searchParam, RequestPartitionId.defaultPartition(new PartitionSettings()));

		assertThat(predicate).isNotNull();
		// IDENTIFIER mode with value → AND predicate containing HASH_IDENTITY and HASH_VALUE
		String sql = predicate.toString();
		assertThat(sql).contains("HASH_IDENTITY");
		assertThat(sql).contains("HASH_VALUE");
	}

	@Test
	void identifierMode_systemOnly_producesHashIdentityAndSystemUrlIdPredicate() {
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(myPrimaryTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);

		RuntimeSearchParam searchParam =
				new RuntimeSearchParam(null, null, "Patient", null, null, null, null, null, null, null);
		// system-only: identifier=http://hospital.org/mrn|
		List<IQueryParameterType> params = List.of(new TokenParam("http://hospital.org/mrn", null));

		Condition predicate =
				builder.createPredicateToken(params, "Patient", null, searchParam, RequestPartitionId.defaultPartition(new PartitionSettings()));

		assertThat(predicate).isNotNull();
		// IDENTIFIER mode system-only → AND predicate containing HASH_IDENTITY and SP_SYSTEM_URL_ID (no HASH_VALUE)
		String sql = predicate.toString();
		assertThat(sql).contains("HASH_IDENTITY");
		assertThat(sql).contains("SP_SYSTEM_URL_ID");
		assertThat(sql).doesNotContain("HASH_VALUE");
	}

	@Test
	void commonMode_systemOnly_producesSubqueryIntoTokenCommonBySystemId() {
		// Constructor gets primary table; system-only subquery needs TOKEN_COMMON too
		when(mySearchQueryBuilder.addTable(Mockito.anyString()))
				.thenReturn(myPrimaryTable)
				.thenReturn(myCommonTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.COMMON);

		RuntimeSearchParam searchParam =
				new RuntimeSearchParam(null, null, "Patient", null, null, null, null, null, null, null);
		// system-only: code=http://loinc.org|
		List<IQueryParameterType> params = List.of(new TokenParam("http://loinc.org", null));

		Condition predicate =
				builder.createPredicateToken(params, "Patient", null, searchParam, RequestPartitionId.defaultPartition(new PartitionSettings()));

		assertThat(predicate).isNotNull();
		// system-only in COMMON mode → IN (subquery) filtering by HASH_IDENTITY and SYSTEM_ID
		assertThat(predicate).isInstanceOf(InCondition.class);
		String sql = predicate.toString();
		assertThat(sql).contains("SYSTEM_ID");
		assertThat(sql).contains("HASH_IDENTITY");
	}

	@Test
	void identifierMode_missingParam_producesNotExistsSubquery() {
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(myPrimaryTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);
		builder.setSearchParamIdentityCacheSvcForUnitTest(mySearchParamIdentityCacheSvc);

		MissingQueryParameterPredicateParams params = new MissingQueryParameterPredicateParams(
				myResourceTablePredicateBuilder, true, "identifier", RequestPartitionId.defaultPartition(new PartitionSettings()));

		Condition predicate = builder.createPredicateParamMissingValue(params);

		assertThat(predicate).isNotNull();
		String sql = predicate.toString();
		// NOT EXISTS subquery joining on HASH_IDENTITY
		assertThat(sql).contains("NOT (EXISTS");
		assertThat(sql).contains("HASH_IDENTITY");
	}

	@Test
	void commonMode_missingParam_producesNotExistsWithJoinToCommonTable() {
		// Constructor gets primary table; missing-param subquery needs TOKEN_COMMON too
		when(mySearchQueryBuilder.addTable(Mockito.anyString()))
				.thenReturn(myPrimaryTable)
				.thenReturn(myCommonTable);

		CompressedTokenPredicateBuilder builder =
				new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.COMMON);
		builder.setSearchParamIdentityCacheSvcForUnitTest(mySearchParamIdentityCacheSvc);

		MissingQueryParameterPredicateParams params = new MissingQueryParameterPredicateParams(
				myResourceTablePredicateBuilder, true, "status", RequestPartitionId.defaultPartition(new PartitionSettings()));

		Condition predicate = builder.createPredicateParamMissingValue(params);

		assertThat(predicate).isNotNull();
		String sql = predicate.toString();
		// NOT EXISTS with both tables in FROM (implicit join via WHERE)
		assertThat(sql).contains("NOT (EXISTS");
		assertThat(sql).contains("HASH_IDENTITY");
		assertThat(sql).contains("HASH_SYS_AND_VALUE");
	}
}
