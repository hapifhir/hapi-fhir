package ca.uhn.fhir.jpa.search.builder.models;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PredicateBuilderCacheKeyTest {

	static final DbSpec ourSpec = new DbSpec();
	static final DbSchema ourSchema = new DbSchema(ourSpec, "schema");
	static final DbTable ourTable = new DbTable(ourSchema, "HFJ_RESOURCE");
	static final DbColumn ourColumn2 = new DbColumn(ourTable, "COL2", "varchar2");
	static final DbColumn ourColumn1 = new DbColumn(ourTable, "COL1", "varchar2");

	@Test
	void testEqualsImpliesHashEqual() {
		var key1 = new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.TOKEN, "identifier");
		var key2 = new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.TOKEN, "identifier");

		assertEquals(key1, key2);
		assertEquals(key1.hashCode(), key2.hashCode());
	}

	static Stream<Object[]> differenceTestCases() {
		return Stream.of(
			new Object[]{
				"different type",
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.TOKEN, "identifier"),
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.DATE, "identifier")
			},
			new Object[]{
				"different sp name",
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.TOKEN, "identifier"),
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.TOKEN, "code")
			},
			new Object[]{
				"different number of columns",
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.TOKEN, "identifier"),
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1}, PredicateBuilderTypeEnum.TOKEN, "code")
			},
			new Object[]{
				"different column order",
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn1, ourColumn2}, PredicateBuilderTypeEnum.TOKEN, "identifier"),
				new PredicateBuilderCacheKey(new DbColumn[]{ourColumn2, ourColumn1}, PredicateBuilderTypeEnum.TOKEN, "code")
			}
		);
	}

	@ParameterizedTest
	@MethodSource("differenceTestCases")
	void testChangesMakeNotEqual(String theMessage, PredicateBuilderCacheKey theValue, PredicateBuilderCacheKey theComparisonValue) {
		assertNotEquals(theValue, theComparisonValue, theMessage + ": equals");
	}

	@ParameterizedTest
	@MethodSource("differenceTestCases")
	void testChangesChangeHashCode(String theMessage, PredicateBuilderCacheKey theValue, PredicateBuilderCacheKey theComparisonValue) {
		assertNotEquals(theValue.hashCode(), theComparisonValue.hashCode(), theMessage);
	}

}
