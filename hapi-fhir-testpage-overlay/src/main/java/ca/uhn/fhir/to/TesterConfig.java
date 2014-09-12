package ca.uhn.fhir.to;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Required;

public class TesterConfig {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TesterConfig.class);
	
	public static final String SYSPROP_FORCE_SERVERS = "ca.uhn.fhir.to.TesterConfig_SYSPROP_FORCE_SERVERS";

	private LinkedHashMap<String, String> myIdToServerName = new LinkedHashMap<String, String>();
	
	public LinkedHashMap<String, String> getIdToServerName() {
		return myIdToServerName;
	}

	public LinkedHashMap<String, String> getIdToServerBase() {
		return myIdToServerBase;
	}

	private LinkedHashMap<String, String> myIdToServerBase = new LinkedHashMap<String, String>();

	@Required
	public void setServers(List<String> theServers) {
		List<String> servers = theServers;

		// This is mostly for unit tests
		String force = System.getProperty(SYSPROP_FORCE_SERVERS);
		if (StringUtils.isNotBlank(force)) {
			ourLog.warn("Forcing server confirguration because of system property: {}", force);
			servers = Collections.singletonList(force);
		}
		
		
		for (String nextRaw : servers) {
			String[] nextSplit = nextRaw.split(",");
			Validate.notBlank(nextSplit[0], "theId can not be blank");
			Validate.notBlank(nextSplit[1], "theDisplayName can not be blank");
			Validate.notBlank(nextSplit[2], "theServerBase can not be blank");
			myIdToServerName.put(nextSplit[0].trim(), nextSplit[1].trim());
			myIdToServerBase.put(nextSplit[0].trim(), nextSplit[2].trim());
		}
	}

	public boolean getDebugTemplatesMode() {
		return true;
	}

}
