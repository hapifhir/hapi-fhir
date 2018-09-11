package ca.uhn.fhir.jpa.migrate.taskdef;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogStartSectionWithMessageTask extends BaseTask {
	private static final Logger ourLog = LoggerFactory.getLogger(LogStartSectionWithMessageTask.class);
	private final String myMessage;

	public LogStartSectionWithMessageTask(String theMessage) {
		myMessage = theMessage;
	}

	@Override
	public void validate() {
		// nothing
	}

	@Override
	public void execute() {
		ourLog.info("");
		ourLog.info(StringUtils.leftPad("", myMessage.length(), "*"));
		ourLog.info(myMessage);
		ourLog.info(StringUtils.leftPad("", myMessage.length(), "*"));
	}
}
