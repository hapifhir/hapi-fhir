package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import ca.uhn.fhir.jpa.search.builder.models.TokenIndexMode;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
		DbSchema schema = new DbSchema(new DbSpec(), "schema");
		myPrimaryTable = new DbTable(schema, "primary");
		myCommonTable = new DbTable(schema, "common");
		DbTable resourceTable = new DbTable(schema, "resource");
		myResourceIdColumn = resourceTable.addColumn("RES_ID");
		when(mySearchQueryBuilder.getPartitionSettings()).thenReturn(new PartitionSettings());
		when(mySearchQueryBuilder.generatePlaceholder(any())).thenReturn("?");
		when(mySearchQueryBuilder.addTable(Mockito.anyString()))
			.thenAnswer(inv ->
				"HFJ_SPIDX2_TOKEN_COMMON".equals(inv.getArgument(0)) ? myCommonTable : myPrimaryTable);
	}

	private static RuntimeSearchParam patientSearchParam() {
		return new RuntimeSearchParam(null, null, "Patient", null, null, null, null, null, null, null);
	}

	private Condition buildTokenPredicate(
		CompressedTokenPredicateBuilder theBuilder, List<IQueryParameterType> theParams) {
		when(mySearchQueryBuilder.getRequestPartitionId())
			.thenReturn(RequestPartitionId.defaultPartition(new PartitionSettings()));
		return theBuilder.createPredicateToken(
			theParams,
			"Patient",
			null,
			patientSearchParam(),
			RequestPartitionId.defaultPartition(new PartitionSettings()));
	}

	private void stubResourceTablePredicateBuilder() {
		when(myResourceTablePredicateBuilder.getResourceIdColumn()).thenReturn(myResourceIdColumn);
		when(myResourceTablePredicateBuilder.getResourceType()).thenReturn("Patient");
	}

	@ParameterizedTest
	@CsvSource(
		value = {
			"null               , abc  , (t0.HASH_SYS_AND_VALUE IN (SELECT t1.HASH_SYS_AND_VALUE FROM schema.common t1 WHERE (t1.HASH_VALUE = '?')) )", // value-only
			"http://loinc.org   , null , (t0.HASH_SYS_AND_VALUE IN (SELECT t1.HASH_SYS_AND_VALUE FROM schema.common t1 WHERE ((t1.HASH_IDENTITY = '?') AND (t1.SYSTEM_ID = '?'))) )", // system-only
			"http://example.com , abc  , (t0.HASH_SYS_AND_VALUE = '?')" // system+value
		},
		nullValues = "null", quoteCharacter = '"')
	void commonMode_producesCorrectQuery(String theSystem, String theValue, String theExpectedSql) {
		// setup
		CompressedTokenPredicateBuilder builder =
			new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.COMMON);

		// execute
		Condition predicate = buildTokenPredicate(builder, List.of(new TokenParam(theSystem, theValue)));

		// validate
		assertThat(predicate).isNotNull();
		assertThat(predicate.toString()).isEqualTo(theExpectedSql);
	}

	@ParameterizedTest
	@CsvSource(
		value = {
			"null                    , abc   , ((t0.HASH_IDENTITY = '?') AND (t0.HASH_VALUE = '?'))", // value-only
			"http://hospital.org/mrn , null  , ((t0.HASH_IDENTITY = '?') AND (t0.SP_SYSTEM_URL_ID = '?'))", // system-only
			"http://hospital.org/mrn , 12345 , ((t0.HASH_IDENTITY = '?') AND ((t0.SP_SYSTEM_URL_ID = '?') AND (t0.HASH_VALUE = '?')))", // system+value
			"\"\"                   , abc   , ((t0.HASH_IDENTITY = '?') AND ((t0.SP_SYSTEM_URL_ID IS NULL) AND (t0.HASH_VALUE = '?')))" // empty system (identifier=|value)
		},
		nullValues = "null", quoteCharacter = '"')
	void identifierMode_producesCorrectQuery(String theSystem, String theValue, String theExpectedSql) {
		// setup
		CompressedTokenPredicateBuilder builder =
			new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);

		// execute
		Condition predicate = buildTokenPredicate(builder, List.of(new TokenParam(theSystem, theValue)));

		// validate
		assertThat(predicate).isNotNull();
		assertThat(predicate.toString()).isEqualTo(theExpectedSql);
	}

	@Test
	void identifierMode_missingParam_producesNotExistsSubquery() {
		// setup
		CompressedTokenPredicateBuilder builder =
			new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);
		builder.setSearchParamIdentityCacheSvcForUnitTest(mySearchParamIdentityCacheSvc);
		stubResourceTablePredicateBuilder();

		MissingQueryParameterPredicateParams params = new MissingQueryParameterPredicateParams(
			myResourceTablePredicateBuilder, true, "identifier", RequestPartitionId.defaultPartition(new PartitionSettings()));

		// execute
		Condition predicate = builder.createPredicateParamMissingValue(params);

		// validate
		assertThat(predicate).isNotNull();
		assertThat(predicate.toString())
			.contains("(NOT (EXISTS (SELECT 1 FROM schema.primary t0 WHERE ((t0.RES_ID = t2.RES_ID) AND (t0.HASH_IDENTITY = '?'))))))");
	}

	@Test
	void commonMode_missingParam_producesNotExistsWithJoinToCommonTable() {
		// setup
		CompressedTokenPredicateBuilder builder =
			new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.COMMON);
		builder.setSearchParamIdentityCacheSvcForUnitTest(mySearchParamIdentityCacheSvc);
		stubResourceTablePredicateBuilder();

		MissingQueryParameterPredicateParams params = new MissingQueryParameterPredicateParams(
			myResourceTablePredicateBuilder, true, "status", RequestPartitionId.defaultPartition(new PartitionSettings()));

		// execute
		Condition predicate = builder.createPredicateParamMissingValue(params);

		// validate
		assertThat(predicate).isNotNull();
		assertThat(predicate.toString())
			.contains("(NOT (EXISTS (SELECT 1 FROM schema.primary t0, schema.common t1 WHERE " +
				"((t0.RES_ID = t2.RES_ID) AND (t0.HASH_SYS_AND_VALUE = t1.HASH_SYS_AND_VALUE) AND (t1.HASH_IDENTITY = '?'))))))");
	}

	@Test
	void identifierMode_ofType_producesTypeHashSysAndValuePredicate() {
		// setup
		CompressedTokenPredicateBuilder builder =
			new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);
		JpaStorageSettings storageSettings = new JpaStorageSettings();
		storageSettings.setIndexIdentifierOfType(true);
		builder.setStorageSettingsForUnitTest(storageSettings);

		TokenParam param = new TokenParam("http://terminology.hl7.org/CodeSystem/v2-0203", "MR|12345")
			.setModifier(TokenParamModifier.OF_TYPE);

		// execute
		Condition predicate = buildTokenPredicate(builder, List.of(param));

		// validate
		assertThat(predicate).isNotNull();
		assertThat(predicate.toString()).isEqualTo("(t0.TYPE_HASH_SYS_AND_VALUE = '?')");
	}

	@Test
	void identifierMode_notModifier_producesNotEqualsScopedByHashIdentity() {
		// setup
		CompressedTokenPredicateBuilder builder =
			new CompressedTokenPredicateBuilder(mySearchQueryBuilder, TokenIndexMode.IDENTIFIER);
		builder.setSearchParamIdentityCacheSvcForUnitTest(mySearchParamIdentityCacheSvc);

		TokenParam param = new TokenParam("http://example.com", "abc").setModifier(TokenParamModifier.NOT);

		// execute
		Condition predicate = buildTokenPredicate(builder, List.of(param));

		// validate
		assertThat(predicate).isNotNull();
		assertThat(predicate.toString())
			.isEqualTo("((t0.HASH_IDENTITY = '?') AND " +
				"(((t0.SP_SYSTEM_URL_ID <> '?') OR (t0.SP_SYSTEM_URL_ID IS NULL)) OR (t0.HASH_VALUE <> '?')))");
	}

}
