package ca.uhn.fhir.empi.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {
	private static final Logger ourEmpiTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.empi_troubleshooting");

	public static Logger getEmpiTroubleshootingLog() {
		return ourEmpiTroubleshootingLog;
	}
}
