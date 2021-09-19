package ca.uhn.fhir.mdm.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {

	private static final Logger ourSecurityTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.security_troubleshooting");
	private static final Logger ourHttpTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.http_troubleshooting");
	private static final Logger ourQueueTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.subscription_troubleshooting");
	private static final Logger ourLiveBundleTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.livebundle_troubleshooting");
	private static final Logger ourMongoDbTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.mongodb_troubleshooting");
	private static final Logger ourChannelImportTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.channel_import_troubleshooting");
	private static final Logger ourMdmTroubleshootingLog = getMdmTroubleshootingLog();
	private static final Logger ourRealtimeExportTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.realtime_export_troubleshooting");
	private static final Logger ourConnectionPoolTroubleshootingLog = LoggerFactory.getLogger("ca.cdr.log.connection_pool_troubleshooting");

	public static Logger getHttpTroubleshootingLog() {
		return ourHttpTroubleshootingLog;
	}

	public static Logger getSubscriptionTroubleshootingLog() {
		return ourQueueTroubleshootingLog;
	}

	public static Logger getSecurityTroubleshootingLog() {
		return ourSecurityTroubleshootingLog;
	}

	public static Logger getLiveBundleTroubleshootingLog() {
		return ourLiveBundleTroubleshootingLog;
	}

	public static Logger getMongoDbTroubleshootingLog() {
		return ourMongoDbTroubleshootingLog;
	}

	public static Logger getMdmTroubleshootingLog() {
		return ourMdmTroubleshootingLog;
	}

	public static Logger getRealtimeExportTroubleshootingLog() {
		return ourRealtimeExportTroubleshootingLog;
	}

	public static Logger getChannelImportTroubleshootingLog() {
		return ourChannelImportTroubleshootingLog;
	}

	public static Logger getConnectionPoolTroubleshootingLog() {
		return ourConnectionPoolTroubleshootingLog;
	}


}
