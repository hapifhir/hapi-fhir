package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AddIndexTask extends BaseTask<AddIndexTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddIndexTask.class);
	private String myTableName;
	private String myIndexName;
	private List<String> myColumns;
	private Boolean myUnique;

	public void setTableName(String theTableName) {
		myTableName = theTableName;
	}

	public void setIndexName(String theIndexName) {
		myIndexName = theIndexName;
	}

	public void setColumns(List<String> theColumns) {
		myColumns = theColumns;
	}

	public void setUnique(boolean theUnique) {
		myUnique = theUnique;
	}

	@Override
	public void validate() {
		Validate.notBlank(myIndexName, "Index name not specified");
		Validate.notBlank(myTableName, "Table name not specified");
		Validate.isTrue(myColumns.size() > 0, "Columns not specified");
		Validate.notNull(myUnique, "Uniqueness not specified");
	}

	@Override
	public void execute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), myTableName);
		if (indexNames.contains(myIndexName)) {
			ourLog.info("Index {} already exists on table {} - No action performed", myIndexName, myTableName);
			return;
		}

		String unique = myUnique ? "UNIQUE " : "";
		String columns = String.join(", ", myColumns);
		String sql = "CREATE " + unique + " INDEX " + myIndexName + " ON " + myTableName + "(" + columns + ")";
		executeSql(sql);
	}

	public void setColumns(String... theColumns) {
		setColumns(Arrays.asList(theColumns));
	}
}
