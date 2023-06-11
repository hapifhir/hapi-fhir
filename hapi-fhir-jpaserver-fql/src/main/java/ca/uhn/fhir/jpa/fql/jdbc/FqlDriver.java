package ca.uhn.fhir.jpa.fql.jdbc;

import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class FqlDriver implements Driver {
	private static final FqlDriver INSTANCE = new FqlDriver();
	public static final String URL_PREFIX = "jdbc:hapifhir:";
	private static boolean ourRegistered;

	static {
		load();
	}

	@Override
	public Connection connect(String theUrl, Properties theProperties) throws SQLException {
		String serverUrl = theUrl.substring(URL_PREFIX.length());

//		URLConnection connection = new URLConnection(new URL(serverUrl + "/$"));
		return new FqlConnection(serverUrl);
	}

	@Override
	public boolean acceptsURL(String theUrl) {
		return theUrl.startsWith(URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
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
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
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
