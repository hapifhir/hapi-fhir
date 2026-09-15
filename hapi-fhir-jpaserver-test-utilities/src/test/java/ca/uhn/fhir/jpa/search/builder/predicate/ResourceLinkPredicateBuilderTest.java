package ca.uhn.fhir.jpa.search.builder.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SqlObjectFactory;
import ca.uhn.fhir.jpa.search.builder.sql.TuplePredicateBuilder;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceLinkPredicateBuilderTest {

	private static final String PLACEHOLDER_BASE = UUID.randomUUID().toString();

	private ResourceLinkPredicateBuilder myResourceLinkPredicateBuilder;
	private PartitionSettings myPartitionSettings = new PartitionSettings();

	@Mock
	private SearchQueryBuilder mySearchQueryBuilder;

	@Mock
	private ISearchParamRegistry mySearchParamRegistry;

	@Mock
	private IIdHelperService<?> myIdHelperService;

	@Mock
	private SqlObjectFactory mySqlObjectFactory;

	@BeforeEach
	public void init() {
		DbSpec spec = new DbSpec();
		DbSchema schema = new DbSchema(spec, "schema");
		DbTable table = new DbTable(schema, "table");
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(table);
		when(mySearchQueryBuilder.getPartitionSettings()).thenReturn(myPartitionSettings);
		lenient().when(mySearchQueryBuilder.getTuplePredicateBuilder()).thenReturn(new TuplePredicateBuilder(mySearchQueryBuilder));
		myResourceLinkPredicateBuilder = new ResourceLinkPredicateBuilder(null, mySearchQueryBuilder);
		myResourceLinkPredicateBuilder.setSearchParamRegistryForUnitTest(mySearchParamRegistry);
		myResourceLinkPredicateBuilder.setIdHelperServiceForUnitTest(myIdHelperService);
	}

	@Test
	public void createEverythingPredicate_withListOfPids_returnsInPredicate() {
		when(myResourceLinkPredicateBuilder.generatePlaceholders(anyCollection())).thenReturn(List.of(PLACEHOLDER_BASE + "1", PLACEHOLDER_BASE + "2"));
		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient", new ArrayList<>(), JpaPid.fromId(1L), JpaPid.fromId(2L));
		assertEquals(InCondition.class, condition.getClass());
	}

	@Test
	public void createEverythingPredicate_withSinglePid_returnsInCondition() {
		when(myResourceLinkPredicateBuilder.generatePlaceholders(anyCollection())).thenReturn(List.of(PLACEHOLDER_BASE + "1"));
		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient", new ArrayList<>(), JpaPid.fromId(1L));
		assertEquals(BinaryCondition.class, condition.getClass());
	}

	@Test
	public void createEverythingPredicate_withNoPids_returnsBinaryCondition() {
		when(mySearchQueryBuilder.generatePlaceholder(nullable(Object.class))).thenReturn("A");

		Condition condition = myResourceLinkPredicateBuilder.createEverythingPredicate("Patient", new ArrayList<>(), new JpaPid[0]);
		assertEquals(BinaryCondition.class, condition.getClass());
	}

	@Test
	void validateInvalidModifiers() {
		when(mySearchQueryBuilder.getFhirContext()).thenReturn(FhirContext.forR4Cached());
		final ReferenceParam referenceParam = new ReferenceParam(new IdDt(":identifier", "123"));
		final List<IQueryParameterType> referenceOrParamList = List.of(referenceParam);
		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		final Map<String, String[]> params = new LinkedHashMap<>();
		params.put("subject:identifier", new String[]{"1"});
		params.put("subject:x", new String[]{"2"});
		params.put("subject:y", new String[]{"3"});
		params.put("patient", new String[]{"4"});
		requestDetails.setParameters(params);

		assertThatThrownBy(() ->
			myResourceLinkPredicateBuilder.createPredicate(requestDetails, "Observation", null, null, Collections.emptyList(), referenceOrParamList, null, RequestPartitionId.allPartitions()))
			.isInstanceOf(Exception.class)
			.hasMessage("HAPI-2498: Unsupported search modifier(s): \"[:identifier, :x, :y]\" for resource type \"Observation\". Valid search modifiers are: [:contains, :exact, :in, :iterate, :missing, :not-in, :of-type, :recurse, :text]");

	}

	@Test
	public void createResourceLinkPaths_withoutChainAndSearchParameterFoundNoQualifiers_returnsFilteredPaths() {
		String paramName = "param.name";
		String resourceType = "Bundle";
		RuntimeSearchParam mockSearchParam = mock(RuntimeSearchParam.class);
		when(mockSearchParam.getPathsSplit()).thenReturn(List.of("Patient.given", "Bundle.composition.subject", "Bundle.type"));
		when(mySearchParamRegistry.getActiveSearchParam(eq(resourceType), eq(paramName), any())).thenReturn(mockSearchParam);
		List<String> result = myResourceLinkPredicateBuilder.createResourceLinkPaths(resourceType, paramName, List.of());
		assertThat(result).containsExactlyInAnyOrder("Bundle.composition.subject", "Bundle.type");
	}

	@Test
	public void createResourceLinkPaths_withoutChainAndSearchParameterNotFoundNoQualifiers_returnsEmpty() {
		String paramName = "param.name";
		String resourceType = "Bundle";
		List<String> result = myResourceLinkPredicateBuilder.createResourceLinkPaths(resourceType, paramName, List.of());
		assertThat(result).isEmpty();
	}

	@Test
	public void createResourceLinkPaths_withChainAndSearchParameterFoundNoQualifiers_returnsPath() {
		String paramName = "subject.identifier";
		String resourceType = "Observation";
		when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("subject.identifier"), any())).thenReturn(null);
		RuntimeSearchParam observationSubjectSP = mock(RuntimeSearchParam.class);
		when(observationSubjectSP.getPathsSplit()).thenReturn(List.of("Observation.subject"));
		when(observationSubjectSP.getTargets()).thenReturn(Set.of("Patient"));
		when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("subject"), any())).thenReturn(observationSubjectSP);
		RuntimeSearchParam patientIdentifierSP = mock(RuntimeSearchParam.class);
		when(patientIdentifierSP.getPathsSplit()).thenReturn(List.of("Patient.identifier"));
		when(mySearchParamRegistry.getActiveSearchParam(eq("Patient"),eq( "identifier"), any())).thenReturn(patientIdentifierSP);
		List<String> result = myResourceLinkPredicateBuilder.createResourceLinkPaths(resourceType, paramName, List.of());
		assertThat(result).containsExactlyInAnyOrder("Observation.subject.identifier");
	}

	@Test
	public void createResourceLinkPaths_withChainAndSearchParameterFoundWithQualifiers_returnsPath() {
		String paramName = "subject.managingOrganization.identifier";
		String resourceType = "Observation";

		when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("subject.managingOrganization.identifier"), any())).thenReturn(null);

		RuntimeSearchParam observationSubjectSP = mock(RuntimeSearchParam.class);
		when(observationSubjectSP.getPathsSplit()).thenReturn(List.of("Observation.subject"));
		when(observationSubjectSP.getTargets()).thenReturn(Set.of("Patient"));
		when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("subject"), any())).thenReturn(observationSubjectSP);

		when(mySearchParamRegistry.getActiveSearchParam(eq("Patient"), eq("managingOrganization.identifier"), any())).thenReturn(null);

		RuntimeSearchParam organizationSP = mock(RuntimeSearchParam.class);
		when(organizationSP.getPathsSplit()).thenReturn(List.of("Patient.managingOrganization"));
		when(organizationSP.getTargets()).thenReturn(Set.of("Organization"));
		when(mySearchParamRegistry.getActiveSearchParam(eq("Patient"), eq("managingOrganization"), any())).thenReturn(organizationSP);

		RuntimeSearchParam organizationIdentifierSP = mock(RuntimeSearchParam.class);
		when(organizationIdentifierSP.getPathsSplit()).thenReturn(List.of("Organization.identifier"));
		when(mySearchParamRegistry.getActiveSearchParam(eq("Organization"), eq("identifier"), any())).thenReturn(organizationIdentifierSP);

		List<String> result = myResourceLinkPredicateBuilder.createResourceLinkPaths(resourceType, paramName, List.of("Patient", "Organization"));
		assertThat(result).containsExactlyInAnyOrder("Observation.subject.managingOrganization.identifier");
	}

	@Test
	public void createResourceLinkPaths_withChainAndSearchParameterFoundWithNonMatchingQualifier_returnsEmpty() {
		String paramName = "subject.identifier";
		String resourceType = "Observation";
		when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("subject.identifier"), any())).thenReturn(null);
		RuntimeSearchParam observationSubjectSP = mock(RuntimeSearchParam.class);
		when(observationSubjectSP.getPathsSplit()).thenReturn(List.of("Observation.subject"));
		when(observationSubjectSP.getTargets()).thenReturn(Set.of("Patient"));
		when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("subject"), any())).thenReturn(observationSubjectSP);
		List<String> result = myResourceLinkPredicateBuilder.createResourceLinkPaths(resourceType, paramName, List.of("Group"));
		assertThat(result).isEmpty();
	}

	// --- GL-9268: the reference site binds large target ID lists as a single JSON array ---

	/**
	 * A16: exercises the large-ID-list threshold boundary at the reference site - above the threshold
	 * TARGET_RESOURCE_ID is constrained by the JSON unpacking subselect rather than by one bind variable
	 * per target ID, at or under the threshold the reference site keeps rendering today's IN list, and a
	 * single target ID still collapses to an equality predicate. Threshold is 3 in every row.
	 */
	@ParameterizedTest(name = "targetIdCount={0}")
	@CsvSource({
		"5, 'TARGET_RESOURCE_ID IN (SELECT', true",
		"3, 'TARGET_RESOURCE_ID IN (', false",
		"1, 'TARGET_RESOURCE_ID = ', false"
	})
	void createPredicateReference_targetIdCountAcrossThreshold_rendersExpectedPredicate(int theTargetIdCount, String theExpectedFragment, boolean theExpectJson) {
		ResourceLinkPredicateBuilder builder = createBuilderOnRealSearchQueryBuilder(3);

		Condition condition = builder.createPredicateReference(false, List.of("Observation.subject"), toTargetPids(theTargetIdCount), List.of());

		assertThat(condition.toString()).contains(theExpectedFragment);
		if (theExpectJson) {
			assertThat(condition.toString()).contains("jsonb_array_elements_text");
		} else {
			assertThat(condition.toString()).doesNotContain("jsonb_array_elements_text");
		}
	}

	private ResourceLinkPredicateBuilder createBuilderOnRealSearchQueryBuilder(int theLargeIdListJsonThreshold) {
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setLargeIdListJsonThreshold(theLargeIdListJsonThreshold);

		HibernatePropertiesProvider dialectProvider = new HibernatePropertiesProvider();
		dialectProvider.setDialectForUnitTest(new HapiFhirPostgresDialect());

		SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder(
			FhirContext.forR4Cached(), storageSettings, new PartitionSettings(), RequestPartitionId.allPartitions(),
			"Observation", mySqlObjectFactory, dialectProvider, false, false);

		return new ResourceLinkPredicateBuilder(null, searchQueryBuilder);
	}

	private static List<Long> toTargetPids(int theCount) {
		return LongStream.rangeClosed(1, theCount).boxed().collect(Collectors.toList());
	}
}
