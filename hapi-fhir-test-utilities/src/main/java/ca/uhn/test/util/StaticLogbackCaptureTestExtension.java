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

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

/**
 * This is a static wrapper around LogbackTestExtension for use in IT tests when you need to assert on App
 * startup log entries
 * @deprecated use {@link StaticLogbackTestExtension}
 */
@Deprecated
public class StaticLogbackCaptureTestExtension implements BeforeAllCallback, AfterAllCallback {
	private final LogbackCaptureTestExtension myLogbackCaptureTestExtension;

	public StaticLogbackCaptureTestExtension(LogbackCaptureTestExtension theLogbackCaptureTestExtension) {
		myLogbackCaptureTestExtension = theLogbackCaptureTestExtension;
	}

	public StaticLogbackCaptureTestExtension() {
		myLogbackCaptureTestExtension = new LogbackCaptureTestExtension();
	}

	@Override
	public void beforeAll(ExtensionContext theExtensionContext) throws Exception {
		myLogbackCaptureTestExtension.beforeEach(theExtensionContext);
	}

	@Override
	public void afterAll(ExtensionContext theExtensionContext) throws Exception {
		myLogbackCaptureTestExtension.afterEach(theExtensionContext);
	}

	public List<ILoggingEvent> filterLoggingEventsWithMessageEqualTo(String theMessageText) {
		return myLogbackCaptureTestExtension.filterLoggingEventsWithMessageEqualTo(theMessageText);
	}
}
