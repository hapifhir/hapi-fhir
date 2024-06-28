package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
import ca.uhn.fhir.rest.server.util.IndexedSearchParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ca.uhn.fhir.util.HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JpaSearchParamCacheTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private static final String RESOURCE_TYPE = "Patient";
	private TestableJpaSearchParamCache myJpaSearchParamCache;


	@BeforeEach
	public void beforeEach(){
		myJpaSearchParamCache = new TestableJpaSearchParamCache();
	}

	@Test
	public void testGetAllActiveComboParams(){
		RuntimeSearchParam unique1 = createSearchParam(ComboSearchParamType.UNIQUE);
		RuntimeSearchParam unique2 = createSearchParam(ComboSearchParamType.UNIQUE);
		RuntimeSearchParam nonUnique1 = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		RuntimeSearchParam nonUnique2 = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		setActiveComboSearchParams(RESOURCE_TYPE, List.of(unique1, unique2, nonUnique1, nonUnique2));

		List<RuntimeSearchParam> result = myJpaSearchParamCache.getActiveComboSearchParams(RESOURCE_TYPE);
		assertThat(result).hasSize(4);
		assertThat(result).containsAll(List.of(unique1, unique2, nonUnique1, nonUnique2));
	}

	@Test
	public void testGetUniqueActiveComboParams(){
		RuntimeSearchParam unique1 = createSearchParam(ComboSearchParamType.UNIQUE);
		RuntimeSearchParam unique2 = createSearchParam(ComboSearchParamType.UNIQUE);
		RuntimeSearchParam nonUnique = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		setActiveComboSearchParams(RESOURCE_TYPE, List.of(unique1, unique2, nonUnique));

		List<RuntimeSearchParam> result = myJpaSearchParamCache.getActiveComboSearchParams(RESOURCE_TYPE, ComboSearchParamType.UNIQUE);
		assertThat(result).hasSize(2);
		assertThat(result).containsAll(List.of(unique1, unique2));
	}

	@Test
	public void testGetNonUniqueActiveComboParams(){
		RuntimeSearchParam nonUnique1 = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		RuntimeSearchParam nonUnique2 = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		RuntimeSearchParam unique = createSearchParam(ComboSearchParamType.UNIQUE);
		setActiveComboSearchParams(RESOURCE_TYPE, List.of(nonUnique1, nonUnique2, unique));

		List<RuntimeSearchParam> result = myJpaSearchParamCache.getActiveComboSearchParams(RESOURCE_TYPE, ComboSearchParamType.NON_UNIQUE);
		assertThat(result).hasSize(2);
		assertThat(result).containsAll(List.of(nonUnique1, nonUnique2));
	}

	@Test
	public void testGetActiveComboParamByIdPresent(){
		IIdType id1 = new IdType("SearchParameter/1");
		RuntimeSearchParam sp1 = createSearchParam(id1, ComboSearchParamType.NON_UNIQUE);

		IIdType id2 = new IdType("SearchParameter/2");
		RuntimeSearchParam sp2 = createSearchParam(id2, ComboSearchParamType.NON_UNIQUE);

		setActiveComboSearchParams(RESOURCE_TYPE, List.of(sp1, sp2));

		Optional<RuntimeSearchParam> found = myJpaSearchParamCache.getActiveComboSearchParamById(RESOURCE_TYPE, id1);
		assertThat(found).isPresent();
		assertEquals(id1, found.get().getIdUnqualifiedVersionless());
	}

	@Test
	public void testGetActiveComboParamByIdAbsent(){
		IIdType id1 = new IdType(1);
		RuntimeSearchParam sp1 = createSearchParam(id1, ComboSearchParamType.NON_UNIQUE);

		IIdType id2 = new IdType(2);

		setActiveComboSearchParams(RESOURCE_TYPE, List.of(sp1));

		Optional<RuntimeSearchParam> found = myJpaSearchParamCache.getActiveComboSearchParamById(RESOURCE_TYPE, id2);
		assertTrue(found.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({
		"Patient,        name,                 name,                 type = string",
		"Patient,        active,               active,               type = token",
		"Patient,        active,               active:of-type,       type = token with of-type",
		"Patient,        birthdate,            birthdate,            type = date",
		"Patient,        general-practitioner, general-practitioner, type = reference",
		"Location,       near,                 near,                 type = special",
		"RiskAssessment, probability,          probability,          type = number",
		"Observation,    value-quantity,       value-quantity,       type = quantity",
		"ValueSet,       url,                  url,                  type = uri",
		"Encounter,      subject,              subject.name,         type = reference with refChain"
	})
	public void getIndexedSearchParamByHashIdentity_returnsCorrectIndexedSearchParam(String theResourceType,
																					 String theSpName,
																					 String theExpectedSpName,
																					 String theSpType) {
		// setup
		RuntimeSearchParamCache runtimeCache = new RuntimeSearchParamCache();
		RuntimeResourceDefinition resourceDefinition = ourFhirContext.getResourceDefinition(theResourceType);
		RuntimeSearchParam runtimeSearchParam = resourceDefinition.getSearchParam(theSpName);
		runtimeSearchParam.addUpliftRefchain("name", EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		runtimeCache.add(theResourceType, theSpName, resourceDefinition.getSearchParam(theSpName));
		Long hashIdentity = SearchParamHash.hashSearchParam(new PartitionSettings(), null, theResourceType, theExpectedSpName);

		// execute
		myJpaSearchParamCache.populateActiveSearchParams(null, null, runtimeCache);
		Optional<IndexedSearchParam> indexedSearchParam = myJpaSearchParamCache.getIndexedSearchParamByHashIdentity(hashIdentity);

		// validate
		assertTrue(indexedSearchParam.isPresent(), "No IndexedSearchParam found for search param with " + theSpType);
		assertEquals(theResourceType, indexedSearchParam.get().getResourceType());
		assertEquals(theExpectedSpName, indexedSearchParam.get().getParameterName());
	}

	private RuntimeSearchParam createSearchParam(ComboSearchParamType theType){
		return createSearchParam(null, theType);
	}

	private RuntimeSearchParam createSearchParam(IIdType theId, ComboSearchParamType theType){
		RuntimeSearchParam sp = mock(RuntimeSearchParam.class);
		when(sp.getIdUnqualifiedVersionless()).thenReturn(theId);
		when(sp.getComboSearchParamType()).thenReturn(theType);
		return sp;
	}

	private void setActiveComboSearchParams(String theResourceType, List<RuntimeSearchParam> theRuntimeSearchParams) {
		Map<String, List<RuntimeSearchParam>> activeComboParams = new HashMap<>();
		activeComboParams.put(theResourceType, theRuntimeSearchParams);
		myJpaSearchParamCache.setActiveComboSearchParams(activeComboParams);
	}

	private static class TestableJpaSearchParamCache extends JpaSearchParamCache {
		public void setActiveComboSearchParams(Map<String, List<RuntimeSearchParam>> theActiveComboSearchParams){
			myActiveComboSearchParams = theActiveComboSearchParams;
		}
	}
}
