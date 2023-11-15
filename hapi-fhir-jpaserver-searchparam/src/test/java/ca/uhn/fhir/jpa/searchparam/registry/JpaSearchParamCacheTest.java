package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.RuntimeSearchParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JpaSearchParamCacheTest {

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
		assertEquals(4, result.size());
		assertTrue(result.containsAll(List.of(unique1, unique2, nonUnique1, nonUnique2)));
	}

	@Test
	public void testGetUniqueActiveComboParams(){
		RuntimeSearchParam unique1 = createSearchParam(ComboSearchParamType.UNIQUE);
		RuntimeSearchParam unique2 = createSearchParam(ComboSearchParamType.UNIQUE);
		RuntimeSearchParam nonUnique = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		setActiveComboSearchParams(RESOURCE_TYPE, List.of(unique1, unique2, nonUnique));

		List<RuntimeSearchParam> result = myJpaSearchParamCache.getActiveComboSearchParams(RESOURCE_TYPE, ComboSearchParamType.UNIQUE);
		assertEquals(2, result.size());
		assertTrue(result.containsAll(List.of(unique1, unique2)));
	}

	@Test
	public void testGetNonUniqueActiveComboParams(){
		RuntimeSearchParam nonUnique1 = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		RuntimeSearchParam nonUnique2 = createSearchParam(ComboSearchParamType.NON_UNIQUE);
		RuntimeSearchParam unique = createSearchParam(ComboSearchParamType.UNIQUE);
		setActiveComboSearchParams(RESOURCE_TYPE, List.of(nonUnique1, nonUnique2, unique));

		List<RuntimeSearchParam> result = myJpaSearchParamCache.getActiveComboSearchParams(RESOURCE_TYPE, ComboSearchParamType.NON_UNIQUE);
		assertEquals(2, result.size());
		assertTrue(result.containsAll(List.of(nonUnique1, nonUnique2)));
	}

	@Test
	public void testGetActiveComboParamByIdPresent(){
		IIdType id1 = new IdType(1);
		RuntimeSearchParam sp1 = createSearchParam(id1, ComboSearchParamType.NON_UNIQUE);

		IIdType id2 = new IdType(2);
		RuntimeSearchParam sp2 = createSearchParam(id2, ComboSearchParamType.NON_UNIQUE);

		setActiveComboSearchParams(RESOURCE_TYPE, List.of(sp1, sp2));

		Optional<RuntimeSearchParam> found = myJpaSearchParamCache.getActiveComboSearchParamById(RESOURCE_TYPE, id1);
		assertTrue(found.isPresent());
		assertEquals(id1, found.get().getId());
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

	private RuntimeSearchParam createSearchParam(ComboSearchParamType theType){
		return createSearchParam(null, theType);
	}

	private RuntimeSearchParam createSearchParam(IIdType theId, ComboSearchParamType theType){
		RuntimeSearchParam sp = mock(RuntimeSearchParam.class);
		when(sp.getId()).thenReturn(theId);
		when(sp.getComboSearchParamType()).thenReturn(theType);
		return sp;
	}

	private void setActiveComboSearchParams(String theResourceType, List<RuntimeSearchParam> theRuntimeSearchParams) {
		Map<String, List<RuntimeSearchParam>> activeComboParams = new HashMap<>();
		activeComboParams.put(theResourceType, theRuntimeSearchParams);
		myJpaSearchParamCache.setActiveComboSearchParams(activeComboParams);
	}

	private class TestableJpaSearchParamCache extends JpaSearchParamCache {
		public void setActiveComboSearchParams(Map<String, List<RuntimeSearchParam>> theActiveComboSearchParams){
			myActiveComboSearchParams = theActiveComboSearchParams;
		}
	}
}
