package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTableColumnTypeTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.thymeleaf.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class JdbcUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(JdbcUtils.class);

	/**
	 * Retrieve all index names
	 */
	public static Set<String> getIndexNames(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		Connection connection = dataSource.getConnection();
		return theConnectionProperties.getTxTemplate().execute(t -> {
			DatabaseMetaData metadata;
			try {
				metadata = connection.getMetaData();
				ResultSet indexes = metadata.getIndexInfo(null, null, theTableName, false, false);

				Set<String> indexNames = new HashSet<>();
				while (indexes.next()) {

					ourLog.debug("*** Next index: {}", new ColumnMapRowMapper().mapRow(indexes, 0));

					String indexName = indexes.getString("INDEX_NAME");
					indexName = StringUtils.toUpperCase(indexName, Locale.US);
					indexNames.add(indexName);
				}

				return indexNames;
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}
		});

	}

	@SuppressWarnings("ConstantConditions")
	public static boolean isIndexUnique(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theIndexName) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		Connection connection = dataSource.getConnection();
		return theConnectionProperties.getTxTemplate().execute(t -> {
			DatabaseMetaData metadata;
			try {
				metadata = connection.getMetaData();
				ResultSet indexes = metadata.getIndexInfo(null, null, theTableName, false, false);

				while (indexes.next()) {
					String indexName = indexes.getString("INDEX_NAME");
					if (indexName.equalsIgnoreCase(theIndexName)) {
						boolean nonUnique = indexes.getBoolean("NON_UNIQUE");
						return !nonUnique;
					}
				}

			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}

			throw new InternalErrorException("Can't find index: " + theIndexName + " on table " + theTableName);
		});

	}

	/**
	 * Retrieve all index names
	 */
	public static String getColumnType(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theColumnName) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					ResultSet indexes = metadata.getColumns(null, null, theTableName, theColumnName);

					indexes.next();

					int dataType = indexes.getInt("DATA_TYPE");
					Long length = indexes.getLong("COLUMN_SIZE");
					switch (dataType) {
						case Types.VARCHAR:
							return BaseTableColumnTypeTask.ColumnTypeEnum.STRING.getDescriptor(length);
						case Types.BIGINT:
							return BaseTableColumnTypeTask.ColumnTypeEnum.LONG.getDescriptor(length);
						default:
							throw new IllegalArgumentException("Don't know how to handle datatype: " + dataType);
					}

				} catch (SQLException e) {
					throw new InternalErrorException(e);
				}

			});
		}
	}

	/**
	 * Retrieve all index names
	 */
	public static Set<String> getColumnNames(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		Connection connection = dataSource.getConnection();
		return theConnectionProperties.getTxTemplate().execute(t -> {
			DatabaseMetaData metadata;
			try {
				metadata = connection.getMetaData();
				ResultSet indexes = metadata.getColumns(null, null, theTableName, null);

				Set<String> columnNames = new HashSet<>();
				while (indexes.next()) {
					String columnName = indexes.getString("COLUMN_NAME");
					columnName = StringUtils.toUpperCase(columnName, Locale.US);
					columnNames.add(columnName);
				}

				return columnNames;
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}
		});

	}

}
