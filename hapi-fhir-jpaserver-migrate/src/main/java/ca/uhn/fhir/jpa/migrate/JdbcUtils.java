package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class JdbcUtils {
	/**
	 * Retrieve all index names
	 */
	public static Set<String> getIndexNames(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		Connection connection = dataSource.getConnection();
		return theConnectionProperties.getTxTemplate().execute(t -> {
			DatabaseMetaData metadata = null;
			try {
				metadata = connection.getMetaData();
				ResultSet indexes = metadata.getIndexInfo(null, null, theTableName, false, false);

				Set<String> indexNames = new HashSet<>();
				while (indexes.next()) {
					String indexName = indexes.getString("INDEX_NAME");
					indexNames.add(indexName);
				}

				return indexNames;
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}
		});

	}
}
