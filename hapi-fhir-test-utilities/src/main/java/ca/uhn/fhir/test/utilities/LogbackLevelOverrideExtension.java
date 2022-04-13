package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
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
