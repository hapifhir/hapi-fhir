package ca.uhn.fhir.jpa.test.config;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionWrapper implements Connection {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ConnectionWrapper.class);

	private Connection myWrap;

	public ConnectionWrapper(Connection theConnection) {
		myWrap = theConnection;
	}

	@Override
	public void abort(Executor theExecutor) throws SQLException {
		myWrap.abort(theExecutor);
	}

	@Override
	public void clearWarnings() throws SQLException {
		myWrap.clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		myWrap.close();
	}

	@Override
	public void commit() throws SQLException {
		myWrap.commit();
	}

	@Override
	public Array createArrayOf(String theTypeName, Object[] theElements) throws SQLException {
		return myWrap.createArrayOf(theTypeName, theElements);
	}

	@Override
	public Blob createBlob() throws SQLException {
		return myWrap.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		return myWrap.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return myWrap.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return myWrap.createSQLXML();
	}

	@Override
	public Statement createStatement() throws SQLException {
		return myWrap.createStatement();
	}

	@Override
	public Statement createStatement(int theResultSetType, int theResultSetConcurrency) throws SQLException {
		return myWrap.createStatement(theResultSetType, theResultSetConcurrency);
	}

	@Override
	public Statement createStatement(int theResultSetType, int theResultSetConcurrency, int theResultSetHoldability) throws SQLException {
		return myWrap.createStatement(theResultSetType, theResultSetConcurrency, theResultSetHoldability);
	}

	@Override
	public Struct createStruct(String theTypeName, Object[] theAttributes) throws SQLException {
		return myWrap.createStruct(theTypeName, theAttributes);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return myWrap.getAutoCommit();
	}

	@Override
	public String getCatalog() throws SQLException {
		return myWrap.getCatalog();
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return myWrap.getClientInfo();
	}

	@Override
	public String getClientInfo(String theName) throws SQLException {
		return myWrap.getClientInfo(theName);
	}

	@Override
	public int getHoldability() throws SQLException {
		return myWrap.getHoldability();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return myWrap.getMetaData();
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return myWrap.getNetworkTimeout();
	}

	@Override
	public String getSchema() throws SQLException {
		return myWrap.getSchema();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return myWrap.getTransactionIsolation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return myWrap.getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return myWrap.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return myWrap.isClosed();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return myWrap.isReadOnly();
	}

	@Override
	public boolean isValid(int theTimeout) throws SQLException {
		return myWrap.isValid(theTimeout);
	}

	@Override
	public boolean isWrapperFor(Class<?> theIface) throws SQLException {
		return myWrap.isWrapperFor(theIface);
	}

	@Override
	public String nativeSQL(String theSql) throws SQLException {
		return myWrap.nativeSQL(theSql);
	}

	@Override
	public CallableStatement prepareCall(String theSql) throws SQLException {
		return myWrap.prepareCall(theSql);
	}

	@Override
	public CallableStatement prepareCall(String theSql, int theResultSetType, int theResultSetConcurrency) throws SQLException {
		return myWrap.prepareCall(theSql, theResultSetType, theResultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String theSql, int theResultSetType, int theResultSetConcurrency, int theResultSetHoldability) throws SQLException {
		return myWrap.prepareCall(theSql, theResultSetType, theResultSetConcurrency, theResultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String theSql) throws SQLException {
		return myWrap.prepareStatement(theSql);
	}

	@Override
	public PreparedStatement prepareStatement(String theSql, int theAutoGeneratedKeys) throws SQLException {
		return myWrap.prepareStatement(theSql, theAutoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String theSql, int theResultSetType, int theResultSetConcurrency) throws SQLException {
		return myWrap.prepareStatement(theSql, theResultSetType, theResultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String theSql, int theResultSetType, int theResultSetConcurrency, int theResultSetHoldability) throws SQLException {
		return myWrap.prepareStatement(theSql, theResultSetType, theResultSetConcurrency, theResultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String theSql, int[] theColumnIndexes) throws SQLException {
		return myWrap.prepareStatement(theSql, theColumnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String theSql, String[] theColumnNames) throws SQLException {
		return myWrap.prepareStatement(theSql, theColumnNames);
	}

	@Override
	public void releaseSavepoint(Savepoint theSavepoint) throws SQLException {
		myWrap.releaseSavepoint(theSavepoint);
	}

	@Override
	public void rollback() throws SQLException {
		myWrap.rollback();
	}

	@Override
	public void rollback(Savepoint theSavepoint) throws SQLException {
		myWrap.rollback(theSavepoint);
	}

	@Override
	public void setAutoCommit(boolean theAutoCommit) throws SQLException {
		myWrap.setAutoCommit(theAutoCommit);
	}

	@Override
	public void setCatalog(String theCatalog) throws SQLException {
		myWrap.setCatalog(theCatalog);
	}

	@Override
	public void setClientInfo(Properties theProperties) throws SQLClientInfoException {
		myWrap.setClientInfo(theProperties);
	}

	@Override
	public void setClientInfo(String theName, String theValue) throws SQLClientInfoException {
		myWrap.setClientInfo(theName, theValue);
	}

	@Override
	public void setHoldability(int theHoldability) throws SQLException {
		myWrap.setHoldability(theHoldability);
	}

	@Override
	public void setNetworkTimeout(Executor theExecutor, int theMilliseconds) throws SQLException {
		myWrap.setNetworkTimeout(theExecutor, theMilliseconds);
	}

	@Override
	public void setReadOnly(boolean theReadOnly) throws SQLException {
		ourLog.debug("Setting connection as readonly");
		myWrap.setReadOnly(theReadOnly);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return myWrap.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String theName) throws SQLException {
		return myWrap.setSavepoint(theName);
	}

	@Override
	public void setSchema(String theSchema) throws SQLException {
		myWrap.setSchema(theSchema);
	}

	@Override
	public void setTransactionIsolation(int theLevel) throws SQLException {
		myWrap.setTransactionIsolation(theLevel);
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> theMap) throws SQLException {
		myWrap.setTypeMap(theMap);
	}

	@Override
	public <T> T unwrap(Class<T> theIface) throws SQLException {
		return myWrap.unwrap(theIface);
	}

}
