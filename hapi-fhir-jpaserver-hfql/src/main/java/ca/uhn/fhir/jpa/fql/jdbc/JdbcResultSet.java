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
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.executor.StaticHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.fql.jdbc.JdbcConnection.newSqlExceptionForFeatureNotSupported;
import static ca.uhn.fhir.jpa.fql.jdbc.JdbcConnection.newSqlExceptionForUnsupportedOperation;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * JDBC ResultSet for HFQL which is backed by a {@link IHfqlExecutionResult} instance
 * (typically a {@link RemoteHfqlExecutionResult} that is streaming results from the
 * server).
 */
class JdbcResultSet implements ResultSet {

	/**
	 * @see <a href="https://en.wikipedia.org/wiki/SQLSTATE">https://en.wikipedia.org/wiki/SQLSTATE</a>
	 */
	public static final String SQL_STATE_CODE_0F001_LOCATOR_EXCEPTION = "0F001";

	private final IHfqlExecutionResult myResult;
	private final Statement myStatement;
	private List<Object> myNextRow;
	private final JdbcResultSetMetadata myMetadata;
	private final Map<String, Integer> myColumnNameToIndex;
	private int myRowCount;
	private Object myLastValue;

	/**
	 * Empty Constructor
	 */
	public JdbcResultSet() {
		this(new StaticHfqlExecutionResult(null));
	}

	/**
	 * Constructor
	 */
	public JdbcResultSet(IHfqlExecutionResult theResult) {
		this(theResult, null);
	}

	/**
	 * Constructor
	 */
	public JdbcResultSet(IHfqlExecutionResult theResult, Statement theStatement) {
		myStatement = theStatement;
		myResult = theResult;
		myMetadata = new JdbcResultSetMetadata();
		myColumnNameToIndex = new HashMap<>();
		List<HfqlStatement.SelectClause> selectClauses = myResult.getStatement().getSelectClauses();
		for (int i = 0; i < selectClauses.size(); i++) {
			myColumnNameToIndex.put(selectClauses.get(i).getAlias(), i + 1);
		}
	}

	@Override
	public boolean next() throws SQLException {
		if (myResult.hasNext()) {
			IHfqlExecutionResult.Row nextRow = myResult.getNextRow();
			if (nextRow.getRowOffset() == IHfqlExecutionResult.ROW_OFFSET_ERROR) {
				String errorMessage = nextRow.getRowValues().get(0).toString();
				throw new SQLException(Msg.code(2395) + errorMessage, SQL_STATE_CODE_0F001_LOCATOR_EXCEPTION, -1);
			}

			myNextRow = nextRow.getRowValues();
			myRowCount++;
			return true;
		}
		return false;
	}

	@Override
	public void close() throws SQLException {
		myResult.close();
	}

	@Override
	public boolean wasNull() {
		return myLastValue == null;
	}

