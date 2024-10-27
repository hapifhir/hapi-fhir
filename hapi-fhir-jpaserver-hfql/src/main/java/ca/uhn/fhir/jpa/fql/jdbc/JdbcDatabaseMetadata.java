/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import ca.uhn.fhir.util.VersionUtil;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

import static ca.uhn.fhir.jpa.fql.jdbc.JdbcConnection.newSqlExceptionForUnsupportedOperation;

public class JdbcDatabaseMetadata implements DatabaseMetaData {
	private final Connection myConnection;
	private final HfqlRestClient myRestClient;

	public JdbcDatabaseMetadata(Connection theConnection, HfqlRestClient theRestClient) {
		myConnection = theConnection;
		myRestClient = theRestClient;
	}

	@Override
	public boolean allProceduresAreCallable() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean allTablesAreSelectable() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getURL() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getUserName() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean nullsAreSortedHigh() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean nullsAreSortedLow() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean nullsAreSortedAtStart() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean nullsAreSortedAtEnd() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getDatabaseProductName() throws SQLException {
		return "HAPI FHIR";
	}

	@Override
	public String getDatabaseProductVersion() throws SQLException {
		return VersionUtil.getVersion();
	}

	@Override
	public String getDriverName() throws SQLException {
		return "HAPI FHIR FQL JDBC";
	}

	@Override
	public String getDriverVersion() throws SQLException {
		return VersionUtil.getVersion();
	}

	@Override
	public int getDriverMajorVersion() {
		return 1;
	}

	@Override
	public int getDriverMinorVersion() {
		return 1;
	}

	@Override
	public boolean usesLocalFiles() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean usesLocalFilePerTable() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getIdentifierQuoteString() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getSQLKeywords() throws SQLException {
		return "";
	}

	@Override
	public String getNumericFunctions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getStringFunctions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getSystemFunctions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getTimeDateFunctions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getSearchStringEscape() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getExtraNameCharacters() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsColumnAliasing() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsConvert() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsConvert(int fromType, int toType) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsTableCorrelationNames() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsOrderByUnrelated() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsGroupBy() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsGroupByUnrelated() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsLikeEscapeClause() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsMultipleResultSets() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsMultipleTransactions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsNonNullableColumns() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsANSI92FullSQL() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsOuterJoins() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsFullOuterJoins() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsLimitedOuterJoins() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getSchemaTerm() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getProcedureTerm() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getCatalogTerm() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean isCatalogAtStart() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getCatalogSeparator() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsPositionedDelete() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsPositionedUpdate() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSelectForUpdate() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsStoredProcedures() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSubqueriesInExists() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSubqueriesInIns() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsUnion() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsUnionAll() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxBinaryLiteralLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxCharLiteralLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxColumnNameLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxColumnsInGroupBy() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxColumnsInIndex() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxColumnsInOrderBy() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxColumnsInSelect() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxColumnsInTable() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxConnections() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxCursorNameLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxIndexLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxSchemaNameLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxProcedureNameLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxCatalogNameLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxRowSize() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxStatementLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxStatements() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxTableNameLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxTablesInSelect() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getMaxUserNameLength() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getDefaultTransactionIsolation() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsTransactions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getProcedureColumns(
			String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
			throws SQLException {
		Parameters input = new Parameters();
		input.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_INTROSPECT_TABLES));
		IHfqlExecutionResult outcome = myRestClient.execute(input, false, null);
		return new JdbcResultSet(outcome);
	}

	@Override
	public ResultSet getSchemas() throws SQLException {
		// Empty result set
		return new JdbcResultSet();
	}

	@Override
	public ResultSet getCatalogs() throws SQLException {
		// Empty result set
		return new JdbcResultSet();
	}

	@Override
	public ResultSet getTableTypes() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
			throws SQLException, SQLException {
		Parameters input = new Parameters();
		input.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_INTROSPECT_COLUMNS));
		IHfqlExecutionResult outcome = myRestClient.execute(input, false, null);
		return new JdbcResultSet(outcome);
	}

	@Override
	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		return new JdbcResultSet();
	}

	@Override
	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		return new JdbcResultSet();
	}

	@Override
	public ResultSet getCrossReference(
			String parentCatalog,
			String parentSchema,
			String parentTable,
			String foreignCatalog,
			String foreignSchema,
			String foreignTable)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getTypeInfo() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsResultSetType(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean ownDeletesAreVisible(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean ownInsertsAreVisible(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean othersDeletesAreVisible(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean othersInsertsAreVisible(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean updatesAreDetected(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean deletesAreDetected(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean insertsAreDetected(int type) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsBatchUpdates() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return myConnection;
	}

	@Override
	public boolean supportsSavepoints() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsNamedParameters() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsMultipleOpenResults() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsGetGeneratedKeys() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getAttributes(
			String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getDatabaseMajorVersion() throws SQLException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[0]);
	}

	@Override
	public int getDatabaseMinorVersion() throws SQLException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[1]);
	}

	@Override
	public int getJDBCMajorVersion() throws SQLException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[0]);
	}

	@Override
	public int getJDBCMinorVersion() throws SQLException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[1]);
	}

	@Override
	public int getSQLStateType() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean locatorsUpdateCopy() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsStatementPooling() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public RowIdLifetime getRowIdLifetime() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getClientInfoProperties() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getFunctionColumns(
			String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSet getPseudoColumns(
			String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean generatedKeyAlwaysReturned() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public <T> T unwrap(Class<T> theInterface) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean isWrapperFor(Class<?> theInterface) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}
}
