package ca.uhn.fhir.cli;

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
