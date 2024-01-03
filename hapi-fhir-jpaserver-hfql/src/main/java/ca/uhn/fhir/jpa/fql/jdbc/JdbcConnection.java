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

import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

class JdbcConnection implements Connection {
	private final String myServerUrl;
	private boolean myClosed;
	private HfqlRestClient myClient;
	private String myUsername;
	private String myPassword;

	public JdbcConnection(String theServerUrl) {
		myServerUrl = theServerUrl;
	}

	@Override
	public Statement createStatement() {
		return new JdbcStatement(this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean getAutoCommit() {
		return false;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		// nothing
	}

	@Override
	public void commit() {
		// nothing
	}

	@Override
	public void rollback() {
		// nothing
	}

	@Override
	public void close() {
		myClosed = true;
	}

	@Override
	public boolean isClosed() {
		return myClosed;
	}

	@Override
	public DatabaseMetaData getMetaData() {
		return new JdbcDatabaseMetadata(this, getClient());
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getCatalog() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getTransactionIsolation() {
		return Connection.TRANSACTION_READ_COMMITTED;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public SQLWarning getWarnings() {
		return null;
	}

	@Override
	public void clearWarnings() {
		// nothing
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) {
		return createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getHoldability() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
		return createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(
			String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public CallableStatement prepareCall(
			String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Clob createClob() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Blob createBlob() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public NClob createNClob() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public boolean isValid(int timeout) {
		return true;
	}

	@Override
	public void setClientInfo(String name, String value) {
		// ignore
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void setClientInfo(Properties properties) {
		// ignore
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getSchema() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
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

	public HfqlRestClient getClient() {
		if (myClient == null) {
			myClient = new HfqlRestClient(myServerUrl, myUsername, myPassword);
		}
		return myClient;
	}

	public void setUsername(String theUsername) {
		myUsername = theUsername;
	}

	public void setPassword(String thePassword) {
		myPassword = thePassword;
	}

	@Nonnull
	static SQLException newSqlExceptionForUnsupportedOperation() {
		return new SQLException(Msg.code(2394) + "This JDBC method is not yet supported by the HFQL JDBC Driver");
	}

	@Nonnull
	static SQLFeatureNotSupportedException newSqlExceptionForFeatureNotSupported() {
		return new SQLFeatureNotSupportedException(
				Msg.code(2398) + "This JDBC method is not yet supported by the HFQL JDBC Driver");
	}
}
