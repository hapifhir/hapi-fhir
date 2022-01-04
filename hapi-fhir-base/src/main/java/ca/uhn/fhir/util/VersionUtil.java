package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * Used internally by HAPI to log the version of the HAPI FHIR framework
 * once, when the framework is first loaded by the classloader.
 */
public class VersionUtil {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(VersionUtil.class);
	private static String ourVersion;
	private static String ourBuildNumber;
	private static String ourBuildTime;
	private static boolean ourSnapshot;

	static {
		initialize();
	}

	public static boolean isSnapshot() {
		return ourSnapshot;
	}

	public static String getBuildNumber() {
		return ourBuildNumber;
	}

	public static String getBuildTime() {
		return ourBuildTime;
	}

	public static String getVersion() {
		return ourVersion;
	}

	private static void initialize() {
		try (InputStream is = VersionUtil.class.getResourceAsStream("/ca/uhn/fhir/hapi-fhir-base-build.properties")) {

			Properties p = new Properties();
			if (is != null) {
				p.load(is);
			}

			ourVersion = p.getProperty("hapifhir.version");
			ourVersion = defaultIfBlank(ourVersion, "(unknown)");

			ourSnapshot = ourVersion.contains("SNAPSHOT");

			ourBuildNumber = StringUtils.left(p.getProperty("hapifhir.buildnumber"), 10);
			ourBuildTime = p.getProperty("hapifhir.timestamp");

			if (System.getProperty("suppress_hapi_fhir_version_log") == null) {
				String buildNumber = ourBuildNumber;
				if (isSnapshot()) {
					buildNumber = buildNumber + "/" + getBuildDate();
				}

				ourLog.info("HAPI FHIR version {} - Rev {}", ourVersion, buildNumber);
			}

		} catch (Exception e) {
			ourLog.warn("Unable to determine HAPI version information", e);
		}
	}

	public static String getBuildDate() {
		return ourBuildTime.substring(0, 10);
	}

}
