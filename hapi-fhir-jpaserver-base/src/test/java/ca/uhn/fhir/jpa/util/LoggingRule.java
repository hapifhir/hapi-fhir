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

package ca.uhn.fhir.jpa.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * This JUnit rule generates log messages to delineate the start and finish of a JUnit test case and also to note any exceptions
 * that are thrown.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @version 1.0.0
 */
public class LoggingRule implements TestRule {

    /**
     * Apply the test rule by building a wrapper {@link Statement} that logs a messages before and after evaluating
     * <code>statement</code> and if
     * @param statement The statement to be modified.
     * @param description A description of the test implemented in <code>statement</code>.
     * @return The modified statement.
     */
    public Statement apply(final Statement statement, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final Logger logger = LoggerFactory.getLogger(description.getTestClass());
                logger.info(MessageFormat.format("Starting test case [{0}]", description.getDisplayName()));
                try {
                    statement.evaluate();
                } catch (final Throwable e) {
                    logger.error(MessageFormat.format("Exception thrown in test case [{0}]: {1}", description.getDisplayName(), e.toString()), e);
                    throw e;
                } finally {
                    logger.info(MessageFormat.format("Finished test case [{0}]", description.getDisplayName()));
                }
            }
        };
    }
}
