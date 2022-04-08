package ca.uhn.fhir.test.utilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Extension to allow temporary log elevation for a single test.
 *
 */
public class LogbackLevelOverrideExtension implements AfterEachCallback {

	private final Map<String, Level> mySavedLevels = new HashMap<>();

	public void setLogLevel(Class theClass, Level theLevel) {
		String name = theClass.getName();
		Logger logger = getClassicLogger(name);
		if (!mySavedLevels.containsKey(name)) {
			// level can be null
			mySavedLevels.put(name, logger.getLevel());
		}
		logger.setLevel(theLevel);
	}

	private Logger getClassicLogger(String name) {
		return (Logger) LoggerFactory.getLogger(name);
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		mySavedLevels.forEach((name,level) ->{
			getClassicLogger(name).setLevel(level);
		});
		mySavedLevels.clear();
	}

	public void resetLevel(Class theClass) {
		String name = theClass.getName();
		if (mySavedLevels.containsKey(name)) {
			getClassicLogger(name).setLevel(mySavedLevels.get(name));
			mySavedLevels.remove(name);
		}
	}
}
