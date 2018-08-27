package ca.uhn.fhir.jpa.migrate.taskdef;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CalculateHashesTask extends BaseTask {

	private String myTableName;
	private String myColumnName;

	public void setTableName(String theTableName) {
		myTableName = theTableName;
	}

	public void setColumnName(String theColumnName) {
		myColumnName = theColumnName;
	}

	@Override
	public void validate() {
		Validate.notBlank(myTableName);
		Validate.notBlank(myColumnName);
	}

	@Override
	public void execute() {
		List<Map<String, Object>> rows = getTxTemplate().execute(t->{
			JdbcTemplate jdbcTemplate = newJdbcTemnplate();
			int batchSize = 10000;
			jdbcTemplate.setMaxRows(batchSize);
			String sql = "SELECT * FROM " + myTableName + " WHERE " + myColumnName + " IS NULL";
			ourLog.info("Loading up to {} rows in {} with no hashes", batchSize, myTableName);
			return jdbcTemplate.queryForList(sql);
		});


	}


	private Map<String, Function<Map<String, Object>, Long>> myColumnMappers;

	public void addCalculator(String theColumnName, Function<Map<String, Object>, Long> theConsumer) {

	}

	private static final Logger ourLog = LoggerFactory.getLogger(CalculateHashesTask.class);
}
