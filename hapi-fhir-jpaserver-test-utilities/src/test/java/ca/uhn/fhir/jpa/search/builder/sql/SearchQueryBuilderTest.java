package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirMariaDBDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import com.google.common.collect.Lists;
import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
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

	/**
	 * Test for SQL GROUP BY issue (6709) with coordinate near sorting and offset.
	 */
	@Test
	public void testCoordinateNearSortingWithGroupByHandlesSQLCorrectly() {
		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new PostgreSQLDialect());

		SearchQueryBuilder builder = new SearchQueryBuilder(myFhirContext, myStorageSettings, myPartitionSettings, myRequestPartitionId, "Location", mySqlBuilderFactory, dialectProvider, false, false);

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

		// Find distance calculation column name (MHD0, MHD1, etc.)
		java.util.regex.Pattern distanceColumnPattern = java.util.regex.Pattern.compile("MHD\\d+");
		java.util.regex.Matcher matcher = distanceColumnPattern.matcher(sql);
		assertTrue(matcher.find(), "SQL should contain distance calculation column (MHD0, MHD1, etc.)");
		String distanceColumnName = matcher.group();

		// Verify GROUP BY clause exists
		assertTrue(sql.contains("GROUP BY"), "SQL should contain GROUP BY clause (this test specifically tests the GROUP BY scenario)");

		// Extract GROUP BY clause and verify distance column is included
		int groupByIndex = sql.indexOf("GROUP BY");
		int orderByIndex = sql.indexOf("ORDER BY");
		if (orderByIndex == -1) {
			orderByIndex = sql.length();
		}
		String groupByClause = sql.substring(groupByIndex, orderByIndex).trim();

		// This assertion should FAIL and expose the bug: MHD0 is NOT in the GROUP BY clause
		assertTrue(groupByClause.contains(distanceColumnName),
			"Distance calculation column (" + distanceColumnName + ") must be included in GROUP BY clause when GROUP BY is present. " +
				"This prevents PostgreSQL error: 'column must appear in the GROUP BY clause'. " +
				"SQL: " + sql + ", GROUP BY clause: " + groupByClause);

		// Verify ORDER BY contains distance column
		assertTrue(sql.contains("ORDER BY " + distanceColumnName),
			"SQL should contain ORDER BY with " + distanceColumnName + " distance calculation");

		// Verify bind variables
		assertThat(generatedSql.getBindVariables()).contains(latitude, longitude);
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
		public ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc searchParamIdentityCacheSvc() {
			return org.mockito.Mockito.mock(ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc.class);
		}

		@Bean
		public SqlObjectFactory sqlObjectFactory() {
			return new SqlObjectFactory();
		}

	}
}
