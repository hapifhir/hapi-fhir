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
import org.assertj.core.api.AbstractAssert;

public class LogEventAssert extends AbstractAssert<LogEventAssert, ILoggingEvent> {

	protected LogEventAssert(ILoggingEvent actual) {
		super(actual, LogEventAssert.class);
	}

	// Example of a custom assertion for a single LogEvent
	// You can add methods for specific assertions here
}
