package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayOfRawJsonObjectsAsStringsDeserializerTest {

	@Test
	void testBulkModifyResourcesResultsJson() {
		BulkModifyResourcesResultsJson outcome = new BulkModifyResourcesResultsJson();
		outcome.setResourcesChangedBodies(List.of(
			"{\"resourceType\":\"Patient\",\"id\":\"123\"}",
		"{\"resourceType\":\"Patient\",\"id\":\"456\"}"));

		String actual = JsonUtil.serialize(outcome, true);
		String expected = """
			{
			  "resourcesChanged" : [{"resourceType":"Patient","id":"123"}, {"resourceType":"Patient","id":"456"}]
			}""";
		assertEquals(expected, actual);

		BulkModifyResourcesResultsJson decoded = JsonUtil.deserialize(actual, BulkModifyResourcesResultsJson.class);
		assertEquals("{\"resourceType\":\"Patient\",\"id\":\"123\"}", decoded.getResourcesChangedBodies().get(0));
		assertEquals("{\"resourceType\":\"Patient\",\"id\":\"456\"}", decoded.getResourcesChangedBodies().get(1));
	}

	@Test
	void testBulkModifyResourcesChunkOutcomeJson() {
		BulkModifyResourcesChunkOutcomeJson outcome = new BulkModifyResourcesChunkOutcomeJson();
		outcome.addChangedResourceBody("{\"resourceType\":\"Patient\",\"id\":\"123\"}");
		outcome.addChangedResourceBody("{\"resourceType\":\"Patient\",\"id\":\"456\"}");

		String actual = JsonUtil.serialize(outcome, true);
		String expected = """
			{
			  "changedResourceBodies" : [{"resourceType":"Patient","id":"123"}, {"resourceType":"Patient","id":"456"}]
			}""";
		assertEquals(expected, actual);

		BulkModifyResourcesChunkOutcomeJson decoded = JsonUtil.deserialize(actual, BulkModifyResourcesChunkOutcomeJson.class);
		assertEquals("{\"resourceType\":\"Patient\",\"id\":\"123\"}", decoded.getChangedResourceBodies().get(0));
		assertEquals("{\"resourceType\":\"Patient\",\"id\":\"456\"}", decoded.getChangedResourceBodies().get(1));
	}

}
