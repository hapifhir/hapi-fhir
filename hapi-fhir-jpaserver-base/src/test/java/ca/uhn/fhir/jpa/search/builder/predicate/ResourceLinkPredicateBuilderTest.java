package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ResourceLinkPredicateBuilderTest {

	private ResourceLinkPredicateBuilder myResourceLinkPredicateBuilder;

	@BeforeEach
	public void init() {
		DbSpec spec = new DbSpec();
		DbSchema schema = new DbSchema(spec, "schema");
		DbTable table = new DbTable(schema, "table");

		SearchQueryBuilder sb = Mockito.mock(SearchQueryBuilder.class);
		Mockito.when(sb.addTable(Mockito.anyString()))
			.thenReturn(table);
		myResourceLinkPredicateBuilder = new ResourceLinkPredicateBuilder(
			null,
			sb,
			false
		);
	}

	@Test
	public void createEverythingPredicate_withListOfPids_returnsInPredicate() {
		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient",
			1l, 2l);

		Assertions.assertTrue(condition instanceof InCondition);
	}

	@Test
	public void createEverythingPredicate_withSinglePid_returnsInCondition() {
		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient",
			1l);

		Assertions.assertTrue(condition instanceof InCondition);
	}

	@Test
	public void createEverythingPredicate_withNoPids_returnsBinaryCondition() {
		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient",
			new Long[0]);

		Assertions.assertTrue(condition instanceof BinaryCondition);
	}
}
