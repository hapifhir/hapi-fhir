package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationInfoService;

import java.util.List;
import java.util.Optional;

public abstract class BaseMigrator {

	private boolean myDryRun;
	private boolean myNoColumnShrink;
	private DriverTypeEnum myDriverType;
	private String myConnectionUrl;
	private String myUsername;
	private String myPassword;

	public abstract void migrate();

	public boolean isDryRun() {
		return myDryRun;
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public boolean isNoColumnShrink() {
		return myNoColumnShrink;
	}

	public void setNoColumnShrink(boolean theNoColumnShrink) {
		myNoColumnShrink = theNoColumnShrink;
	}

	public abstract Optional<MigrationInfoService> getMigrationInfo();

	public abstract void addTasks(List<BaseTask<?>> theMigrationTasks);

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public String getConnectionUrl() {
		return myConnectionUrl;
	}

	public void setConnectionUrl(String theConnectionUrl) {
		myConnectionUrl = theConnectionUrl;
	}

	public String getUsername() {
		return myUsername;
	}

	public void setUsername(String theUsername) {
		myUsername = theUsername;
	}

	public String getPassword() {
		return myPassword;
	}

	public void setPassword(String thePassword) {
		myPassword = thePassword;
	}

}
