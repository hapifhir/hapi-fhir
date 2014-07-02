package ca.uhn.fhir.to;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Required;

public class TesterConfig {

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
		for (String nextRaw : theServers) {
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
