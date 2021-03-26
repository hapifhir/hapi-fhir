package ca.uhn.fhir.jpa.dao.mdm;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * The purpose of this class is to share context between steps of a given GroupBulkExport job.
 *
 * This cache allows you to port state between reader/processor/writer. In this case, we are maintaining
 * a cache of Source Resource ID -> Golden Resource ID, so that we can annotate outgoing resources with their golden owner
 * if applicable.
 *
 */
public class MdmExpansionCacheSvc {
	private static final Logger ourLog = getLogger(MdmExpansionCacheSvc.class);

	private final ConcurrentHashMap<String, String> mySourceToGoldenIdCache = new ConcurrentHashMap<>();

	public String getGoldenResourceId(String theSourceId) {
		ourLog.debug(buildLogMessage("About to lookup cached resource ID " + theSourceId));
		String goldenResourceId = mySourceToGoldenIdCache.get(theSourceId);

		//A golden resources' golden resource ID is itself.
		if (StringUtils.isBlank(goldenResourceId)) {
			if (mySourceToGoldenIdCache.containsValue(theSourceId)) {
				goldenResourceId = theSourceId;
			}
		}
		return goldenResourceId;
	}

	public String buildLogMessage(String theMessage) {
		return buildLogMessage(theMessage, false);
	}

	/**
	 * Builds a log message, potentially enriched with the cache content.
	 *
	 * @param message The log message
	 * @param theAddCacheContentContent If true, will annotate the log message with the current cache contents.
	 * @return a built log message, which may include the cache content.
	 */
	public String buildLogMessage(String message, boolean theAddCacheContentContent) {
		StringBuilder builder = new StringBuilder();
		builder.append(message);
		if (ourLog.isDebugEnabled() || theAddCacheContentContent) {
			builder.append("\n")
				.append("Current cache content is:")
				.append("\n");
			mySourceToGoldenIdCache.entrySet().stream().forEach(entry -> builder.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n"));
			return builder.toString();
		}
		return builder.toString();

	}

	public void setCacheContents(Map<String, String> theSourceResourceIdToGoldenResourceIdMap) {
		if (mySourceToGoldenIdCache.isEmpty()) {
			this.mySourceToGoldenIdCache.putAll(theSourceResourceIdToGoldenResourceIdMap);
		}
	}

	public boolean hasBeenPopulated() {
		return !mySourceToGoldenIdCache.isEmpty();
	}
}