	private void validateColumnIndex(int columnIndex) throws SQLException {
		if (columnIndex <= 0) {
			throw new SQLException(Msg.code(2396) + "Invalid column index: " + columnIndex);
		}
		if (columnIndex > myResult.getStatement().getSelectClauses().size()) {
			throw new SQLException(Msg.code(2397) + "Invalid column index: " + columnIndex);
		}
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		String retVal = (String) myNextRow.get(columnIndex - 1);
		myLastValue = retVal;
		return retVal;
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		Integer retVal = (Integer) myNextRow.get(columnIndex - 1);
		myLastValue = retVal;
		retVal = defaultIfNull(retVal, 0);
		return retVal;
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		Boolean retVal = (Boolean) myNextRow.get(columnIndex - 1);
		myLastValue = retVal;
		retVal = defaultIfNull(retVal, Boolean.FALSE);
		return retVal;
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		Long retVal = (Long) myNextRow.get(columnIndex - 1);
		myLastValue = retVal;
		retVal = defaultIfNull(retVal, 0L);
		return retVal;
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		BigDecimal retVal = getBigDecimal(columnIndex);
		return retVal != null ? retVal.floatValue() : 0f;
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		BigDecimal retVal = getBigDecimal(columnIndex);
		return retVal != null ? retVal.doubleValue() : 0d;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return getBigDecimal(columnIndex);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		Object retVal = myNextRow.get(columnIndex - 1);
		if (retVal != null) {
			retVal = new Date(((java.util.Date) retVal).getTime());
		}
		myLastValue = retVal;
		return (Date) retVal;
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		Object retVal = myNextRow.get(columnIndex - 1);
		if (retVal != null) {
			String time = (String) retVal;
			if (StringUtils.countMatches(time, ':') == 1) {
				time = time + ":00";
			}
			int pointIdx = time.indexOf('.');
			if (pointIdx != -1) {
				time = time.substring(0, pointIdx);
			}
			retVal = Time.valueOf(time);
		}
		myLastValue = retVal;
		return (Time) retVal;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		Object retVal = myNextRow.get(columnIndex - 1);
		if (retVal != null) {
			retVal = new Timestamp(((java.util.Date) retVal).getTime());
		}
		myLastValue = retVal;
		return (Timestamp) retVal;
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return getString(findColumn(columnLabel));
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return getInt(findColumn(columnLabel));
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return getBoolean(findColumn(columnLabel));
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return getLong(findColumn(columnLabel));
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return getFloat(findColumn(columnLabel));
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return getDouble(findColumn(columnLabel));
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return getBigDecimal(findColumn(columnLabel));
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return getDate(findColumn(columnLabel));
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return getTime(findColumn(columnLabel));
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return getTimestamp(findColumn(columnLabel));
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public void clearWarnings() {
		// ignored
	}

	@Override
	public String getCursorName() throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public ResultSetMetaData getMetaData() {
		return myMetadata;
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		switch (myResult.getStatement().getSelectClauses().get(columnIndex - 1).getDataType()) {
			case INTEGER:
				return getInt(columnIndex);
			case BOOLEAN:
				return getBoolean(columnIndex);
			case DATE:
				return getDate(columnIndex);
			case TIMESTAMP:
				return getTimestamp(columnIndex);
			case LONGINT:
				return getLong(columnIndex);
			case TIME:
				return getTime(columnIndex);
			case DECIMAL:
				return getBigDecimal(columnIndex);
			case STRING:
			case JSON:
			default:
				return getString(columnIndex);
		}
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return getObject(findColumn(columnLabel));
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		Integer retVal = myColumnNameToIndex.get(columnLabel);
		if (retVal != null) {
			return retVal;
		}
		throw new SQLException(
				Msg.code(2416) + "Unknown column: " + columnLabel + ". Valid columns: " + myColumnNameToIndex.keySet());
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		throw newSqlExceptionForUnsupportedOperation();
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		validateColumnIndex(columnIndex);
		BigDecimal retVal = (BigDecimal) myNextRow.get(columnIndex - 1);
		myLastValue = retVal;
		return retVal;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return getBigDecimal(findColumn(columnLabel));
	}

	@Override
	public boolean isBeforeFirst() {
		return myNextRow == null;
	}

	@Override
	public boolean isAfterLast() {
		return !myResult.hasNext();
	}

	@Override
	public boolean isFirst() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public boolean isLast() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void beforeFirst() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void afterLast() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public boolean first() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public boolean last() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public int getRow() {
		return myRowCount;
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public boolean previous() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public int getFetchDirection() {
		return ResultSet.FETCH_FORWARD;
	}

	@Override
	public void setFetchDirection(int direction) {
		// ignored
	}

	@Override
	public int getFetchSize() {
		return 0;
	}

	@Override
	public void setFetchSize(int rows) {
		// ignored
	}

	@Override
	public int getType() throws SQLException {
		return ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public int getConcurrency() {
		return ResultSet.CONCUR_READ_ONLY;
	}

	@Override
	public boolean rowUpdated() {
		return false;
	}

	@Override
	public boolean rowInserted() {
		return false;
	}

	@Override
	public boolean rowDeleted() {
		return false;
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void insertRow() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateRow() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void deleteRow() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void refreshRow() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Statement getStatement() throws SQLException {
		return myStatement;
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public int getHoldability() {
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	@Override
	public boolean isClosed() {
		return myResult.isClosed();
	}

	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		throw newSqlExceptionForFeatureNotSupported();
	}

	@Override
	public <T> T unwrap(Class<T> theInterface) {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> theInterface) {
		return false;
	}

	private class JdbcResultSetMetadata implements ResultSetMetaData {
		@Override
		public int getColumnCount() {
			return myColumnNameToIndex.size();
		}

		@Override
		public boolean isAutoIncrement(int column) {
			return false;
		}

		@Override
		public boolean isCaseSensitive(int column) {
			return false;
		}

		@Override
		public boolean isSearchable(int column) {
			return false;
		}

		@Override
		public boolean isCurrency(int column) {
			return false;
		}

		@Override
		public int isNullable(int column) {
			return columnNullableUnknown;
		}

		@Override
		public boolean isSigned(int column) {
			return false;
		}

		@Override
		public int getColumnDisplaySize(int column) {
			return 0;
		}

		@Override
		public String getColumnLabel(int column) {
			return myResult.getStatement().getSelectClauses().get(column - 1).getAlias();
		}

		@Override
		public String getColumnName(int column) {
			return getColumnLabel(column);
		}

		@Override
		public String getSchemaName(int column) {
			return null;
		}

		@Override
		public int getPrecision(int column) {
			return 0;
		}

		@Override
		public int getScale(int column) {
			return 0;
		}

		@Override
		public String getTableName(int column) {
			return null;
		}

		@Override
		public String getCatalogName(int column) {
			return null;
		}

		@Override
		public int getColumnType(int column) {
			return myResult.getStatement()
					.getSelectClauses()
					.get(column - 1)
					.getDataType()
					.getSqlType();
		}

		@Override
		public String getColumnTypeName(int column) {
			return myResult.getStatement()
					.getSelectClauses()
					.get(column - 1)
					.getDataType()
					.name();
		}

		@Override
		public boolean isReadOnly(int column) {
			return true;
		}

		@Override
		public boolean isWritable(int column) {
			return false;
		}

		@Override
		public boolean isDefinitelyWritable(int column) {
			return false;
		}

		@Override
		public String getColumnClassName(int column) {
			return String.class.getName();
		}

		@Override
		public <T> T unwrap(Class<T> theInterface) {
			return null;
		}

		@Override
		public boolean isWrapperFor(Class<?> theInterface) {
			return false;
		}
	}
}
