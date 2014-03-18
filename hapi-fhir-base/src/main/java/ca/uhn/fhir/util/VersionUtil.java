package ca.uhn.fhir.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(VersionUtil.class);
	private static String ourVersion;

	static {
		initialize();
	}
	
	public static String getVersion() {
		return ourVersion;
	}

	private static void initialize() {
		InputStream is = null;
		try {
			is = VersionUtil.class
					.getResourceAsStream("/ca/uhn/fhir/hapi-version.properties");
			Properties p = new Properties();
			p.load(is);
			ourVersion = p.getProperty("version");
			ourLog.info("HAPI-FHIR version is: " + ourVersion);
		} catch (IOException e) {
			ourLog.warn("Unable to determine HAPI version information", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}
