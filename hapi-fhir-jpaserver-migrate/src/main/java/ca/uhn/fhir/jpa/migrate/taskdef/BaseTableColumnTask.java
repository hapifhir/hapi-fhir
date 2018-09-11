package ca.uhn.fhir.jpa.migrate.taskdef;

import org.apache.commons.lang3.Validate;
import org.thymeleaf.util.StringUtils;

import java.util.Locale;

public abstract class BaseTableColumnTask<T extends BaseTableTask> extends BaseTableTask<T> {

	private String myColumnName;

	@SuppressWarnings("unchecked")
	public T setColumnName(String theColumnName) {
		myColumnName = StringUtils.toUpperCase(theColumnName, Locale.US);
		return (T) this;
	}


	public String getColumnName() {
		return myColumnName;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myColumnName, "Column name not specified");
	}


}
