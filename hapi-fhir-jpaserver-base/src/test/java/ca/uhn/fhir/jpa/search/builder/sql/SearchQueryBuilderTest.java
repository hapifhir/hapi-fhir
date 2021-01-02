package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.HibernateDialectProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import com.google.common.collect.Lists;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.SQLServer2005Dialect;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SearchQueryBuilderTest.MyConfig.class})
public class SearchQueryBuilderTest {

	private FhirContext myFhirContext;
	private ModelConfig myModelConfig;
	private PartitionSettings myPartitionSettings;
	private RequestPartitionId myRequestPartitionId;

	@Autowired
	private SqlObjectFactory mySqlBuilderFactory;

	@BeforeEach
	public void before() {
		myFhirContext = FhirContext.forCached(FhirVersionEnum.R4);
		myModelConfig = new ModelConfig();
		myPartitionSettings = new PartitionSettings();
		myRequestPartitionId = RequestPartitionId.allPartitions();
	}

	@Test
	public void testRangeSqlServer2005_NoSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new SQLServer2005Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT TOP(?) t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains(10, "Patient", 500L, 501L));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("WITH query AS (SELECT inner_query.*, ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __hibernate_row_nr__ FROM ( SELECT t0.RES_ID as page0_ FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ) inner_query ) SELECT page0_ FROM query WHERE __hibernate_row_nr__ >= ? AND __hibernate_row_nr__ < ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 11, 16));

	}

	@Test
	public void testRangeSqlServer2005_WithSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new SQLServer2005Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		builder.addSort(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT TOP(?) t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains(10, "Patient", 500L, 501L));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("WITH query AS (SELECT inner_query.*, ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __hibernate_row_nr__ FROM ( SELECT TOP(?) t0.RES_ID as page0_ FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST ) inner_query ) SELECT page0_ FROM query WHERE __hibernate_row_nr__ >= ? AND __hibernate_row_nr__ < ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains(5, "Patient", 500L, 501L, 11, 16));

	}


	@Test
	public void testRangeSqlServer2012_NoSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new SQLServer2012Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT TOP(?) t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains(10, "Patient", 500L, 501L));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("WITH query AS (SELECT inner_query.*, ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __hibernate_row_nr__ FROM ( SELECT t0.RES_ID as page0_ FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ) inner_query ) SELECT page0_ FROM query WHERE __hibernate_row_nr__ >= ? AND __hibernate_row_nr__ < ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 11, 16));

	}

	@Test
	public void testRangeSqlServer2012_WithSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new SQLServer2012Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		builder.addSort(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST offset 0 rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST offset ? rows fetch next ? rows only", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10, 5));

	}

	@Test
	public void testRangePostgreSQL95_NoSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new PostgreSQL95Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ? offset ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 5, 10));

	}

	@Test
	public void testRangePostgreSQL95_WithSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new PostgreSQL95Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		builder.addSort(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST limit ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST limit ? offset ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 5, 10));

	}

	@Test
	public void testRangeOracle12c_NoSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new Oracle12cDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("select * from ( SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ) where rownum <= ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("select * from ( select row_.*, rownum rownum_ from ( SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ) row_ where rownum <= ?) where rownum_ > ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 15, 10));

	}

	@Test
	public void testRangeOracle12c_WithSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new Oracle12cDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		builder.addSort(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("select * from ( SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST ) where rownum <= ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("select * from ( select row_.*, rownum rownum_ from ( SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST ) row_ where rownum <= ?) where rownum_ > ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 15, 10));

	}


	@Test
	public void testRangeMySQL8_NoSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new MySQL8Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?, ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10, 5));

	}

	@Test
	public void testRangeMySQL8_WithSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new MySQL8Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		builder.addSort(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST limit ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST limit ?, ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10, 5));

	}


	@Test
	public void testRangeMariaDB103_NoSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new MariaDB103Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) limit ?, ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10, 5));

	}

	@Test
	public void testRangeMariaDB103_WithSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new MariaDB103Dialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		builder.addSort(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST limit ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST limit ?, ?", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L, 10, 5));

	}


	@Test
	public void testRangeDerbyTenSeven_NoSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new DerbyTenSevenDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) ))", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) fetch first 10 rows only", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) offset 10 rows fetch next 5 rows only", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

	}

	@Test
	public void testRangeDerbyTenSeven_WithSort() {

		HibernateDialectProvider dialectProvider = new HibernateDialectProvider();
		dialectProvider.setDialectForUnitTest(new DerbyTenSevenDialect());
		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, "Patient", mySqlBuilderFactory, dialectProvider, false);
		builder.addResourceIdsPredicate(Lists.newArrayList(500L, 501L));
		builder.addSort(builder.getOrCreateResourceTablePredicateBuilder().getColumnLastUpdated(), true);
		GeneratedSql generated;

		// No range
		generated = builder.generate(null, null);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Max only
		generated = builder.generate(null, 10);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST fetch first 10 rows only", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

		// Range
		generated = builder.generate(10, 5);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN (?,?) )) ORDER BY t0.RES_UPDATED ASC NULLS LAST offset 10 rows fetch next 5 rows only", generated.getSql());
		assertThat(generated.getBindVariables().toString(), generated.getBindVariables(), contains("Patient", 500L, 501L));

	}
	
	@Configuration
	public static class MyConfig {

		@Bean
		@Scope("prototype")
		public ResourceTablePredicateBuilder ResourceTablePredicateBuilder(SearchQueryBuilder theSearchQueryBuilder) {
			return new ResourceTablePredicateBuilder(theSearchQueryBuilder);
		}

		@Bean
		public SqlObjectFactory sqlObjectFactory() {
			return new SqlObjectFactory();
		}

	}
}
