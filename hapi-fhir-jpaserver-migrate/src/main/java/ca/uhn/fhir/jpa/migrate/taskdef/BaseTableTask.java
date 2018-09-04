package ca.uhn.fhir.jpa.migrate.taskdef;

import org.apache.commons.lang3.Validate;

public abstract class BaseTableTask<T extends BaseTableTask> extends BaseTask {
	private String myTableName;

	public String getTableName() {
		return myTableName;
	}

	public T setTableName(String theTableName) {
		myTableName = theTableName;
		return (T) this;
	}

	@Override
	public void validate() {
		Validate.notBlank(myTableName);
	}
}
