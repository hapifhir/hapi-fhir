package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MigrateTextClobToBinaryClobTask extends BaseTableColumnTask {
	private static final Logger ourLog = LoggerFactory.getLogger(MigrateTextClobToBinaryClobTask.class);
	private String myNewColumnName;
	private String myPidColumnName;

	/**
	 * Constructor
	 */
	public MigrateTextClobToBinaryClobTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	public void setPidColumnName(String thePidColumnName) {
		myPidColumnName = thePidColumnName;
	}

	public void setNewColumnName(String theNewColumnName) {
		myNewColumnName = theNewColumnName;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.isTrue(isNotBlank(myNewColumnName), "No new column name specified");
		Validate.isTrue(isNotBlank(myPidColumnName), "No pid column name specified");
		setDescription("Migrate text clob " + getColumnName() + " from table " + getTableName() + " to binary blob " + myNewColumnName);
	}

	@Override
	protected void doExecute() throws SQLException {

		Boolean foundToMigrate;
		do {
			foundToMigrate = getConnectionProperties().getTxTemplate().execute(t -> {
				JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
				List<Pair<Long, String>> nonNullClobs = jdbcTemplate.execute(new FindNonNullClobs());
				assert nonNullClobs != null;
				if (nonNullClobs.isEmpty()) {
					return Boolean.FALSE;
				}

				for (Pair<Long, String> next : nonNullClobs) {
					Long pid = next.getFirst();
					String textValue = next.getSecond();
					byte[] textValueBytes = textValue.getBytes(StandardCharsets.UTF_8);
					@Language("SQL")
					String sql = "update " +
						getTableName() +
						" set " +
						getColumnName() + " = ?, " +
						myNewColumnName + " = ? " +
						"where " + myPidColumnName + " = ?";
					jdbcTemplate.update(sql, ps -> {
						ps.setNull(1, Types.CLOB);
						ps.setBlob(2, new ByteArrayInputStream(textValueBytes));
						ps.setLong(3, pid);
					});
				}

				return Boolean.TRUE;
			});
		} while (Boolean.TRUE.equals(foundToMigrate));
	}

	private class FindNonNullClobs implements StatementCallback<List<Pair<Long, String>>> {
		@Override
		public List<Pair<Long, String>> doInStatement(Statement stmt) throws SQLException, DataAccessException {
			stmt.setMaxRows(500);
			stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
			logInfo(ourLog, "Searching for non-null values in column {} on table {}", getColumnName(), getTableName());

			String sql = "SELECT " + myPidColumnName + ", " + getColumnName() + " FROM " + getTableName() + " WHERE " + getColumnName() + " IS NOT NULL";
			boolean result = stmt.execute(sql);
			if (!result) {
				logInfo(ourLog, "None found. Task is complete.");
				return Collections.emptyList();
			}

			List<Pair<Long, String>> values = new ArrayList<>();

			ResultSet resultSet = stmt.getResultSet();
			while (resultSet.next()) {
				long pid = resultSet.getLong(1);
				Clob clob = resultSet.getClob(2);
				String clobValue;
				try (Reader characterStream = clob.getCharacterStream()) {
					clobValue = IOUtils.toString(characterStream);
				} catch (IOException e) {
					throw new InternalErrorException(e);
				}

				values.add(Pair.of(pid, clobValue));
			}

			logInfo(ourLog, "{} non-null rows found. Migrating them now.", values.size());

			return values;
		}
	}
}
