package ca.uhn.fhir.jpa.dao.mdm;

import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

public class MdmExpansionCacheSvc {
	private static final Logger ourLog = getLogger(MdmExpansionCacheSvc.class);

	private ConcurrentHashMap<String, String> mySourceToGoldenIdCache = new ConcurrentHashMap<>();

	public String getGoldenResourceId(String theSourceId) {
		ourLog.info(buildLog("About to lookup cached resource ID " + theSourceId, true));
		return mySourceToGoldenIdCache.get(theSourceId);
	}

	public String buildLog(String message, boolean theShowContent) {
		StringBuilder builder = new StringBuilder();
		builder.append(message);
		if (ourLog.isDebugEnabled() || theShowContent) {
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
