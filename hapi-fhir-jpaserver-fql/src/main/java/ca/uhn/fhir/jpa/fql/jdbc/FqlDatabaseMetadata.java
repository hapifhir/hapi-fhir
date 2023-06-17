/*-
 * #%L
 * HAPI FHIR JPA Server - Firely Query Language
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.fql.executor.IFqlExecutionResult;
import ca.uhn.fhir.jpa.fql.executor.StaticFqlExecutionResult;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.util.VersionUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.util.List;

public class FqlDatabaseMetadata implements DatabaseMetaData {
	private final Connection myConnection;
	private final FqlRestClient myRestClient;

	public FqlDatabaseMetadata(Connection theConnection, FqlRestClient theRestClient) {
		myConnection = theConnection;
		myRestClient = theRestClient;
	}

	@Override
	public boolean allProceduresAreCallable() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean allTablesAreSelectable() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getURL() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUserName() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean nullsAreSortedHigh() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean nullsAreSortedLow() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean nullsAreSortedAtStart() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean nullsAreSortedAtEnd() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDatabaseProductName() throws UnsupportedOperationException {
		return "HAPI FHIR";
	}

	@Override
	public String getDatabaseProductVersion() throws UnsupportedOperationException {
		return VersionUtil.getVersion();
	}

	@Override
	public String getDriverName() throws UnsupportedOperationException {
		return "HAPI FHIR FQL JDBC";
	}

	@Override
	public String getDriverVersion() throws UnsupportedOperationException {
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
	public boolean usesLocalFiles() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean usesLocalFilePerTable() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean storesUpperCaseIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean storesLowerCaseIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean storesMixedCaseIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getIdentifierQuoteString() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSQLKeywords() throws UnsupportedOperationException {
		return "";
	}

	@Override
	public String getNumericFunctions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getStringFunctions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSystemFunctions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTimeDateFunctions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSearchStringEscape() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getExtraNameCharacters() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsColumnAliasing() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsConvert() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsConvert(int fromType, int toType) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsTableCorrelationNames() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsExpressionsInOrderBy() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsOrderByUnrelated() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsGroupBy() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsGroupByUnrelated() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsGroupByBeyondSelect() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsLikeEscapeClause() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsMultipleResultSets() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsMultipleTransactions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsNonNullableColumns() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsMinimumSQLGrammar() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsExtendedSQLGrammar() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsANSI92IntermediateSQL() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsANSI92FullSQL() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsOuterJoins() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsFullOuterJoins() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsLimitedOuterJoins() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSchemaTerm() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProcedureTerm() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCatalogTerm() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCatalogAtStart() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCatalogSeparator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSchemasInDataManipulation() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsPositionedDelete() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsPositionedUpdate() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSelectForUpdate() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsStoredProcedures() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSubqueriesInComparisons() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSubqueriesInExists() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSubqueriesInIns() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsSubqueriesInQuantifieds() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsCorrelatedSubqueries() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsUnion() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsUnionAll() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxBinaryLiteralLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxCharLiteralLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxColumnNameLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxColumnsInGroupBy() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxColumnsInIndex() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxColumnsInOrderBy() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxColumnsInSelect() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxColumnsInTable() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxConnections() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxCursorNameLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxIndexLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxSchemaNameLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxProcedureNameLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxCatalogNameLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxRowSize() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxStatementLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxStatements() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxTableNameLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxTablesInSelect() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxUserNameLength() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getDefaultTransactionIsolation() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsTransactions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsTransactionIsolationLevel(int level) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
		Parameters input = new Parameters();
		input.addParameter(FqlRestProvider.PARAM_ACTION, new CodeType(FqlRestProvider.PARAM_ACTION_INTROSPECT_TABLES));
		IFqlExecutionResult outcome = myRestClient.execute(input, false);
		return new FqlResultSet(outcome);
	}

	@Override
	public ResultSet getSchemas() throws UnsupportedOperationException {
		// Empty result set
		return new FqlResultSet();
	}

	@Override
	public ResultSet getCatalogs() throws UnsupportedOperationException {
		// Empty result set
		return new FqlResultSet();
	}

	@Override
	public ResultSet getTableTypes() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws UnsupportedOperationException, SQLException {
		Parameters input = new Parameters();
		input.addParameter(FqlRestProvider.PARAM_ACTION, new CodeType(FqlRestProvider.PARAM_ACTION_INTROSPECT_COLUMNS));
		IFqlExecutionResult outcome = myRestClient.execute(input, false);
		return new FqlResultSet(outcome);
	}

	@Override
	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getVersionColumns(String catalog, String schema, String table) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getImportedKeys(String catalog, String schema, String table) throws UnsupportedOperationException {
		return new FqlResultSet();
	}

	@Override
	public ResultSet getExportedKeys(String catalog, String schema, String table) throws UnsupportedOperationException {
		return new FqlResultSet();
	}

	@Override
	public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getTypeInfo() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsResultSetType(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsResultSetConcurrency(int type, int concurrency) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean ownUpdatesAreVisible(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean ownDeletesAreVisible(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean ownInsertsAreVisible(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean othersUpdatesAreVisible(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean othersDeletesAreVisible(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean othersInsertsAreVisible(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean updatesAreDetected(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deletesAreDetected(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean insertsAreDetected(int type) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsBatchUpdates() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Connection getConnection() throws UnsupportedOperationException {
		return myConnection;
	}

	@Override
	public boolean supportsSavepoints() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsNamedParameters() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsMultipleOpenResults() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsGetGeneratedKeys() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsResultSetHoldability(int holdability) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getDatabaseMajorVersion() throws UnsupportedOperationException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[0]);
	}

	@Override
	public int getDatabaseMinorVersion() throws UnsupportedOperationException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[1]);
	}

	@Override
	public int getJDBCMajorVersion() throws UnsupportedOperationException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[0]);
	}

	@Override
	public int getJDBCMinorVersion() throws UnsupportedOperationException {
		return Integer.parseInt(VersionUtil.getVersion().split("\\.")[1]);
	}

	@Override
	public int getSQLStateType() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean locatorsUpdateCopy() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsStatementPooling() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public RowIdLifetime getRowIdLifetime() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getSchemas(String catalog, String schemaPattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getClientInfoProperties() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean generatedKeyAlwaysReturned() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}


