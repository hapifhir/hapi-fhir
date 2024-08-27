/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.api.TermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;

import java.util.concurrent.atomic.AtomicBoolean;

public class TermCodeSystemDeleteJobSvcWithUniTestFailures extends TermCodeSystemDeleteJobSvc
		implements ITermCodeSystemDeleteJobSvc {

	private static final AtomicBoolean ourFailNextDeleteCodeSystemVersion = new AtomicBoolean(false);

	/**
	 * This is here for unit tests only
	 */
	@VisibleForTesting
	public static void setFailNextDeleteCodeSystemVersion(boolean theFailNextDeleteCodeSystemVersion) {
		ourFailNextDeleteCodeSystemVersion.set(theFailNextDeleteCodeSystemVersion);
	}

	@Override
	public void deleteCodeSystemVersion(long theVersionPid) {
		// Force a failure for unit tests
		if (ourFailNextDeleteCodeSystemVersion.getAndSet(false)) {
			throw new InternalErrorException("Unit test exception");
		}

		super.deleteCodeSystemVersion(theVersionPid);
	}
}
