package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTableColumnTypeTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.engine.jdbc.env.internal.NormalizingIdentifierHelperImpl;
import org.hibernate.engine.jdbc.env.spi.*;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.jdbc.spi.TypeInfo;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.extract.spi.ExtractionContext;
import org.hibernate.tool.schema.extract.spi.SequenceInformation;
import org.hibernate.tool.schema.extract.spi.SequenceInformationExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static org.thymeleaf.util.StringUtils.toUpperCase;

public class JdbcUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(JdbcUtils.class);

	/**
	 * Retrieve all index names
	 */
	public static Set<String> getIndexNames(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName) throws SQLException {

		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					ResultSet indexes = metadata.getIndexInfo(connection.getCatalog(), connection.getSchema(), massageIdentifier(metadata, theTableName), false, true);

					Set<String> indexNames = new HashSet<>();
					while (indexes.next()) {

						ourLog.debug("*** Next index: {}", new ColumnMapRowMapper().mapRow(indexes, 0));

						String indexName = indexes.getString("INDEX_NAME");
						indexName = toUpperCase(indexName, Locale.US);
						indexNames.add(indexName);
					}

					return indexNames;
				} catch (SQLException e) {
					throw new InternalErrorException(e);
				}
			});
		}
	}

	@SuppressWarnings("ConstantConditions")
	public static boolean isIndexUnique(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theIndexName) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					ResultSet indexes = metadata.getIndexInfo(connection.getCatalog(), connection.getSchema(), massageIdentifier(metadata, theTableName), false, false);

					while (indexes.next()) {
						String indexName = indexes.getString("INDEX_NAME");
						if (theIndexName.equalsIgnoreCase(indexName)) {
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
					String catalog = connection.getCatalog();
					String schema = connection.getSchema();
					ResultSet indexes = metadata.getColumns(catalog, schema, massageIdentifier(metadata, theTableName), null);

					while (indexes.next()) {

						String tableName = toUpperCase(indexes.getString("TABLE_NAME"), Locale.US);
						if (!theTableName.equalsIgnoreCase(tableName)) {
							continue;
						}
						String columnName = toUpperCase(indexes.getString("COLUMN_NAME"), Locale.US);
						if (!theColumnName.equalsIgnoreCase(columnName)) {
							continue;
						}

						int dataType = indexes.getInt("DATA_TYPE");
						Long length = indexes.getLong("COLUMN_SIZE");
						switch (dataType) {
							case Types.VARCHAR:
								return BaseTableColumnTypeTask.ColumnTypeEnum.STRING.getDescriptor(length);
							case Types.NUMERIC:
							case Types.BIGINT:
							case Types.DECIMAL:
								return BaseTableColumnTypeTask.ColumnTypeEnum.LONG.getDescriptor(null);
							case Types.INTEGER:
								return BaseTableColumnTypeTask.ColumnTypeEnum.INT.getDescriptor(null);
							case Types.TIMESTAMP:
							case Types.TIMESTAMP_WITH_TIMEZONE:
								return BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP.getDescriptor(null);
							default:
								throw new IllegalArgumentException("Don't know how to handle datatype " + dataType + " for column " + theColumnName + " on table " + theTableName);
						}

					}

					return null;

				} catch (SQLException e) {
					throw new InternalErrorException(e);
				}

			});
		}
	}

	/**
	 * Retrieve all index names
	 */
	public static Set<String> getForeignKeys(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theForeignTable) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					ResultSet indexes = metadata.getCrossReference(connection.getCatalog(), connection.getSchema(), massageIdentifier(metadata, theTableName), connection.getCatalog(), connection.getSchema(), massageIdentifier(metadata, theForeignTable));

					Set<String> columnNames = new HashSet<>();
					while (indexes.next()) {
						String tableName = toUpperCase(indexes.getString("PKTABLE_NAME"), Locale.US);
						if (!theTableName.equalsIgnoreCase(tableName)) {
							continue;
						}
						tableName = toUpperCase(indexes.getString("FKTABLE_NAME"), Locale.US);
						if (!theForeignTable.equalsIgnoreCase(tableName)) {
							continue;
						}

						String fkName = indexes.getString("FK_NAME");
						fkName = toUpperCase(fkName, Locale.US);
						columnNames.add(fkName);
					}

					return columnNames;
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
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					ResultSet indexes = metadata.getColumns(connection.getCatalog(), connection.getSchema(), massageIdentifier(metadata, theTableName), null);

					Set<String> columnNames = new HashSet<>();
					while (indexes.next()) {
						String tableName = toUpperCase(indexes.getString("TABLE_NAME"), Locale.US);
						if (!theTableName.equalsIgnoreCase(tableName)) {
							continue;
						}

						String columnName = indexes.getString("COLUMN_NAME");
						columnName = toUpperCase(columnName, Locale.US);
						columnNames.add(columnName);
					}

					return columnNames;
				} catch (SQLException e) {
					throw new InternalErrorException(e);
				}
			});
		}
	}

	public static Set<String> getSequenceNames(DriverTypeEnum.ConnectionProperties theConnectionProperties) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				try {
					DialectResolver dialectResolver = new StandardDialectResolver();
					Dialect dialect = dialectResolver.resolveDialect(new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData()));

					Set<String> sequenceNames = new HashSet<>();
					if (dialect.supportsSequences()) {

						// Use Hibernate to get a list of current sequences
						SequenceInformationExtractor sequenceInformationExtractor = dialect.getSequenceInformationExtractor();
						ExtractionContext extractionContext = new ExtractionContext.EmptyExtractionContext() {
							@Override
							public Connection getJdbcConnection() {
								return connection;
							}

							@Override
							public ServiceRegistry getServiceRegistry() {
								return super.getServiceRegistry();
							}

							@Override
							public JdbcEnvironment getJdbcEnvironment() {
								return new JdbcEnvironment() {
									@Override
									public Dialect getDialect() {
										return dialect;
									}

									@Override
									public ExtractedDatabaseMetaData getExtractedDatabaseMetaData() {
										return null;
									}

									@Override
									public Identifier getCurrentCatalog() {
										return null;
									}

									@Override
									public Identifier getCurrentSchema() {
										return null;
									}

									@Override
									public QualifiedObjectNameFormatter getQualifiedObjectNameFormatter() {
										return null;
									}

									@Override
									public IdentifierHelper getIdentifierHelper() {
										return new NormalizingIdentifierHelperImpl(this, null, true, true, true, null, null, null);
									}

									@Override
									public NameQualifierSupport getNameQualifierSupport() {
										return null;
									}

									@Override
									public SqlExceptionHelper getSqlExceptionHelper() {
										return null;
									}

									@Override
									public LobCreatorBuilder getLobCreatorBuilder() {
										return null;
									}

									@Override
									public TypeInfo getTypeInfoForJdbcCode(int jdbcTypeCode) {
										return null;
									}
								};
							}
						};
						Iterable<SequenceInformation> sequences = sequenceInformationExtractor.extractMetadata(extractionContext);
						for (SequenceInformation next : sequences) {
							sequenceNames.add(next.getSequenceName().getSequenceName().getText());
						}

					}
					return sequenceNames;
				} catch (SQLException e) {
					throw new InternalErrorException(e);
				}
			});
		}
	}

	public static Set<String> getTableNames(DriverTypeEnum.ConnectionProperties theConnectionProperties) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					ResultSet tables = metadata.getTables(connection.getCatalog(), connection.getSchema(), null, null);

					Set<String> columnNames = new HashSet<>();
					while (tables.next()) {
						String tableName = tables.getString("TABLE_NAME");
						tableName = toUpperCase(tableName, Locale.US);

						String tableType = tables.getString("TABLE_TYPE");
						if ("SYSTEM TABLE".equalsIgnoreCase(tableType)) {
							continue;
						}

						columnNames.add(tableName);
					}

					return columnNames;
				} catch (SQLException e) {
					throw new InternalErrorException(e);
				}
			});
		}
	}

	public static boolean isColumnNullable(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theColumnName) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			//noinspection ConstantConditions
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					ResultSet tables = metadata.getColumns(connection.getCatalog(), connection.getSchema(), massageIdentifier(metadata, theTableName), null);

					while (tables.next()) {
						String tableName = toUpperCase(tables.getString("TABLE_NAME"), Locale.US);
						if (!theTableName.equalsIgnoreCase(tableName)) {
							continue;
						}

						if (theColumnName.equalsIgnoreCase(tables.getString("COLUMN_NAME"))) {
							String nullable = tables.getString("IS_NULLABLE");
							if ("YES".equalsIgnoreCase(nullable)) {
								return true;
							} else if ("NO".equalsIgnoreCase(nullable)) {
								return false;
							} else {
								throw new IllegalStateException("Unknown nullable: " + nullable);
							}
						}
					}

					throw new IllegalStateException("Did not find column " + theColumnName);
				} catch (SQLException e) {
					throw new InternalErrorException(e);
				}
			});
		}
	}

	private static String massageIdentifier(DatabaseMetaData theMetadata, String theCatalog) throws SQLException {
		String retVal = theCatalog;
		if (theMetadata.storesLowerCaseIdentifiers()) {
			retVal = retVal.toLowerCase();
		} else {
			retVal = retVal.toUpperCase();
		}
		return retVal;
	}
}
