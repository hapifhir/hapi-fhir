/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ConcurrencyTestUtil {

	private static final Logger ourLog = LoggerFactory.getLogger(ConcurrencyTestUtil.class);

	private ConcurrencyTestUtil() {}

	public static void executeFutures(CompletionService<Boolean> theCompletionService, int theTotal) {
		List<String> errors = new ArrayList<>();
		int count = 0;

		while (count + errors.size() < theTotal) {
			try {
				// svc.take() will process the next future that is ready
				Future<Boolean> future = theCompletionService.take();
				boolean r = future.get();
				assertTrue(r);
				count++;
			} catch (Exception ex) {
				// we will run all the threads to completion, even if we have errors
				// this is so we don't have background threads kicking around with
				// partial changes.
				// we either do this, or shutdown the completion service in an
				// "inelegant" manner, dropping all threads (which we aren't doing)
				ourLog.error("Failed after checking {} futures", count);
				String[] frames = ExceptionUtils.getRootCauseStackTrace(ex);
				errors.add(ex + "\n" + String.join("\n   ", frames));
			}
		}

		if (!errors.isEmpty()) {
			fail(String.format("Failed to execute futures. Found %d errors :%n", errors.size())
					+ String.join(", ", errors));
		}
	}
}
