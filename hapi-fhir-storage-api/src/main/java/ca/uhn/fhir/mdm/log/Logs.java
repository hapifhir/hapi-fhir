package ca.uhn.fhir.mdm.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {
	private static final Logger ourMdmTroubleshootingLog = LoggerFactory.getLogger("ca.uhn.fhir.log.mdm_troubleshooting");

	public static Logger getMdmTroubleshootingLog() {
		return ourMdmTroubleshootingLog;
	}
}
