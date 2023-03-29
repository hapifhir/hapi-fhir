package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceLinkPredicateBuilderTest {

    private static final String PLACEHOLDER_BASE = UUID.randomUUID().toString();
	private ResourceLinkPredicateBuilder myResourceLinkPredicateBuilder;

    @Mock
    private SearchQueryBuilder mySearchQueryBuilder;

	@BeforeEach
	public void init() {
		DbSpec spec = new DbSpec();
		DbSchema schema = new DbSchema(spec, "schema");
		DbTable table = new DbTable(schema, "table");
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(table);
        myResourceLinkPredicateBuilder = new ResourceLinkPredicateBuilder(null, mySearchQueryBuilder, false);
	}

	@Test
	public void createEverythingPredicate_withListOfPids_returnsInPredicate() {
        when(myResourceLinkPredicateBuilder.generatePlaceholders(anyCollection())).thenReturn(List.of(PLACEHOLDER_BASE+"1", PLACEHOLDER_BASE+"2"));
		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient", new ArrayList<>(), 1l, 2l);
        assertEquals(InCondition.class, condition.getClass());
	}

	@Test
	public void createEverythingPredicate_withSinglePid_returnsInCondition() {
        when(myResourceLinkPredicateBuilder.generatePlaceholders(anyCollection())).thenReturn(List.of(PLACEHOLDER_BASE+"1"));
		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient", new ArrayList<>(), 1l);
        assertEquals(BinaryCondition.class, condition.getClass());
	}

	@Test
	public void createEverythingPredicate_withNoPids_returnsBinaryCondition() {
        Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient", new ArrayList<>(), new Long[0]);
        assertEquals(BinaryCondition.class, condition.getClass());
	}
}
