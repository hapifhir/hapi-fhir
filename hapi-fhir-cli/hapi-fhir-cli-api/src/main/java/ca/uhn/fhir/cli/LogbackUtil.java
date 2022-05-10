package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LogbackUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(LogbackUtil.class);

	static void loggingConfigOff() {
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
			configurator.doConfigure(App.class.getResourceAsStream("/logback-cli-off.xml"));
		} catch (JoranException e) {
			e.printStackTrace();
		}
	}

	static void loggingConfigOnWithColour() {
		setLogbackConfig("/logback-cli-on.xml");
	}

	static void loggingConfigOnWithoutColour() {
		setLogbackConfig("/logback-cli-on-no-colour.xml");
	}

	static void loggingConfigOnDebug() {
		setLogbackConfig("/logback-cli-on-debug.xml");
		ourLog.info("Debug logging is enabled");
	}

	static void setLogbackConfig(String logbackConfigFilename) {
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
			((LoggerContext) LoggerFactory.getILoggerFactory()).reset();
			configurator.doConfigure(App.class.getResourceAsStream(logbackConfigFilename));
			ourLog.info("Logging configuration set from file " + logbackConfigFilename);
		} catch (JoranException e) {
			e.printStackTrace();
		}
	}
}
