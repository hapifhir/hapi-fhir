/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.test.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.ThresholdFilter;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * This is a static wrapper around LogbackTestExtension for use in IT tests when you need to assert on App
 * startup log entries
 */

public class StaticLogbackTestExtension implements BeforeAllCallback, AfterAllCallback {
	private final LogbackTestExtension myLogbackTestExtension;

    public StaticLogbackTestExtension(LogbackTestExtension theLogbackTestExtension) {
        myLogbackTestExtension = theLogbackTestExtension;
    }

	public StaticLogbackTestExtension() {
		myLogbackTestExtension = new LogbackTestExtension();
	}

	public static StaticLogbackTestExtension withThreshold(Level theLevel) {
		LogbackTestExtension logbackTestExtension = new LogbackTestExtension();
		logbackTestExtension.setUp(theLevel);
		ThresholdFilter thresholdFilter = new ThresholdFilter();
		thresholdFilter.setLevel(theLevel.levelStr);
		logbackTestExtension.getAppender().addFilter(thresholdFilter);

		return new StaticLogbackTestExtension(logbackTestExtension);
	}

	@Override
	public void beforeAll(ExtensionContext theExtensionContext) throws Exception {
		myLogbackTestExtension.beforeEach(theExtensionContext);
	}

	@Override
	public void afterAll(ExtensionContext theExtensionContext) throws Exception {
		myLogbackTestExtension.afterEach(theExtensionContext);
	}

	public LogbackTestExtension getLogbackTestExtension() {
		return myLogbackTestExtension;
	}

}
