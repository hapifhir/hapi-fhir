package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.engine.jdbc.env.internal.NormalizingIdentifierHelperImpl;
import org.hibernate.engine.jdbc.env.spi.ExtractedDatabaseMetaData;
import org.hibernate.engine.jdbc.env.spi.IdentifierHelper;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.jdbc.env.spi.LobCreatorBuilder;
import org.hibernate.engine.jdbc.env.spi.NameQualifierSupport;
import org.hibernate.engine.jdbc.env.spi.QualifiedObjectNameFormatter;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.jdbc.spi.TypeInfo;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.extract.spi.ExtractionContext;
import org.hibernate.tool.schema.extract.spi.SequenceInformation;
import org.hibernate.tool.schema.extract.spi.SequenceInformationExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(JdbcUtils.class);

	/**
	 * Retrieve all index names
	 */
	public static Set<String> getIndexNames(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName) throws SQLException {

		if (!getTableNames(theConnectionProperties).contains(theTableName)) {
			return Collections.emptySet();
		}

		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());
		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();

					ResultSet indexes = getIndexInfo(theTableName, connection, metadata, false);
					Set<String> indexNames = new HashSet<>();
					while (indexes.next()) {
						ourLog.debug("*** Next index: {}", new ColumnMapRowMapper().mapRow(indexes, 0));
						String indexName = indexes.getString("INDEX_NAME");
						indexNames.add(indexName);
					}

					indexes = getIndexInfo(theTableName, connection, metadata, true);
					while (indexes.next()) {
						ourLog.debug("*** Next index: {}", new ColumnMapRowMapper().mapRow(indexes, 0));
						String indexName = indexes.getString("INDEX_NAME");
						indexNames.add(indexName);
					}

					indexNames = indexNames
						.stream()
						.filter(Objects::nonNull)	// filter out the nulls first
						.map(s -> s.toUpperCase(Locale.US)) // then convert the non-null entries to upper case
						.collect(Collectors.toSet());

					return indexNames;

				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(29) + e);
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
					ResultSet indexes = getIndexInfo(theTableName, connection, metadata, false);

					while (indexes.next()) {
						String indexName = indexes.getString("INDEX_NAME");
						if (theIndexName.equalsIgnoreCase(indexName)) {
							boolean nonUnique = indexes.getBoolean("NON_UNIQUE");
							return !nonUnique;
						}
					}

				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(30) + e);
				}

				throw new InternalErrorException(Msg.code(31) + "Can't find index: " + theIndexName + " on table " + theTableName);
			});
		}
	}

	private static ResultSet getIndexInfo(String theTableName, Connection theConnection, DatabaseMetaData theMetadata, boolean theUnique) throws SQLException {
		// FYI Using approximate=false causes a very slow table scan on Oracle
		boolean approximate = true;
		return theMetadata.getIndexInfo(theConnection.getCatalog(), theConnection.getSchema(), massageIdentifier(theMetadata, theTableName), theUnique, approximate);
	}

	/**
	 * Retrieve all index names
	 */
	public static ColumnType getColumnType(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theColumnName) throws SQLException {
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

						String tableName = indexes.getString("TABLE_NAME").toUpperCase(Locale.US);
						if (!theTableName.equalsIgnoreCase(tableName)) {
							continue;
						}
						String columnName = indexes.getString("COLUMN_NAME").toUpperCase(Locale.US);
						if (!theColumnName.equalsIgnoreCase(columnName)) {
							continue;
						}

						int dataType = indexes.getInt("DATA_TYPE");
						Long length = indexes.getLong("COLUMN_SIZE");
						switch (dataType) {
							case Types.BIT:
							case Types.BOOLEAN:
								return new ColumnType(ColumnTypeEnum.BOOLEAN, length);
							case Types.VARCHAR:
								return new ColumnType(ColumnTypeEnum.STRING, length);
							case Types.NUMERIC:
							case Types.BIGINT:
							case Types.DECIMAL:
								return new ColumnType(ColumnTypeEnum.LONG, length);
							case Types.INTEGER:
								return new ColumnType(ColumnTypeEnum.INT, length);
							case Types.TIMESTAMP:
							case Types.TIMESTAMP_WITH_TIMEZONE:
								return new ColumnType(ColumnTypeEnum.DATE_TIMESTAMP, length);
							case Types.BLOB:
								return new ColumnType(ColumnTypeEnum.BLOB, length);
							case Types.LONGVARBINARY:
								if (DriverTypeEnum.MYSQL_5_7.equals(theConnectionProperties.getDriverType())) {
									//See git
									return new ColumnType(ColumnTypeEnum.BLOB, length);
								} else {
									throw new IllegalArgumentException(Msg.code(32) + "Don't know how to handle datatype " + dataType + " for column " + theColumnName + " on table " + theTableName);
								}
							case Types.VARBINARY:
								if (DriverTypeEnum.MSSQL_2012.equals(theConnectionProperties.getDriverType())) {
									// MS SQLServer seems to be mapping BLOB to VARBINARY under the covers, so we need to reverse that mapping
									return new ColumnType(ColumnTypeEnum.BLOB, length);

								} else {
									throw new IllegalArgumentException(Msg.code(33) + "Don't know how to handle datatype " + dataType + " for column " + theColumnName + " on table " + theTableName);
								}
							case Types.CLOB:
								return new ColumnType(ColumnTypeEnum.CLOB, length);
							case Types.DOUBLE:
								return new ColumnType(ColumnTypeEnum.DOUBLE, length);
							case Types.FLOAT:
								return new ColumnType(ColumnTypeEnum.FLOAT, length);
							default:
								throw new IllegalArgumentException(Msg.code(34) + "Don't know how to handle datatype " + dataType + " for column " + theColumnName + " on table " + theTableName);
						}

					}

					ourLog.debug("Unable to find column {} in table {}.", theColumnName, theTableName);
					return null;

				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(35) + e);
				}

			});
		}
	}

	/**
	 * Retrieve all index names
	 */
	public static Set<String> getForeignKeys(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, @Nullable String theForeignTable) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());

		try (Connection connection = dataSource.getConnection()) {
			TransactionTemplate txTemplate = theConnectionProperties.getTxTemplate();
			return txTemplate.execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					String catalog = connection.getCatalog();
					String schema = connection.getSchema();


					List<String> parentTables = new ArrayList<>();
					if (theTableName != null) {
						parentTables.add(massageIdentifier(metadata, theTableName));
					} else {
						// If no foreign table is specified, we'll try all of them
						parentTables.addAll(JdbcUtils.getTableNames(theConnectionProperties));
					}

					String foreignTable = massageIdentifier(metadata, theForeignTable);

					Set<String> fkNames = new HashSet<>();
					for (String nextParentTable : parentTables) {
						ResultSet indexes = metadata.getCrossReference(catalog, schema, nextParentTable, catalog, schema, foreignTable);

						while (indexes.next()) {
							String fkName = indexes.getString("FK_NAME");
							fkName = fkName.toUpperCase(Locale.US);
							fkNames.add(fkName);
						}
					}

					return fkNames;
				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(36) + e);
				}
			});
		}
	}

	/**
	 * Retrieve names of foreign keys that reference a specified foreign key column.
	 */
	public static Set<String> getForeignKeysForColumn(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theForeignKeyColumn, String theForeignTable) throws SQLException {
		DataSource dataSource = Objects.requireNonNull(theConnectionProperties.getDataSource());

		try (Connection connection = dataSource.getConnection()) {
			return theConnectionProperties.getTxTemplate().execute(t -> {
				DatabaseMetaData metadata;
				try {
					metadata = connection.getMetaData();
					String catalog = connection.getCatalog();
					String schema = connection.getSchema();


					List<String> parentTables = new ArrayList<>();
					parentTables.addAll(JdbcUtils.getTableNames(theConnectionProperties));

					String foreignTable = massageIdentifier(metadata, theForeignTable);

					Set<String> fkNames = new HashSet<>();
					for (String nextParentTable : parentTables) {
						ResultSet indexes = metadata.getCrossReference(catalog, schema, nextParentTable, catalog, schema, foreignTable);

						while (indexes.next()) {
							if (theForeignKeyColumn.equals(indexes.getString("FKCOLUMN_NAME"))) {
								String fkName = indexes.getString("FK_NAME");
								fkName = fkName.toUpperCase(Locale.US);
								fkNames.add(fkName);
							}
						}
					}

					return fkNames;
				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(37) + e);
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
						String tableName = indexes.getString("TABLE_NAME").toUpperCase(Locale.US);
						if (!theTableName.equalsIgnoreCase(tableName)) {
							continue;
						}

						String columnName = indexes.getString("COLUMN_NAME");
						columnName = columnName.toUpperCase(Locale.US);
						columnNames.add(columnName);
					}

					return columnNames;
				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(38) + e);
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
					throw new InternalErrorException(Msg.code(39) + e);
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
						tableName = tableName.toUpperCase(Locale.US);

						String tableType = tables.getString("TABLE_TYPE");
						if ("SYSTEM TABLE".equalsIgnoreCase(tableType)) {
							continue;
						}
						if (SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME.equalsIgnoreCase(tableName)) {
							continue;
						}

						columnNames.add(tableName);
					}

					return columnNames;
				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(40) + e);
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
						String tableName = tables.getString("TABLE_NAME").toUpperCase(Locale.US);
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
								throw new IllegalStateException(Msg.code(41) + "Unknown nullable: " + nullable);
							}
						}
					}

					throw new IllegalStateException(Msg.code(42) + "Did not find column " + theColumnName);
				} catch (SQLException e) {
					throw new InternalErrorException(Msg.code(43) + e);
				}
			});
		}
	}

	private static String massageIdentifier(DatabaseMetaData theMetadata, String theCatalog) throws SQLException {
		String retVal = theCatalog;
		if (theCatalog == null) {
			return null;
		} else if (theMetadata.storesLowerCaseIdentifiers()) {
			retVal = retVal.toLowerCase();
		} else {
			retVal = retVal.toUpperCase();
		}
		return retVal;
	}

	public static class ColumnType {
		private final ColumnTypeEnum myColumnTypeEnum;
		private final Long myLength;

		public ColumnType(ColumnTypeEnum theColumnType, Long theLength) {
			myColumnTypeEnum = theColumnType;
			myLength = theLength;
		}

		public ColumnType(ColumnTypeEnum theColumnType, int theLength) {
			this(theColumnType, (long) theLength);
		}

		public ColumnType(ColumnTypeEnum theColumnType) {
			this(theColumnType, null);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}

			if (theO == null || getClass() != theO.getClass()) {
				return false;
			}

			ColumnType that = (ColumnType) theO;

			return new EqualsBuilder()
				.append(myColumnTypeEnum, that.myColumnTypeEnum)
				.append(myLength, that.myLength)
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(myColumnTypeEnum)
				.append(myLength)
				.toHashCode();
		}

		@Override
		public String toString() {
			ToStringBuilder b = new ToStringBuilder(this);
			b.append("type", myColumnTypeEnum);
			if (myLength != null) {
				b.append("length", myLength);
			}
			return b.toString();
		}

		public ColumnTypeEnum getColumnTypeEnum() {
			return myColumnTypeEnum;
		}

		public Long getLength() {
			return myLength;
		}

		public boolean equals(ColumnTypeEnum theTaskColumnType, Long theTaskColumnLength) {
			ourLog.debug("Comparing existing {} {} to new {} {}", myColumnTypeEnum, myLength, theTaskColumnType, theTaskColumnLength);
			return myColumnTypeEnum == theTaskColumnType && (theTaskColumnLength == null || theTaskColumnLength.equals(myLength));
		}
	}
}
