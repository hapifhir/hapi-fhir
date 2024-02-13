package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceLinkPredicateBuilderTest {

    private static final String PLACEHOLDER_BASE = UUID.randomUUID().toString();

	private ResourceLinkPredicateBuilder myResourceLinkPredicateBuilder;

    @Mock
    private SearchQueryBuilder mySearchQueryBuilder;

	@Mock
	private ISearchParamRegistry mySearchParamRegistry;

	@Mock
	private IIdHelperService myIdHelperService;

	@BeforeEach
	public void init() {
		DbSpec spec = new DbSpec();
		DbSchema schema = new DbSchema(spec, "schema");
		DbTable table = new DbTable(schema, "table");
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(table);
        myResourceLinkPredicateBuilder = new ResourceLinkPredicateBuilder(null, mySearchQueryBuilder, false);
		myResourceLinkPredicateBuilder.setSearchParamRegistryForUnitTest(mySearchParamRegistry);
		myResourceLinkPredicateBuilder.setIdHelperServiceForUnitTest(myIdHelperService);
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

	@Test
	void validateInvalidModifiers() {
		when(mySearchQueryBuilder.getFhirContext()).thenReturn(FhirContext.forR4Cached());
		final ReferenceParam referenceParam = new ReferenceParam(new IdDt("Observation", "123"));
		final List<IQueryParameterType> referenceOrParamList = List.of(referenceParam);
		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		final Map<String, String[]> params = new LinkedHashMap<>();
		params.put("subject:identifier", new String[]{"1"});
		params.put("subject:x", new String[]{"2"});
		params.put("subject:y", new String[]{"3"});
		params.put("patient", new String[]{"4"});
		requestDetails.setParameters(params);

		try {
			myResourceLinkPredicateBuilder.createPredicate(requestDetails, "Observation", "", Collections.emptyList(), referenceOrParamList, null, RequestPartitionId.allPartitions());
			fail();
		} catch (Exception exception) {
			assertEquals("HAPI-2498: Unsupported search modifier(s): \"[subject:identifier, subject:x, subject:y]\" for resource type \"Observation\". Valid search modifiers are: [:contains, :exact, :in, :iterate, :missing, :not-in, :of-type, :text]", exception.getMessage());
		}
	}
}
