/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;

import java.util.ArrayList;
import java.util.List;

public class PerformanceLogCapturingInterceptor {

	private final List<StorageProcessingMessage> myInfoMessages = new ArrayList<>();
	private final List<StorageProcessingMessage> myWarningMessages = new ArrayList<>();


	@Hook(Pointcut.JPA_PERFTRACE_INFO)
	void info(StorageProcessingMessage theMessage) {
		myInfoMessages.add(theMessage);
	}

	@Hook(Pointcut.JPA_PERFTRACE_WARNING)
	void warning(StorageProcessingMessage theMessage) {
		myWarningMessages.add(theMessage);
	}

	public List<StorageProcessingMessage> getInfoMessages() {
		return myInfoMessages;
	}

	public List<StorageProcessingMessage> getWarningMessages() {
		return myWarningMessages;
	}
}
