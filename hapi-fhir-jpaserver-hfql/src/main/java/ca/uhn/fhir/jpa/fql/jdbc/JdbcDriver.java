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

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This is the JDBC driver class for the HFQL driver. It is intended to be
 * imported into a JDBC-compliant database tool, and implements the basic
 * functionality required to introspect the "database" and execute queries.
 * <p>
 * Connections returned by this driver are only semi-stateful. In a normal
 * JDBC driver, each connection represents an open and persistent TCP
 * connection to the server with shared state between the client and the
 * server, but in this driver we keep most of the state in the client. When
 * a query is executed it is translated into a FHIR search (with further
 * processing on the search results happening in
 * {@link ca.uhn.fhir.jpa.fql.executor.HfqlExecutor}).
 */
public class JdbcDriver implements Driver {
	private static final JdbcDriver INSTANCE = new JdbcDriver();
	public static final String URL_PREFIX = "jdbc:hapifhirql:";
	private static boolean ourRegistered;

	static {
		load();
	}

	@Override
	public Connection connect(String theUrl, Properties theProperties) throws SQLException {
		String serverUrl = theUrl.substring(URL_PREFIX.length());

		JdbcConnection connection = new JdbcConnection(serverUrl);
		connection.setUsername(theProperties.getProperty("user", null));
		connection.setPassword(theProperties.getProperty("password", null));
		return connection;
	}

	@Override
	public boolean acceptsURL(String theUrl) {
		return theUrl.startsWith(URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String theUrl, Properties theInfo) {
		return new DriverPropertyInfo[0];
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}

	@Override
	public Logger getParentLogger() {
		return Logger.getLogger(getClass().getPackageName());
	}

	public static synchronized Driver load() {
		try {
			if (!ourRegistered) {
				ourRegistered = true;
				DriverManager.registerDriver(INSTANCE);
			}
		} catch (SQLException e) {
			logException(e);
		}

		return INSTANCE;
	}

	private static void logException(SQLException e) {
		PrintStream out = System.out;
		e.printStackTrace(out);
	}

	public static synchronized void unload() {
		try {
			if (ourRegistered) {
				ourRegistered = false;
				DriverManager.deregisterDriver(INSTANCE);
			}
		} catch (SQLException e) {
			logException(e);
		}
	}
}
