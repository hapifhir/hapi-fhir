package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Migrator {

	private static final Logger ourLog = LoggerFactory.getLogger(Migrator.class);
	private DriverTypeEnum myDriverType;
	private String myConnectionUrl;
	private String myUsername;
	private String myPassword;
	private List<BaseTask> myTasks = new ArrayList<>();
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private int myChangesCount;
	private boolean myDryRun;

	public int getChangesCount() {
		return myChangesCount;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public void setConnectionUrl(String theConnectionUrl) {
		myConnectionUrl = theConnectionUrl;
	}

	public void setUsername(String theUsername) {
		myUsername = theUsername;
	}

	public void setPassword(String thePassword) {
		myPassword = thePassword;
	}

	public void addTask(BaseTask theTask) {
		myTasks.add(theTask);
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public void migrate() {
		ourLog.info("Starting migration with {} tasks", myTasks.size());

		myConnectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(myConnectionUrl, myUsername, myPassword);
		try {
			for (BaseTask next : myTasks) {
				next.setDriverType(myDriverType);
				next.setConnectionProperties(myConnectionProperties);
				next.setDryRun(myDryRun);
				try {
					next.execute();
				} catch (SQLException e) {
					throw new InternalErrorException("Failure executing task \"" + next.getDescription() + "\", aborting! Cause: " + e.toString(), e);
				}

				myChangesCount += next.getChangesCount();
			}
		} finally {
			myConnectionProperties.close();
		}

		ourLog.info("Finished migration of {} tasks", myTasks.size());
	}
}
