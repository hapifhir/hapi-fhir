package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.api.params.MdmQuerySearchParameters;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MdmQuerySearchParametersTest {

	public static final String GOLDEN_RESOURCE_ID = "Patient/456";
	public static final String SOURCE_ID = "Patient/123";
	public static final MdmMatchResultEnum MATCH_RESULT = MdmMatchResultEnum.MATCH;
	public static final MdmLinkSourceEnum LINK_SOURCE = MdmLinkSourceEnum.MANUAL;
	public static final List<Integer> PARTITION_ID = Collections.singletonList(1);
	public static final MdmPageRequest PAGE_REQUEST = new MdmPageRequest(0,1,1,5);
	public static final String RESOURCE_TYPE = "Patient";

	@Test
	public void testMdmQuerySearchParameters() {
		MdmQuerySearchParameters params = new MdmQuerySearchParameters(PAGE_REQUEST);
		params.setGoldenResourceId(GOLDEN_RESOURCE_ID);
		params.setSourceId(SOURCE_ID);
		params.setMatchResult(MATCH_RESULT);
		params.setLinkSource(LINK_SOURCE);
		params.setPartitionIds(PARTITION_ID);
		params.setResourceType(RESOURCE_TYPE);
		assertEquals(GOLDEN_RESOURCE_ID, params.getGoldenResourceId().getValueAsString());
		assertEquals(SOURCE_ID, params.getSourceId().getValueAsString());
		assertEquals(MATCH_RESULT, params.getMatchResult());
		assertEquals(LINK_SOURCE, params.getLinkSource());
		assertEquals(PARTITION_ID, params.getPartitionIds());
		assertEquals(PAGE_REQUEST, params.getPageRequest());
		assertEquals(RESOURCE_TYPE, params.getResourceType());
	}
}
