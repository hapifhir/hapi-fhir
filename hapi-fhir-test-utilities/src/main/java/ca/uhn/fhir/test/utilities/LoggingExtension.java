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
/*
 * Copyright 2013 Brian Thomas Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.uhn.fhir.test.utilities;

import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This JUnit rule generates log messages to delineate the start and finish of a JUnit test case and also to note any exceptions
 * that are thrown.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @version 1.0.0
 */
public class LoggingExtension implements BeforeEachCallback, BeforeTestExecutionCallback, AfterEachCallback, AfterTestExecutionCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		getLoggerForTestClass(context).info("Starting setup for test case [{}]", getMethodName(context));
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) {
		getLoggerForTestClass(context).info("Starting test case [{}]", getMethodName(context));

	}
	@Override
	public void afterTestExecution(ExtensionContext context) {
		getLoggerForTestClass(context).info("Finished test case [{}]", getMethodName(context));
	}

	@Override
	public void afterEach(ExtensionContext context) {
		getLoggerForTestClass(context).info("Finished teardown for test case [{}]", getMethodName(context));
	}

	private static Logger getLoggerForTestClass(ExtensionContext context) {
		return LoggerFactory.getLogger(context.getTestClass().orElseThrow());
	}

	private static @Nonnull String getMethodName(ExtensionContext context) {
		return context.getTestMethod().orElseThrow().getName();
	}

}
