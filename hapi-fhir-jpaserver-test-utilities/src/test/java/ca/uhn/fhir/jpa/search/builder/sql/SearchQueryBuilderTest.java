package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirMariaDBDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import com.google.common.collect.Lists;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SearchQueryBuilderTest.MyConfig.class})
public class SearchQueryBuilderTest {

	private FhirContext myFhirContext;
	private StorageSettings myStorageSettings;
	private PartitionSettings myPartitionSettings;
	private RequestPartitionId myRequestPartitionId;

	@Autowired
	private SqlObjectFactory mySqlBuilderFactory;

	@BeforeEach
	public void before() {
		myFhirContext = FhirContext.forR4Cached();
		myStorageSettings = new StorageSettings();
		myPartitionSettings = new PartitionSettings();
		myRequestPartitionId = RequestPartitionId.allPartitions();
	}

	@Test
	public void testRangeSqlServer2005_NoSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new SQLServerDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT T0.RES_ID FROM HFJ_RESOURCE T0 WHERE (((T0.RES_TYPE = ?) AND (T0.RES_DELETED_AT IS NULL)) AND (T0.RES_ID IN (?,?) )) ORDER BY RES_ID OFFSET 0 ROWS FETCH FIRST ? ROWS ONLY", generated.getSql().toUpperCase(Locale.ROOT));
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) order by RES_ID offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeSqlServer2005_WithSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new SQLServerDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		builder.addSortDate(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT T0.RES_ID FROM HFJ_RESOURCE T0 WHERE (((T0.RES_TYPE = ?) AND (T0.RES_DELETED_AT IS NULL)) AND (T0.RES_ID IN (?,?) )) ORDER BY T0.RES_UPDATED ASC OFFSET 0 ROWS FETCH FIRST ? ROWS ONLY", generated.getSql().toUpperCase(Locale.ROOT));
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}


	@Test
	public void testRangeSqlServer2012_NoSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new SQLServer2012Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT T0.RES_ID FROM HFJ_RESOURCE T0 WHERE (((T0.RES_TYPE = ?) AND (T0.RES_DELETED_AT IS NULL)) AND (T0.RES_ID IN (?,?) )) ORDER BY RES_ID OFFSET 0 ROWS FETCH FIRST ? ROWS ONLY", generated.getSql().toUpperCase(Locale.ROOT));
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) order by RES_ID offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeSqlServer2012_WithSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new SQLServer2012Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		builder.addSortDate(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC offset 0 rows fetch first ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangePostgreSQL95_NoSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new PostgreSQLDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) fetch first ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangePostgreSQL95_WithSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new PostgreSQLDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		builder.addSortDate(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST fetch first ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeOracle12c_NoSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new HapiFhirOracleDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) fetch first ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeOracle12c_WithSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new HapiFhirOracleDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		builder.addSortDate(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST fetch first ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeMySQL8_NoSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new MySQL8Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?,?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeMySQL8_WithSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new MySQL8Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		builder.addSortDate(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
//		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY CASE WHEN t0.RES_UPDATED IS NULL THEN 1 ELSE 0 END ASC, t0.RES_UPDATED ASC", generated.getSql());
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
//		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY CASE WHEN t0.RES_UPDATED IS NULL THEN 1 ELSE 0 END ASC, t0.RES_UPDATED ASC limit ?", generated.getSql());
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC limit ?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
//		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY CASE WHEN t0.RES_UPDATED IS NULL THEN 1 ELSE 0 END ASC, t0.RES_UPDATED ASC limit ?, ?", generated.getSql());
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC limit ?,?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}


	@Test
	public void testRangeMariaDB103_NoSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new HapiFhirMariaDBDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?,?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeMariaDB103_WithSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new HapiFhirMariaDBDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		builder.addSortDate(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
//		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY CASE WHEN t0.RES_UPDATED IS NULL THEN 1 ELSE 0 END ASC, t0.RES_UPDATED ASC", generated.getSql());
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
//		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY CASE WHEN t0.RES_UPDATED IS NULL THEN 1 ELSE 0 END ASC, t0.RES_UPDATED ASC limit ?", generated.getSql());
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC limit ?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
//		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY CASE WHEN t0.RES_UPDATED IS NULL THEN 1 ELSE 0 END ASC, t0.RES_UPDATED ASC limit ?, ?", generated.getSql());
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC limit ?,?", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}


	@Test
	public void testRangeDerbyTenSeven_NoSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new DerbyDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) fetch first ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	public void testRangeDerbyTenSeven_WithSort() {

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new DerbyDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(500L), JpaPid.fromId(501L)));
		builder.addSortDate(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L);

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST fetch first ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10);

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables()).as(generated.getBindVariables().toString()).containsExactly("Patient", 500L, 501L, 10, 5);

	}

	@Test
	void toJoinCondition_singleColumn_returnsBinaryCondition() {
		DbSchema schema = createSchema();
		DbTable fromTable = schema.addTable("FROM_TABLE");
		DbTable toTable = schema.addTable("TO_TABLE");
		DbColumn fromCol = fromTable.addColumn("RES_ID");
		DbColumn toCol = toTable.addColumn("RES_ID");

		Condition result = SearchQueryBuilder.toJoinCondition(
				new DbColumn[]{fromCol}, new DbColumn[]{toCol});

		assertThat(result)
				.isInstanceOf(BinaryCondition.class)
				.hasToString("(t0.RES_ID = t1.RES_ID)");
	}

	@Test
	void toJoinCondition_multipleColumns_returnsComboCondition() {
		DbSchema schema = createSchema();
		DbTable fromTable = schema.addTable("FROM_TABLE");
		DbTable toTable = schema.addTable("TO_TABLE");
		DbColumn fromA = fromTable.addColumn("COL_A");
		DbColumn fromB = fromTable.addColumn("COL_B");
		DbColumn fromC = fromTable.addColumn("COL_C");
		DbColumn toA = toTable.addColumn("COL_A");
		DbColumn toB = toTable.addColumn("COL_B");
		DbColumn toC = toTable.addColumn("COL_C");

		Condition result = SearchQueryBuilder.toJoinCondition(
				new DbColumn[]{fromA, fromB, fromC},
				new DbColumn[]{toA, toB, toC});

		assertThat(result)
				.isInstanceOf(ComboCondition.class)
				.hasToString("((t0.COL_A = t1.COL_A) AND (t0.COL_B = t1.COL_B) AND (t0.COL_C = t1.COL_C))");
	}

	private static DbSchema createSchema() {
		DbSpec spec = new DbSpec();
		return spec.addDefaultSchema();
	}

	/**
	 * Test for SQL GROUP BY issue (6709) with coordinate near sorting and offset.
	 */
	@Test
	void testCoordinateNearSortingWithGroupByHandlesSQLCorrectly() {
		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new PostgreSQLDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(
				myFhirContext,
				myStorageSettings,
				myPartitionSettings,
				myRequestPartitionId,
				"Location",
				mySqlBuilderFactory,
				dialectProvider,
				false,
				false);

		// Add coordinate distance sorting
		ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder coordsPredicate =
			builder.addCoordsPredicateBuilder(null);

		// Trigger GROUP BY scenario BEFORE adding coordinate sorting
		// This simulates the correct production sequence where GROUP BY exists before coordinate sorting is added
		builder.getSelect().addGroupings(builder.getOrCreateFirstPredicateBuilder().getResourceIdColumn());

		double latitude = 50.097;
		double longitude = 8.6648;
		builder.addSortCoordsNear(coordsPredicate, latitude, longitude, true);


		// Generate SQL with offset
		GeneratedSql generatedSql = builder.generate(1, 1);
		String sql = generatedSql.getSql();

		assertTrue(sql.contains("SELECT"), "SQL should contain SELECT clause");

		// Find distance calculation column name (EUC_DIST0, EUC_DIST1, etc.)
		java.util.regex.Pattern distanceColumnPattern = java.util.regex.Pattern.compile("EUC_DIST\\d+");
		java.util.regex.Matcher matcher = distanceColumnPattern.matcher(sql);
		assertTrue(matcher.find(), "SQL should contain distance calculation column (EUC_DIST0, EUC_DIST1, etc.)");
		String distanceColumnName = matcher.group();

		// Verify GROUP BY clause exists
		assertTrue(sql.contains("GROUP BY"), "SQL should contain GROUP BY clause (this test specifically tests the GROUP BY scenario)");

		// Extract GROUP BY clause
		int groupByIndex = sql.indexOf("GROUP BY");
		int orderByIndex = sql.indexOf("ORDER BY");
		if (orderByIndex == -1) {
			orderByIndex = sql.length();
		}
		String groupByClause = sql.substring(groupByIndex, orderByIndex).trim();

		assertThat(sql).contains("MIN(");
		assertThat(groupByClause).doesNotContain(distanceColumnName);

		// Verify ORDER BY contains distance column
		assertTrue(sql.contains("ORDER BY " + distanceColumnName),
			"SQL should contain ORDER BY with " + distanceColumnName + " distance calculation");

		// Verify offset/limit pagination is present (bug reproduction requires offset path)
		assertTrue(sql.contains("offset ? rows fetch next ? rows only"),
			"SQL should include offset pagination syntax for this reproduction scenario");

		// Verify bind variables
		assertThat(generatedSql.getBindVariables()).contains(latitude, longitude);
	}

	// --- GL-9268: large ID lists are bound as a single JSON array above a threshold ---

	/**
	 * A6/A7/A8: exercises the large-ID-list threshold boundary - a list exactly at the threshold is
	 * unchanged (A6, the branch is strictly greater-than), a single ID still collapses to an equality
	 * predicate at or below the threshold (A7), the documented disable value keeps today's SQL for lists
	 * of any size (A8, disable half), and a threshold of zero means every list is rendered as a JSON
	 * array, including a single ID (A8, zero half).
	 */
	@ParameterizedTest(name = "threshold={0}, idCount={1}, expectJson={2}")
	@CsvSource({
		"3, 3, false",
		"3, 1, false",
		"1, 1, false",
		"-1, 5, false",
		"0, 1, true"
	})
	void testResourceIdsThresholdBoundary_rendersExpectedPredicate(int theThreshold, int theIdCount, boolean theExpectJson) {
		SearchQueryBuilder builder = createPostgresBuilder(theThreshold);
		builder.addPredicate(createResourceIdsPredicate(builder, false, theIdCount));

		GeneratedSql generated = builder.generate(null, null);
		String sql = generated.getSql();

		if (theExpectJson) {
			assertThat(sql).contains("t0.RES_ID IN (SELECT CAST(j.value AS BIGINT) FROM jsonb_array_elements_text(CAST(? AS jsonb)) AS j)");
			assertThat(generated.getBindVariables()).hasSize(2);
		} else {
			assertThat(sql).doesNotContain("jsonb_array_elements_text");
			if (theIdCount == 1) {
				assertThat(sql).contains("t0.RES_ID = ?");
			} else {
				assertThat(sql).contains("t0.RES_ID IN (" + "?,".repeat(theIdCount - 1) + "?)");
			}
			assertThat(generated.getBindVariables()).hasSize(theIdCount + 1);
		}
	}

	/**
	 * A8 (validation half): values below the disable value are rejected by the setter.
	 */
	@Test
	void testThresholdBelowDisableValue_isRejected() {
		StorageSettings storageSettings = new StorageSettings();

		assertThatThrownBy(() -> storageSettings.setLargeIdListJsonThreshold(-2))
			.isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * A10: the JSON payload carries the IDs, and the SQL text carries none of them - so there is no
	 * injection surface and the SQL text stays stable for the query plan cache.
	 */
	@Test
	void testJsonPayloadHoldsIdsAndSqlTextDoesNot() {
		SearchQueryBuilder builder = createPostgresBuilder(3);
		builder.addPredicate(createResourceIdsPredicate(builder, false, 5));

		GeneratedSql generated = builder.generate(null, null);

		String sql = generated.getSql();
		assertThat(sql).doesNotContain("1,2");
		assertThat(StringUtils.countMatches(sql, "?")).as(sql).isEqualTo(2);
		assertThat(generated.getBindVariables()).containsExactly("Patient", "[1,2,3,4,5]");
	}

	/**
	 * A11: the JSON placeholder keeps its position in the bind list, between the binds that precede it
	 * in the SQL text and those appended afterwards.
	 */
	@Test
	void testBindOrderAroundJsonPlaceholder() {
		SearchQueryBuilder withLimit = createPostgresBuilder(3);
		withLimit.addPredicate(createResourceIdsPredicate(withLimit, false, 5));

		GeneratedSql generatedWithLimit = withLimit.generate(10, 5);
		assertThat(generatedWithLimit.getBindVariables()).containsExactly("Patient", "[1,2,3,4,5]", 10, 5);

		// A second predicate whose binds follow the JSON placeholder inside the WHERE clause
		SearchQueryBuilder withTrailingPredicate = createPostgresBuilder(3);
		withTrailingPredicate.addPredicate(createResourceIdsPredicate(withTrailingPredicate, false, 5));
		withTrailingPredicate.addResourceIdsPredicate(Lists.newArrayList(JpaPid.fromId(10L), JpaPid.fromId(11L)));

		GeneratedSql generatedWithTrailingPredicate = withTrailingPredicate.generate(null, null);
		String sql = generatedWithTrailingPredicate.getSql();
		assertThat(sql.indexOf("jsonb_array_elements_text")).isLessThan(sql.lastIndexOf("t0.RES_ID IN (?,?)"));
		assertThat(generatedWithTrailingPredicate.getBindVariables()).containsExactly("Patient", "[1,2,3,4,5]", 10L, 11L);
	}

	/**
	 * A12: an inverted predicate (_id:not) takes the same JSON path; the existing NOT wrap is applied
	 * to the resulting condition.
	 */
	@Test
	void testInverseResourceIdsOverThreshold_rendersNegatedJsonArray() {
		SearchQueryBuilder builder = createPostgresBuilder(3);
		builder.addPredicate(createResourceIdsPredicate(builder, true, 5));

		GeneratedSql generated = builder.generate(null, null);

		assertThat(generated.getSql()).contains("NOT (t0.RES_ID IN (SELECT CAST(j.value AS BIGINT) FROM jsonb_array_elements_text(CAST(? AS jsonb)) AS j))");
		assertThat(generated.getBindVariables()).containsExactly("Patient", "[1,2,3,4,5]");
	}

	/**
	 * A13: in Database Partition Mode the top-level _id predicate goes through the very same method, so it
	 * gets the JSON array binding too, with the partition predicate left untouched beside it.
	 */
	@Test
	void testDatabasePartitionModeResourceIdsOverThreshold_rendersPartitionPredicateAndJsonArray() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDatabasePartitionMode(true);
		myRequestPartitionId = RequestPartitionId.fromPartitionId(1);

		SearchQueryBuilder builder = createPostgresBuilder(3);
		BaseJoiningPredicateBuilder queryRootTable = builder.getOrCreateResourceTablePredicateBuilder();
		Condition predicate = queryRootTable.createPredicateResourceIds(false, toPids(5));
		builder.addPredicate(queryRootTable.combineWithRequestPartitionIdPredicate(myRequestPartitionId, predicate));

		GeneratedSql generated = builder.generate(null, null);

		String sql = generated.getSql();
		assertThat(sql).contains("t0.PARTITION_ID = ?");
		assertThat(sql).contains("t0.RES_ID IN (SELECT CAST(j.value AS BIGINT) FROM jsonb_array_elements_text(CAST(? AS jsonb)) AS j)");
		assertThat(generated.getBindVariables()).containsExactly("Patient", 1, "[1,2,3,4,5]");
	}

	private SearchQueryBuilder createPostgresBuilder(int theLargeIdListJsonThreshold) {
		myStorageSettings.setLargeIdListJsonThreshold(theLargeIdListJsonThreshold);
		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new HapiFhirPostgresDialect());
		return new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false, false);
	}

	private static Condition createResourceIdsPredicate(SearchQueryBuilder theBuilder, boolean theInverse, int theCount) {
		return theBuilder.getOrCreateResourceTablePredicateBuilder().createPredicateResourceIds(theInverse, toPids(theCount));
	}

	private static List<JpaPid> toPids(int theCount) {
		return LongStream.rangeClosed(1, theCount).mapToObj(JpaPid::fromId).collect(Collectors.toList());
	}

	@Configuration
	public static class MyConfig {

		@Bean
		public PartitionSettings partitionSettings() {
			return new PartitionSettings();
		}

		@Bean
		@Scope("prototype")
		public ResourceTablePredicateBuilder ResourceTablePredicateBuilder(SearchQueryBuilder theSearchQueryBuilder, SearchIncludeDeletedEnum theSearchIncludeDeleted) {
			return new ResourceTablePredicateBuilder(theSearchQueryBuilder, theSearchIncludeDeleted);
		}

		@Bean
		@Scope("prototype")
		public ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder coordsPredicateBuilder(SearchQueryBuilder theSearchQueryBuilder) {
			return new ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder(theSearchQueryBuilder);
		}

		@Bean
		public ISearchParamIdentityCacheSvc searchParamIdentityCacheSvc() {
			return new ISearchParamIdentityCacheSvc() {
				@Override
				public boolean hasInFlightTasks() {
					return false;
				}

				@Override
				public void findOrCreateSearchParamIdentity(Long theHashIdentity, String theResourceType, String theParamName) {
					// no-op for this unit test context
				}
			};
		}

		@Bean
		public SqlObjectFactory sqlObjectFactory() {
			return new SqlObjectFactory();
		}

	}
}
